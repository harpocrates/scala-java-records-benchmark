package records;

import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public final class ScalaObjectMethods {

    // This is just a static object - instances don't make sense
    private ScalaObjectMethods() { }

    private static final MethodHandle FALSE = MethodHandles.constant(boolean.class, false);
    private static final MethodHandle TRUE = MethodHandles.constant(boolean.class, true);
    private static final MethodHandle CLASS_IS_INSTANCE;
    private static final MethodHandle OBJECTS_EQUALS;
    private static final MethodHandle OBJECT_EQ;

    private static boolean eq(Object a, Object b) { return a == b; }
    private static boolean eq(byte a, byte b) { return a == b; }
    private static boolean eq(short a, short b) { return a == b; }
    private static boolean eq(char a, char b) { return a == b; }
    private static boolean eq(int a, int b) { return a == b; }
    private static boolean eq(long a, long b) { return a == b; }
    private static boolean eq(float a, float b) { return Float.compare(a, b) == 0; }
    private static boolean eq(double a, double b) { return Double.compare(a, b) == 0; }
    private static boolean eq(boolean a, boolean b) { return a == b; }

    private static final HashMap<Class<?>, MethodHandle> equalHandles = new HashMap<>();

    static {
        try {
            MethodHandles.Lookup publicLookup = MethodHandles.publicLookup();
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            ClassLoader loader = ScalaObjectMethods.class.getClassLoader();

            CLASS_IS_INSTANCE = publicLookup.findVirtual(
                Class.class,
                "isInstance",
                MethodType.methodType(boolean.class, Object.class)
            );
            OBJECTS_EQUALS = publicLookup.findStatic(
                Objects.class,
                "equals",
                MethodType.methodType(boolean.class, Object.class, Object.class)
            );
            OBJECT_EQ = lookup.findStatic(
                ScalaObjectMethods.class,
                "eq",
                MethodType.methodType(boolean.class, Object.class, Object.class)
            );

            // Populate types for which there is a concrete equality method
            equalHandles.put(
                byte.class,
                lookup.findStatic(
                    ScalaObjectMethods.class,
                    "eq",
                    MethodType.fromMethodDescriptorString("(BB)Z", loader)
                )
            );
            equalHandles.put(
                short.class,
                lookup.findStatic(
                    ScalaObjectMethods.class,
                    "eq",
                    MethodType.fromMethodDescriptorString("(SS)Z", loader)
                )
            );
            equalHandles.put(
                char.class,
                lookup.findStatic(
                    ScalaObjectMethods.class,
                    "eq",
                    MethodType.fromMethodDescriptorString("(CC)Z", loader)
                )
            );
            equalHandles.put(
                int.class,
                lookup.findStatic(
                    ScalaObjectMethods.class,
                    "eq",
                    MethodType.fromMethodDescriptorString("(II)Z", loader)
                )
            );
            equalHandles.put(
                long.class,
                lookup.findStatic(
                    ScalaObjectMethods.class,
                    "eq",
                    MethodType.fromMethodDescriptorString("(JJ)Z", loader)
                )
            );
            equalHandles.put(
                float.class,
                lookup.findStatic(
                    ScalaObjectMethods.class,
                    "eq",
                    MethodType.fromMethodDescriptorString("(FF)Z", loader)
                )
            );
            equalHandles.put(
                double.class,
                lookup.findStatic(
                    ScalaObjectMethods.class,
                    "eq",
                    MethodType.fromMethodDescriptorString("(DD)Z", loader)
                )
            );
            equalHandles.put(
                boolean.class,
                lookup.findStatic(
                    ScalaObjectMethods.class,
                    "eq",
                    MethodType.fromMethodDescriptorString("(ZZ)Z", loader)
                )
            );
            equalHandles.put( // Scala is special! Objects (or erased generics) use `BoxesRunTime.equals`
                Object.class,
                lookup.findStatic(
                    scala.runtime.BoxesRunTime.class,
                    "equals",
                    MethodType.methodType(boolean.class, Object.class, Object.class)
                )
            );
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /** Get the method handle for combining two values of a given type */
    private static MethodHandle equalator(Class<?> clazz) {
        return (clazz.isPrimitive() || clazz == Object.class)
            ? equalHandles.get(clazz)
            : OBJECTS_EQUALS.asType(MethodType.methodType(boolean.class, clazz, clazz));
    }


    /** Make a method handle that corresponds to the semantics of `equals` on a `case class`.
      *
      * The method handle should behave exactly as `BoxesRunTime.equals` (but be more efficient).
      *
      * TODO: `canEqual`
      */
    private static MethodHandle makeEquals(Class<?> receiverClass, List<MethodHandle> getters) {
        MethodType rr = MethodType.methodType(boolean.class, receiverClass, receiverClass);
        MethodType ro = MethodType.methodType(boolean.class, receiverClass, Object.class);
        MethodHandle instanceFalse = MethodHandles.dropArguments(FALSE, 0, receiverClass, Object.class); // (RO)Z
        MethodHandle instanceTrue = MethodHandles.dropArguments(TRUE, 0, receiverClass, Object.class); // (RO)Z
        MethodHandle isSameObject = OBJECT_EQ.asType(ro); // (RO)Z
        MethodHandle isInstance = MethodHandles.dropArguments(CLASS_IS_INSTANCE.bindTo(receiverClass), 0, receiverClass); // (RO)Z
        MethodHandle accumulator = MethodHandles.dropArguments(TRUE, 0, receiverClass, receiverClass); // (RR)Z

        for (MethodHandle getter : getters) {
            // Skip over fields of type `Nothing`, `Null`, or `Unit`
            Class<?> getterType = getter.type().returnType();
            if (getterType == scala.runtime.Nothing$.class ||
                getterType == scala.runtime.Null$.class ||
                getterType == scala.Unit.class) {
              continue;
            }
            MethodHandle equalator = equalator(getter.type().returnType()); // (TT)Z
            MethodHandle thisFieldEqual = MethodHandles.filterArguments(equalator, 0, getter, getter); // (RR)Z
            accumulator = MethodHandles.guardWithTest(thisFieldEqual, accumulator, instanceFalse.asType(rr));
        }

        return MethodHandles.guardWithTest(
            isSameObject,
            instanceTrue,
            MethodHandles.guardWithTest(isInstance, accumulator.asType(ro), instanceFalse)
        );
    }

    public static ConstantCallSite bootstrap(
        MethodHandles.Lookup lookup,
        String methodName,
        MethodType methodType,
        Class<?> caseClass,
        MethodHandle... getters
    ) throws Throwable {
        List<MethodHandle> getterList = Arrays.asList(getters);
        switch (methodName) {
            case "equals":
                if (methodType != null && !methodType.equals(MethodType.methodType(boolean.class, caseClass, Object.class)))
                    throw new IllegalArgumentException("Bad method type: " + methodType);
                return new ConstantCallSite(makeEquals(caseClass, getterList));
        //    case "hashCode":
        //        if (methodType != null && !methodType.equals(MethodType.methodType(int.class, recordClass)))
        //            throw new IllegalArgumentException("Bad method type: " + methodType);
        //        handle = makeHashCode(recordClass, getterList);
        //        return methodType != null ? new ConstantCallSite(handle) : handle;
        //    case "toString":
        //        if (methodType != null && !methodType.equals(MethodType.methodType(String.class, recordClass)))
        //            throw new IllegalArgumentException("Bad method type: " + methodType);
        //        List<String> nameList = "".equals(names) ? List.of() : List.of(names.split(";"));
        //        if (nameList.size() != getterList.size())
        //            throw new IllegalArgumentException("Name list and accessor list do not match");
        //        handle = makeToString(recordClass, getterList, nameList);
        //        return methodType != null ? new ConstantCallSite(handle) : handle;
            default:
                throw new IllegalArgumentException(methodName);
        }
    }
}
