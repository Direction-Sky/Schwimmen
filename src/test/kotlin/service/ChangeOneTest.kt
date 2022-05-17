package service

import entity.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

/**
 * Tester class for [PlayerActionService.changeOne].
 */
class ChangeOneTest {
    private val pList = listOf(
        SchwimmenPlayer("P1"),
        SchwimmenPlayer("P2"),
        SchwimmenPlayer("P3"),
        SchwimmenPlayer("P4")
    )

    /**
     * Test method for [PlayerActionService.changeOne].
     * Hand card and table card are randomly selected using [random].
     */
    @Test
    fun changeOneTest() {
        val rs = RootService()
        val gs = GameService(rs)
        gs.startGame(pList)
        val pas = PlayerActionService(gs.rootService)
        val oldTable = "${gs.rootService.currentGame!!.tableCards}"
        println("Table before swapping: $oldTable")
        val oldHand = "${gs.rootService.currentGame!!.players[0].handCards}"
        println("Hand before swapping: $oldHand")
        val hand = gs.rootService.currentGame!!.players[0].handCards.random()
        val table = gs.rootService.currentGame!!.tableCards.random()
        println("Swapping card $hand from hand with $table from table:")

        pas.changeOne(gs.rootService.currentGame!!.players[0], hand, table)

        assertNotEquals(oldTable, "${gs.rootService.currentGame!!.tableCards}")
        assertNotEquals(oldHand, "${gs.rootService.currentGame!!.players[0].handCards}")
        println("Table after swapping: ${gs.rootService.currentGame!!.tableCards}")
        println("Hand after swapping: ${gs.rootService.currentGame!!.players[0].handCards}")
        assertEquals(0, gs.rootService.currentGame!!.passCounter)
    }
}