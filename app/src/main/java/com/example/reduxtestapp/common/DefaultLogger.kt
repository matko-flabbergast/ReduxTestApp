package com.example.reduxtestapp.common

import android.util.Log

object DefaultLogger : Logger {
    override fun log(tag: String, text: String, logType: Logger.LogType) {
        when (logType) {
            Logger.LogType.DEBUG -> Log.d(tag, text)
            Logger.LogType.ERROR -> Log.e(tag, text)
        }
    }

}