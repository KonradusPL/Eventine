package com.racjonalnytraktor.findme3.utils

object StringHelper {
    fun getErrorCode(message: String): String{
        var errorCode = ""
        for(char in message){
            if (char.isDigit()){
                val index = message.indexOf(char,0,false)
                errorCode = message.substring(index,message.lastIndex)
                break
            }
        }
        return errorCode
    }
}