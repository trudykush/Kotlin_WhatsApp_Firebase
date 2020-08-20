package com.kush.learningkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kush.learningkotlin.data.PersonListAdapter
import com.kush.learningkotlin.model.Person
import kotlinx.android.synthetic.main.activity_list_view.*

class ListView : AppCompatActivity() {

    private var adapter: PersonListAdapter? = null
    private var personList: ArrayList<Person>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view)

        /*var namesArray: Array<String> = arrayOf("Kush", "K", "A", "s", "w", "h", "Volvo", "BMW", "Ford", "Mazda",
                                                "MIhs", "SuperMan", "Batman", "IronMan", "GoodMan", "Badman")

        var namesAdapter: ArrayAdapter<String> = ArrayAdapter(this@ListView,
                            android.R.layout.simple_list_item_1, namesArray)

        listViewID.adapter = namesAdapter

        listViewID.setOnItemClickListener { adapterView, view, position, id ->
            var itemName: String = listViewID.getItemAtPosition(position).toString()
            Toast.makeText(this, "Clicked item is: ${namesArray[position]}", Toast.LENGTH_SHORT).show()
            Toast.makeText(this, itemName, Toast.LENGTH_LONG).show()

            }*/

        personList = ArrayList<Person>()

        for (i in 0..19) {
            val person = Person()
            person.name = "Kush $i"
            person.age = i

            personList!!.add(person)
        }

        layoutManager = LinearLayoutManager(this)
        adapter = PersonListAdapter(personList!!, this)

        recyclerViewContainer.layoutManager = layoutManager
        recyclerViewContainer.adapter = adapter

        adapter!!.notifyDataSetChanged()

    }
}