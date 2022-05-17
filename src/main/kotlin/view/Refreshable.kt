package view

import entity.*
import service.*

/**
 * This interface provides some events-like functions to notify [service] elements about any changes
 * that have been made to entity in order to trigger an update on the UI.
 */
interface Refreshable {

    /**
     * Trigger a refresh upon starting the game,
     */
    fun refreshAfterStartNewGame() {}

    /**
     * After a player has made a move, some elements will need to be refreshed.
     */
    fun refreshAfterTurn() {}

    /**
     * At the end of the game, elements also have to be refreshed.
     */
    fun refreshAfterGameEnd() {}

}