package klox

import java.lang.RuntimeException

class Interpreter : Visitor<Any?> {
    override fun visitBinaryExpr(binary: Expr.Binary): Any? {
        val left = evaluate(binary.left)
        val right = evaluate(binary.right)

        when (binary.operator.type) {
            TokenType.MINUS -> return left as Double - right as Double
            TokenType.PLUS -> {
                if (left is Double && right is Double) {
                    return left + right
                }

                if (left is String && right is String) {
                    return left + right
                }
            }
            TokenType.SLASH -> return left as Double / right as Double
            TokenType.STAR -> return left as Double * right as Double
            TokenType.GREATER -> return left as Double > right as Double
            TokenType.GREATER_EQUAL -> return left as Double >= right as Double
            TokenType.LESS -> return (left as Double) < (right as Double)
            TokenType.LESS_EQUAL -> return (left as Double) <= (right as Double)
            TokenType.BANG_EQUAL -> return !isEqual(left, right)
            TokenType.EQUAL_EQUAL -> return isEqual(left, right)
            else -> return null
        }

        return null
    }

    private fun isEqual(left: Any?, right: Any?): Any {
        return left == right
    }

    override fun visitGroupingExpr(grouping: Expr.Grouping): Any? {
        return evaluate(grouping.expression)
    }

    override fun visitLiteralExpr(literal: Expr.Literal): Any? {
        return literal.literalValue
    }

    override fun visitUnaryExpr(unary: Expr.Unary): Any? {
        val right = evaluate(unary.right)

        when (unary.operator.type) {
            TokenType.MINUS -> return -(right as Double)
            TokenType.BANG -> return !isTruthy(right)
            else -> return null
        }
    }

    private fun evaluate(expression: Expr): Any? {
        return expression.accept(this)
    }

    private fun isTruthy(obj: Any?): Boolean {
        if (obj == null) return false
        if (obj is Boolean) return obj
        return true
    }}