package klox

interface Visitor<T> {
    fun visitBinaryExpr(binary: Expr.Binary): T
    fun visitGroupingExpr(grouping: Expr.Grouping): T
    fun visitLiteralExpr(literal: Expr.Literal): T
    fun visitUnaryExpr(unary: Expr.Unary): T
    fun visitVariableExpr(variable: Expr.Variable): T
    fun visitAssignExpr(assign: Expr.Assign): T
    fun visitLogicalExpr(logical: Expr.Logical): T
}
