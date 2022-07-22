package service

import entity.SchwimmenGame
import view.Refreshable

/**
 * This is the class that serves the purpose of connecting the entity layer with the service layer.
 * @property playerActionService refers to the instance of [PlayerActionService].
 * @property gameService refers to the instance of [GameService].
 */
class RootService {
    var currentGame: SchwimmenGame? = null

    val gameService = GameService(this)
    val playerActionService: PlayerActionService = PlayerActionService(this)

    /**
     * Adds the provided [newRefreshable] to all services connected
     * to this root service
     */
    private fun addRefreshable(newRefreshable: Refreshable) {
        gameService.addRefreshable(newRefreshable)
        playerActionService.addRefreshable(newRefreshable)
    }

    /**
     * Adds each of the provided [newRefreshables] to all services
     * connected to this root service
     */
    fun addRefreshables(vararg newRefreshables: Refreshable) {
        newRefreshables.forEach { addRefreshable(it) }
    }

}