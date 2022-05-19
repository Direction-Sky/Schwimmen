package view

import service.*
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.visual.ImageVisual

class SchwimmenApplication: BoardGameApplication("♦ Schwimmen ♦"), Refreshable {
    private val rootService = RootService()

    private val gameScene = SchwimmenGameScene(rootService)

    private val newGameMenuScene = NewGameMenuScene(this, rootService).apply {
        this.exitButton.onMouseClicked = {
            exit()
        }
    }

    init {
        this.icon = ImageVisual("SchwimmenIcon2.png")
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
        this.showMenuScene(newGameMenuScene, 500)

    }

    override fun refreshAfterStartNewGame() {
        this.hideMenuScene(500)
    }

    override fun refreshAfterGameEnd() {
        //this.showMenuScene(gameFinishedMenuScene)
    }
}