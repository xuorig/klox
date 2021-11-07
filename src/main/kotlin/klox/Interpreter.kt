package klox

class Interpreter : Visitor<Any?>, StmtVisitor {
    var environment: Environment = Environment()

    fun interpret(statements: List<Stmt>) {
       try {
           for (statement in statements) {
               statement.accept(this)
           }
       } catch (error: RuntimeError) {
           Klox.runtimeError(error)
       }
    }

    private fun stringify(value: Any?): String {
        if (value == null) return "nil"

        if (value is Double) {
            var text = value.toString()

            if (text.endsWith(".0")) {
                text = text.substring(0, text.length - 2)
            }

            return text
        }

        return value.toString()
    }

    override fun visitBinaryExpr(binary: Expr.Binary): Any? {
        val left = evaluate(binary.left)
        val right = evaluate(binary.right)

        when (binary.operator.type) {
            TokenType.MINUS -> {
                checkNumberOperands(binary.operator, left, right)
                return left as Double - right as Double
            }
            TokenType.PLUS -> {
                if (left is Double && right is Double) {
                    return left + right
                }

                if (left is String && right is String) {
                    return left + right
                }

                throw RuntimeError(binary.operator, "Operands must be two numbers or two strings.")
            }
            TokenType.SLASH -> {
                checkNumberOperands(binary.operator, left, right)
                return left as Double / right as Double
            }
            TokenType.STAR -> {
                checkNumberOperands(binary.operator, left, right)
                return left as Double * right as Double
            }
            TokenType.GREATER -> {
                checkNumberOperands(binary.operator, left, right)
                return left as Double > right as Double
            }
            TokenType.GREATER_EQUAL -> {
                checkNumberOperands(binary.operator, left, right)
                return left as Double >= right as Double
            }
            TokenType.LESS -> {
                checkNumberOperands(binary.operator, left, right)
                return (left as Double) < (right as Double)
            }
            TokenType.LESS_EQUAL -> {
                checkNumberOperands(binary.operator, left, right)
                return (left as Double) <= (right as Double)
            }
            TokenType.BANG_EQUAL -> return !isEqual(left, right)
            TokenType.EQUAL_EQUAL -> return isEqual(left, right)
            else -> return null
        }

        return null
    }

    private fun isEqual(left: Any?, right: Any?): Boolean {
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
            TokenType.MINUS -> {
                checkNumberOperand(unary.operator, right)
                return -(right as Double)
            }
            TokenType.BANG -> return !isTruthy(right)
            else -> return null
        }
    }

    private fun checkNumberOperand(operator: Token, operand: Any?) {
        if (operand !is Double) {
            throw RuntimeError(operator, "Operand must be a number.")
        }
    }

    private fun checkNumberOperands(operator: Token, left: Any?, right: Any?) {
        if (left !is Double || right !is Double) {
            throw RuntimeError(operator, "Operand must be numbers.")
        }
    }

    private fun evaluate(expression: Expr): Any? {
        return expression.accept(this)
    }

    private fun isTruthy(obj: Any?): Boolean {
        if (obj == null) return false
        if (obj is Boolean) return obj
        return true
    }

    override fun visitExpressionStmt(stmt: Stmt.Expression) {
        evaluate(stmt.expression)
    }

    override fun visitPrintStmt(stmt: Stmt.Print) {
        val value = evaluate(stmt.expression)
        println(stringify(value))
    }

    override fun visitVarStmt(arg: Stmt.Var) {
        if (arg.initializer != null) {
           val value = evaluate(arg.initializer)
            environment.define(arg.name.lexeme, value)
        } else {
            environment.define(arg.name.lexeme, null)
        }
    }

    override fun visitBlockStmt(block: Stmt.Block) {
        executeBlock(block.statements, Environment(environment))
    }

    override fun visitIfStmt(stmt: Stmt.If) {
        if (isTruthy(evaluate(stmt.condition))) {
            stmt.thenBranch.accept(this)
        } else if (stmt.elseBranch != null) {
            stmt.elseBranch.accept(this)
        }
    }

    override fun visitWhileStmt(stmt: Stmt.While) {
        while (isTruthy(evaluate(stmt.condition))) {
            stmt.body.accept(this)
        }
    }

    private fun executeBlock(statements: List<Stmt>, environment: Environment) {
        val previous = this.environment

        try {
            this.environment = environment

            for (statement in statements) {
                statement.accept(this)
            }
        } finally {
            this.environment = previous
        }

    }

    override fun visitVariableExpr(variable: Expr.Variable): Any? {
        return environment.get(variable.name)
    }

    override fun visitAssignExpr(assign: Expr.Assign): Any? {
        val value = evaluate(assign.value)
        environment.assign(assign.name, value)
        return value
    }

    override fun visitLogicalExpr(logical: Expr.Logical): Any? {
        val left = evaluate(logical.left)

        if (logical.operator.type == TokenType.OR) {
            if (isTruthy(left)) return left
        } else {
            if (!isTruthy(left)) return left
        }

        return evaluate(logical.right)
    }
}