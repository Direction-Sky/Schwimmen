package service

import javax.security.auth.Refreshable

abstract class AbstractRefreshingService {
    public fun addRefreshable(newRefeshable: Refreshable) {}
    public fun Iterable<Refreshable>.onAllRefreshables(predicate: (Refreshable) -> Unit): Unit {}
}