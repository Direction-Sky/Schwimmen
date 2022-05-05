package entity

import kotlin.test.*

/**
 * Test class for [SchwimmenPlayer]
 */
class SchwimmenPlayerTest {
    val player = SchwimmenPlayer("John", 0)
    val card1 = SchwimmenCard(CardSuit.CLUBS, CardValue.ACE)
    val card2 = SchwimmenCard(CardSuit.CLUBS, CardValue.NINE)
    val card3 = SchwimmenCard(CardSuit.CLUBS, CardValue.TEN)
    val card4 = SchwimmenCard(CardSuit.SPADES, CardValue.SEVEN)

    @Test
    fun checkHandScoreTestCaseOne() {
        // Test: similar cards -> handscore = 30.5
        player.dealtHandCards.addAll(mutableListOf(card1, card2, card3))
        println("${player.dealtHandCards} make ${player.checkHandScore()} combined.")
        assertEquals(30.5, player.checkHandScore())
        player.dealtHandCards.clear()
    }

    @Test
    fun checkHandScoreTestCaseTwo() {
        player.dealtHandCards.addAll(mutableListOf(card2, card3, card4))
        println("${player.dealtHandCards} make ${player.checkHandScore()} combined.")
        assertEquals(26.0, player.checkHandScore())
        player.dealtHandCards.clear()
    }

    @Test
    fun checkHandScoreTestCaseThree() {
        player.dealtHandCards.addAll(mutableListOf(card1, card3, card4))
        println("${player.dealtHandCards} make ${player.checkHandScore()} combined.")
        assertEquals(28.0, player.checkHandScore())
        player.dealtHandCards.clear()
    }

    @Test
    fun checkHandScoreTestCaseFour() {
        player.dealtHandCards.addAll(mutableListOf(card1, card2, card4))
        println("${player.dealtHandCards} make ${player.checkHandScore()} combined.")
        assertEquals(27.0, player.checkHandScore())
        player.dealtHandCards.clear()
    }
}