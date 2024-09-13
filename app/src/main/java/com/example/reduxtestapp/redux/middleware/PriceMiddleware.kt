package com.example.reduxtestapp.redux.middleware

import com.example.reduxtestapp.common.Poller
import com.example.reduxtestapp.data.repository.price.PriceRepository
import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.Middleware
import kotlinx.coroutines.flow.map
import org.reduxkotlin.Store

const val POLL_PRICE_KEY = "poll_price_key"
class PriceMiddleware(
    private val repo: PriceRepository,
    private val poller: Poller
): Middleware() {
    override fun middleware(store: Store<AppState>, action: Any) {
        when (action) {
            is Action.Home.GetPrice -> {
                store.dispatch(Action.Async{
                    val result = repo.getPrice()
                    store.dispatch(Action.Home.UpdatePrice(result))
                })
            }
            is Action.Home.PollPrice -> {
                val priceFlow = poller.poll(3000L) {
                    repo.getPrice()
                }.map {
                    Action.Home.UpdatePrice(it)
                }
                store.dispatch(Action.Stream(POLL_PRICE_KEY, priceFlow))
            }
            is Action.Home.StopPollingPrice -> {
                store.dispatch(Action.StopStream(POLL_PRICE_KEY))
            }
        }
    }
}