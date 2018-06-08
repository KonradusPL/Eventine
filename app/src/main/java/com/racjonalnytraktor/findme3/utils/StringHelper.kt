package com.racjonalnytraktor.findme3.utils

import android.util.Log

object StringHelper {
    fun getErrorCode(message: String): String{
        var errorCode = ""
        for(char in message){
            if (char.isDigit()){
                val index = message.indexOf(char,0,false)
                errorCode = message.substring(index,message.lastIndex)
                if(errorCode.length > 3)
                    errorCode = message
                break
            }
        }
        return errorCode
    }

    fun getCalendarText(message: String): String{
        var newText = ""
        if(message.isEmpty())
            return ""
        var text = ""
        if(message.length > 10){
            text = text.plus(message.substring(0,10))
        }
        if(message.length > 19){
            text = text.plus(" " + message.substring(11,19))
            var char1 = text[11]
            var char2 = text[12]
            val newHour = transformHours(char1,char2)
            newText = message.substring(0,10) + " " + newHour + message.substring(13,19)

        }
        return newText
    }

    fun transformHours(char1: Char, char2: Char): String{

        if(!char1.isDigit() || !char2.isDigit())
            return  ""

        var mChar1 = char1
        var mChar2 = char2

        if(mChar2.toInt() >= 56/*8*/ && (mChar1 == '0' || mChar1 == '1')){
            if(mChar2 == '8')
                mChar2 = '0'
            else if(mChar2 == '9')
                mChar2 = '1'

            mChar1 = (mChar1.toInt() + 1).toChar()
        }
        else if(mChar2.toInt() in 48..55 && (mChar1 == '0' || mChar1 == '1')){
            mChar2 =  (mChar2.toInt() + 2).toChar()
        }
        else if(mChar1 == '2'){
            if(mChar2 == '3' || mChar2 == '2'){
                mChar1 = '0'
                if (mChar2 == '3')
                    mChar2 = '1'
                else
                    mChar2 = '0'
            }else{
                mChar2 = (mChar2.toInt() + 2).toChar()

            }

        }

        var string: String  = ""
        string = string.plus(mChar1)
        string = string.plus(mChar2)

        return string
    }
}