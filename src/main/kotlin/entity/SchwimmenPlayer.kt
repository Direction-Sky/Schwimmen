package entity

class SchwimmenPlayer constructor(val name: String, val pos: Int) {
    var hasKnocked: Boolean = false
    val dealtHandCards: MutableList<SchwimmenCard> = mutableListOf()

    fun checkHandScore(): Double {
        var result: Double = 0.0
        // Do maths
        return result
    }
}