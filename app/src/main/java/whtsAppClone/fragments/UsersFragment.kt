package whtsAppClone.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.kush.learningkotlin.R
import com.kush.learningkotlin.learningRandomStuff.data.PersonListAdapter
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_list_view.*
import kotlinx.android.synthetic.main.fragment_users.*
import kotlinx.android.synthetic.main.users_row.view.*
import whtsAppClone.models.Users


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UsersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UsersFragment : Fragment() {

    var fragmentView : View? = null

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var mUserDatabase: DatabaseReference? = null
    var firedatabase : FirebaseDatabase? = null
    var userList : ArrayList<Users> ? = null
    var ref : DatabaseReference? = null

    var mRecyclerView : RecyclerView? =null
    var layoutManager: RecyclerView.LayoutManager? = null

    var adapter: FirebaseRecyclerAdapter<*, *>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentView = LayoutInflater.from(activity).inflate(
            R.layout.fragment_users,
            container,
            false
        )

//        mUserDatabase = FirebaseDatabase.getInstance().reference.child("Users")
        var query: Query = FirebaseDatabase.getInstance().reference.child("Users")
        Log.d("TAG", "onCreateView: $query")

        var options: FirebaseRecyclerOptions<Users?> = FirebaseRecyclerOptions.Builder<Users>()
            .setQuery(query, Users::class.java).build()

        adapter = object : FirebaseRecyclerAdapter<Users?, MyViewHolder?>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                    // Create a new instance of the ViewHolder, in this case we are using a custom
                    // layout called R.layout.message for each item
                    val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.users_row, parent, false)
                    return MyViewHolder(view)
                }
                override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Users) {
                    holder.bindView(context, model)
                }

            override fun onDataChanged() {
                super.onDataChanged()
            }

            override fun onError(error: DatabaseError) {
                super.onError(error)
            }

        }
        return fragmentView
    }



    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var linearLayoutManager = LinearLayoutManager(this.context)
        friendsRecyclerViewId.layoutManager = linearLayoutManager
        friendsRecyclerViewId.adapter = adapter
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //        fun bindView(databaseReference: DatabaseReference?, context: Context?, model: Users) {
        fun bindView(context: Context?, model: Users?) {
            var userProfilePic: CircleImageView = itemView.usersProfile
            var userName: TextView = itemView.userName
            var userStatus: TextView = itemView.userStatus

            if (model != null) {
                userName.text = model.DisplayName
                userStatus.text = model.Status

                Picasso.get()
                    .load(model.image)
                    .placeholder(R.drawable.profile_img)
                    .into(userProfilePic)
            }

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UsersFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UsersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}