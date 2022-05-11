package entity


/**
 * A simple class to store a player's names and position as well as the hand.
 */
class SchwimmenPlayer constructor(val name: String) {
    var hasKnocked: Boolean = false
    val handCards: MutableList<SchwimmenCard> = mutableListOf()

    override fun toString(): String {
        return name
    }

    /**
     * Calculates how much the player's hand is worth based on the rules of the game.
     * Since a card value ranges between 7 and 11, no single card value can be greater than
     * the sum value of a combination of two, therefore there is no need to find the maximum.
     * We recognize following cases for any player at any point of the game:
     * 1. All 3 cards have same value -> 30.5
     * 2. Cards 1, 2 and 3 have the same suit
     * 3. Cards 1 and 2 have the same suit
     * 4. Cards 1 and 3 have the same suit
     * 5. Cards 2 and 3 have the same suit
     * 6. All cards have different suits.
     * @return 30.5 if all cards have the same value, or the highest sum of cards of a suit
     */
    fun checkHandScore(): Double {
        var result: Double = 0.0
        // Case 1
        if(handCards.all{it.value.equals(handCards.get(0).value)}) {
            return 30.5
        }
        // Case 2
        else if(handCards.all {
                result += it.points()
                it.suit.equals(handCards.get(0).suit)}){
            return result
        }
        // Case 3
        else if(handCards[0].suit.equals(handCards[1].suit)) {
            return result - handCards[2].points()
        }
        // Case 4
        else if(handCards[0].suit.equals(handCards[2].suit)) {
            return result - handCards[1].points() + handCards[2].points()
        }
        // Case 5
        else if(handCards[1].suit.equals(handCards[2].suit)) {
            return handCards[1].points() + handCards[2].points()
        }
        // Case 6
        else {
            return maxOf(handCards[0].points(), handCards[1].points(), handCards[2].points())
        }
    }
}