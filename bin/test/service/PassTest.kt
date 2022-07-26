package service

import entity.*
import kotlin.test.*

/**
 * Tester class for [PlayerActionService.pass].
 */
class PassTest {
    private val pList = listOf(
        SchwimmenPlayer("P1"),
        SchwimmenPlayer("P2"),
        SchwimmenPlayer("P3"),
        SchwimmenPlayer("P4")
    )
    val rs = RootService()

    /**
     * Case one: [SchwimmenGame.passCounter] to be incremented by 1 for each pass.
     */
    @Test
    fun passTestCaseOne() {
        val gs = GameService(rs)
        rs.currentGame = gs.startGame(pList)
        val pas = PlayerActionService(gs.rootService)
        // Before pass
        assertEquals(0, gs.rootService.currentGame!!.passCounter)
        pas.pass()
        pas.pass()
        pas.pass()
        // After 3 passes
        assertEquals(3, gs.rootService.currentGame!!.passCounter)
    }

    /**
     * Case two: all players have passed -> draw 3 new cards.
     */
    @Test
    fun passTestCaseTwo() {
        val gs = GameService(rs)
        rs.currentGame = gs.startGame(pList)
        val pas = PlayerActionService(gs.rootService)
        // Before passing
        assertEquals(0, gs.rootService.currentGame!!.passCounter, "Before pass")
        pas.pass()
        pas.pass()
        pas.pass()
        val oldCards = "${gs.rootService.currentGame!!.tableCards}"   // To check new cards
        val oldSize: Int = gs.rootService.currentGame!!.deck.cards.size
        // 3 players have passed
        assertEquals(3, gs.rootService.currentGame!!.passCounter, "3 players passed")
        assertEquals(17, gs.rootService.currentGame!!.deck.cards.size)
        pas.pass()
        // 4 players have passed in a row -> reset pass counter + draw 3 new cards
        assertEquals(0, gs.rootService.currentGame!!.passCounter, "4 players passed in a row")
        assertNotEquals(oldCards, "${gs.rootService.currentGame!!.tableCards}", "New cards on the table")
        assertEquals(oldSize - 3, gs.rootService.currentGame!!.deck.cards.size, "Deck now has 3 cards less")
    }

    /**
     * Case three: all players have passed, and < 3 cards on the deck -> game over.
     */
    @Test
    fun passTestCaseThree() {
        val gs = GameService(rs)
        rs.currentGame = gs.startGame(pList)
        val pas = PlayerActionService(gs.rootService)
        // Before passing
        assertEquals(0, gs.rootService.currentGame!!.passCounter, "Before pass")
        pas.pass()
        pas.pass()
        pas.pass()
        // 3 players have passed
        assertEquals(3, gs.rootService.currentGame!!.passCounter, "3 players passed")
        // Let's assume there are only 2 cards left on the deck by removing 15 cards
        for(i in 1..15) gs.rootService.currentGame!!.deck.cards.removeLast()
        pas.pass()
        // 4 players have passed in a row -> insufficient cards -> end game
        assertFalse(gs.rootService.currentGame!!.gameLoop, "Insufficient cards! Game over.")
    }
}