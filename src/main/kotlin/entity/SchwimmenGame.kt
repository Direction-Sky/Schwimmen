package entity

class SchwimmenGame {
    private var passCounter: Int = 0
    private var gameLoop: Boolean = false
    private val tableCards: MutableList<SchwimmenCard> = mutableListOf()
    private val players: List<SchwimmenPlayer> = listOf()
    var currentPlayer: SchwimmenPlayer = players.get(0)

    // To initialize deck with an empty list of cards
    val startingCards: MutableList<SchwimmenCard> = mutableListOf()
    private val deck: Deck = Deck(startingCards)

    // Build the cross product in init block
    val suits: List<CardSuit> = listOf(
        CardSuit.CLUBS,
        CardSuit.SPADES,
        CardSuit.HEARTS,
        CardSuit.DIAMONDS
    )
    val values: List<CardValue> = listOf(
        CardValue.SEVEN,
        CardValue.EIGHT,
        CardValue.NINE,
        CardValue.TEN,
        CardValue.JACK,
        CardValue.QUEEN,
        CardValue.KING,
        CardValue.ACE
    )

    init {
        // Initialize players
        // ...

        // Initialize deck with 32 cards of all possible combinations then shuffle
        for (suit in suits) {
            for (value in values) {
                deck.cards.add(SchwimmenCard(suit, value))
            }
        }
        deck.cards.shuffle()

        deck.drawThreeCards()?.forEach{
            tableCards.add(deck.cards.last())
        }

        for (player in players) {
            deck.drawThreeCards()?.forEach{
                player.dealtHandCards.add(deck.cards.last())
            }
        }
    }

    fun incrementPassCounter(): Unit {
        passCounter += 1
    }

}