package klox

class RuntimeError(val token: Token, override val message: String) : Throwable() {
}
