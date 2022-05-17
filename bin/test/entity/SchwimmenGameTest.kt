package entity

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