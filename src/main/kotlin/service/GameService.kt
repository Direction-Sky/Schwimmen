package service

import entity.*

class GameService {
    lateinit var rootService: RootService

    /**
     * Initialize players, create 32 cards using cross product, then shuffle and distribute cards
     */
    fun startGame(players: List<SchwimmenPlayer>): Unit {
        val deck = Deck(mutableListOf())
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
        for (player in players) {
            deck.drawThreeCards()?.forEach{
                player.handCards.add(it)
            }
        }
        val newGame = SchwimmenGame(
            0,
            true,
            deck.drawThreeCards()!!,
            players,
            players[0],
            deck
        )
        rootService = RootService(newGame, this)
    }

    fun isGameOver(): Boolean {
        return !rootService.retrieveCurrentGame().gameLoop
    }

    fun endGame(): Unit {
        rootService.retrieveCurrentGame().gameLoop = false
    }
}