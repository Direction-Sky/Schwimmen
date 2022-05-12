package service

import entity.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

/**
 * Tester class for [PlayerActionService.changeAll]
 */
class ChangeAllTest {
    private val pList = listOf(
        SchwimmenPlayer("P1"),
        SchwimmenPlayer("P2"),
        SchwimmenPlayer("P3"),
        SchwimmenPlayer("P4")
    )

    @Test
    fun changeAllTest() {
        val gs = GameService()
        gs.startGame(pList)
        val pas = PlayerActionService(gs.rootService)
        val oldTable = "${gs.rootService.currentGame.tableCards}"
        println("Table before swapping all: $oldTable")
        val oldHand = "${gs.rootService.currentGame.players[0].handCards}"
        println("Hand before swapping all: $oldHand")
        pas.changeAll(gs.rootService.currentGame.players[0])
        println("Table after swapping all: ${gs.rootService.currentGame.tableCards}")
        println("Hand after swapping all: ${gs.rootService.currentGame.players[0].handCards}")
        assertNotEquals(oldTable,"${gs.rootService.currentGame.tableCards}")
        assertNotEquals(oldHand,"${gs.rootService.currentGame.players[0].handCards}")
        assertEquals(0, gs.rootService.currentGame.passCounter)
    }
}