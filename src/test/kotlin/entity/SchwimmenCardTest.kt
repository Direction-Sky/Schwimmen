package entity

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

    /**
     * We need to test whether toString() is capable of producing suitable strings for each one
     * of our 32 cards. Since each card is unique, it represents a test case.
     */
    @Test
    fun toStringTest() {
        for (suit in suits) {
            for (value in CardValue.shortDeck()) {
                val card = SchwimmenCard(suit, value)
                // Test all clubs
                if(suit == CardSuit.CLUBS) {
                    if(value == CardValue.SEVEN) {
                        assertEquals("♣7", "$card")
                        println("Seven of clubs: $card")
                    }
                    if(value == CardValue.EIGHT) {
                        assertEquals("♣8", "$card")
                        println("Eight of clubs: $card")
                    }
                    if(value == CardValue.NINE) {
                        assertEquals("♣9", "$card")
                        println("Nine of clubs: $card")
                    }
                    if(value == CardValue.TEN) {
                        assertEquals("♣10", "$card")
                        println("Ten of clubs: $card")
                    }
                    if(value == CardValue.JACK) {
                        assertEquals("♣J", "$card")
                        println("Jack of clubs: $card")
                    }
                    if(value == CardValue.QUEEN) {
                        assertEquals("♣Q", "$card")
                        println("Queen of clubs: $card")
                    }
                    if(value == CardValue.KING) {
                        assertEquals("♣K", "$card")
                        println("King of clubs: $card")
                    }
                    if(value == CardValue.ACE) {
                        assertEquals("♣A", "$card")
                        println("Ace of clubs: $card")
                    }
                }
                // Test all spades
                if(suit == CardSuit.SPADES) {
                    if(value == CardValue.SEVEN) {
                        assertEquals("♠7", "$card")
                        println("Seven of clubs: $card")
                    }
                    if(value == CardValue.EIGHT) {
                        assertEquals("♠8", "$card")
                        println("Eight of spades: $card")
                    }
                    if(value == CardValue.NINE) {
                        assertEquals("♠9", "$card")
                        println("Nine of spades: $card")
                    }
                    if(value == CardValue.TEN) {
                        assertEquals("♠10", "$card")
                        println("Ten of spades: $card")
                    }
                    if(value == CardValue.JACK) {
                        assertEquals("♠J", "$card")
                        println("Jack of spades: $card")
                    }
                    if(value == CardValue.QUEEN) {
                        assertEquals("♠Q", "$card")
                        println("Queen of spades: $card")
                    }
                    if(value == CardValue.KING) {
                        assertEquals("♠K", "$card")
                        println("King of spades: $card")
                    }
                    if(value == CardValue.ACE) {
                        assertEquals("♠A", "$card")
                        println("Ace of spades: $card")
                    }
                }
                // Test all hearts
                if(suit == CardSuit.HEARTS) {
                    if(value == CardValue.SEVEN) {
                        assertEquals("♥7", "$card")
                        println("Seven of hearts: $card")
                    }
                    if(value == CardValue.EIGHT) {
                        assertEquals("♥8", "$card")
                        println("Eight of hearts: $card")
                    }
                    if(value == CardValue.NINE) {
                        assertEquals("♥9", "$card")
                        println("Nine of hearts: $card")
                    }
                    if(value == CardValue.TEN) {
                        assertEquals("♥10", "$card")
                        println("Ten of hearts: $card")
                    }
                    if(value == CardValue.JACK) {
                        assertEquals("♥J", "$card")
                        println("Jack of hearts: $card")
                    }
                    if(value == CardValue.QUEEN) {
                        assertEquals("♥Q", "$card")
                        println("Queen of hearts: $card")
                    }
                    if(value == CardValue.KING) {
                        assertEquals("♥K", "$card")
                        println("King of hearts: $card")
                    }
                    if(value == CardValue.ACE) {
                        assertEquals("♥A", "$card")
                        println("Ace of hearts: $card")
                    }
                }
                // Test all diamonds
                if(suit == CardSuit.DIAMONDS) {
                    if(value == CardValue.SEVEN) {
                        assertEquals("♦7", "$card")
                        println("Seven of diamonds: $card")
                    }
                    if(value == CardValue.EIGHT) {
                        assertEquals("♦8", "$card")
                        println("Eight of diamonds: $card")
                    }
                    if(value == CardValue.NINE) {
                        assertEquals("♦9", "$card")
                        println("Nine of diamonds: $card")
                    }
                    if(value == CardValue.TEN) {
                        assertEquals("♦10", "$card")
                        println("Ten of diamonds: $card")
                    }
                    if(value == CardValue.JACK) {
                        assertEquals("♦J", "$card")
                        println("Jack of diamonds: $card")
                    }
                    if(value == CardValue.QUEEN) {
                        assertEquals("♦Q", "$card")
                        println("Queen of diamonds: $card")
                    }
                    if(value == CardValue.KING) {
                        assertEquals("♦K", "$card")
                        println("King of diamonds: $card")
                    }
                    if(value == CardValue.ACE) {
                        assertEquals("♦A", "$card")
                        println("Ace of diamonds: $card")
                    }
                }
            }
        }
    }
}