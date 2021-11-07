package klox

interface StmtVisitor {
    fun visitExpressionStmt(stmt: Stmt.Expression)
    fun visitPrintStmt(stmt: Stmt.Print)
    fun visitVarStmt(arg: Stmt.Var)
    fun visitBlockStmt(block: Stmt.Block)
    fun visitIfStmt(arg: Stmt.If)
    fun visitWhileStmt(arg: Stmt.While)
}
