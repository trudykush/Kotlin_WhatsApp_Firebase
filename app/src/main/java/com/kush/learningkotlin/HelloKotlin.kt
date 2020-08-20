package com.kush.learningkotlin

import android.os.Build
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

fun main(args: Array<String>) {
    println("Hello Kush")

    var name: String? = null
    var count: Double = 45.05

    println("The name is $name and count is $count")

    var firstNum = 1420
    var secondNum = 48

    println("The sum is: ${firstNum + secondNum}")

    val myArray = IntArray(5)
    myArray[0] = 10
    myArray[1] = 20
    myArray[2] = 30
    myArray[3] = 40
    myArray[4] = 50

    myArray.set(0, 25)

    for (elements in myArray.indices) {
        println("The Array Yo - ${myArray[elements]}")
    }

    myArray.forEach {
     //   println("Printing array : ${myArray}")
    }

    for ((index, value) in myArray.withIndex()) {
        println("The Element at $index and $value")
    }

    var hashMap = HashMap<String, String>()
    hashMap["Kush"] = "Bwoy"
    hashMap.put("Piyu", "Boy")

    println(hashMap.get("Kush"))
    println(hashMap["Piyu"])

    for (k in hashMap.keys) println(hashMap[k])

    // Default is immutable
    var myArrayKotlin = listOf<String>("Kush", "Hello", "Hi")

    for (items in myArrayKotlin) println("Items $items")

    var mutableList = mutableListOf<String>("Kush", "Hi", "Hello", "Kotlin", "Java")
    mutableList.add("Moo")
    mutableList.add( "Yoo")

    for (items in mutableList) println("Items from mutable List $items")

    var myHashMap = hashMapOf(1 to "Kush", 2 to "Yo")

    try {
        var writer = FileWriter("message.txt", true)
        writer.write("Kush" + "\n")
        writer.close()
    } catch (e: Exception) {
    }

    var reader = FileReader("message.txt")
    var readMessage: String? = null
    var fileChar: Int?

    do {
        fileChar = reader.read()
        print(fileChar.toChar())
    } while (fileChar != -1)

    println("------------")

    /*val stream = Files.newInputStream(Paths.get("message.txt"))
    stream.buffered().reader().use {
            reader -> println(reader.readText())
    }*/


}