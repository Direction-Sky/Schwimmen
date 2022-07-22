package view

import entity.SchwimmenPlayer
import org.junit.jupiter.api.Test
import service.GameService
import service.PlayerActionService
import service.RootService

class NewGameMenuSceneTest {
    private val pList = listOf(
        SchwimmenPlayer("P1"),
        SchwimmenPlayer("P2"),
        SchwimmenPlayer("P3"),
        SchwimmenPlayer("P4")
    )
    val rs = RootService()

    @Test
    fun test() {
        val gs = GameService(rs)
        gs.startGame(pList)
        val pas = PlayerActionService(gs.rootService)
    }
}