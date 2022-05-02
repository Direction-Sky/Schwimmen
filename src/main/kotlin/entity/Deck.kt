package entity

class Deck constructor(val cards: MutableList<SchwimmenCard>) {
    /**
     * Assumption is that last covered card has highest index
     * @return MutableList<SchwimmenCard>, or null if size < 3
     */
    fun drawThreeCards(): MutableList<SchwimmenCard>? {
        require(cards.size > 2) { return null }
        val result: MutableList<SchwimmenCard> = mutableListOf()
        for(i in 1..3) {
            result.add(cards.removeAt(cards.lastIndex))
        }
        return result
    }

    fun drawOneCard(): SchwimmenCard? {
        require(cards.size > 0) { return null }
        return cards.removeAt(cards.lastIndex)
    }
}