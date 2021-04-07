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
    private static final MethodHandle HASH_INIT = MethodHandles.constant(int.class, 0xcafebabe);
    private static final MethodHandle CLASS_IS_INSTANCE;
    private static final MethodHandle OBJECTS_EQUALS;
    private static final MethodHandle OBJECT_EQ;
    private static final MethodHandle HASH_ANY;
    private static final MethodHandle HASH_COMBINER;
    private static final MethodHandle HASH_FINALIZER;
    private static final MethodHandle INDEX_EXCEPTION_MAKER;
    private static final MethodHandle INDEX_EQ;

    private static boolean eq(Object a, Object b) { return a == b; }
    private static boolean eq(byte a, byte b) { return a == b; }
    private static boolean eq(short a, short b) { return a == b; }
    private static boolean eq(char a, char b) { return a == b; }
    private static boolean eq(int a, int b) { return a == b; }
    private static boolean eq(long a, long b) { return a == b; }
    private static boolean eq(float a, float b) { return Float.compare(a, b) == 0; }
    private static boolean eq(double a, double b) { return Double.compare(a, b) == 0; }
    private static boolean eq(boolean a, boolean b) { return a == b; }
    private static IndexOutOfBoundsException oob(int i) {
        return new IndexOutOfBoundsException(Integer.toString(i));
    }

    private static final HashMap<Class<?>, MethodHandle> equalHandles = new HashMap<>();
    private static final HashMap<Class<?>, MethodHandle> hashHandles = new HashMap<>();

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
            HASH_ANY = lookup.findStatic(
                scala.runtime.Statics.class,
                "anyHash",
                MethodType.methodType(int.class, Object.class)
            );
            HASH_COMBINER = lookup.findStatic(
                scala.runtime.Statics.class,
                "mix",
                MethodType.methodType(int.class, int.class, int.class)
            );
            HASH_FINALIZER = lookup.findStatic(
                scala.runtime.Statics.class,
                "finalizeHash",
                MethodType.methodType(int.class, int.class, int.class)
            );
            INDEX_EXCEPTION_MAKER = lookup.findStatic(
                ScalaObjectMethods.class,
                "oob",
                MethodType.methodType(IndexOutOfBoundsException.class, int.class)
            );
            INDEX_EQ = lookup.findStatic(
                ScalaObjectMethods.class,
                "eq",
                MethodType.methodType(boolean.class, int.class, int.class)
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

            // Populate types for hashing
            hashHandles.put(
                scala.Unit.class,
                MethodHandles.constant(int.class, 0)
            );
            hashHandles.put(
                scala.runtime.Null$.class,
                MethodHandles.constant(int.class, 0)
            );
            hashHandles.put(
                boolean.class,
                MethodHandles.guardWithTest(
                    MethodHandles.identity(boolean.class),
                    MethodHandles.dropArguments(MethodHandles.constant(int.class, 1231), 0, boolean.class),
                    MethodHandles.dropArguments(MethodHandles.constant(int.class, 1237), 0, boolean.class)
                )
            );
            hashHandles.put(
                int.class,
                MethodHandles.identity(int.class)
            );
            hashHandles.put(
                short.class,
                MethodHandles.explicitCastArguments(
                    MethodHandles.identity(int.class),
                    MethodType.fromMethodDescriptorString("(S)I", loader)
                )
            );
            hashHandles.put(
                byte.class,
                MethodHandles.explicitCastArguments(
                    MethodHandles.identity(int.class),
                    MethodType.fromMethodDescriptorString("(B)I", loader)
                )
            );
            hashHandles.put(
                char.class,
                MethodHandles.explicitCastArguments(
                    MethodHandles.identity(int.class),
                    MethodType.fromMethodDescriptorString("(C)I", loader)
                )
            );
            hashHandles.put(
                long.class,
                lookup.findStatic(
                    scala.runtime.Statics.class,
                    "longHash",
                    MethodType.methodType(int.class, long.class)
                )
            );
            hashHandles.put(
                double.class,
                lookup.findStatic(
                    scala.runtime.Statics.class,
                    "doubleHash",
                    MethodType.methodType(int.class, double.class)
                )
            );
            hashHandles.put(
                float.class,
                lookup.findStatic(
                    scala.runtime.Statics.class,
                    "floatHash",
                    MethodType.methodType(int.class, float.class)
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

    /** Make a method handle that corresponds to the semantics of `hashCode` on a `case class`.
      */
    private static MethodHandle hasher(Class<?> clazz) {
        MethodHandle hasher = hashHandles.get(clazz);
        return (hasher != null)
            ? hasher
            : HASH_ANY.asType(MethodType.methodType(int.class, clazz));
    }

    private static MethodHandle makeHashCode(Class<?> receiverClass, List<MethodHandle> getters) {
        MethodHandle accumulator = MethodHandles.dropArguments(HASH_INIT, 0, receiverClass); // (R)I

        for (MethodHandle getter : getters) {
            MethodHandle hasher = hasher(getter.type().returnType()); // (T)I
            MethodHandle hashThisField = MethodHandles.filterArguments(hasher, 0, getter);    // (R)I
            MethodHandle combineHashes = MethodHandles.filterArguments(HASH_COMBINER, 0, accumulator, hashThisField); // (RR)I
            accumulator = MethodHandles.permuteArguments(combineHashes, accumulator.type(), 0, 0); // adapt (R)I to (RR)I
        }

        return MethodHandles.filterArguments(
          MethodHandles.insertArguments(HASH_FINALIZER, 1, getters.size()),
          0,
          accumulator
        );
    }

    /** Make a method handle that corresponds to the semantics of `productElement` on a `case class`.
      *
      * The approach taken is to build an array of the getters and then index into that. This is
      * constant time (since array inddexing is O(1)), but still not as efficient as a manually
      * created `tableswitch`.
      */
    private static MethodHandle makeProductElement(Class<?> receiverClass, List<MethodHandle> getters) {
        MethodHandle[] boxedGetters = getters
            .stream()
            .map(getter -> getter.asType(getter.type().changeReturnType(java.lang.Object.class)))
            .toArray(MethodHandle[]::new);

        MethodHandle getGetter = MethodHandles      // (I)H
            .arrayElementGetter(MethodHandle[].class)
            .bindTo(boxedGetters);
        MethodHandle invokeGetter = MethodHandles.permuteArguments( // (RH)O
            MethodHandles.exactInvoker(MethodType.methodType(java.lang.Object.class, receiverClass)),
            MethodType.methodType(java.lang.Object.class, receiverClass, MethodHandle.class),
            1,
            0
        );

        return MethodHandles.filterArguments(invokeGetter, 1, getGetter);
    }

    /** Make a method handle that corresponds to the semantics of `productElement` on a `case class`.
      *
      * The approach taken is to build a chain of `if`/`else if`/`else` checks on the index. This
      * is O(n) in the worst case, but I was curious how well it would JIT. Answer: not as well as
      * `tableswitch`
      */
    private static MethodHandle makeProductElementChained(Class<?> receiverClass, MethodHandle[] getters) {
        MethodHandle accumulator = MethodHandles.filterReturnValue(  // (IR)O
            MethodHandles.dropArguments(INDEX_EXCEPTION_MAKER, 1, receiverClass),
            MethodHandles.throwException(Object.class, IndexOutOfBoundsException.class)
        );

        for (int i = getters.length - 1; i >= 0; i--) {
            MethodHandle getter = getters[i];
            MethodHandle boxedGetter = getter.asType(getter.type().changeReturnType(java.lang.Object.class)); // (R)O

            accumulator = MethodHandles.guardWithTest(
                MethodHandles.insertArguments(INDEX_EQ, 0, i),            // (I)Z
                MethodHandles.dropArguments(boxedGetter, 0, int.class),   // (IR)O
                accumulator                                               // (IR)O
            );
        }

        return MethodHandles.permuteArguments(
            accumulator,
            MethodType.methodType(java.lang.Object.class, receiverClass, int.class),
            1,
            0
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
            // instance methods
            case "equals":
                if (methodType != null && !methodType.equals(MethodType.methodType(boolean.class, caseClass, Object.class)))
                    throw new IllegalArgumentException("Bad method type: " + methodType);
                return new ConstantCallSite(makeEquals(caseClass, getterList));

            case "hashCode":
                if (methodType != null && !methodType.equals(MethodType.methodType(int.class, caseClass)))
                    throw new IllegalArgumentException("Bad method type: " + methodType);
                return new ConstantCallSite(makeHashCode(caseClass, getterList));

            case "productElement":
                if (methodType != null && !methodType.equals(MethodType.methodType(Object.class, caseClass, int.class)))
                    throw new IllegalArgumentException("Bad method type: " + methodType);
                return new ConstantCallSite(makeProductElement(caseClass, getterList));

            // static methods
            case "apply":
                Class<?>[] parameterTypes = Arrays
                    .stream(getters)
                    .map(getter -> getter.type().returnType())
                    .toArray(Class<?>[]::new);
                MethodType applyType = MethodType.methodType(caseClass, parameterTypes);
                if (methodType != null && !methodType.equals(applyType))
                    throw new IllegalArgumentException("Bad method type: " + methodType);

                MethodHandle ctor = lookup.findConstructor(caseClass, applyType);
                return new ConstantCallSite(ctor);


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
