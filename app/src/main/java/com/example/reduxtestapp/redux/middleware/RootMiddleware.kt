package com.example.reduxtestapp.redux.middleware

import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.Middleware
import com.example.reduxtestapp.redux.MiddlewareResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.reduxkotlin.Dispatcher
import org.reduxkotlin.Store

class RootMiddleware(
    private val dispatcher: CoroutineDispatcher,
    private val middlewareList : List<Middleware>
) {


    private fun middleware(store: Store<AppState>, action: Any) {
        val resultList = mutableListOf<MiddlewareResult>()
        if (action is Action) middlewareList.forEach {
            resultList.add(it.handleAction(store.state, action))
        }
        executeResult(MiddlewareResult.Composite(resultList), store)
    }

    fun launchMiddleware(store: Store<AppState>) = { next: Dispatcher ->
        { action: Any ->
            val result = next(action)
            middleware(store, action)
            result
        }
    }

    private fun executeResult(result: MiddlewareResult, store: Store<AppState>) {
        when (result) {
            is MiddlewareResult.ResultAction -> {
                store.dispatch(result.action)
            }
            is MiddlewareResult.Composite -> {
                result.composite.forEach {
                    executeResult(it, store)
                }
            }
            is MiddlewareResult.Effect -> {
                result.effect.invoke()
            }
            is MiddlewareResult.Async -> {
                CoroutineScope(dispatcher).launch {
                    executeResult(result.asyncFunc(), store)
                }
            }
            is MiddlewareResult.Stream -> {
                cancelJob(result.key)
                streamJobMap[result.key] = CoroutineScope(dispatcher).launch {
                    result.stream.collect {
                        executeResult(it, store)
                    }
                }
            }
            is MiddlewareResult.StopStream -> {
                cancelJob(result.key)
            }
            else -> {}
        }
    }

    private val streamJobMap: HashMap<String, Job?> = hashMapOf()

    private fun cancelJob(key: String){
        streamJobMap[key]?.cancel()
        streamJobMap[key] = null
    }

}