package service

import entity.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

/**
 * Tester class for [PlayerActionService.changeOne]
 */
class ChangeOneTest {
    private val pList = listOf(
        SchwimmenPlayer("P1"),
        SchwimmenPlayer("P2"),
        SchwimmenPlayer("P3"),
        SchwimmenPlayer("P4")
    )

    @Test
    fun changeOneTest() {
        val gs = GameService()
        gs.startGame(pList)
        val pas = PlayerActionService(gs.rootService)
        val oldTable = "${gs.rootService.currentGame.tableCards}"
        println("Table before swapping: $oldTable")
        val oldHand = "${gs.rootService.currentGame.players[0].handCards}"
        println("Hand before swapping: $oldHand")
        val hand = (0..2).random()
        val table = (0..2).random()
        println("Swapping card ${gs.rootService.currentGame.players[0].handCards[hand]} from hand with " +
                "${gs.rootService.currentGame.tableCards[table]} from table:")
        pas.changeOne(
            gs.rootService.currentGame.players[0],
            gs.rootService.currentGame.players[0].handCards[hand],
            gs.rootService.currentGame.tableCards[table]
        )
        assertNotEquals(oldTable, "${gs.rootService.currentGame.tableCards}")
        assertNotEquals(oldHand, "${gs.rootService.currentGame.players[0].handCards}")
        println("Table after swapping: ${gs.rootService.currentGame.tableCards}")
        println("Hand after swapping: ${gs.rootService.currentGame.players[0].handCards}")
        assertEquals(0, gs.rootService.currentGame.passCounter)
    }
}