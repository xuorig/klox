package klox

import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.exp
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size > 1) {
        println("Usage: klox [script]")
    } else if (args.size == 1) {
        Klox().runFile(args[0])
    } else {
        Klox().runPrompt()
    }
}

class Klox {
    fun runPrompt() {
        val reader = System.`in`.bufferedReader()

        while (true) {
            println("> ")
            val line = reader.readLine() ?: break
            runKlox(line)
            hadError = false
        }
    }

    fun runFile(path: String) {
        val bytes = Files.readAllBytes(Paths.get(path))
        runKlox(String(bytes, Charset.defaultCharset()))

        if (hadError) {
            exitProcess(65)
        }

        if (hadRuntimeError) {
            exitProcess(70)
        }
    }

    private fun runKlox(source: String) {
        val scanner = Scanner(source)
        val tokens = scanner.scanTokens()

        val parser = Parser(tokens as MutableList<Token>)
        val statements = parser.parse()

        if (hadError) return

        interpreter.interpret(statements)
    }

    companion object {
        private var hadRuntimeError: Boolean = false
        private var hadError: Boolean = false

        private val interpreter: Interpreter = Interpreter()

        fun error(line: Int, message: String) {
            report(line, "", message)
        }

        fun error(token: Token, message: String) {
            if (token.type == TokenType.EOF) {
                report(token.line, " at end", message)
            } else {
                report(token.line, " at '${token.lexeme}'", message)
            }
        }

        private fun report(line: Int, where: String, message: String) {
            println("[Line $line] Error $where: $message")
            hadError = true
        }

        fun runtimeError(error: RuntimeError) {
            println(error.message + "[line $error.token.line]")
            hadRuntimeError = true
        }
    }
}