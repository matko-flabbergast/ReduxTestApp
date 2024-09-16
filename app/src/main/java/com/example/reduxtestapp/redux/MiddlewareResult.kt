package com.example.reduxtestapp.redux

import kotlinx.coroutines.flow.Flow

sealed interface MiddlewareResult {
    data class ResultAction(val action: Action): MiddlewareResult
    data class Composite(val composite: List<MiddlewareResult>): MiddlewareResult
    data class Effect(val effect: () -> Unit): MiddlewareResult
    data class Async(val asyncFunc: suspend () -> MiddlewareResult): MiddlewareResult
    data class Stream(val key: String, val stream: Flow<MiddlewareResult>): MiddlewareResult
    data class StopStream(val key: String): MiddlewareResult
    data object Nothing: MiddlewareResult
}