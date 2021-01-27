# tcpServer
Async NIO TCP Server

# Execute
Start app running `App.kt`

# Implementation
## Server
Using asynchronous channels 
```kotlin
    AsynchronousServerSocketChannel.open(channelGroup)
        .setOption(StandardSocketOptions.SO_REUSEPORT, true)
        .bind(InetSocketAddress(port))
        .acceptClientConnection(channelGroup)
```
## File writer
Asynchonous IO operation using _Coroutines Actor Channel_
```kotlin
val scope = CoroutineScope(Dispatchers.IO)

val logMessageActorNIO = scope.actor<String> {
    for (msg in channel) {
        if (validInputs.add(msg)) saveLog(msg, "numbers.log")
        else duplicated.addAndGet(1)
    }
}
```
## Report scheduler
Using `tickerChannel` coroutine
```kotlin
fun startReportScheduler() {
    GlobalScope.launch {
        val tickerChannel = ticker(Duration.ofSeconds(10).toMillis())
        for (event in tickerChannel) {
            val lastReport = createLastReport(
                Collections.unmodifiableList(reports),
                Collections.unmodifiableSet(validInputs), duplicated.get()
            )
            reports.add(lastReport)
            println(lastReport)
        }
    }
}
```
