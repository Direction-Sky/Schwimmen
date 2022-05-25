package view

import entity.CardSuit
import entity.CardValue
import entity.SchwimmenCard
import entity.SchwimmenPlayer
import service.CardImageLoader
import service.RootService
import tools.aqua.bgw.animation.*
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.ToggleButton
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.*
import java.awt.Color

/**
 * In-game scene.
 * @author Ahmad Jammal.
 * @property cardImageLoader is used to load buffer image.
 * @property viewEffect provides visual effects on mouse actions as well as show / hide features.
 * @property handView is tuple of type ([Label], [SchwimmenCard]), where the card belongs to the current [SchwimmenPlayer].
 * @property handView is tuple of type ([Label], [SchwimmenCard]), where the card belongs to the table.
 * @property namesView is a tuple of type ([Label], [SchwimmenPlayer])
 * @property hidden is set to true if hand cards are hidden.
 * @property globalFont is the standard font used in this UI.
 */
class SchwimmenGameScene(
    val app: SchwimmenApplication,
    private val rootService: RootService): BoardGameScene(1920, 1080), Refreshable {

    private val cardImageLoader = CardImageLoader()
    private val viewEffect = CardViewEffect()
    var condition = false

    private val handView: BidirectionalMap<Label, SchwimmenCard> = BidirectionalMap()
    private val tableView: BidirectionalMap<Label, SchwimmenCard> = BidirectionalMap()
    private val namesView: BidirectionalMap<Label, SchwimmenPlayer> = BidirectionalMap()

    private var currentPlayer: Int = 0
    private var hidden: Boolean = false
    var handSelection: MutableList<SchwimmenCard> = mutableListOf()
    var tableSelection: MutableList<SchwimmenCard> = mutableListOf()

    private val globalFont: Font = Font(
        size = 46, color = Color.WHITE,
        family = "Comic Sans MS",
        fontWeight = Font.FontWeight.SEMI_BOLD,
    )
    private val smallFont = Font(
        size = 30, color = Color.WHITE,
        family = "Comic Sans MS",
        fontWeight = Font.FontWeight.SEMI_BOLD,
    )

    val testCard = Label(
        10, 10, 130, 200,
        visual = ImageVisual(cardImageLoader.getImageByCoordinates(4,2))
    ).apply {
        onMouseClicked = {
            /*
            this@SchwimmenGameScene.playAnimation(ParallelAnimation(
                RotationAnimation(this, 360.0, 400),
                MovementAnimation(this, 200, 0, 400),
                FlipAnimation(this,
                    ImageVisual(cardImageLoader.resizedBufferedImage(cardImageLoader.getImageByCoordinates(4,2), 130, 200)),
                    ImageVisual(cardImageLoader.resizedBufferedImage(cardImageLoader.backImage, 130, 200)),
                    200
                )
            ))
            */
            drawThreeCards()
        }
    }

    /**
     * To switch turn to the next player.
     */
    val nextButton = Button(
        width = 222, height = 107, posX = 1624, posY = 871,
        alignment = Alignment.CENTER,
        visual = ImageVisual("images/ActionNextDisabled.png")
    ).apply {
        this.isDisabled = true
        onMouseEntered = {
            if (!this.isDisabled) {
                this.visual = ImageVisual("images/ActionNextHover.png")
            }
        }
        onMouseExited = {
            if (!this.isDisabled) {
                this.visual = ImageVisual("images/ActionNext.png")
            }
        }
        onMouseClicked = {
            if (!this.isDisabled) {
                this.visual = ImageVisual("images/ActionNextDisabled.png")
                this.isDisabled = true
            }
        }
    }

    /**
     * This is just a visual that shows the back image of a card representing the deck.
     */
    val deck = Label(
        width = 190, height = 250, posY = 300, posX = 200,
        visual = ImageVisual(cardImageLoader.getImageByCoordinates(0,4))
    ).apply { this.isDisabled = true }

    val deckCount = Label(
        posX = deck.posX, posY = deck.posY + 235,
        width = 190, height = 100, font = globalFont
    ).apply { this.isDisabled = true }

    /**
     * Shows hand score under the deck.
     */
    private val handScore = Label(
        width = 300, height = 100,
        posX = deck.posX + 35, posY = deck.posY + 350, font = globalFont
    ).apply { this.isDisabled = true }

    /**
     * Reads "Points:" next to the hand score. I decided to split those for animation purposes.
     */
    private val handScorePoints = Label(
        posX = handScore.posX - 120, posY = handScore.posY + 5,
        width = 300, height = 100, text =  "Points:", font = globalFont
    ).apply { this.isDisabled = true }

    /**
     * GG
     */
    val exitButton = Button(
        width = 222, height = 107, posX = 75, posY = 871,
        alignment = Alignment.CENTER,
        visual = ImageVisual("images/ExitButton.png"),
    ).apply {
        onMouseEntered = {
            if (!this.isDisabled) {
                this.visual = ImageVisual("images/ExitButtonHover2.png")
            }
        }
        onMouseExited = {
            if (!this.isDisabled) {
                this.visual = ImageVisual("images/ExitButton.png")
            }
        }
    }

    /**
     * Used only at the first turn to show cards and action buttons. This helps prevent
     * peeking on the first player's cards by other players.
     */
    private val startButton = Button(
        width = 222, height = 107, posX = 835, posY = 871,
        alignment = Alignment.CENTER,
        visual = ImageVisual("images/ActionStart.png")
    ).apply {
        onMouseEntered = {
            if (!this.isDisabled) {
                this.visual = ImageVisual("images/ActionStartHover.png")
            }
        }
        onMouseExited = {
            if (!this.isDisabled) {
                this.visual = ImageVisual("images/ActionStart.png")
            }
        }
        onMouseClicked = {
            if (!this.isDisabled) {
                loadActionButtons()
                if (!condition) {
                    initializeBoard()
                    condition = true
                }
                //refreshView(rootService.currentGame?.players[0].handCards, rootService.currentGame!!.players[0].handCards, handView)
                //refreshView(rootService.currentGame!!.tableCards, rootService.currentGame!!.tableCards, tableView)
                refreshDeckCount()
                refreshHandScore(rootService.currentGame!!.players[currentPlayer])
                this.isDisabled = true
                removeComponents(this)
            }
        }

    }

    /**
     * Reads "Show" / "Hide" next to [showHideToggle].
     */
    private val showHideText = Label (
        posX = 1240, posY = 627, width = 300, height = 100,
        font = smallFont
    ).apply { this.isDisabled = true }

    /**
     * Shows/hides cards with beautiful flip animations
     */
    private val showHideToggle: ToggleButton = ToggleButton(
        posX = 1280, posY = 650, width = 10, height = 10
    ).apply {
        hidden = true
        showHideText.isDisabled = true
        selectedProperty.addListener { _, _ ->
            /* Show cards */
            if(this.isSelected) {
                showHideText.text = "Hide"
                hidden = false
                refreshHandScore(rootService.currentGame!!.players[currentPlayer])
                /* Play flip animation and refresh after it finishes */
                rootService.currentGame!!.players[currentPlayer].handCards.forEach {
                    this@SchwimmenGameScene.playAnimation(
                        FlipAnimation(
                            componentView = handView.backward(it),
                            fromVisual = ImageVisual(
                                cardImageLoader.resizedBufferedImage(cardImageLoader.backImage, 190, 250)
                            ),
                            toVisual = ImageVisual(
                                cardImageLoader.resizedBufferedImage(cardImageLoader.frontImageForCard(it), 190, 250)
                            ),
                            duration = 300
                        ).apply {
                            /* No need to refresh after each animation. Only one is enough */
                            if(it == rootService.currentGame!!.players[currentPlayer].handCards[2]) {
                                onFinished = {
                                    refreshView(
                                        rootService.currentGame!!.players[currentPlayer].handCards.toList(),
                                        rootService.currentGame!!.players[currentPlayer].handCards.toList(),
                                        handView
                                    )
                                }
                            }
                        }
                    )
                }
            }
            /* Hide cards */
            else {
                showHideText.text = "Show"
                hidden = true
                refreshHandScore(rootService.currentGame!!.players[currentPlayer])
                /* Play flip animation and refresh after it finishes */
                rootService.currentGame!!.players[currentPlayer].handCards.forEach {
                    this@SchwimmenGameScene.playAnimation(
                        FlipAnimation(
                            componentView = handView.backward(it),
                            fromVisual = ImageVisual(
                                cardImageLoader.resizedBufferedImage(cardImageLoader.frontImageForCard(it), 190, 250)
                            ),
                            toVisual = ImageVisual(
                                cardImageLoader.resizedBufferedImage(cardImageLoader.backImage, 190, 250)
                            ),
                            duration = 300
                        ).apply {
                            /* No need to refresh after each animation. Only one is enough */
                            if(it.equals(rootService.currentGame!!.players[currentPlayer].handCards[2])) {
                                onFinished = {
                                    refreshView(
                                        rootService.currentGame!!.players[currentPlayer].handCards,
                                        rootService.currentGame!!.players[currentPlayer].handCards,
                                        handView
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    /**
     * Reads "Table cards" at the top center of the screen.
     */
    private val tableLabel: Label = Label(
        posX = 800, posY = 50, width = 300, height = 50,
        text = "", font = globalFont, alignment = Alignment.CENTER
    ).apply{ this.isDisabled = true }

    /**
     * Text visualized at the bottom of hand cards to indicate current player.
     */
    private val currentPlayerLabel: Label = Label(
        posX = 800, posY = 750, width = 300, height = 100,
        text = "Click Start", font = globalFont, alignment = Alignment.CENTER
    )

    /**
     * @return the index of the next player in the circle.
     */
    fun refreshCurrentPlayer(index: Int): Int {
        if(index < rootService.currentGame!!.players.size - 1) {
            return index + 1
        }
        else {
            return 0
        }
    }

    /**
     *
     * @param buttons
     * @param next
     * @param disable set to true if [buttons] need to be disabled.
     */
    fun refreshActionButtons(buttons: List<Button>, next: Button, disable: Boolean): Unit {
        buttons.forEach {
            it.isDisabled = true
            it.visual = ImageVisual("${it.visual.toString().removeSuffix(".png")}Disabled.png")
        }
        next.isDisabled = false
        next.visual = ImageVisual("ActionNext.png")
    }

    /**
     * Updates the view of a given 3-card group. It operates on both player cards view and table cards view.
     * Refreshed elements will be removed from the corresponding selection [MutableList].
     * @param oldSet is the old set of cards. This is used to retrieve the corresponding [Label]s.
     * @param newSet is the new set of cards. This is used to retrieve the corresponding [Label]s.
     * @param view is used to retrieve the [Label] components, remove them and new ones.
     */
    private fun refreshView(
        oldSet: List<SchwimmenCard>,
        newSet: List<SchwimmenCard>,
        view: BidirectionalMap<Label, SchwimmenCard>
    ): Unit {
        /* Approach is to remove old cards and add new ones */
        val y = if(view == handView) 490 else 150
        oldSet.forEach {
            removeComponents(view.backward(it))
            /* Clear selection to make it easier to implement change one card */
            when(view) {
                handView -> handSelection.remove(it)
                tableView -> tableSelection.remove(it)
            }
            /* Only remove tuple at the end, otherwise [Label] won't be found */
            view.remove(view.backward(it), it)
        }
        /* Play little animation to indicate change in the table view */
        when(view) { tableView -> this@SchwimmenGameScene.playAnimation(SequentialAnimation(
            MovementAnimation(tableLabel, 0, -3,200),
            MovementAnimation(tableLabel, 0, 6,100)
        )) }
        /* Create the new image labels  representing the cards */
        var x = 630
        newSet.forEach {
            addComponents(
                Label(
                    x, y, 190, 250,
                ).apply {
                    view.add(this, it)
                    var selected = false
                    var effect = CompoundVisual(
                        ImageVisual(cardImageLoader.frontImageForCard(it))
                    )
                    /* Table cards are never hidden therefore always enabled */
                    if(view == handView) {
                        this.isDisabled = hidden
                        if(hidden) {
                            effect = viewEffect.hide(effect)
                        }
                    }
                    visual = effect
                    this.onMouseEntered = {
                        if (!this.isDisabled) {
                            effect = viewEffect.hover(effect)
                            visual = effect
                        }
                    }
                    this.onMouseExited = {
                        if (!this.isDisabled) {
                            effect = viewEffect.unhover(effect)
                            visual = effect
                        }
                    }
                    /* Make sure clicking the cards will either add or remove from selection */
                    this.onMouseClicked = {
                        if (!this.isDisabled) {
                            when (selected) {
                                false -> {
                                    effect = viewEffect.select(effect)
                                    visual = effect
                                    selected = true
                                    when (view) {
                                        handView -> handSelection.add(handView.forward(this))
                                        tableView -> tableSelection.add(tableView.forward(this))
                                    }
                                }
                                true -> {
                                    effect = viewEffect.unselect(effect)
                                    visual = effect
                                    selected = false
                                    when (view) {
                                        handView -> handSelection.remove(handView.forward(this))
                                        tableView -> tableSelection.remove(tableView.forward(this))
                                    }
                                }
                            }
                        }
                    }
                }
            )
            x += 220
        }
    }

    /**
     * Prepares the game scene. It also instantiates all objects. This is needed in
     * order to avoid null exceptions.
     */
    private fun initializeBoard(): Unit {
        showHideText.text = "Show"
        deckCount.text = "${rootService.currentGame!!.deck.cards.size} left"
        addComponents(showHideToggle, showHideText, handScore, deckCount,handScorePoints, nextButton)
        refreshHandScore(rootService.currentGame!!.players[currentPlayer])
        tableLabel.text = "Table cards"
        currentPlayerLabel.text = rootService.currentGame!!.players[0].name
        /* Create view for player cards */
        var x = 630
        rootService.currentGame?.players?.get(currentPlayer)?.handCards?.forEach {
            addComponents(
                Label(
                    x, 490, 190, 250,
                    visual = CompoundVisual()
                ).apply {
                    handView.add(this, it)
                    var selected: Boolean = false
                    this.isDisabled = hidden
                    var effect = CompoundVisual(
                        ImageVisual(cardImageLoader.frontImageForCard(it))
                    )
                    if(hidden) {
                        effect = viewEffect.hide(effect)
                    }
                    visual = effect
                    this.onMouseEntered = {
                        if (!this.isDisabled) {
                            effect = viewEffect.hover(effect)
                            visual = effect
                        }
                    }
                    this.onMouseExited = {
                        if (!this.isDisabled) {
                            effect = viewEffect.unhover(effect)
                            visual = effect
                        }
                    }
                    this.onMouseClicked = {
                        if (!this.isDisabled) {
                            when (selected) {
                                false -> {
                                    effect = viewEffect.select(effect)
                                    visual = effect
                                    selected = true
                                    handSelection.add(handView.forward(this))
                                }
                                true -> {
                                    effect = viewEffect.unselect(effect)
                                    visual = effect
                                    selected = false
                                    handSelection.remove(handView.forward(this))
                                }
                            }
                        }
                    }

                }
            )
            x += 220
        }
        /* Create view for table cards */
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
                    this.onMouseEntered = {
                        if (!this.isDisabled) {
                            effect = viewEffect.hover(effect)
                            visual = effect
                        }
                    }
                    this.onMouseExited = {
                        if (!this.isDisabled) {
                            effect = viewEffect.unhover(effect)
                            visual = effect
                        }
                    }
                    this.onMouseClicked = {
                        if (!this.isDisabled) {
                            when (selected) {
                                false -> {
                                    effect = viewEffect.select(effect)
                                    visual = effect
                                    selected = true
                                    tableSelection.add(tableView.forward(this))
                                }
                                true -> {
                                    effect = viewEffect.unselect(effect)
                                    visual = effect
                                    selected = false
                                    tableSelection.remove(tableView.forward(this))
                                }
                            }
                        }
                    }

                }
            )
            x += 220
        }
        /* Create view for player names on the right */
        var playerLabelsY = 250
        rootService.currentGame?.players?.forEach{
            addComponents(Label(
                posX = 1550, posY = playerLabelsY, width = 300, height = 10,
                text = it.name, font = globalFont, alignment = Alignment.TOP_LEFT
            ).apply {
                this.isDisabled = true
                namesView.add(this, it)
            })
            playerLabelsY += 70
        }
    }

    /**
     * Updates the count of deck cards.
     * @param current is the current [Label] that's showing the count.
     * @return the updated count as [Label].
     */
    private fun refreshDeckCount(): Unit {
        this@SchwimmenGameScene.playAnimation(SequentialAnimation(
            MovementAnimation(deckCount, 0, -3,200).apply {
                onFinished = { deckCount.text = "${rootService.currentGame!!.deck.cards.size} left" }
            },
            MovementAnimation(deckCount, 0, 6,100)
        ))
    }

    /**
     * Updates the score of a player's hand. Shown on the left. If hidden, "??" will be shown
     * instead of the number.
     * @param current is the current [Label] that's showing the score.
     * @param player is the player who is holding the cards.
     */
    private fun refreshHandScore(player: SchwimmenPlayer): Unit {
        this@SchwimmenGameScene.playAnimation(SequentialAnimation(
            MovementAnimation(handScore, 0, -3,200).apply {
                onFinished = {
                    val score = player.checkHandScore()
                    if(score != 30.5) {
                        handScore.text = "${score.toInt()}"
                    }
                    else {
                        handScore.text = "$score"
                    }
                    if(hidden) {
                        handScore.text = "??"
                    }
                }
            },
            MovementAnimation(handScore, 0, 6,100)
        ))
    }

    /**
     * Loads the four action buttons at the bottom of the screen.
     */
    private fun loadActionButtons(): Unit {
        val actionChangeOne = Button(
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

        val actionChangeAll = Button(
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

        val actionKnock = Button(
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

        val actionPass = Button(
            width = 222, height =  107, posX = 1170, posY = 871,
            alignment = Alignment.CENTER,
            visual = ImageVisual("images/ActionPass.png")
        ).apply {
            onMouseEntered = {
                if (!this.isDisabled) {
                    this.visual = ImageVisual("images/ActionPassHover.png")
                }
            }
            onMouseExited = {
                if (!this.isDisabled) {
                    this.visual = ImageVisual("images/ActionPass.png")
                }
            }
            onMouseClicked = {
                if (!this.isDisabled) {

                }
            }

        }

        addComponents(actionChangeOne, actionChangeAll, actionKnock, actionPass)
    }

    /**
     * Shows beautiful animations of drawing 3 cards to the table.
     * Updates the [tableCards] list and refreshes [tableView].
     * Game will be ended if deck has insufficient cards.
     */
    private fun drawThreeCards(): Unit {
        rootService.currentGame!!.deck.drawThreeCards().let { newCards ->
            if(newCards == null) {
                /* Game ends on the spot */
                println("Game over")
            }
            else {
                /* Create card images on top of the deck for animations */
                newCards.forEach { card ->
                    addComponents(Label(
                        width = 190, height = 250, posY = 300, posX = 200,
                    ).apply {
                        this.isDisabled = true
                        var effect = CompoundVisual(ImageVisual(cardImageLoader.frontImageForCard(card)))
                        effect = viewEffect.hide(effect)
                        this.visual = effect
                        tableView.add(this, card)
                    })
                }
                /* Play beautiful animations on each of the three drawn cards
                * and make sure that each one is facing front upon landing */
                this@SchwimmenGameScene.playAnimation(SequentialAnimation(
                    ParallelAnimation(
                        RotationAnimation(tableView.backward(newCards[0]), 360.0, 400),
                        MovementAnimation(tableView.backward(newCards[0]), 430, -150, 400),
                        FlipAnimation(tableView.backward(newCards[0]),
                            ImageVisual(cardImageLoader.resizedBufferedImage(cardImageLoader.backImage, 190, 250)),
                            ImageVisual(cardImageLoader.resizedBufferedImage(cardImageLoader.frontImageForCard(newCards[0]), 190, 250)),
                            400
                        )
                    ).apply {
                        onFinished = {
                            var effect = CompoundVisual(ImageVisual(cardImageLoader.frontImageForCard(newCards[0])))
                            tableView.backward(newCards[0]).visual = effect
                        }
                    },
                    ParallelAnimation(
                        RotationAnimation(tableView.backward(newCards[1]), 360.0, 400),
                        MovementAnimation(tableView.backward(newCards[1]), 650, -150, 400),
                        FlipAnimation(tableView.backward(newCards[1]),
                            ImageVisual(cardImageLoader.resizedBufferedImage(cardImageLoader.backImage, 190, 250)),
                            ImageVisual(cardImageLoader.resizedBufferedImage(cardImageLoader.frontImageForCard(newCards[1]), 190, 250)),
                            300
                        )
                    ).apply {
                        onFinished = {
                            var effect = CompoundVisual(ImageVisual(cardImageLoader.frontImageForCard(newCards[1])))
                            tableView.backward(newCards[1]).visual = effect
                        }
                    },
                    ParallelAnimation(
                        RotationAnimation(tableView.backward(newCards[2]), 360.0, 400),
                        MovementAnimation(tableView.backward(newCards[2]), 870, -150, 400),
                        FlipAnimation(tableView.backward(newCards[2]),
                            ImageVisual(cardImageLoader.resizedBufferedImage(cardImageLoader.backImage, 190, 250)),
                            ImageVisual(cardImageLoader.resizedBufferedImage(cardImageLoader.frontImageForCard(newCards[2]), 190, 250)),
                            200
                        )
                    ).apply {
                        onFinished = {
                            var effect = CompoundVisual(ImageVisual(cardImageLoader.frontImageForCard(newCards[2])))
                            tableView.backward(newCards[2]).visual = effect
                        }
                    }
                ).apply {
                    onFinished = {
                        refreshDeckCount()
                        /* Since those labels are created for the sole purpose of animation,
                        * they have to be deleted, and recreated using [refreshView] */
                        newCards.forEach { card ->
                            removeComponents(tableView.backward(card))
                            tableView.remove(tableView.backward(card), card)
                        }
                        refreshView(oldSet = rootService.currentGame!!.tableCards, newSet = newCards, view = tableView)
                        rootService.currentGame!!.tableCards.clear()
                        newCards.forEach { card ->
                            rootService.currentGame!!.tableCards.add(card)
                        }
                    }
                })
            }
        }
    }

    init {
        background = CompoundVisual(
            //ImageVisual("images/Background.jpg"),
            ColorVisual(17, 26, 37, 255)
        )
        opacity = 1.0
        addComponents(deck, exitButton,
            startButton, testCard,
            tableLabel, currentPlayerLabel
        )
    }
}