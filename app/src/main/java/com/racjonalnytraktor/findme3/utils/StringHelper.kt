package com.racjonalnytraktor.findme3.utils

import android.util.Log
import java.util.*

object StringHelper {
    fun getErrorCode(message: String): String{
        var errorCode = ""
        for(char in message){
            if (char.isDigit()){
                val index = message.indexOf(char,0,false)
                if(index + 2 < message.length)
                    errorCode = message.substring(index,index + 3)
                if(errorCode.length > 3)
                    errorCode = message
                return errorCode
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

    fun getTimeForAction(currentDate: Date, stealDate: Date): String{
        var different = currentDate.time - stealDate.time

        var timeText = "niedawno"

        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        val elapsedDays = (different / daysInMilli).toInt()
        different %= daysInMilli

        val elapsedHours = (different / hoursInMilli).toInt()
        different %= hoursInMilli

        val elapsedMinutes = (different / minutesInMilli).toInt()
        different %= minutesInMilli

        val elapsedSeconds = (different / secondsInMilli).toInt()

        var textPart = ""

        if(elapsedDays > 0){
            timeText = elapsedDays.toString()
            if (timeText.last() == '1' && timeText.length == 1)
                timeText = "dzień"
            else
                textPart = " dni"
        }else if(elapsedHours > 0){
            timeText = elapsedHours.toString()
            textPart = when(timeText.last()){
                '2' -> " godziny"
                '3' -> " godziny"
                '4' -> " godziny"
                else -> " godzin"
            }

            if(timeText.last() == '1' && timeText.length == 1)
                textPart = " godzina"
        }
        else if(elapsedMinutes > 0){
            timeText = elapsedMinutes.toString()
            textPart = when(timeText.last()){
                '2' -> " minuty"
                '3' -> " minuty"
                '4' -> " minuty"
                else -> " minut"
            }
        }
        else{
            return "Teraz"
        }
        return "$timeText$textPart temu"
    }
}