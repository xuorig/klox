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

    class Var(val name: Token, val initializer: Expr?) : Stmt() {
        override fun accept(visitor: StmtVisitor) {
            visitor.visitVarStmt(this)
        }
    }

    class Block(val statements: List<Stmt>) : Stmt() {
        override fun accept(visitor: StmtVisitor) {
            visitor.visitBlockStmt(this)
        }
    }

    class If(val condition: Expr, val thenBranch: Stmt, val elseBranch: Stmt?) : Stmt() {
        override fun accept(visitor: StmtVisitor) {
            visitor.visitIfStmt(this)
        }
    }

    class While(val condition: Expr, val body: Stmt) : Stmt() {
        override fun accept(visitor: StmtVisitor) {
            visitor.visitWhileStmt(this)
        }
    }
}