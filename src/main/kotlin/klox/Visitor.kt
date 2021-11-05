package klox

interface Visitor<T> {
    fun visitBinaryExpr(binary: Expr.Binary): T
    fun visitGroupingExpr(grouping: Expr.Grouping): T
    fun visitLiteralExpr(literal: Expr.Literal): T
    fun visitUnaryExpr(unary: Expr.Unary): T
}
