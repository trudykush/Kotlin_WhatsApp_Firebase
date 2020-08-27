package whtsAppClone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.kush.learningkotlin.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.fragment_users.*
import kotlinx.android.synthetic.main.item_message.*
import whtsAppClone.models.FriendlyMessage

class ChatActivity : AppCompatActivity() {

    var userId: String? = null
    var mFirebaseDatabase: DatabaseReference? = null
    var mFirebaseUser: FirebaseUser? = null

    var mLinearLayoutManager: LinearLayoutManager? = null
    var mFirebaseAdapter: FirebaseRecyclerAdapter<FriendlyMessage, MyMessageViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mFirebaseUser = FirebaseAuth.getInstance().currentUser

        userId = intent.extras!!.get("UserID").toString()
        mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager!!.stackFromEnd = true

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mFirebaseDatabase = FirebaseDatabase.getInstance().reference.child("messages")

        var query: Query = FirebaseDatabase.getInstance().reference.child("messages")

        var options: FirebaseRecyclerOptions<FriendlyMessage?> = FirebaseRecyclerOptions.Builder<FriendlyMessage>()
            .setQuery(query, FriendlyMessage::class.java).build()

        mFirebaseAdapter = object : FirebaseRecyclerAdapter<FriendlyMessage, MyMessageViewHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyMessageViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message, parent, false)
                return MyMessageViewHolder(view)
            }

            override fun onBindViewHolder(
                holder: MyMessageViewHolder,
                position: Int,
                model: FriendlyMessage
            ) {
                var x = model.text
                if (model.text != null) {
                    holder.bindView(model)

                    var currentUserID = mFirebaseUser!!.uid

                    var isMe: Boolean = model.id.equals(currentUserID)

                    if (isMe) {
                        // Me to the right side
                        holder.profileImageViewRight!!.visibility = View.VISIBLE
                        holder.profileImageView!!.visibility = View.GONE
                        holder.messageTextView!!.gravity = (Gravity.CENTER_VERTICAL or Gravity.RIGHT)
                        holder.messengerTV!!.gravity = (Gravity.CENTER_VERTICAL or Gravity.RIGHT)

                        // Get imageUrl for me
                        mFirebaseDatabase!!.child("Users")
                            .child(currentUserID)
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    var imageUrl: String? = snapshot.child("image").value.toString()
                                    var displayName = snapshot.child("DisplayName").value

                                    holder.messengerTV!!.text = displayName.toString()

                                    Picasso.get().load(imageUrl).placeholder(R.drawable.profile_img).into(holder.profileImageViewRight)
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }
                            })
                    } else {
                        // the other person show image view to the left side
                        holder.profileImageViewRight!!.visibility = View.GONE
                        holder.profileImageView!!.visibility = View.VISIBLE
                        holder.messageTextView!!.gravity = (Gravity.CENTER_VERTICAL or Gravity.LEFT)
                        holder.messengerTV!!.gravity = (Gravity.CENTER_VERTICAL or Gravity.LEFT)

                        // Get imageUrl for me
                        mFirebaseDatabase!!.child("Users")
                            .child(userId!!)
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    var imageUrl: String? = snapshot.child("thumb_image").value.toString()
                                    var displayName = snapshot.child("DisplayName").value

                                    holder.messengerTV!!.text = displayName.toString()

                                    Picasso.get().load(imageUrl).placeholder(R.drawable.profile_img).into(holder.profileImageViewRight)
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }
                            })
                    }
                }
            }

        }

        // Set the RecyclerView
        recyclerViewChat.layoutManager = mLinearLayoutManager
        recyclerViewChat.adapter = mFirebaseAdapter

        sendMsgBtn.setOnClickListener{
            if (intent.extras!!.get("UserName").toString() != "") {
                var currentName = intent.extras!!.get("UserName").toString()
                var mCurrentUserID = mFirebaseUser!!.uid

                var friendlyMessage = FriendlyMessage(mCurrentUserID,
                        messageET.text.toString().trim(),
                        currentName.trim())

                mFirebaseDatabase!!.child("messages")
                    .push().setValue(friendlyMessage)

                messageET.setText("")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mFirebaseAdapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        mFirebaseAdapter!!.stopListening()
    }

    class MyMessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var messageTextView: TextView? = null
        var messengerTV: TextView? = null
        var profileImageView: CircleImageView? = null
        var profileImageViewRight: CircleImageView? = null


        fun bindView(friendlyMessage: FriendlyMessage) {
            messageTextView = itemView.findViewById(R.id.msgTV)
            messengerTV = itemView.findViewById(R.id.messengerTV)

            profileImageView = itemView.findViewById(R.id.msgIV)
            profileImageViewRight = itemView.findViewById(R.id.messengerIVRight)

            messageTextView!!.text = friendlyMessage.name
            messengerTV!!.text = friendlyMessage.text
        }
    }

}