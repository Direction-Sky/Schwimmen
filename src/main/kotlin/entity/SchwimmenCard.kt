package entity

/**
 * A game card object. All 32 Schwimmen cards are unique and belong to the cross product of:
 * {Kreuz, Pik, Herz, Karo} x {7, 8, 9, 10, Bube, Dame, König, Ass}.
 */
data class SchwimmenCard constructor(val suit:CardSuit, val value: CardValue) {
    override fun toString() = "$suit$value"
}