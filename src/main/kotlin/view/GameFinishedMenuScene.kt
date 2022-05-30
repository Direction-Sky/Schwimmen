package view

import service.CardImageLoader
import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.dialog.Dialog
import tools.aqua.bgw.dialog.DialogType
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

/**
 * This is the leaderboard scene shown at the end of the game. It displays the cards of each player.
 * @param app is used to display dialog messages.
 * @param rootService is the backbone of the whole thing.
 */
class GameFinishedMenuScene(private val app: BoardGameApplication, private val rootService: RootService):
    MenuScene(1920, 1080), Refreshable {

    /**
     * To load buffered images for our cards.
     */
    private val loader = CardImageLoader()

    /**
     * All-purposes font.
     */
    private val globalFont: Font = Font(
        size = 46, color = Color.WHITE,
        family = "Comic Sans MS",
        fontWeight = Font.FontWeight.SEMI_BOLD,
    )

    /**
     * First things first
     * @author Ahmad Jammal.
     */
    private val tuButton = Button(
        width = 100, height =  100, posX = 1800, posY = 20,
        visual = ImageVisual("images/TUButtonSmall.png")
    ).apply {
        if(!this.isDisabled) {
            onMouseEntered = {
                this.visual = ImageVisual("images/TUButtonSmallHover.png")
            }
            onMouseExited = {
                this.visual = ImageVisual("images/TUButtonSmall.png")
            }
            onMousePressed = {
                //this.visual = ImageVisual("images/TUButtonPressed2.png")
            }
            onMouseClicked = {
                app.showDialog(
                    Dialog(
                    dialogType = DialogType.INFORMATION,
                    title = "TU Dortmund",
                    header = "Technische Universität Dortmund",
                    message = "https://www.tu-dortmund.de/"
                )
                )
            }
        }
    }

    /**
     * Clicking this button should lead back to main menu.
     */
    private val continueButton: Button = Button(
        posX = 760, posY = 871, width = 394, height = 98,
        visual = ImageVisual("images/Continue.png")
    ).apply {
        onMouseEntered = {
            if (!this.isDisabled) {
                visual = ImageVisual("images/ContinueHover.png")
            }
        }
        onMouseExited = {
            if (!this.isDisabled) {
                visual = ImageVisual("images/Continue.png")
            }
        }
        onMouseClicked = {
            rootService.playerActionService.onAllRefreshables {
                refreshAfterLeaderboard()
            }
        }
    }

    /**
     * Shows a list of all players with their hand cards.
     */
    private fun showLeaderboard() {
        var y = when(rootService.currentGame!!.players.size) {
            2 -> 310
            3 -> 215
            else -> 120
        }
        var x = 740
        for (player in rootService.currentGame!!.players) {
            var points = "${player.checkHandScore()}"
            if(points != "30.5") {
                points = points.substringBefore('.')
            }
            addComponents(
                Label(
                    posX = 400, posY = y, width = 300, height = 10, text = "$player:",
                    font = globalFont, alignment = Alignment.CENTER_RIGHT
                ).apply { this.isDisabled = true }, Label(
                    posX = 1200, posY = y, width = 400, height = 10, text = "$points points!",
                    font = globalFont, alignment = Alignment.CENTER_LEFT
                ).apply { this.isDisabled = true })
            player.handCards.forEach{ card ->
                addComponents(Label(
                    posX = x , posY = y - 60, width = 140, height = 175,
                    visual = ImageVisual(loader.frontImageForCard(card))
                ))
                x += 145
            }
            x = 740
            y += 190
        }
    }

    /**
     * To be called before [refreshAfterGameEnd].
     */
    fun initialize() {
        addComponents(continueButton, tuButton)
        showLeaderboard()
    }

    init {
        background = ColorVisual(17, 26, 37, 255)
    }
}