package view

import service.*
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

/**
 * This is the application that creates a window on the presentation layer. It allows interaction, and manages
 * different scenes. For visualizing elements, we use two types of scenes, GameScene and MenuScene.
 * Scenes are the layer which allows user interaction.
 */
class SchwimmenApplication: BoardGameApplication("♦ Schwimmen ♦"), Refreshable {
    private val rootService = RootService()

    /**
     * In-game scene.
     */
    private val gameScene = SchwimmenGameScene(this, rootService).apply {
        this.exitButton.onMouseClicked = {
            exit()
        }
    }

    /**
     * Lobby scene.
     */
    private val newGameMenuScene = NewGameMenuScene(this, rootService).apply {
        this.exitButton.onMouseClicked = {
            exit()
        }
    }

    /**
     * Leaderboard.
     */
    private val gameFinishedMenuScene = GameFinishedMenuScene(this, rootService)

    init {
        this.icon = ImageVisual("images/SchwimmenIcon2.png")
        /* Make sure all scenes are accessible in order to be refreshed when needed */
        rootService.addRefreshables(
            this,
            gameScene,
            gameFinishedMenuScene,
            newGameMenuScene
        )
        this.showMenuScene(newGameMenuScene, 500)
    }

    /**
     * Switches to in-game scene.
     */
    override fun refreshAfterStartNewGame() {
        gameScene.unlock()
        gameScene.startGame()
        this.showGameScene(gameScene)
        this.hideMenuScene(700)
    }

    /**
     * Due to the limitation of BGW, [SchwimmenGameScene] cannot be hidden. While deleting the old instance
     * and creating a new one is a bit too inconvenient, a workaround is to add a black screen layer to
     * hide the original background, and then removing it when starting a new game. This way we keep using
     * the same [SchwimmenGameScene] instance without seeing its components when showing a menu scene.
     */
    override fun refreshAfterGameEnd() {
        gameScene.clearComponents()
        gameScene.background = ColorVisual(0,0,0,255)
        this.gameScene.lock()
        rootService.gameService.finishGame()
        this.hideMenuScene()
        gameFinishedMenuScene.initialize()
        this.showMenuScene(gameFinishedMenuScene)
    }

    /**
     * Switches to lobby.
     */
    override fun refreshAfterLeaderboard() {
        this.hideMenuScene(500)
        this.showMenuScene(newGameMenuScene)
        gameFinishedMenuScene.clearComponents()
    }
}