package view

import entity.SchwimmenPlayer
import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.dialog.ButtonType
import tools.aqua.bgw.animation.Animation
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.dialog.Dialog
import tools.aqua.bgw.dialog.DialogType
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.Visual
import java.awt.Color
import java.net.URL

/**
 * This is the main menu screen. Players list sizing 2 - 4 players will be constructed and
 * forwarded from here to [SchwimmenGameScene].
 * @param app is the [SchwimmenApplication] that allows showing dialog popups.
 * @param rootService is the core that holds every part of this project connected.
 */
class NewGameMenuScene(val app: BoardGameApplication, private val rootService: RootService):
    MenuScene(1920, 1080), Refreshable {

    /**
     * Global font.
     */
    val globalFont: Font = Font(
        size = 36,
        family = "Comic Sans MS",
        fontWeight = Font.FontWeight.SEMI_BOLD
    )

    /**
     * Great personalities.
     */
    private val namesList = listOf(
        "Nick", "Ahmad", "Ali", "Kareem", "Florian",
        "Jan", "Christos", "Ibrahim", "Nils", "Lasse"
    )

    /**
     * First things first
     */
    private val tuButton = Button(
        width = 192, height =  192, posX = 108, posY = 70,
        visual = ImageVisual("TUButton.png")
    ).apply {
        if(!this.isDisabled) {
            onMouseEntered = {
                this.visual = ImageVisual("TUButtonHover.png")
            }
            onMouseExited = {
                this.visual = ImageVisual("TUButton.png")
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
     * Shows game rules as dialog window.
     */
    private val helpButton = Button(
        width = 192, height =  192, posX = 1620, posY = 70,
        visual = ImageVisual("HelpButton.png")
    ).apply {
        if(!this.isDisabled) {
            onMouseEntered = {
                this.visual = ImageVisual("HelpButtonHover.png")
            }
            onMouseExited = {
                this.visual = ImageVisual("HelpButton.png")
            }
            onMouseClicked = {
                app.showDialog(Dialog(
                    dialogType = DialogType.INFORMATION,
                    title = "How to play",
                    header = "",
                    message = "- Player with highest score wins.\n" +
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
                ))
            }
        }
    }

    /*
    private val tuLogo = Button(
        width = 540, height =  138, posX = 670, posY = 850,
        visual = ImageVisual("TULogo.png")
        ).apply {
        if(!this.isDisabled) {
            onMouseEntered = {
                this.visual = ImageVisual("TULogoHover.png")
            }
            onMouseExited = {
                this.visual = ImageVisual("TULogo.png")
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
    */

    /**
     * Schwimmen title in the middle top of the screen.
     */
    private val headlineLabel = Label(
        width = 876, height = 152, posX = 522, posY = 85,
        text = "",
        visual = ImageVisual("Schwimmen.png")
    )

    /**
     * Player 1 label on the left. Permanently visible.
     */
    private val p1Label = Label(
        width = 158, height = 65, posX = 578, posY = 347,
        text  = "",
        visual = ImageVisual("Player1.png")
    )

    /**
     * Player 2 label on the left. Permanently visible.
     */
    private val p2Label = Label(
        width = 158, height = 65, posX = 578, posY = 451,
        text  = "",
        visual = ImageVisual("Player2.png")
    )

    /**
     * Player 3 label on the left. Visible when player is added using [addButton3],
     * and disappears upon clicking [removeButton3].
     */
    private val p3Label = Label(
        width = 158, height = 65, posX = 578, posY = 555,
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
        width = 158, height = 65, posX = 578, posY = 659,
        text  = "",
        visual = Visual.EMPTY
    ).apply {
        this.isDisabled = true
    }

    /**
     * Adds [p3Label] and [removeButton3] and activates [p3Input] and [addButton4]. Also removes iteslf.
     */
    private val addButton3 = Button(
        width = 90, height =  90, posX = 620, posY = 550,
        visual = ImageVisual("AddButton.png")
    ).apply {
        onMouseEntered = {
            if(!this.isDisabled) this.visual = ImageVisual("AddButtonHover.png")
        }
        onMouseExited = {
            if(!this.isDisabled) this.visual = ImageVisual("AddButton.png")
        }
        onMouseClicked = {
            if(!this.isDisabled) {
                addButton4.visual = ImageVisual("AddButton.png")
                addButton4.isDisabled = false
                p3Label.isDisabled = false
                p3Label.visual = ImageVisual("Player3.png")
                p3Input.isDisabled = false
                p3Input.text = namesList.random()
                removeButton3.isDisabled = false
                removeButton3.visual = ImageVisual("RemoveButton.png")
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
        width = 90, height =  90, posX = 620, posY = 650,
        visual = ImageVisual("AddButtonDisabled.png")
    ).apply {
        this.isDisabled = true
        onMouseEntered = {
            if(!this.isDisabled) this.visual = ImageVisual("AddButtonHover.png")
        }
        onMouseExited = {
            if(!this.isDisabled) this.visual = ImageVisual("AddButton.png")
        }
        onMouseClicked = {
            if(!this.isDisabled) {
                p4Label.isDisabled = false
                p4Label.visual = ImageVisual("Player4.png")
                p4Input.isDisabled = false
                p4Input.text = namesList.random()
                removeButton3.isDisabled = true
                removeButton3.visual = Visual.EMPTY
                removeButton4.isDisabled = false
                removeButton4.visual = ImageVisual("RemoveButton.png")
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
        width = 90, height =  90, posX = 1300, posY = 550,
        visual = Visual.EMPTY
    ).apply {
        this.isDisabled = true
        onMouseEntered = {
            if(!this.isDisabled) this.visual = ImageVisual("RemoveButtonHover2.png")
        }
        onMouseExited = {
            if(!this.isDisabled) this.visual = ImageVisual("RemoveButton.png")
        }
        onMouseClicked = {
            if(!this.isDisabled) {
                this.visual = Visual.EMPTY
                p3Input.text = "Add player 3"
                p3Input.isDisabled = true
                addButton3.isDisabled = false
                addButton3.visual = ImageVisual("AddButton.png")
                p3Label.isDisabled = true
                p3Label.visual = Visual.EMPTY
                addButton4.visual = ImageVisual("AddButtonDisabled.png")
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
        width = 90, height =  90, posX = 1300, posY = 650,
        visual = Visual.EMPTY
    ).apply {
        this.isDisabled = true
        onMouseEntered = {
            if(!this.isDisabled) this.visual = ImageVisual("RemoveButtonHover2.png")
        }
        onMouseExited = {
            if(!this.isDisabled) this.visual = ImageVisual("RemoveButton.png")
        }
        onMouseClicked = {
            if(!this.isDisabled) {
                p4Input.text = "Add player 4"
                p4Input.isDisabled = true
                p4Label.isDisabled = true
                p4Label.visual = Visual.EMPTY
                addButton4.isDisabled = false
                addButton4.visual = ImageVisual("AddButton.png")
                removeButton3.isDisabled = false
                removeButton3.visual = ImageVisual("RemoveButton.png")
                this.visual = Visual.EMPTY
                this.isDisabled = true
                countPlayers()
            }
        }
    }

    /**
     * Input field for player 1's name. Permanently enabled.
     */
    private val p1Input = TextField(
        width = 428, height = 90, posX = 795, posY = 367 - 45,
        text = namesList.random(),
        font = globalFont,
        prompt = "Enter name",
    ).apply {
        onKeyTyped = {
            countPlayers()
        }
    }

    /**
     * Input field for player 2's name. Permanently enabled.
     */
    private val p2Input = TextField(
        width = 428, height = 90, posX = 795, posY = 476 - 45,
        text = namesList.random(),
        font = globalFont,
        prompt = "Enter name",
    ).apply {
        onKeyTyped = {
            countPlayers()
        }
    }

    /**
     * Input field for player 1's name. Enabled upon clicking [addButton3],
     * and disabled upon clicking [removeButton3].
     */
    private val p3Input = TextField(
        width = 428, height = 90, posX = 795, posY = 587 - 45,
        text = "Add player 3",
        font = globalFont,
        prompt = "Enter name",
    ).apply {
        this.isDisabled = true
        onKeyTyped = {
            countPlayers()
        }
    }

    /**
     * Input field for player 1's name. Enabled upon clicking [addButton4],
     * and disabled upon clicking [removeButton4].
     */
    private val p4Input = TextField(
        width = 428, height = 90, posX = 795, posY = 697 - 45,
        text = "Add player 4",
        font = globalFont,
        prompt = "Enter name",
    ).apply {
        this.isDisabled = true
        onKeyTyped = {
            countPlayers()
        }
    }

    /**
     * Starts the game, transitioning into [SchwimmenGameScene]. Enabled if at least 2/4 players at entered.
     * This guarantee is provided by the function [countPlayers].
     */
    private val startButton = Button(
        width = 222, height =  107, posX = 1624, posY = 871,
        alignment = Alignment.CENTER,
        visual = ImageVisual("StartButton.png")
    ).apply {
        if(!this.isDisabled) {
            onMouseEntered = {
                if(countPlayers() >= 2) {
                    this.visual = ImageVisual("StartButtonHover.png")
                }
            }
            onMouseExited = {
                if(countPlayers() >= 2) {
                    this.visual = ImageVisual("StartButton.png")
                }
            }
            onMouseClicked = {
                val players = mutableListOf<SchwimmenPlayer>()
                if(!p1Input.text.isBlank()) {
                    players.add(SchwimmenPlayer(p1Input.text.trim()))
                }
                if(!p2Input.text.isBlank()) {
                    players.add(SchwimmenPlayer(p2Input.text.trim()))
                }
                if(!p3Input.isDisabled && !p3Input.text.isBlank()) {
                    players.add(SchwimmenPlayer(p3Input.text.trim()))
                }
                if(!p4Input.isDisabled && !p4Input.text.isBlank()) {
                    players.add(SchwimmenPlayer(p4Input.text.trim()))
                }
                println("$players")
            }
        }
    }

    /**
     * GG
     */
    val exitButton = Button(
        width = 222, height =  107, posX = 75, posY = 871,
        alignment = Alignment.CENTER,
        visual = ImageVisual("ExitButton.png"),

    ).apply {
        if(!this.isDisabled) {
            onMouseEntered = {
                this.visual = ImageVisual("ExitButtonHover2.png")
            }
            onMouseExited = {
                this.visual = ImageVisual("ExitButton.png")
            }
        }
    }

    init {
        background = CompoundVisual(
            ImageVisual("Background.jpg"),
            ColorVisual(70,40,120,100)
        )
        opacity = .3
        addComponents(
            helpButton, tuButton,
            headlineLabel,
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
     * [addButton3], [addButton4], [removeButton3] and [removeButton4].
     * It can enable / disable the "Start" button depending on how many valid player names in total are entered.
     * @return the number of entered player names (spaces don't count).
     */
    private fun countPlayers(): Int {
        var n = 2
        if(p1Input.text.isBlank()) {
            n--
        }
        if(p2Input.text.isBlank()) {
            n--
        }
        if(!p3Input.isDisabled && !p3Input.text.isBlank()) {
            n++
        }
        if(!p4Input.isDisabled && !p4Input.text.isBlank()) {
            n++
        }
        if(n >= 2) {
            startButton.isDisabled = false
            startButton.visual = ImageVisual("StartButton.png")
        }
        if(n < 2) {
            startButton.isDisabled = true
            startButton.visual = ImageVisual("StartButtonDisabled.png")
        }
        return n
    }
}