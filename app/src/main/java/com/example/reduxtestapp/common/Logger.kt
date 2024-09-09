package com.example.reduxtestapp.common

interface Logger {

    fun log(tag: String, text: String, logType: LogType = LogType.DEBUG)

    enum class LogType{
        DEBUG, ERROR
    }
}