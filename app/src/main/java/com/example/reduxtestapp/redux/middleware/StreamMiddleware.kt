package com.example.reduxtestapp.redux.middleware

import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.Middleware
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.reduxkotlin.Store

class StreamMiddleware(
    private val dispatcher: CoroutineDispatcher,
): Middleware() {

    private val jobMap: HashMap<String, Job?> = hashMapOf()

    override fun middleware(store: Store<AppState>, action: Any) {
        when (action) {
            is Action.Stream -> {
                cancelJob(action.key)
                jobMap[action.key] = CoroutineScope(dispatcher).launch {
                    action.actionFlow.collect {
                        store.dispatch(it)
                    }
                }
            }
            is Action.StopStream -> {
                cancelJob(action.key)
            }
        }
    }

    private fun cancelJob(key: String){
        jobMap[key]?.cancel()
        jobMap[key] = null
    }
}