package io.challenge.tcpserver.nio

import io.challenge.tcpserver.scope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.actor
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

var validInputs = mutableSetOf<String>()
var duplicated = AtomicInteger(0)

@ObsoleteCoroutinesApi
val logMessageActorNIO = scope.actor<String> {
    for (msg in channel) {
        if (validInputs.add(msg)) saveLog(msg, "numbers.log")
        else duplicated.addAndGet(1)
    }
}


fun saveLog(line: String, fileName: String) = File(fileName).appendText(line + "\n")
