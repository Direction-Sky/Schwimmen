package service

import entity.*

/**
 * This class is used to manage [SchwimmenGame], that represents the current state of the game.
 */
class GameService(val rootService: RootService): AbstractRefreshingService() {

    /**
     * Initialize players, create 32 cards using cross product, then shuffle and distribute cards.
     * @param players is the list of assigned players that will be forwarded in order to create
     * a [SchwimmenGame] instance.
     */
    fun startGame(players: List<SchwimmenPlayer>): SchwimmenGame {
        val deck = Deck(mutableListOf())
        for (suit in listOf(CardSuit.CLUBS, CardSuit.SPADES, CardSuit.HEARTS, CardSuit.DIAMONDS)) {
            for (value in CardValue.shortDeck()) {
                deck.cards.add(SchwimmenCard(suit, value))
            }
        }
        deck.cards.shuffle()
        for (player in players) {
            deck.drawThreeCards()?.forEach{
                player.handCards.add(it)
            }
        }
        deck.drawThreeCards()!!.let {
            val newGame = SchwimmenGame(0, true, it, players, deck)
            //rootService.currentGame = newGame
            onAllRefreshables { refreshAfterStartNewGame() }
            return newGame
        }
    }
}