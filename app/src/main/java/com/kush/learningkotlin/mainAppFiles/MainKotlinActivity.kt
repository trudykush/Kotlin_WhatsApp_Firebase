package com.kush.learningkotlin.mainAppFiles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kush.learningkotlin.R

class MainKotlinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_kotlin)

        var firebaseDatabase = FirebaseDatabase.getInstance()
        var databaseReference = firebaseDatabase.reference.push()

        // Write
        var employee = Employee("Kush", "CEO", "Malibu", 29)
        //databaseReference.setValue(employee)

//        databaseReference.setValue("Hello Kush!")

        // Read
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var value = snapshot.value as HashMap<*, *>
                var name = value["name"]
                Log.d("TAG", "onDataChange: $name")
                Log.d("TAG", "onDataChange: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        })

    }

    data class Employee(var name: String, var position: String,
                       var homeAddress: String, var age: Int )
}