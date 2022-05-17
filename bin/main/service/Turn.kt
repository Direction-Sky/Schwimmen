package service

/**
 * Enum for recognizable player actions.
 */
enum class Turn {
    PASS,
    KNOCK,
    CHANGEONE,
    CHANGEALL,
    ;

    override fun toString() = when(this) {
        Turn.PASS -> "Pass"
        Turn.KNOCK -> "Knock"
        Turn.CHANGEONE -> "Change one"
        Turn.CHANGEALL -> "Change all"
    }
}