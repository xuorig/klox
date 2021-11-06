package klox

import kotlin.math.exp

sealed class Stmt {
    abstract fun accept(visitor: StmtVisitor)

    class Expression(val expression: Expr) : Stmt() {
        override fun accept(visitor: StmtVisitor) {
            visitor.visitExpressionStmt(this)
        }
    }

    class Print(val expression: Expr) : Stmt() {
        override fun accept(visitor: StmtVisitor) {
            visitor.visitPrintStmt(this)
        }
    }
}