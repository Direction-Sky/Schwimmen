package service
import entity.SchwimmenGame

class RootService(val currentGame: SchwimmenGame, val gameService: GameService) {
    val playerActionService: PlayerActionService = PlayerActionService(this)

    public fun retrieveCurrentGame(): SchwimmenGame {
        return currentGame
    }
}