package com.example.ingram.data

open class Event <out T>(val content: T) {
    var hasBeenhandle = false
    fun getContentOrNull():T? {
        return if(hasBeenhandle) null
        else{
            hasBeenhandle = true
            content
        }
    }
}