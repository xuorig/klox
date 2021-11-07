package klox

fun main() {
    val expr = Expr.Binary(
        Expr.Unary(
            Token(TokenType.MINUS, "-", null, 1),
            Expr.Literal(123)
        ),
        Token(TokenType.STAR, "*", null, 1),
        Expr.Grouping(Expr.Literal(45.67))
    )

    val printed = AstPrinter().print(expr)
    println(printed)
}

class AstPrinter : Visitor<String> {
    fun print(expr: Expr): String {
        return expr.accept(this)
    }

    override fun visitBinaryExpr(binary: Expr.Binary): String {
        return parenthesize(binary.operator.lexeme, binary.left, binary.right)
    }

    override fun visitGroupingExpr(grouping: Expr.Grouping): String {
        return parenthesize("group", grouping.expression)
    }

    override fun visitLiteralExpr(literal: Expr.Literal): String {
        return literal.literalValue.toString()
    }

    override fun visitUnaryExpr(unary: Expr.Unary): String {
        return parenthesize(unary.operator.lexeme, unary.right)
    }

    private fun parenthesize(name: String, vararg exprs: Expr): String {
        val builder = StringBuilder()

        builder.append("(").append(name)

        for (expr in exprs) {
            builder.append(" ")
            builder.append(expr.accept(this))
        }

        builder.append(")")

        return builder.toString()
    }

    override fun visitVariableExpr(variable: Expr.Variable): String {
        TODO("Not yet implemented")
    }

    override fun visitAssignExpr(assign: Expr.Assign): String {
        TODO("Not yet implemented")
    }

    override fun visitLogicalExpr(logical: Expr.Logical): String {
        TODO("Not yet implemented")
    }
}