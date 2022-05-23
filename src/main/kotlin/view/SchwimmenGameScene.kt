package view

import entity.CardSuit
import entity.CardValue
import entity.SchwimmenCard
import entity.SchwimmenPlayer
import service.CardImageLoader
import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.TextVisual
import java.awt.Color

class SchwimmenGameScene(val app: SchwimmenApplication, private val rootService: RootService): BoardGameScene(1920, 1080), Refreshable {

    private val cardImageLoader = CardImageLoader()
    private val viewEffect = CardViewEffect()
    var condition = false

    var deckCount = Label()
    var handScore = Label()
    val handView: BidirectionalMap<Label, SchwimmenCard> = BidirectionalMap()
    val tableView: BidirectionalMap<Label, SchwimmenCard> = BidirectionalMap()

    val playerNames: MutableList<Label> = mutableListOf()


    /**
     * Global font.
     */
    val globalFont: Font = Font(
        size = 46, color = Color.WHITE,
        family = "Comic Sans MS",
        fontWeight = Font.FontWeight.SEMI_BOLD,
    )

    fun refreshView(oldSet: MutableList<SchwimmenCard>, newSet: MutableList<SchwimmenCard>, view: BidirectionalMap<Label, SchwimmenCard>): Unit {
        val y = view.backward(oldSet[0]).posY
        oldSet.forEach {
            removeComponents(view.backward(it))
            view.removeBackward(it)
        }
        var x = 630
        newSet.forEach {
            addComponents(
                Label(
                    x, y, 190, 250,
                    visual = CompoundVisual()
                ).apply {
                    view.add(this, it)
                    var selected: Boolean = false
                    //this.isDisabled = true
                    var effect = CompoundVisual(
                        ImageVisual(cardImageLoader.frontImageForCard(it))
                    )
                    visual = effect
                    if (!this.isDisabled) {
                        this.onMouseEntered = {
                            effect = viewEffect.hover(effect)
                            visual = effect
                        }
                        this.onMouseExited = {
                            effect = viewEffect.unhover(effect)
                            visual = effect
                        }
                        this.onMouseClicked = {
                            when (selected) {
                                false -> {
                                    effect = viewEffect.select(effect)
                                    visual = effect
                                    selected = true
                                }
                                true -> {
                                    effect = viewEffect.unselect(effect)
                                    visual = effect
                                    selected = false
                                }
                            }
                        }
                    }
                }
            )
            x += 220
        }
    }

    fun initializeBoard(): Unit {
        var x = 630
        rootService.currentGame?.players?.get(0)?.handCards?.forEach {
            addComponents(
                Label(
                    x, 490, 190, 250,
                    visual = CompoundVisual()
                ).apply {
                    handView.add(this, it)
                    var selected: Boolean = false
                    //this.isDisabled = true
                    var effect = CompoundVisual(
                        ImageVisual(cardImageLoader.frontImageForCard(it))
                    )
                    visual = effect
                    if (!this.isDisabled) {
                        this.onMouseEntered = {
                            effect = viewEffect.hover(effect)
                            visual = effect
                        }
                        this.onMouseExited = {
                            effect = viewEffect.unhover(effect)
                            visual = effect
                        }
                        this.onMouseClicked = {
                            when (selected) {
                                false -> {
                                    effect = viewEffect.select(effect)
                                    visual = effect
                                    selected = true
                                }
                                true -> {
                                    effect = viewEffect.unselect(effect)
                                    visual = effect
                                    selected = false
                                }
                            }
                        }
                    }
                }
            )
            x += 220
        }

        x = 630
        rootService.currentGame?.tableCards?.forEach {
            addComponents(
                Label(
                    x, 150, 190, 250,
                    visual = CompoundVisual()
                ).apply {
                    tableView.add(this, it)
                    var selected: Boolean = false
                    //this.isDisabled = true
                    var effect = CompoundVisual(
                        ImageVisual(cardImageLoader.frontImageForCard(it))
                    )
                    visual = effect
                    if (!this.isDisabled) {
                        this.onMouseEntered = {
                            effect = viewEffect.hover(effect)
                            visual = effect
                        }
                        this.onMouseExited = {
                            effect = viewEffect.unhover(effect)
                            visual = effect
                        }
                        this.onMouseClicked = {
                            when (selected) {
                                false -> {
                                    effect = viewEffect.select(effect)
                                    visual = effect
                                    selected = true
                                }
                                true -> {
                                    effect = viewEffect.unselect(effect)
                                    visual = effect
                                    selected = false
                                }
                            }
                        }
                    }
                }
            )
            x += 220
        }
        var playerLabelsY = 250
        rootService.currentGame?.players?.forEach{
            playerNames.add(Label(
                posX = 1400, posY = playerLabelsY, width = 300, height = 10,
                text = it.name, font = globalFont, alignment = Alignment.CENTER
            ).apply { this.isDisabled })
            playerLabelsY += 70
        }
        for (playerName in playerNames) {
            addComponents(playerName)
        }
    }

    val deck = Label(
        width = 190, height = 250, posY = 300, posX = 200,
        visual = ImageVisual(cardImageLoader.getImageByCoordinates(0,4))
    ).apply {
        this.isDisabled = true
    }
    fun updateDeckCount(current: Label): Label {
        val result = Label(
            width = 190, height = 100, posX = deck.posX, posY = deck.posY + 235, alignment = Alignment.CENTER,
            text = "${rootService.currentGame!!.deck.cards.size} left", font = globalFont
        ).apply { this.isDisabled = true }
        removeComponents(current)
        addComponents(result)
        return result
    }

    fun updateHandScore(current: Label, player: SchwimmenPlayer): Label {
        val score = player.checkHandScore()
        val result: Label
        if(score != 31.5) {
            result = Label(
                width = 300, height = 100, posX = deck.posX - 50, posY = deck.posY + 350, alignment = Alignment.CENTER, // old X,Y = 790, 730
                text = "${score.toInt()} points!", font = globalFont
            ).apply { this.isDisabled = true }
        }
        else {
            result = Label(
                width = 300, height = 100, posX = deck.posX - 50, posY = deck.posY + 350, alignment = Alignment.CENTER,
                text = "$score points!", font = globalFont
            ).apply { this.isDisabled = true }
        }
        removeComponents(current)
        addComponents(result)
        return result
    }

    /**
     * GG
     */
    val exitButton = Button(
        width = 222, height = 107, posX = 75, posY = 871,
        alignment = Alignment.CENTER,
        visual = ImageVisual("images/ExitButton.png"),

        ).apply {
        if (!this.isDisabled) {
            onMouseEntered = {
                this.visual = ImageVisual("images/ExitButtonHover2.png")
            }
            onMouseExited = {
                this.visual = ImageVisual("images/ExitButton.png")
            }
        }
    }

    /**
     * Player action buttons
     */
    private val actionChangeOne = Button(
        width = 222, height =  107, posX = 480, posY = 871,
        alignment = Alignment.CENTER,
        visual = ImageVisual("images/ActionChangeOne.png")
    ).apply {
        if(!this.isDisabled) {
            onMouseEntered = {
                this.visual = ImageVisual("images/ActionChangeOneHover.png")
            }
            onMouseExited = {
                this.visual = ImageVisual("images/ActionChangeOne.png")
            }
            onMouseClicked = {
            }
        }
    }

    private val actionChangeAll = Button(
        width = 222, height =  107, posX = 710, posY = 871,
        alignment = Alignment.CENTER,
        visual = ImageVisual("images/ActionChangeAll.png")
    ).apply {
        if(!this.isDisabled) {
            onMouseEntered = {
                this.visual = ImageVisual("images/ActionChangeAllHover.png")
            }
            onMouseExited = {
                this.visual = ImageVisual("images/ActionChangeAll.png")
            }
            onMouseClicked = {
            }
        }
    }

    private val actionKnock = Button(
        width = 222, height =  107, posX = 940, posY = 871,
        alignment = Alignment.CENTER,
        visual = ImageVisual("images/ActionKnock.png")
    ).apply {
        if(!this.isDisabled) {
            onMouseEntered = {
                this.visual = ImageVisual("images/ActionKnockHover.png")
            }
            onMouseExited = {
                this.visual = ImageVisual("images/ActionKnock.png")
            }
            onMouseClicked = {
            }
        }
    }

    private val actionPass = Button(
        width = 222, height =  107, posX = 1170, posY = 871,
        alignment = Alignment.CENTER,
        visual = ImageVisual("images/ActionPass.png")
    ).apply {
        if(!this.isDisabled) {
            onMouseEntered = {
                this.visual = ImageVisual("images/ActionPassHover.png")
            }
            onMouseExited = {
                this.visual = ImageVisual("images/ActionPass.png")
            }
            onMouseClicked = {
                if(!condition) {
                    initializeBoard()
                    condition = true
                }
                //refreshView(rootService.currentGame?.players[0].handCards, rootService.currentGame!!.players[0].handCards, handView)
                //refreshView(rootService.currentGame!!.tableCards, rootService.currentGame!!.tableCards, tableView)
                deckCount = updateDeckCount(deckCount)
                handScore = updateHandScore(handScore, rootService.currentGame!!.players[0])
            }
        }
    }

    val tableLabel: Label = Label(
        posX = 800, posY = 50, width = 300, height = 50,
        text = "Table cards", font = globalFont, alignment = Alignment.CENTER
    )

    val currentPlayerLabel: Label = Label(
        posX = 850, posY = 750, width = 200, height = 100,
        text = "Player 1", font = globalFont, alignment = Alignment.CENTER
    )

    init {
        background = CompoundVisual(
            //ImageVisual("images/Background.jpg"),
            ColorVisual(17, 26, 37, 255)
        )
        opacity = 1.0
        addComponents(deck, exitButton,
            actionChangeOne, actionChangeAll, actionKnock, actionPass,
            tableLabel, currentPlayerLabel
        )
    }
}