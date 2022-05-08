package entity

import kotlin.test.*

/**
 * Test class for [SchwimmenPlayer]
 */
class SchwimmenPlayerTest {
    val player = SchwimmenPlayer("John", 0)
    val card1 = SchwimmenCard(CardSuit.CLUBS, CardValue.SEVEN)
    val card2 = SchwimmenCard(CardSuit.CLUBS, CardValue.NINE)
    val card3 = SchwimmenCard(CardSuit.CLUBS, CardValue.TEN)
    val card4 = SchwimmenCard(CardSuit.HEARTS, CardValue.SEVEN)
    val card5 = SchwimmenCard(CardSuit.SPADES, CardValue.ACE)
    val card6 = SchwimmenCard(CardSuit.SPADES, CardValue.JACK)
    val card7 = SchwimmenCard(CardSuit.SPADES, CardValue.SEVEN)
    val card8 = SchwimmenCard(CardSuit.SPADES, CardValue.TEN)


    /**
     * Test case 1: cards have same value -> 30.5
     */
    @Test
    fun checkHandScoreTestCaseOne() {
        player.handCards.addAll(mutableListOf(card1, card4, card7))
        println("${player.handCards} make ${player.checkHandScore()} combined.")
        assertEquals(30.5, player.checkHandScore())
        player.handCards.clear()
    }

    /**
     * Test case 2: all 3 cards share the same suit. In this example:
     */
    @Test
    fun checkHandScoreTestCaseTwo() {
        player.handCards.addAll(mutableListOf(card5, card6, card8))
        println("${player.handCards} make ${player.checkHandScore()} combined.")
        assertEquals(31.0, player.checkHandScore())
        player.handCards.clear()
    }

    /**
     * Test case 3: cards 1 and 2 share the same suit
     */
    @Test
    fun checkHandScoreTestCaseThree() {
        player.handCards.addAll(mutableListOf(card1, card2, card4))
        println("${player.handCards} make ${player.checkHandScore()} combined.")
        assertEquals(16.0, player.checkHandScore())
        player.handCards.clear()
    }

    /**
     * Test case 4: cards 1 and 3 share the same suit
     */
    @Test
    fun checkHandScoreTestCaseFour() {
        player.handCards.addAll(mutableListOf(card1, card4, card2))
        println("${player.handCards} make ${player.checkHandScore()} combined.")
        assertEquals(16.0, player.checkHandScore())
        player.handCards.clear()
    }

    /**
     * Test case 5: cards 2 and 3 share the same suit
     */
    @Test
    fun checkHandScoreTestCaseFive() {
        player.handCards.addAll(mutableListOf(card4, card1, card2))
        println("${player.handCards} make ${player.checkHandScore()} combined.")
        assertEquals(16.0, player.checkHandScore())
        player.handCards.clear()
    }

    /**
     * Test case 6: all cards have different suits
     */
    @Test
    fun checkHandScoreTestCaseSix() {
        player.handCards.addAll(mutableListOf(card3, card4, card5))
        println("${player.handCards} make ${player.checkHandScore()} combined.")
        assertEquals(11.0, player.checkHandScore())
        player.handCards.clear()
    }
}