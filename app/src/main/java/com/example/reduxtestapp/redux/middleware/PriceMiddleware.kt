package com.example.reduxtestapp.redux.middleware

import com.example.reduxtestapp.common.Poller
import com.example.reduxtestapp.data.repository.price.PriceRepository
import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.Middleware
import com.example.reduxtestapp.redux.MiddlewareResult
import kotlinx.coroutines.flow.map

const val POLL_PRICE_KEY = "poll_price_key"
class PriceMiddleware(
    private val repo: PriceRepository,
    private val poller: Poller
): Middleware() {
    override fun handleAction(state: AppState, action: Any): MiddlewareResult {
        return when (action) {
            is Action.Home.GetPrice -> handleGetPrice()
            is Action.Home.PollPrice -> handlePollPrice()
            is Action.Home.StopPollingPrice -> handleStopPolling()
            else -> MiddlewareResult.Nothing
        }
    }

    private fun handleGetPrice(): MiddlewareResult {
        return MiddlewareResult.Async{
            val result = repo.getPrice()
            MiddlewareResult.ResultAction(Action.Home.UpdatePrice(result))
        }
    }

    private fun handlePollPrice(): MiddlewareResult {
        val priceFlow = poller.poll(3000L) {
            repo.getPrice()
        }.map {
            MiddlewareResult.ResultAction(Action.Home.UpdatePrice(it))
        }
        return MiddlewareResult.Stream(POLL_PRICE_KEY, priceFlow)
    }

    private fun handleStopPolling(): MiddlewareResult {
         return MiddlewareResult.StopStream(POLL_PRICE_KEY)
    }
}