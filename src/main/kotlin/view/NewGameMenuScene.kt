package view

import entity.SchwimmenPlayer
import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.dialog.Dialog
import tools.aqua.bgw.dialog.DialogType
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.Visual
import java.awt.Color

/**
 * This is the main menu screen. Players list sizing 2 - 4 players will be constructed and
 * forwarded from here to [SchwimmenGameScene].
 * @param app is the [SchwimmenApplication] that allows showing dialog popups.
 * @param rootService is the core that holds every part of this project connected.
 */
class NewGameMenuScene(private val app: BoardGameApplication, private val rootService: RootService):
    MenuScene(1920, 1080), Refreshable {

    /**
     * Global font.
     */
    private val globalFont: Font = Font(
        size = 36, color = Color(239,239,239,255),
        family = "Comic Sans MS",
        fontWeight = Font.FontWeight.SEMI_BOLD,
    )

    /**
     * CSS for JavaFX. Used for styling the text fields.
     */
    private val style: String = (
        "-fx-background-color: rgba(0, 0, 0, 0.0);" +
        "-fx-background-image: url('images/TextInputField.png');" +
        "-fx-background-size: cover;" +
        "-fx-alignment: center;"
    )

    /**
     * Great personalities.
     */
    private val namesList = listOf(
        "Nick", "Ahmad", "Ali", "Kareem", "Florian",
        "Jan", "Christos", "Ibrahim", "Nils", "Lasse"
    )

    /**
     * Schwimmen title in the middle top of the screen.
     */
    private val schwimmenLabel = Label(
        width = 876, height = 152, posX = 522, posY = 85,
        text = "",
        visual = ImageVisual("images/Schwimmen.png")
    ).apply {
        onMouseEntered = {
            this.visual = ImageVisual("images/SchwimmenHover.png")
        }
        onMouseExited = {
            this.visual = ImageVisual("images/Schwimmen.png")
        }
        onMouseClicked = {
            app.showDialog(Dialog(
                dialogType = DialogType.INFORMATION,
                title = "How to play",
                header = "",
                message = (
                        "- Player with highest score wins.\n" +
                                "- All cards have same value -> 30.5.\n" +
                                "- Cards of the same suit can be summed together.\n" +
                                "- 10, J, Q, K -> 10, A -> 11\n" +
                                "- If a full circle is passed, 3 new cards from deck\n" +
                                "  are to be drawn replacing the old table cards.\n" +
                                "- In case a full circle is passed and deck has less\n" +
                                "  than three cards, game will end on the spot.\n" +
                                "- If someone knocks, each player gets to play one\n" +
                                "  turn and then game will end.\n\n" +
                                "Good luck and have fun!"
                        )
            ))
        }
    }

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
            onMouseClicked = {
                app.showDialog(Dialog(
                    dialogType = DialogType.INFORMATION,
                    title = "TU Dortmund",
                    header = "Technische Universität Dortmund",
                    message = "https://www.tu-dortmund.de/"
                ))
            }
        }
    }

    /**
     * Input field for player 1's name. Permanently enabled.
     */
    private val p1Input = TextField(
        width = 428, height = 90, posX = 795, posY = 322,
        text = namesList.random(),
        font = globalFont,
        prompt = "Enter name",
    ).apply {
        this.componentStyle = style
        onKeyTyped = {
            countPlayers()
        }
    }

    /**
     * Input field for player 2's name. Permanently enabled.
     */
    private val p2Input = TextField(
        width = 428, height = 90, posX = 795, posY = 431,
        text = namesList.random(),
        font = globalFont,
        prompt = "Enter name",
    ).apply {
        this.componentStyle = style
        onKeyTyped = {
            countPlayers()
        }
    }

    /**
     * Input field for player 1's name. Enabled upon clicking [addButton3],
     * and disabled upon clicking [removeButton3].
     */
    private val p3Input = TextField(
        width = 428, height = 90, posX = 795, posY = 542,
        text = "Add player 3",
        font = globalFont,
        prompt = "Enter name"
    ).apply {
        this.componentStyle = style
        this.isDisabled = true
        this.visual = Visual.EMPTY
        onKeyTyped = {
            countPlayers()
        }
    }

    /**
     * Input field for player 1's name. Enabled upon clicking [addButton4],
     * and disabled upon clicking [removeButton4].
     */
    private val p4Input = TextField(
        width = 428, height = 90, posX = 795, posY = 652,
        text = "Add player 4",
        font = globalFont,
        prompt = "Enter name",
    ).apply {
        this.componentStyle = style
        this.isDisabled = true
        onKeyTyped = {
            countPlayers()
        }
    }

    /**
     * Player 1 label on the left. Permanently visible.
     */
    private val p1Label = Label(
        width = 158, height = 65, posX = 578, posY = p1Input.posY + 20,
        text  = "",
        visual = ImageVisual("images/Player1.png")
    )

    /**
     * Player 2 label on the left. Permanently visible.
     */
    private val p2Label = Label(
        width = 158, height = 65, posX = 578, posY = p2Input.posY + 20,
        text  = "",
        visual = ImageVisual("images/Player2.png")
    )

    /**
     * Player 3 label on the left. Visible when player is added using [addButton3],
     * and disappears upon clicking [removeButton3].
     */
    private val p3Label = Label(
        width = 158, height = 65, posX = 578, posY = p3Input.posY + 20,
        text  = "",
        visual = Visual.EMPTY
    ).apply {
        this.isDisabled = true
    }

    /**
     * Player 4 label on the left. Visible when player is added using [addButton4],
     * and disappears upon clicking [removeButton4].
     */
    private val p4Label = Label(
        width = 158, height = 65, posX = 578, posY = p4Input.posY + 20,
        text  = "",
        visual = Visual.EMPTY
    ).apply {
        this.isDisabled = true
    }

    /**
     * Adds [p3Label] and [removeButton3] and activates [p3Input] and [addButton4]. Also removes itself.
     */
    private val addButton3 = Button(
        width = 90, height =  90, posX = 620, posY = p3Input.posY + 2,
        visual = ImageVisual("images/AddButton.png")
    ).apply {
        onMouseEntered = {
            if(!this.isDisabled) this.visual = ImageVisual("images/AddButtonHover.png")
        }
        onMouseExited = {
            if(!this.isDisabled) this.visual = ImageVisual("images/AddButton.png")
        }
        onMouseClicked = {
            if(!this.isDisabled) {
                addButton4.visual = ImageVisual("images/AddButton.png")
                addButton4.isDisabled = false
                p3Label.isDisabled = false
                p3Label.visual = ImageVisual("images/Player3.png")
                p3Input.isDisabled = false
                p3Input.text = namesList.random()
                removeButton3.isDisabled = false
                removeButton3.visual = ImageVisual("images/RemoveButton.png")
                this.visual = Visual.EMPTY
                this.isDisabled = true
                countPlayers()
            }
        }
    }

    /**
     * Adds [p4Label] and [removeButton4], activates [p4Input], and removes itself with [removeButton3].
     */
    private val addButton4 = Button(
        width = 90, height =  90, posX = 620, posY = p4Input.posY + 2,
        visual = ImageVisual("images/AddButtonDisabled.png")
    ).apply {
        this.isDisabled = true
        onMouseEntered = {
            if(!this.isDisabled) this.visual = ImageVisual("images/AddButtonHover.png")
        }
        onMouseExited = {
            if(!this.isDisabled) this.visual = ImageVisual("images/AddButton.png")
        }
        onMouseClicked = {
            if(!this.isDisabled) {
                p4Label.isDisabled = false
                p4Label.visual = ImageVisual("images/Player4.png")
                p4Input.isDisabled = false
                p4Input.text = namesList.random()
                removeButton3.isDisabled = true
                removeButton3.visual = Visual.EMPTY
                removeButton4.isDisabled = false
                removeButton4.visual = ImageVisual("images/RemoveButton.png")
                this.visual = Visual.EMPTY
                this.isDisabled = true
                countPlayers()
            }
        }
    }

    /**
     * Removes [p3Label] and itself, deactivates [p3Input] and adds [addButton3].
     */
    private val removeButton3: Button = Button(
        width = 90, height =  90, posX = 1300, posY = addButton3.posY + 2,
        visual = Visual.EMPTY
    ).apply {
        this.isDisabled = true
        onMouseEntered = {
            if(!this.isDisabled) this.visual = ImageVisual("images/RemoveButtonHover2.png")
        }
        onMouseExited = {
            if(!this.isDisabled) this.visual = ImageVisual("images/RemoveButton.png")
        }
        onMouseClicked = {
            if(!this.isDisabled) {
                this.visual = Visual.EMPTY
                p3Input.text = "Add player 3"
                p3Input.isDisabled = true
                addButton3.isDisabled = false
                addButton3.visual = ImageVisual("images/AddButton.png")
                p3Label.isDisabled = true
                p3Label.visual = Visual.EMPTY
                addButton4.visual = ImageVisual("images/AddButtonDisabled.png")
                addButton4.isDisabled = true
                this.isDisabled = true
                countPlayers()
            }
        }
    }

    /**
     * Removes [p4Label] and itself, deactivates [p4Input] and adds [removeButton3], [addButton3].
     */
    private val removeButton4: Button = Button(
        width = 90, height =  90, posX = 1300, posY = addButton4.posY + 2,
        visual = Visual.EMPTY
    ).apply {
        this.isDisabled = true
        onMouseEntered = {
            if(!this.isDisabled) this.visual = ImageVisual("images/RemoveButtonHover2.png")
        }
        onMouseExited = {
            if(!this.isDisabled) this.visual = ImageVisual("images/RemoveButton.png")
        }
        onMouseClicked = {
            if(!this.isDisabled) {
                p4Input.text = "Add player 4"
                p4Input.isDisabled = true
                p4Label.isDisabled = true
                p4Label.visual = Visual.EMPTY
                addButton4.isDisabled = false
                addButton4.visual = ImageVisual("images/AddButton.png")
                removeButton3.isDisabled = false
                removeButton3.visual = ImageVisual("images/RemoveButton.png")
                this.visual = Visual.EMPTY
                this.isDisabled = true
                countPlayers()
            }
        }
    }

    /**
     * Starts the game, transitioning into [SchwimmenGameScene]. Enabled if at least 2/4 players at entered.
     * This guarantee is provided by the function [countPlayers].
     */
    private val startButton = Button(
        width = 222, height =  107, posX = 1624, posY = 871,
        alignment = Alignment.CENTER,
        visual = ImageVisual("images/StartButton.png")
    ).apply {
        if(!this.isDisabled) {
            onMouseEntered = {
                if(countPlayers() >= 2) {
                    this.visual = ImageVisual("images/StartButtonHover.png")
                }
            }
            onMouseExited = {
                if(countPlayers() >= 2) {
                    this.visual = ImageVisual("images/StartButton.png")
                }
            }
            onMouseClicked = {
                val players = mutableListOf<SchwimmenPlayer>()
                for (field in listOf(p1Input, p2Input, p3Input, p4Input)) {
                    if(!field.isDisabled && field.text.isNotBlank()) {
                        players.add(SchwimmenPlayer(field.text.trim()))
                    }
                }
                rootService.currentGame = rootService.gameService.startGame(players)
            }
        }
    }

    /**
     * GG
     */
    val exitButton = Button(
        width = 222, height =  107, posX = 75, posY = 871,
        alignment = Alignment.CENTER,
        visual = ImageVisual("images/ExitButton.png"),

    ).apply {
        if(!this.isDisabled) {
            onMouseEntered = {
                this.visual = ImageVisual("images/ExitButtonHover2.png")
            }
            onMouseExited = {
                this.visual = ImageVisual("images/ExitButton.png")
            }
        }
    }

    init {
        background = CompoundVisual(
            ImageVisual("images/Background.jpg"),
            ColorVisual(70,40,120,50)
        )
        opacity = .5
        addComponents(
            tuButton,
            schwimmenLabel,
            p1Label, p1Input,
            p2Label, p2Input,
            addButton3, p3Label, p3Input, removeButton3,
            addButton4, p4Input, p4Label, removeButton4,
            startButton, exitButton
        )
        countPlayers()
    }

    /**
     * This function serves as a security layer to prevent starting the game with
     * less than 2 players. It is called upon any change in the input value in each of
     * [p1Input], [p2Input], [p3Input] and [p4Input] as well as upon clicking any of
     * [addButton3], [addButton4], [removeButton3] and [removeButton4]. It can both
     * enable and disable the "Start" button depending on how many valid player names
     * in total are entered.
     * @return the number of entered player names (spaces don't count).
     */
    private fun countPlayers(): Int {
        var n = 0
        for(field in listOf(p1Input, p2Input, p3Input, p4Input)) {
            if(!field.isDisabled && field.text.isNotBlank()) {
                n++
            }
        }
        startButton.isDisabled = n < 2
        startButton.visual =
            if(n < 2) ImageVisual("images/StartButtonDisabled.png")
            else ImageVisual("images/StartButton.png")
        return n
    }
}