package com.kush.learningkotlin.learningRandomStuff.data

import android.widget.Toast
import kotlinx.coroutines.*

fun main(args: Array<String>) {

    println("Start")
    GlobalScope.launch {
        delay(1000)
        println("World")
    }
    println("Hello, ")
    runBlocking {
        delay(2000)
    }

    //jobCoroutine()

    var str: String? = "Hello Kotlin old friend"
    println(str)
    str = null
    //var toPrint = str ?: "NUll Value"
    println(str)
}

fun jobCoroutine() = runBlocking {
    val job = launch {
        delay(1000)
        println("Kush World")
    }
    println("Hello Kush, ")
    job.join()

}
