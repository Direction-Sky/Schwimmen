package view

import service.*
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.visual.ImageVisual

class SchwimmenApplication: BoardGameApplication("♦ Schwimmen ♦"), Refreshable {
    private val rootService = RootService()

    private val gameScene = SchwimmenGameScene(this, rootService).apply {
        this.exitButton.onMouseClicked = {
            exit()
        }
    }

    private val newGameMenuScene = NewGameMenuScene(this, rootService).apply {
        this.exitButton.onMouseClicked = {
            exit()
        }
    }

    init {
        this.icon = ImageVisual("images/SchwimmenIcon2.png")
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

        //
        this.showMenuScene(newGameMenuScene, 500)

    }

    override fun refreshAfterStartNewGame() {
        this.showGameScene(gameScene)
        this.hideMenuScene(700)
    }

    override fun refreshAfterGameEnd() {
        //this.showMenuScene(gameFinishedMenuScene)
    }
}