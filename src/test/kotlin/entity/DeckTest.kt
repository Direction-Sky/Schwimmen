package entity

import entity.*
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.test.*

class DeckTest {
    val deck = Deck(mutableListOf())
    val card1 = SchwimmenCard(CardSuit.CLUBS, CardValue.ACE)
    val card2 = SchwimmenCard(CardSuit.CLUBS, CardValue.NINE)
    val card3 = SchwimmenCard(CardSuit.CLUBS, CardValue.TEN)
    val card4 = SchwimmenCard(CardSuit.SPADES, CardValue.SEVEN)

    @BeforeTest
    fun setUpCaseOne() {
        deck.cards.addAll(mutableListOf(card1, card2, card3, card4))
    }

    /**
     * Tests the drawing of three cards
     */
    @Test
    fun drawThreeCardsTestCaseOne() {
        assertEquals(4, deck.cards.size)
        val temp: MutableList<SchwimmenCard>? = deck.drawThreeCards()
        if (temp != null) {
            assertEquals(1, deck.cards.size)
            assertEquals(3, temp.size)
            assertEquals(card4, temp.get(0))
            assertEquals(card3, temp.get(1))
            assertEquals(card2, temp.get(2))
        }
        else {
            assertEquals(4, deck.cards.size)
            assertEquals(null, temp)
        }
    }
}