package klox

interface StmtVisitor {
    fun visitExpressionStmt(stmt: Stmt.Expression)
    fun visitPrintStmt(stmt: Stmt.Print)
}
