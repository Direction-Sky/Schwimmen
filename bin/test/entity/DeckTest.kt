package entity

import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.test.*

/**
 * Tester class for [Deck]
 */
class DeckTest {
    /**
     * Case one: deck has at least 3 cards available
     */
    @Test
    fun drawThreeCardsTestCaseOne() {
        val card1 = SchwimmenCard(CardSuit.CLUBS, CardValue.ACE)
        val card2 = SchwimmenCard(CardSuit.CLUBS, CardValue.NINE)
        val card3 = SchwimmenCard(CardSuit.CLUBS, CardValue.TEN)
        val card4 = SchwimmenCard(CardSuit.SPADES, CardValue.SEVEN)
        val deck = Deck(mutableListOf(card1, card2, card3, card4))
        assertEquals(4, deck.cards.size)
        println("Deck has ${deck.cards}")
        // val temp: MutableList<SchwimmenCard>? = deck.drawThreeCards()
        deck.drawThreeCards()?.let {
            println("$it have been drawn")
            assertEquals(1, deck.cards.size)
            assertEquals(3, it.size)
            assertEquals(card4, it[0])
            assertEquals(card3, it[1])
            assertEquals(card2, it[2])
            println("Deck has ${deck.cards}")
        }
    }

    /**
     * Case two: deck has less than 3 cards
     */
    @Test
    fun drawThreeCardsTestCaseTwo() {
        val card1 = SchwimmenCard(CardSuit.CLUBS, CardValue.ACE)
        val card2 = SchwimmenCard(CardSuit.CLUBS, CardValue.NINE)
        val deck = Deck(mutableListOf(card1, card2))
        assertEquals(2, deck.cards.size)
        // val temp: MutableList<SchwimmenCard>? = deck.drawThreeCards()
        deck.drawThreeCards().let {
            assertEquals(2, deck.cards.size)
            assertNull(it)
        }
    }
}