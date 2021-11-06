package klox

class Scanner(val source: String) {
    private var start: Int = 0
    private var current: Int = 0
    private var line: Int = 1

    private val tokens: MutableList<Token> = mutableListOf()

    fun scanTokens(): List<Token> {
        while(!isAtEnd()) {
            start = current
            scanToken()
        }

        tokens.add(Token(TokenType.EOF, "", null, line))

        return tokens
    }

    private fun scanToken() {
        when (val c = advance()) {
            '(' -> addToken(TokenType.LEFT_PAREN)
            ')' -> addToken(TokenType.RIGHT_PAREN)
            '{' -> addToken(TokenType.LEFT_BRACE)
            '}' -> addToken(TokenType.RIGHT_BRACE)
            ',' -> addToken(TokenType.COMMA)
            '.' -> addToken(TokenType.DOT)
            '-' -> addToken(TokenType.MINUS)
            '+' -> addToken(TokenType.PLUS)
            ';' -> addToken(TokenType.SEMICOLON)
            '*' -> addToken(TokenType.STAR)

            '!' -> {
                if (match('=')) {
                    addToken(TokenType.BANG_EQUAL)
                } else {
                    addToken(TokenType.BANG)
                }
            }

            '=' -> {
                if (match('=')) {
                    addToken(TokenType.EQUAL_EQUAL)
                } else {
                    addToken(TokenType.EQUAL)
                }
            }

            '<' -> {
                if (match('=')) {
                    addToken(TokenType.LESS_EQUAL)
                } else {
                    addToken(TokenType.LESS)
                }
            }

            '>' -> {
                if (match('=')) {
                    addToken(TokenType.GREATER_EQUAL)
                } else {
                    addToken(TokenType.GREATER)
                }
            }

            '/' -> {
                if (match('/')) {
                    while (peek() != '\n' && !isAtEnd()) advance()
                } else {
                    addToken(TokenType.SLASH)
                }
            }

            ' ' -> {}
            '\r' -> {}
            '\t' -> {}
            '\n' -> line++

            '"' -> string()

            'o' -> {
                if (match('r')) {
                    addToken(TokenType.OR)
                }
            }

            else -> {
                if (Character.isDigit(c)) {
                    number()
                } else if (isAlpha(c)) {
                    identifier()
                } else {
                    Klox.error(line, "Unexpected character.")
                }
            }
        }
    }

    private fun identifier() {
        while (isAlpha(peek()) || Character.isDigit(peek())) advance()

        val text = source.substring(start, current)
        var type = keywords[text]

        if (type == null) {
            type = TokenType.IDENTIFIER
        }

        addToken(type)
    }

    private fun isAlpha(c: Char): Boolean {
        return Character.isAlphabetic(c.code)
    }

    private fun number() {
        while (Character.isDigit(peek())) advance()

        if (peek() == '.' && Character.isDigit(peekNext())) {
            advance()

            while (Character.isDigit(peek())) advance()
        }

        addToken(TokenType.NUMBER, source.substring(start, current).toDouble())
    }

    private fun peekNext(): Char {
        if (current + 1 >= source.length) return '\u0000'
        return source[current + 1]
    }

    private fun string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++

            advance()
        }

        if (isAtEnd()) {
            Klox.error(line, "Unterminated string.")
            return
        }

        advance() // closing "

        val strValue = source.substring(start + 1, current - 1)
        addToken(TokenType.STRING, strValue)
    }

    private fun peek(): Char {
        if (isAtEnd()) {
            return '\u0000'
        }
        return source[current]

    }

    private fun match(c: Char): Boolean {
        if (isAtEnd()) return false
        if (source[current] != c) return false

        current++
        return true
    }

    private fun addToken(type: TokenType, literal: Any? = null) {
        val text = source.substring(start, current)
        tokens.add(Token(type, text, literal, line))
    }

    private fun advance(): Char {
        return source[current++]

    }

    private fun isAtEnd(): Boolean {
        return current >= source.length
    }

    companion object {
        val keywords = hashMapOf<String, TokenType>(
            "and" to TokenType.AND,
            "class" to TokenType.CLASS,
            "else" to TokenType.ELSE,
            "false" to TokenType.FALSE,
            "for" to TokenType.FOR,
            "fun" to TokenType.FUN,
            "if" to TokenType.IF,
            "nil" to TokenType.NIL,
            "or" to TokenType.OR,
            "print" to TokenType.PRINT,
            "return" to TokenType.RETURN,
            "super" to TokenType.SUPER,
            "this" to TokenType.THIS,
            "true" to TokenType.TRUE,
            "var" to TokenType.VAR,
            "while" to TokenType.WHILE,
        )
    }
}