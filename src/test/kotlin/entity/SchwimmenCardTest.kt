package entity

import entity.CardSuit
import entity.CardValue
import entity.SchwimmenPlayer
import entity.SchwimmenCard
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.test.*

/**
 * Tester class for [SchwimmenCard]
 */
class SchwimmenCardTest {

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
    @Test
    fun toStringTest() {
        for (suit in suits) {
            for (value in values) {
                val card = SchwimmenCard(suit, value)
                if(suit == CardSuit.CLUBS) {
                    if(value == CardValue.SEVEN) {
                        assertEquals("♣7", "$card")
                    }
                    if(value == CardValue.EIGHT) {
                        assertEquals("♣8", "$card")
                    }
                    if(value == CardValue.NINE) {
                        assertEquals("♣9", "$card")
                    }
                    if(value == CardValue.TEN) {
                        assertEquals("♣10", "$card")
                    }
                    if(value == CardValue.JACK) {
                        assertEquals("♣J", "$card")
                    }
                    if(value == CardValue.QUEEN) {
                        assertEquals("♣Q", "$card")
                    }
                    if(value == CardValue.KING) {
                        assertEquals("♣K", "$card")
                    }
                    if(value == CardValue.ACE) {
                        assertEquals("♣A", "$card")
                    }
                }

                if(suit == CardSuit.SPADES) {
                    if(value == CardValue.SEVEN) {
                        assertEquals("♠7", "$card")
                    }
                    if(value == CardValue.EIGHT) {
                        assertEquals("♠8", "$card")
                    }
                    if(value == CardValue.NINE) {
                        assertEquals("♠9", "$card")
                    }
                    if(value == CardValue.TEN) {
                        assertEquals("♠10", "$card")
                    }
                    if(value == CardValue.JACK) {
                        assertEquals("♠J", "$card")
                    }
                    if(value == CardValue.QUEEN) {
                        assertEquals("♠Q", "$card")
                    }
                    if(value == CardValue.KING) {
                        assertEquals("♠K", "$card")
                    }
                    if(value == CardValue.ACE) {
                        assertEquals("♠A", "$card")
                    }
                }

                if(suit == CardSuit.HEARTS) {
                    if(value == CardValue.SEVEN) {
                        assertEquals("♥7", "$card")
                    }
                    if(value == CardValue.EIGHT) {
                        assertEquals("♥8", "$card")
                    }
                    if(value == CardValue.NINE) {
                        assertEquals("♥9", "$card")
                    }
                    if(value == CardValue.TEN) {
                        assertEquals("♥10", "$card")
                    }
                    if(value == CardValue.JACK) {
                        assertEquals("♥J", "$card")
                    }
                    if(value == CardValue.QUEEN) {
                        assertEquals("♥Q", "$card")
                    }
                    if(value == CardValue.KING) {
                        assertEquals("♥K", "$card")
                    }
                    if(value == CardValue.ACE) {
                        assertEquals("♥A", "$card")
                    }
                }

                if(suit == CardSuit.DIAMONDS) {
                    if(value == CardValue.SEVEN) {
                        assertEquals("♦7", "$card")
                    }
                    if(value == CardValue.EIGHT) {
                        assertEquals("♦8", "$card")
                    }
                    if(value == CardValue.NINE) {
                        assertEquals("♦9", "$card")
                    }
                    if(value == CardValue.TEN) {
                        assertEquals("♦10", "$card")
                    }
                    if(value == CardValue.JACK) {
                        assertEquals("♦J", "$card")
                    }
                    if(value == CardValue.QUEEN) {
                        assertEquals("♦Q", "$card")
                    }
                    if(value == CardValue.KING) {
                        assertEquals("♦K", "$card")
                    }
                    if(value == CardValue.ACE) {
                        assertEquals("♦A", "$card")
                    }
                }
            }
        }
    }
}