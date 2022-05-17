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
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color
import java.net.URL

class NewGameMenuScene(val app: BoardGameApplication, private val rootService: RootService): MenuScene(1920, 1080), Refreshable {

    /**
     * First things first
     */
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
                            "- If a full round is passed, 3 new cards from deck\n" +
                            "  are to be drawn and replace the old table cards.\n" +
                            "- In case a full round is passed and deck has less\n" +
                            "  than three cards, game will end on the spot.\n" +
                            "- If someone knocks, each player gets to play one\n" +
                            "  turn and then game will end.\n\n" +
                            "Good luck and have fun!"
                ))
            }
        }
    }

    private val headlineLabel = Label(
        width = 876, height = 152, posX = 522, posY = 85,
        text = "",
        visual = ImageVisual("Schwimmen.png")
    )

    private val p1Label = Label(
        width = 158, height = 65, posX = 578, posY = 347,
        text  = "",
        visual = ImageVisual("Player1.png")
    )

    private val p2Label = Label(
        width = 158, height = 65, posX = 578, posY = 451,
        text  = "",
        visual = ImageVisual("Player2.png")
    )

    private val p3Label = Label(
        width = 158, height = 65, posX = 578, posY = 555,
        text  = "",
        visual = ColorVisual(0,0,0,0)
    ).apply {
        this.isDisabled = true
    }

    private val p4Label = Label(
        width = 158, height = 65, posX = 578, posY = 659,
        text  = "",
        visual = ColorVisual(0,0,0,0)
    ).apply {
        this.isDisabled = true
    }

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
                p3Input.text = listOf("Homer", "Marge", "Bart", "Lisa", "Maggie").random()
                removeButton3.isDisabled = false
                removeButton3.visual = ImageVisual("RemoveButton.png")
                this.visual = ColorVisual(0,0,0,0)
                this.isDisabled = true
                countPlayers()
            }
        }
    }

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
                p4Input.text = listOf("Homer", "Marge", "Bart", "Lisa", "Maggie").random()
                removeButton3.isDisabled = true
                removeButton3.visual = ColorVisual(0,0,0,0)
                removeButton4.isDisabled = false
                removeButton4.visual = ImageVisual("RemoveButton.png")
                this.visual = ColorVisual(0,0,0,0)
                this.isDisabled = true
                countPlayers()
            }
        }
    }

    private val removeButton3: Button = Button(
        width = 90, height =  90, posX = 1300, posY = 550,
        visual = ColorVisual(0,0,0,0)
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
                this.visual = ColorVisual(0,0,0,0)
                p3Input.text = "Add player 3"
                p3Input.isDisabled = true
                addButton3.isDisabled = false
                addButton3.visual = ImageVisual("AddButton.png")
                p3Label.isDisabled = true
                p3Label.visual = ColorVisual(0,0,0,0)
                addButton4.visual = ImageVisual("AddButtonDisabled.png")
                addButton4.isDisabled = true
                this.isDisabled = true
                countPlayers()
            }
        }
    }

    private val removeButton4: Button = Button(
        width = 90, height =  90, posX = 1300, posY = 650,
        visual = ColorVisual(0,0,0,0)
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
                p4Label.visual = ColorVisual(0,0,0,0)
                addButton4.isDisabled = false
                addButton4.visual = ImageVisual("AddButton.png")
                removeButton3.isDisabled = false
                removeButton3.visual = ImageVisual("RemoveButton.png")
                this.visual = ColorVisual(0,0,0,0)
                this.isDisabled = true
                countPlayers()
            }
        }
    }

    private val p1Input = TextField(
        width = 428, height = 90, posX = 795, posY = 367 - 45,
        text = listOf("Homer", "Marge", "Bart", "Lisa", "Maggie").random(),
        font = Font(size = 32, family = "Boogaloo"),
        prompt = "Enter name for player 1",
    ).apply {
        onKeyTyped = {
            countPlayers()
        }
    }

    private val p2Input = TextField(
        width = 428, height = 90, posX = 795, posY = 476 - 45,
        text = listOf("Homer", "Marge", "Bart", "Lisa", "Maggie").random(),
        font = Font(size = 32, family = "Boogaloo"),
        prompt = "Enter name for player 2"
    ).apply {
        onKeyTyped = {
            countPlayers()
        }
    }

    private val p3Input = TextField(
        width = 428, height = 90, posX = 795, posY = 587 - 45,
        text = "Add player 3",
        prompt = "Enter name for player 3",
        font = Font(size = 32, family = "Boogaloo")
    ).apply {
        this.isDisabled = true
        onKeyTyped = {
            countPlayers()
        }
    }

    private val p4Input = TextField(
        width = 428, height = 90, posX = 795, posY = 697 - 45,
        text = "Add player 4",
        prompt = "Enter name for player 4",
        font = Font(size = 32, family = "Boogaloo")
    ).apply {
        this.isDisabled = true
        onKeyTyped = {
            countPlayers()
        }
    }

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

    val exitButton = Button(
        width = 222, height =  107, posX = 75, posY = 871,
        alignment = Alignment.CENTER,
        font = Font(30, Color.WHITE, fontWeight = Font.FontWeight.BOLD),
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
        background = ImageVisual("background.jpg", 1920, 1080)
        opacity = 1.0
        addComponents(
            tuLogo, headlineLabel, helpButton,
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
     * less than 2 players.
     * It can enable / disable the "Start" button.
     * @return the number of entered player names (spaces don't count).
     */
    private fun countPlayers(): Int {
        var n = 2
        if(p1Input.text.isBlank()) n--
        if(p2Input.text.isBlank()) n--
        if(!p3Input.isDisabled && !p3Input.text.isBlank()) n++
        if(!p4Input.isDisabled && !p4Input.text.isBlank()) n++
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