package view

import service.*
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.visual.ImageVisual

class SchwimmenApplication: BoardGameApplication("Schwimmen Game ♣ developed at TU Dortmund, Germany"), Refreshable {
    private val rootService = RootService()

    private val gameScene = SchwimmenGameScene(rootService)

    private val newGameMenuScene = NewGameMenuScene(this, rootService).apply {
        this.exitButton.onMouseClicked = {
            exit()
        }
    }

    init {
        this.icon = ImageVisual("SchwimmenIcon.png")
        // all scenes and the application itself need too
        // react to changes done in the service layer
        rootService.addRefreshables(
            this,
            gameScene,
            //gameFinishedMenuScene,
            newGameMenuScene
        )

        // This is just done so that the blurred background when showing
        // the new game menu has content and looks nicer
        //rootService.gameService.startGame("Bob", "Alice")

        this.showGameScene(gameScene)
        this.showMenuScene(newGameMenuScene, 0)

    }

    override fun refreshAfterStartNewGame() {
        this.hideMenuScene()
    }

    override fun refreshAfterGameEnd() {
        //this.showMenuScene(gameFinishedMenuScene)
    }
}