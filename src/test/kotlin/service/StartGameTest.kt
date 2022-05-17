package service

import entity.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * A test class for the method [GameService.startGame]
 */
class StartGameTest {
    /**
     * Start game test
     */
    @Test
    fun startGameTest() {
        val pList = listOf(
            SchwimmenPlayer("P1"),
            SchwimmenPlayer("P2"),
            SchwimmenPlayer("P3"),
            SchwimmenPlayer("P4")
        )
        val rs = RootService()
        val gs = GameService(rs)
        gs.startGame(pList)
        assertTrue(gs.rootService.currentGame!!.gameLoop)
        assertEquals(4, gs.rootService.currentGame!!.players.size)
        assertEquals("P1", gs.rootService.currentGame!!.players[0].name)
        assertEquals("P2", gs.rootService.currentGame!!.players[1].name)
        assertEquals("P3", gs.rootService.currentGame!!.players[2].name)
        assertEquals("P4", gs.rootService.currentGame!!.players[3].name)
        assertEquals(17, gs.rootService.currentGame!!.deck.cards.size)
    }
}