package service

import view.Refreshable

/**
 * Abstract service class that handles multiple [Refreshable]s.
 * @property refreshables Contains all the elements on which a given method will be called
 * using [onAllRefreshables].
 */
abstract class AbstractRefreshingService {
    private val refreshables = mutableListOf<Refreshable>()

    /**
     * Adds a [Refreshable] element to the list.
     */
    fun addRefreshable(newRefreshable: Refreshable) {
        refreshables.add(newRefreshable)
    }

    /**
     * Applies a forwarded method on all elements from [refreshables]
     */
    fun onAllRefreshables(method: Refreshable.() -> Unit) =
        refreshables.forEach { it.method() }
}