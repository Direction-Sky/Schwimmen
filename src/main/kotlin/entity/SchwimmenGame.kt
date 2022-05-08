package entity

/**
 * This class is the basis of our data storage, it helps manage and keep track of the game state at any given point.
 * @param passCounter - is used to track the number of players who have passed in a row.
 * @param gameLoop - is the lever to end game.
 * @param tableCards - is a mutable list of 3 uncovered cards on the table.
 * @param players - is a list of participating players.
 * @param currentPlayer - to know whose turn it is.
 * @param deck - is a [Deck] object that is initialized with 32 cards.
 */
class SchwimmenGame constructor(
    private var passCounter: Int,
    private var gameLoop: Boolean,
    private val tableCards: MutableList<SchwimmenCard>,
    private val players: List<SchwimmenPlayer>,
    var currentPlayer: SchwimmenPlayer,
    private val deck: Deck) {

    /**
     * Getter method to retrieve the private attribute passCounter
     * @return Int
     */
    fun passCounter(): Int {
        return passCounter
    }

    /**
     * Initialize players, create 32 cards using cross product, then shuffle and distribute cards
     */
    init {
        // Initialize players..
        // ...

        val suits: List<CardSuit> = listOf(
            CardSuit.CLUBS,
            CardSuit.SPADES,
            CardSuit.HEARTS,
            CardSuit.DIAMONDS
        )
        for (suit in suits) {
            for (value in CardValue.shortDeck()) {
                deck.cards.add(SchwimmenCard(suit, value))
            }
        }
        deck.cards.shuffle()

        deck.drawThreeCards()?.forEach{
            tableCards.add(it)
        }
        for (player in players) {
            deck.drawThreeCards()?.forEach{
                player.handCards.add(it)
            }
        }
    }

    /**
     * This is used to model the game scenario where all players have passed
     */
    fun incrementPassCounter(): Unit {
        passCounter += 1
    }

}