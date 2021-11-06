package klox

class Parser(val tokens: MutableList<Token>) {
    var current: Int = 0

    private fun expression(): Expr {
        return equality()
    }

    private fun equality(): Expr {
        var expr = comparison()

        while (match(TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)) {
            val operator = previous()
            val right = comparison()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    private fun comparison(): Expr {
        var expr = term()

        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            val operator = previous()
            val right = term()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    private fun term(): Expr {
        var expr = factor()

        while (match(TokenType.MINUS, TokenType.PLUS)) {
            val operator = previous()
            val right = factor()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    private fun factor(): Expr {
        var expr = unary()

        while (match(TokenType.SLASH, TokenType.STAR)) {
            val operator = previous()
            val right = unary()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    private fun unary(): Expr {
        if (match(TokenType.BANG, TokenType.MINUS)) {
            val operator = previous()
            val right = unary()
            return Expr.Unary(operator, right)
        }

        return primary()
    }

    private fun primary(): Expr {
        if (match(TokenType.FALSE)) return Expr.Literal(false)
        if (match(TokenType.TRUE)) return Expr.Literal(true)
        if (match(TokenType.NIL)) return Expr.Literal(null)

        if (match(TokenType.NUMBER, TokenType.STRING)) {
            return Expr.Literal(previous().literal)
        }

        if (match(TokenType.LEFT_PAREN)) {
            val expr = expression()
            consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.")
            return Expr.Grouping(expr)
        }

        throw Exception("Expected Expression")
    }

    private fun consume(rightParen: TokenType, s: String) {

    }

    private fun match(vararg types: TokenType): Boolean {
        for (type in types) {
            if (checkType(type)) {
                advance()
                return true
            }
        }

        return false
    }

    private fun checkType(type: TokenType): Boolean {
        if (isAtEnd()) return false;
        return peek().type == type
    }

    private fun isAtEnd(): Boolean {
        return peek().type == TokenType.EOF
    }

    private fun peek(): Token {
        return tokens[current]
    }

    private fun advance(): Token {
        if (!isAtEnd()) current++
        return previous()
    }

    private fun previous(): Token {
        return tokens[current - 1]
    }
}