package entity

/**
 * A simple class to store a player's names and position as well as the hand.
 */
class SchwimmenPlayer constructor(val name: String, val pos: Int) {
    var hasKnocked: Boolean = false
    val dealtHandCards: MutableList<SchwimmenCard> = mutableListOf()

    /**
     * Calculates how much the player's hand is worth based on the rules of the game.
     * @return Double
     */
    fun checkHandScore(): Double {
        var result: Double = 0.0

        // It's easier to prove that at least one card is different than to prove that all cards are similar
        val differentSuit: Boolean
        val differentValue: Boolean
        differentSuit = dealtHandCards.all {
            it.suit != dealtHandCards[0].suit
        }
        if(differentSuit) {
            differentValue = dealtHandCards.all {
                it.value != dealtHandCards[0].value
            }
        }
        else {
            differentValue = true
        }
        if(!differentSuit || !differentValue) {
                result = 30.5
        }
        else {
            for (card in dealtHandCards) {
                result += when(card.value) {
                    CardValue.SEVEN -> 7.0
                    CardValue.EIGHT -> 8.0
                    CardValue.NINE -> 9.0
                    CardValue.ACE -> 11.0
                    else -> 10.0
                }
            }
        }
        return result
    }
}