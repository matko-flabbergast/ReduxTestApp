# Poller Implementation

This implementation adds a `Poller` class that implements polling with an increasing time interval strategy as requested.

## Features

- **Increasing Interval Strategy**: 10s → 20s → 1min → 2min, then continues at 2min intervals
- **Coroutine-based**: Built with Kotlin coroutines for async/await support
- **Cancellable**: Proper cancellation support and lifecycle management
- **Error Handling**: Optional error callback that doesn't stop polling for non-cancellation exceptions
- **Thread-safe**: Uses SupervisorJob and appropriate coroutine scopes
- **Testable**: Comprehensive unit tests covering all scenarios

## Usage

### Basic Usage

```kotlin
val scope = CoroutineScope(Dispatchers.IO)
val poller = Poller(scope)

// Start polling
val job = poller.startPolling(
    pollFunction = {
        // Your polling logic here
        println("Polling executed at ${System.currentTimeMillis()}")
    },
    onError = { exception ->
        // Handle errors (optional)
        println("Error: ${exception.message}")
    }
)

// Stop polling
poller.stopPolling()

// Cleanup
poller.cancel()
```

### Integration with Redux

The `PollingMiddleware` class demonstrates how to integrate the Poller with the existing Redux architecture:

```kotlin
// In your store creation
val store = createThreadSafeStore(
    reducer = { state, action -> todosReducer(state, action) },
    preloadedState = AppState(),
    enhancer = applyMiddleware(
        get<RepoMiddleware>()::todoMiddleware,
        get<PollingMiddleware>()::pollingMiddleware,
        get<AsyncMiddleware>()::asyncMiddleware
    )
)

// Start polling
store.dispatch(Action.Todo.StartPolling)

// Stop polling
store.dispatch(Action.Todo.StopPolling)
```

### Todo Repository Polling

The `TodoPollingService` provides a higher-level abstraction for polling todo updates:

```kotlin
val todoPollingService = TodoPollingService(todoRepository, store, scope)

// Start polling todos
todoPollingService.startTodoPolling()

// Stop polling
todoPollingService.stopTodoPolling()
```

## Implementation Details

### Polling Strategy

The poller follows this interval progression:

1. **First execution**: Immediate
2. **Second execution**: After 10 seconds
3. **Third execution**: After 20 seconds  
4. **Fourth execution**: After 1 minute (60 seconds)
5. **Subsequent executions**: After 2 minutes (120 seconds) each

### Error Handling

- **Non-cancellation exceptions**: Handled by optional error callback, polling continues
- **CancellationException**: Properly stops polling and cancels the job
- **Error recovery**: Polling continues even after errors to maintain resilience

### Lifecycle Management

- **Start/Stop**: Can start and stop polling multiple times
- **Cancellation**: Proper cleanup with `cancel()` method
- **Job management**: Returns Job instance for external cancellation if needed

## Files Created

- `app/src/main/java/com/example/reduxtestapp/data/poller/Poller.kt` - Main Poller class
- `app/src/main/java/com/example/reduxtestapp/data/poller/PollerUsageExample.kt` - Usage examples
- `app/src/main/java/com/example/reduxtestapp/redux/middleware/PollingMiddleware.kt` - Redux integration
- `app/src/test/java/com/example/reduxtestapp/data/poller/PollerTest.kt` - Comprehensive unit tests

## Testing

The implementation includes comprehensive unit tests covering:

- Interval progression validation
- Error handling scenarios  
- Cancellation behavior
- Lifecycle management
- Multiple polling sessions
- Exception handling vs cancellation

Run tests with:
```bash
./gradlew :app:testDebugUnitTest --tests="com.example.reduxtestapp.data.poller.PollerTest"
```

## Dependencies

The implementation uses:
- Kotlin Coroutines (`kotlinx-coroutines-core`)
- Kotlin Time (`kotlin.time.Duration`)
- Existing Redux architecture

No additional dependencies were added to maintain minimal impact on the codebase.