package service

import entity.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

/**
 * Tester class for [PlayerActionService.knock]
 */
class KnockTest {
    private val pList = listOf(
        SchwimmenPlayer("P1"),
        SchwimmenPlayer("P2"),
        SchwimmenPlayer("P3"),
        SchwimmenPlayer("P4")
    )

    @Test
    fun knockTest() {
        val gs = GameService()
        gs.startGame(pList)
        val pas = PlayerActionService(gs.rootService)
        pas.pass(pList[0])
        assertEquals(1, gs.rootService.currentGame.passCounter)
        assertEquals(0, pas.afterKnock)
        pas.knock(pList[1])
        assertEquals(0, gs.rootService.currentGame.passCounter)
        assertEquals(1, pas.afterKnock)
    }
}