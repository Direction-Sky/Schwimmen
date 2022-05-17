package entity

/**
 * The covered card on top has the greatest index.
 * @param cards - contains all the cards.
 */
class Deck constructor(val cards: MutableList<SchwimmenCard>) {
    /**
     * Removes the three covered cards on top of the deck and returns them as a mutable list.
     * If the deck contains less than 3 cards, null will be returned.
     * @return MutableList<SchwimmenCard>?
     */
    fun drawThreeCards(): MutableList<SchwimmenCard>? {
        require(cards.size > 2) { return null }
        val result: MutableList<SchwimmenCard> = mutableListOf()
        for(i in 1..3) {
            result.add(cards.removeAt(cards.lastIndex))
        }
        return result
    }
}