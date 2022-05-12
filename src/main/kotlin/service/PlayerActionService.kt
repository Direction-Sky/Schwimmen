package service

import entity.*

/**
 * This class is responsible for managing and executing player actions.
 * The property [afterKnock] keeps track of the players who have played after someone had knocked.
 */
class PlayerActionService(val rootService: RootService) {
    var afterKnock: Int = 0

    /**
     * The turn function is the backbone of our program. It keeps looping and switching
     * players based on the game rules.
     *
     */
    fun turn(): Unit {
        var action: Turn
        while(!rootService.currentGame.gameLoop) {
            /**
             * Game rule: all players have played once after a knock
             */
            if(afterKnock == rootService.currentGame.players.size) {
                rootService.currentGame.gameLoop = false
                continue
            }
            for(player in rootService.currentGame.players) {
                if(afterKnock >= 1) {
                    afterKnock++
                }
                // wait for player's selection
                action = Turn.PASS

                when(action) {
                    Turn.PASS -> pass(player)
                    Turn.KNOCK -> knock(player)
                    Turn.CHANGEONE -> changeOne(player,null ,null)
                    Turn.CHANGEALL -> changeAll(player)
                }
            }
        }
    }

    /**
     * If all players have passed successively, cards on the table are to be thrown away
     * and replaced with three cards from deck.
     * If deck has insufficient cards, then game has to be ended immediately
     */
    fun pass(player: SchwimmenPlayer): Unit {
        rootService.currentGame.incrementPassCounter()
        /**
         * Game rule: all players have passed -> throw table cards and draw 3 new ones
         */
        if (rootService.currentGame.passCounter() == rootService.currentGame.players.size) {
            rootService.currentGame.tableCards.clear()
            rootService.currentGame.deck.drawThreeCards()
                ?.let { rootService.currentGame.tableCards.addAll(it) }
            rootService.currentGame.passCounter = 0
            /**
             * Game rule: insufficient cards -> game over
             */
            if (rootService.currentGame.tableCards.size == 0) {
                rootService.currentGame.gameLoop = false
            }
        }
    }

    /**
     * Once a player has knocked, a countdown is started, so that each player gets exactly one turn after.
     */
    fun knock(player: SchwimmenPlayer): Unit {
        if(afterKnock == 0) {
            afterKnock++
        }
        rootService.currentGame.passCounter = 0
    }

    /**
     * Swaps two selected cards between table and hand.
     * @param player - the person whose turn it is.
     */
    fun changeOne(player: SchwimmenPlayer, hand: SchwimmenCard?, table: SchwimmenCard?): Unit {
        rootService.currentGame.tableCards.let {
            val tableIndex = rootService.currentGame.tableCards.indexOf(table)
            val handIndex = player.handCards.indexOf(hand)
            it.remove(table)
            player.handCards.remove(hand)
            hand?.let { h -> it.add(tableIndex, h) }
            table?.let { t -> player.handCards.add(handIndex, t) }
        }
        rootService.currentGame.passCounter = 0
    }

    /**
     * Swaps a player's hand with the cards on the table.
     * @param player - the person whose turn it is.
     */
    fun changeAll(player: SchwimmenPlayer): Unit {
        for(i in 1..3) {
            rootService.currentGame.tableCards.let {
                player.handCards.add(it.removeFirst())
                it.add(player.handCards.removeFirst())
            }
        }
        rootService.currentGame.passCounter = 0
    }
}