package service

import entity.*

/**
 * This class is responsible for managing and executing player actions.
 * @param rootService is a reference to [RootService] that hold connection with [GameService]
 * @property afterKnock is the number of players who've had a turn after someone had knocked.
 * 0 means nobody has knocked yet. The knocking player is also included in the counting.
 */
class PlayerActionService(private val rootService: RootService): AbstractRefreshingService() {
    var afterKnock: Int = 0

    /**
     * The turn function is the backbone of our program. It keeps looping and switching
     * players based on the game rules.
     */
    fun turn(
        action: Turn,
        player: SchwimmenPlayer,
        handCard: SchwimmenCard? = null,
        tableCard: SchwimmenCard? = null
    ) {
        if(!rootService.currentGame!!.gameLoop) {
            if(afterKnock >= 1) {
                afterKnock++
            }

            when(action) {
                Turn.PASS -> pass(player)
                Turn.KNOCK -> knock(player)
                Turn.CHANGEONE -> changeOne(player, handCard, tableCard)
                Turn.CHANGEALL -> changeAll(player)
            }
        }
    }

    /**
     * If all players have passed successively, cards on the table are to be thrown away
     * and replaced with three cards from deck.
     * If deck has insufficient cards, then game has to be ended immediately
     * @param player is the person that is currently playing.
     * @param returns three cards as a mutable list in case a full round had been passed.
     * Otherwise, null. If the size of the returned [MutableList] is 0, that means the
     * deck has insufficient cards.
     */
    fun pass(player: SchwimmenPlayer): MutableList<SchwimmenCard>? {
        rootService.currentGame!!.incrementPassCounter()
        /**
         * Game rule: all players have passed -> throw table cards and draw 3 new ones.
         */
        if (rootService.currentGame!!.passCounter == rootService.currentGame!!.players.size) {
            val list = mutableListOf<SchwimmenCard>()
            rootService.currentGame!!.tableCards.clear()
            rootService.currentGame!!.deck.drawThreeCards()
                ?.let { list.addAll(it) }
            rootService.currentGame!!.passCounter = 0
            /**
             * Game rule: insufficient cards -> game over
             */
            if (list.size == 0) {
                rootService.currentGame!!.gameLoop = false
            }
            return list
        }
        return null
    }

    /**
     * Once a player has knocked, a countdown is started, so that each player gets exactly one turn after.
     * @param player is the person that is currently playing.
     */
    fun knock(player: SchwimmenPlayer): Unit {
        if(afterKnock == 0) {
            afterKnock++
        }
        rootService.currentGame!!.passCounter = 0
    }

    /**
     * Swaps two selected cards between table and hand.
     * @param player is the person that is currently playing.
     * @param hand is the selected card from hand.
     * @param table is the selected card from table.
     */
    fun changeOne(player: SchwimmenPlayer, hand: SchwimmenCard?, table: SchwimmenCard?): Unit {
        rootService.currentGame!!.tableCards.let {
            // It is important to remember the indices of our cards to keep the order
            val tableIndex = rootService.currentGame?.tableCards?.indexOf(table)
            val handIndex = player.handCards.indexOf(hand)
            it.remove(table)
            player.handCards.remove(hand)
            hand?.let { h -> it.add(tableIndex!!, h) }
            table?.let { t -> player.handCards.add(handIndex, t) }
        }
        rootService.currentGame!!.passCounter = 0
    }

    /**
     * Swaps a player's hand with the cards on the table.
     * @param player is the person that is currently playing.
     */
    fun changeAll(player: SchwimmenPlayer): Unit {
        for(i in 1..3) {
            rootService.currentGame!!.tableCards.let {
                player.handCards.add(it.removeFirst())
                it.add(player.handCards.removeFirst())
            }
        }
        rootService.currentGame!!.passCounter = 0
    }
}