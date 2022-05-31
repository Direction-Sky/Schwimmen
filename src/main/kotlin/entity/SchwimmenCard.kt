package entity

/**
 * A game card object. All 32 Schwimmen cards are unique and belong to the cross product of:
 * {Kreuz, Pik, Herz, Karo} x {7, 8, 9, 10, Bube, Dame, König, Ass}.
 */
data class SchwimmenCard constructor(val suit:CardSuit, val value: CardValue) {
    override fun toString() = "$suit$value"

    /**
     * A simple function that assigns each card a value according [CardValue] and game rules.
     */
    fun points(): Double {
        return when(value) {
            CardValue.SEVEN -> 7.0
            CardValue.EIGHT -> 8.0
            CardValue.NINE -> 9.0
            CardValue.ACE -> 11.0
            else -> 10.0
        }
    }
}