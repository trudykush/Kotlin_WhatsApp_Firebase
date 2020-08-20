package com.kush.learningkotlin.data

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kush.learningkotlin.R
import com.kush.learningkotlin.model.Person

/**
 * Created by Kush on 19/08/2020.
 */
class PersonListAdapter(private val list: ArrayList<Person>,
              private val conteext: Context) : RecyclerView.Adapter<PersonListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PersonListAdapter.MyViewHolder {
        val view = LayoutInflater.from(conteext).inflate(R.layout.list_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonListAdapter.MyViewHolder, position: Int) {
        holder.bindItems(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(person: Person) {
            var name: TextView = itemView.findViewById(R.id.nameTV) as TextView
            var age: TextView = itemView.findViewById(R.id.ageTV) as TextView

            name.text = person.name
            age.text = person.age.toString()
        }
    }

}