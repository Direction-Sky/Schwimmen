package entity

import entity.CardSuit
import entity.CardValue
import entity.SchwimmenPlayer
import entity.SchwimmenCard
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.test.*

/**
 * Tester class for [SchwimmenGame]
 */
class SchwimmenGameTest {
    val player = SchwimmenPlayer("Test")
    val game: SchwimmenGame = SchwimmenGame(
        0,
        true,
        mutableListOf(),
        listOf(),
        player,
        Deck(mutableListOf())
    )

    @Test
    fun incrementPassCounterTest() {
        game.incrementPassCounter()
        assertEquals(1, game.passCounter())
        game.incrementPassCounter()
        assertEquals(2, game.passCounter())
    }
}