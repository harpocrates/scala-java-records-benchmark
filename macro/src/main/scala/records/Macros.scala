package records

import scala.reflect._
import scala.reflect.internal.SymbolTable
import scala.reflect.macros.blackbox._
import language.experimental.macros

object Macros {
  def equals[A](a: A, b: Any): Boolean = macro equalsImpl[A]

  def equalsImpl[A: c.WeakTypeTag](c: Context)(a: c.Tree, b: c.Tree): c.Tree = {
    val symtab = c.universe.asInstanceOf[SymbolTable]
    import symtab._

    // Collect getters
    val fieldGetters: List[Literal] = weakTypeOf[A].declarations
      .collect {
        case m if m.isCaseAccessor && m.isField =>
          Literal(Constant(m: Symbol)).setType(typeOf[java.lang.invoke.MethodHandle])
      }
      .toList

    val bootstrapMethod = typeOf[records.ScalaObjectMethods].companion.member(TermName("bootstrap"))
    val param1Sym = NoSymbol.newTermSymbol(TermName("thiz")).setInfo(weakTypeOf[A])
    val param2Sym = NoSymbol.newTermSymbol(TermName("other")).setInfo(typeOf[Any])
    val dummySymbol = NoSymbol
      .newTermSymbol(TermName("equals"))
      .setInfo(internal.methodType(param1Sym :: param2Sym :: Nil, typeOf[Boolean]))

    val bootstrapArgTrees: List[Tree] =
        Literal(Constant(bootstrapMethod)).setType(NoType) ::
        Literal(Constant(weakTypeOf[A])).setType(definitions.ClassClass.tpe) :: // .setType(typeOf[java.lang.Class[_]]) ::
        fieldGetters

    val result = ApplyDynamic(
      Ident(dummySymbol).setType(dummySymbol.info),
      bootstrapArgTrees ::: List(a.asInstanceOf[symtab.Tree], b.asInstanceOf[symtab.Tree])
    )
    result.setType(dummySymbol.info.resultType)
    result.asInstanceOf[c.Tree]
  }
}
