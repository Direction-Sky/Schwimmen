package service

import entity.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Tester class for [PlayerActionService.knock].
 */
class KnockTest {
    private val pList = listOf(
        SchwimmenPlayer("P1"),
        SchwimmenPlayer("P2"),
        SchwimmenPlayer("P3"),
        SchwimmenPlayer("P4")
    )

    /**
     * Test method for [PlayerActionService.knock].
     */
    @Test
    fun knockTest() {
        val rs = RootService()
        val gs = GameService(rs)
        rs.currentGame = gs.startGame(pList)
        val pas = PlayerActionService(gs.rootService)
        pas.pass(pList[0])
        assertEquals(1, gs.rootService.currentGame!!.passCounter)
        assertEquals(0, pas.afterKnock)
        pas.knock(pList[1])
        assertEquals(0, gs.rootService.currentGame!!.passCounter)
        assertEquals(1, pas.afterKnock)
    }
}