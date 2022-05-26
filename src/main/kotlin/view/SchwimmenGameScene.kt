package view

import entity.SchwimmenCard
import entity.SchwimmenPlayer
import entity.SchwimmenGame
import service.AbstractRefreshingService
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
 * @property handView is a tuple set of type ([Label], [SchwimmenCard]), where the card belongs to the current [SchwimmenPlayer].
 * @property tableView is a tuple set of type ([Label], [SchwimmenCard]), where the card belongs to the table.
 * @property namesView is a tuple set of type ([Label], [SchwimmenPlayer]). This holds every player
 * connected with their displayed names.
 * @property checkView is a tuple set of type([Label], [SchwimmenPlayer]). This holds every player
 * connected with their status, whether it's a pass, or play-after-knock.
 * @property knocked indicates whether someone has already knocked, meaning it's the last round.
 * @property afterKnock is the number of players who've had a turn after knock. Starting from the
 * player who knocked first with the value of 1. This is controlled by [refreshCheckView].
 * @property currentPlayer is the index of the player currently playing.
 * @property hidden is set to true if hand cards are hidden.
 * @property globalFont is the standard font used in this UI.
 * @property handSelection is the list of selected cards from hand. This is necessary for [actionChangeOne].
 * @property tableSelection is the list of selected cards from table. This is necessary for [actionChangeOne].
 */
class SchwimmenGameScene(
    val app: SchwimmenApplication,
    private val rootService: RootService): BoardGameScene(1920, 1080), Refreshable {

    private val cardImageLoader = CardImageLoader()
    private val viewEffect = CardViewEffect()
    /* This serves the purpose of initializing the board */
    private var initialized = false

    private val handView: BidirectionalMap<Label, SchwimmenCard> = BidirectionalMap()
    private val tableView: BidirectionalMap<Label, SchwimmenCard> = BidirectionalMap()
    private val namesView: BidirectionalMap<Label, SchwimmenPlayer> = BidirectionalMap()
    private val checkView: BidirectionalMap<Label, SchwimmenPlayer> = BidirectionalMap()

    private var knocked: Boolean = false
    private var afterKnock: Int = 0
    private var currentPlayer: Int = 0
    private var hidden: Boolean = false

    private val handSelection: MutableList<SchwimmenCard> = mutableListOf()
    private val tableSelection: MutableList<SchwimmenCard> = mutableListOf()

    /**
     * @author sang.
     * https://pngtree.com/freepng/rotate-left-circular-arrow-blue_6421127.html
     */
    private val passImage = "images/Pass.png"

    private val checkImage = "images/Check.png"

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

    private val testCard: Label = Label(
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
            println(this.visual.toString())
            println(this.name)
            println(::testCard.name)
            //drawThreeCards()
        }
    }

    /**
     * To switch turn to the next player.
     */
    private val nextButton = Button(
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
                /* Next player's turn */
                refreshActionButtons(this, false)
                rootService.playerActionService.onAllRefreshables {
                    refreshAfterTurn()
                }
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

    private val deckCount = Label(
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
                if (!initialized) {
                    initializeBoard()
                    initialized = true
                }
                //refreshView(rootService.currentGame?.players[0].handCards, rootService.currentGame!!.players[0].handCards, handView)
                //refreshView(rootService.currentGame!!.tableCards, rootService.currentGame!!.tableCards, tableView)
                refreshDeckCount()
                refreshHandScore()
                this.isDisabled = true
                removeComponents(this)
            }
        }

    }

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
                val list: MutableList<SchwimmenCard>? = rootService.playerActionService.pass(
                    rootService.currentGame!!.players[currentPlayer]
                )
                /* If list was created, it means a full round has been passed */
                if(list != null) {
                    /* If the list is empty, this means deck had insufficient cards */
                    if(list.size == 0) {
                        // End the game
                        rootService.currentGame!!.gameLoop = false
                    }
                    else {
                        drawThreeCards(list)
                    }
                }
                refreshActionButtons(this, true)
                refreshCheckView()
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
    private val showHideToggle = ToggleButton(
        posX = 1280, posY = 650, width = 10, height = 10
    ).apply {
        hidden = true
        showHideText.isDisabled = true
        selectedProperty.addListener { _, _ ->
            /* Show cards */
            if(this.isSelected) {
                showHideText.text = "Hide"
                hidden = false
                refreshHandScore()
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
                refreshHandScore()
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
    private val tableLabel = Label(
        posX = 800, posY = 50, width = 300, height = 50,
        text = "", font = globalFont, alignment = Alignment.CENTER
    ).apply{ this.isDisabled = true }

    /**
     * Text visualized at the bottom of hand cards to indicate current player.
     */
    private val currentPlayerLabel = Label(
        posX = 800, posY = 750, width = 300, height = 100,
        text = "Click Start", font = globalFont, alignment = Alignment.CENTER
    )

    /**
     * @return the index of the next player in the circle, cycling between 0 and player count.
     */
    private fun getNextPlayerIndex(): Int {
        return if(currentPlayer < rootService.currentGame!!.players.size - 1) {
            currentPlayer + 1
        } else {
            0
        }
    }

    /**
     * Updates the value of [currentPlayer] to the next index.
     */
    private fun refreshCurrentPlayerIndex() {
        currentPlayer = getNextPlayerIndex()
    }

    /**
     * Inherited from [Refreshable]. Prepares for the next player's turn.
     */
    override fun refreshAfterTurn() {
        showHideToggle.isSelected = false
        refreshView(
            oldSet = rootService.currentGame!!.players[currentPlayer].handCards,
            newSet = rootService.currentGame!!.players[getNextPlayerIndex()].handCards,
            view = handView
        )
        refreshCurrentPlayerIndex()
        refreshPlayerNames()
        refreshHandScore()
    }

    /**
     * Enables / disables action buttons after an action had been clicked.
     * @param disable set to true if desired action is to disable  the buttons.
     */
    private fun refreshActionButtons(clicked: Button, disable: Boolean) {
        if(disable) {
            /* Disable action buttons -> enable next and vice versa */
            actionChangeOne.isDisabled = true
            actionChangeOne.visual = ImageVisual("images/ActionChangeOneDisabled.png")
            actionChangeAll.isDisabled = true
            actionChangeAll.visual = ImageVisual("images/ActionChangeAllDisabled.png")
            actionKnock.isDisabled = true
            actionKnock.visual = ImageVisual("images/ActionKnockDisabled.png")
            actionPass.isDisabled = true
            actionPass.visual = ImageVisual("images/ActionPassDisabled.png")
            /* In all cases except for this one a player had to click Next clicking an action button */
            if(clicked != actionChangeOne) {
                nextButton.isDisabled = false
                nextButton.visual = ImageVisual("images/ActionNext.png")
            }
        }
        else {
            actionChangeOne.isDisabled = false
            actionChangeOne.visual = ImageVisual("images/ActionChangeOne.png")
            actionChangeAll.isDisabled = false
            actionChangeAll.visual = ImageVisual("images/ActionChangeAll.png")
            actionKnock.isDisabled = false
            actionKnock.visual = ImageVisual("images/ActionKnock.png")
            actionPass.isDisabled = false
            actionPass.visual = ImageVisual("images/ActionPass.png")
            nextButton.isDisabled = true
            nextButton.visual = ImageVisual("images/ActionNextDisabled.png")
        }

    }

    /**
     * Updates the view of a given 3-card group. It operates on both player cards view and table cards view.
     * Refreshed elements will be removed from the corresponding selection [MutableList].
     * @param oldSet is the old set of cards. This is used to retrieve the corresponding [Label]s.
     * @param newSet is the new set of cards. This is used to retrieve the corresponding [Label]s.
     * @param view is the map that helps find UI components to be removed.
     */
    private fun refreshView(
        oldSet: List<SchwimmenCard>,
        newSet: List<SchwimmenCard>,
        view: BidirectionalMap<Label, SchwimmenCard>
    ) {
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
     * Updates the count of deck cards and refreshes [deckCount]-
     */
    private fun refreshDeckCount() {
        this@SchwimmenGameScene.playAnimation(SequentialAnimation(
            MovementAnimation(deckCount, 0, -3,200).apply {
                onFinished = { deckCount.text = "${rootService.currentGame!!.deck.cards.size} left" }
            },
            MovementAnimation(deckCount, 0, 6,100)
        ))
    }

    /**
     * Refreshes player names on the right. Please note that [currentPlayer] is
     * to be refreshed using [getNextPlayerIndex] before applying this function.
     */
    private fun refreshPlayerNames() {
        for (player in rootService.currentGame!!.players) {
            if (player == rootService.currentGame!!.players[currentPlayer]) {
                this@SchwimmenGameScene.playAnimation(SequentialAnimation(
                    MovementAnimation(namesView.backward(player), 0, -3,200).apply {
                        onFinished = {
                            namesView.backward(player).isDisabled = false
                        }
                    },
                    MovementAnimation(namesView.backward(player), 0, 6,100)
                ))
            }
            else {
                namesView.backward(player).isDisabled = true
            }
        }
        currentPlayerLabel.text = "${rootService.currentGame!!.players[currentPlayer]}"
    }

    /**
     * Refreshes the check image on the left of player names. This fun is to be called after turn
     * is finished, and before clicking [nextButton]. It increments [afterKnock] and can end the
     * game if a full round had been made after someone had knocked.
     */
    private fun refreshCheckView() {
        /* Knock section */
        if(rootService.playerActionService.afterKnock > 0) {
            checkView.backward(
                rootService.currentGame!!.players[currentPlayer]
            ).visual = ImageVisual(checkImage)
            /* afterKnock will be incremented from playerActionService */
            if (rootService.playerActionService.afterKnock >= rootService.currentGame!!.players.size) {
                /* finish game */
            }
        }
        /* Pass section */
        else {
            if(rootService.currentGame!!.passCounter == 0) {
                /* This means either a full round or no round had been made with pass */
                rootService.currentGame!!.players.forEach { player ->
                    checkView.backward(player).visual = Visual.EMPTY
                }
            }
            else {
                checkView.backward(
                    rootService.currentGame!!.players[currentPlayer]
                ).visual = ImageVisual(passImage)
                /* Play beautiful spinning arrow animations */
                playAnimation(RotationAnimation(
                    checkView.backward(rootService.currentGame!!.players[currentPlayer]),
                    -270.0,
                    500
                ))
            }
        }
    }

    /**
     * Updates the score of the current player's hand. Shown on the left.
     * If hidden, "??" will be shown instead of the number.
     */
    private fun refreshHandScore() {
        this@SchwimmenGameScene.playAnimation(SequentialAnimation(
            MovementAnimation(handScore, 0, -3,200).apply {
                onFinished = {
                    val score = rootService.currentGame!!.players[currentPlayer].checkHandScore()
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
     * Shows beautiful animations of drawing 3 cards to the table.
     * Updates the list of table cards and refreshes [tableView],
     * and adds the drawn cards to [SchwimmenGame.tableCards].
     */
    private fun drawThreeCards(newCards: MutableList<SchwimmenCard>) {
        refreshDeckCount()
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
                    tableView.backward(newCards[0]).visual = ImageVisual(cardImageLoader.frontImageForCard(newCards[0]))
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
                    tableView.backward(newCards[1]).visual = ImageVisual(cardImageLoader.frontImageForCard(newCards[1]))
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
                    tableView.backward(newCards[2]).visual = ImageVisual(cardImageLoader.frontImageForCard(newCards[2]))
                }
            }
        ).apply {
            onFinished = {
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

    /**
     * Prepares the game scene. It also instantiates all objects. This is needed in
     * order to avoid null exceptions.
     */
    private fun initializeBoard() {
        showHideText.text = "Show"
        deckCount.text = "${rootService.currentGame!!.deck.cards.size} left"
        addComponents(showHideToggle, showHideText, handScore, deckCount,handScorePoints, nextButton)
        addComponents(actionChangeOne, actionChangeAll, actionKnock, actionPass)
        refreshHandScore()
        tableLabel.text = "Table cards"
        currentPlayerLabel.text = rootService.currentGame!!.players[0].name
        /* Create view for player cards */
        var x = 630
        rootService.currentGame?.players?.get(currentPlayer)?.handCards?.forEach {
            addComponents(
                Label(
                    x, 490, 190, 250,
                    visual = Visual.EMPTY
                ).apply {
                    handView.add(this, it)
                    var selected = false
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
                    var selected = false
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
        /* Create view for player names on the right with check view labels */
        var playerLabelsY = 250
        rootService.currentGame?.players?.forEach{
            addComponents(Label(
                posX = 1500, posY = playerLabelsY, width = 300, height = 10,
                text = it.name, font = globalFont, alignment = Alignment.TOP_LEFT
            ).apply {
                this.isDisabled = true
                namesView.add(this, it)
            }, Label(
                posX = 1440, posY = playerLabelsY + 15, width = 40, height = 40
            ).apply {
                this.isDisabled = true
                checkView.add(this, it)
            })
            playerLabelsY += 70
        }
        refreshPlayerNames()
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