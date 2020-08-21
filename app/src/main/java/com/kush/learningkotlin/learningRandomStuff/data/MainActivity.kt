package com.kush.learningkotlin.learningRandomStuff.data

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kush.learningkotlin.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CoroutineScope(Dispatchers.Main).launch {
            val currentMillis = System.currentTimeMillis()
            val retVal1 = downloadTask1()
            val retVal2 = downloadTask2()
            val retVal3 = downloadTask3()
            println("All launch tasks downloaded! ${retVal1}, ${retVal2}, $retVal3 in ${(System.currentTimeMillis() - currentMillis)/1000} seconds")
        }

        // async
        CoroutineScope(Dispatchers.Main).launch {
            val currentMillis = System.currentTimeMillis()
            val retVal1 = async(Dispatchers.IO) { downloadTask1() }
            val retVal2 = async(Dispatchers.IO) { downloadTask2() }
            val retVal3 = async(Dispatchers.IO) { downloadTask3() }

            println("All async tasks downloaded! ${retVal1.await()}, ${retVal2.await()}, ${retVal3.await()} in ${(System.currentTimeMillis() - currentMillis) / 1000} seconds")
        }

    }
}

// Task 1 will take 5 seconds to complete download
suspend fun downloadTask1() : String {
    kotlinx.coroutines.delay(5000);
    return "Complete";
}

// Task 1 will take 8 seconds to complete download
suspend fun downloadTask2() : Int {
    kotlinx.coroutines.delay(8000);
    return 100;
}

// Task 1 will take 5 seconds to complete download
suspend fun downloadTask3() : Float {
    kotlinx.coroutines.delay(5000);
    return 4.0f;
}