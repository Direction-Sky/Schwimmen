package entity

/**
 * This class is the basis of our data storage, it helps manage and keep track of the game state at any given point.
 * @param passCounter is used to track the number of players who have passed in a row.
 * @param gameLoop true means game is still running. Can be modified.
 * @param tableCards is a mutable list of 3 uncovered cards on the table.
 * @param players is a list of participating players.
 * @param deck is a [Deck] object that is initialized with 32 cards.
 */
class SchwimmenGame constructor(
    var passCounter: Int,
    var gameLoop: Boolean,
    val tableCards: MutableList<SchwimmenCard>,
    val players: List<SchwimmenPlayer>,
    val deck: Deck) {

    /**
     * This is used to model the game scenario where all players have passed
     */
    fun incrementPassCounter() {
        passCounter += 1
    }
}