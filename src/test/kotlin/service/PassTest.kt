package service

import entity.*
import kotlin.test.*

/**
 * A tester class for [PlayerActionService.pass]
 */
class PassTest {
    /**
     * Case one: [SchwimmenGame.passCounter] to be incremented by 1 for each pass
     */
    @Test
    fun PassTestCaseOne() {
        val pList: List<SchwimmenPlayer> = listOf(
            SchwimmenPlayer("P1"),
            SchwimmenPlayer("P2"),
            SchwimmenPlayer("P3"),
            SchwimmenPlayer("P4")
        )
        val gs: GameService = GameService()
        gs.startGame(pList)
        val pas: PlayerActionService = PlayerActionService(gs.rootService)
        // Before pass
        assertEquals(0, gs.rootService.currentGame.passCounter)
        pas.pass(pList[0])
        pas.pass(pList[1])
        pas.pass(pList[2])
        // After 3 passes
        assertEquals(3, gs.rootService.currentGame.passCounter)
    }

    @Test
    fun passTestCaseTwo() {
        val pList: List<SchwimmenPlayer> = listOf(
            SchwimmenPlayer("P1"),
            SchwimmenPlayer("P2"),
            SchwimmenPlayer("P3"),
            SchwimmenPlayer("P4")
        )
        val gs: GameService = GameService()
        gs.startGame(pList)
        val pas: PlayerActionService = PlayerActionService(gs.rootService)
        // Before passing
        assertEquals(0, gs.rootService.currentGame.passCounter, "Before pass")
        pas.pass(pList[0])
        pas.pass(pList[1])
        pas.pass(pList[2])
        val oldCards: String = "${gs.rootService.currentGame.tableCards}"   // To check new cards
        val oldSize: Int = gs.rootService.currentGame.deck.cards.size
        // 3 players have passed
        assertEquals(3, gs.rootService.currentGame.passCounter, "3 players passed")
        assertEquals(17, gs.rootService.currentGame.deck.cards.size)
        pas.pass(pList[3])
        // 4 players have passed in a row -> reset pass counter + draw 3 new cards
        assertEquals(0, gs.rootService.currentGame.passCounter, "4 players passed in a row")
        assertNotEquals(oldCards, "${gs.rootService.currentGame.tableCards}", "New cards on the table")
        assertEquals(oldSize - 3, gs.rootService.currentGame.deck.cards.size, "Deck now has 3 cards less")
    }

    /**
     * Case three: all players have passed, and < 3 cards on the deck -> game over
     */
    @Test
    fun passTestCaseThree() {
        val pList: List<SchwimmenPlayer> = listOf(
            SchwimmenPlayer("P1"),
            SchwimmenPlayer("P2"),
            SchwimmenPlayer("P3"),
            SchwimmenPlayer("P4")
        )
        val gs: GameService = GameService()
        gs.startGame(pList)
        val pas: PlayerActionService = PlayerActionService(gs.rootService)
        // Before passing
        assertEquals(0, gs.rootService.currentGame.passCounter, "Before pass")
        pas.pass(pList[0])
        pas.pass(pList[1])
        pas.pass(pList[2])
        val oldList: List<SchwimmenCard> = gs.rootService.currentGame.tableCards
        val oldSize: Int = gs.rootService.currentGame.tableCards.size
        // 3 players have passed
        assertEquals(3, gs.rootService.currentGame.passCounter, "3 players passed")
        // Let's assume there are only 2 cards left on the deck by removing 15 cards
        for(i in 1..15) {
            gs.rootService.currentGame.deck.cards.removeLast()
        }
        pas.pass(pList[3])
        // 4 players have passed in a row -> insufficient cards -> end game
        assertFalse(gs.rootService.currentGame.gameLoop, "Insufficient cards! Game over.")
    }
}