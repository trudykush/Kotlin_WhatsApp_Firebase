package whtsAppClone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.kush.learningkotlin.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile_activities.*
import kotlinx.android.synthetic.main.users_row.*

class ProfileActivities : AppCompatActivity() {

    var mCurrentUser: FirebaseUser? = null
    var mUsersDatabase: DatabaseReference? = null
    var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_activities)

        supportActionBar!!.title = "Profile"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (intent.extras != null) {
            userId = intent.extras!!.get("UserID").toString()

            mCurrentUser = FirebaseAuth.getInstance().currentUser
            mUsersDatabase = FirebaseDatabase.getInstance().reference.child("Users")
                .child(userId!!)

            setUpProfile()
        }
    }

    private fun setUpProfile() {

        mUsersDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var displayName = snapshot.child("DisplayName").value.toString()
                var status = snapshot.child("Status").value.toString()
                var image: String = snapshot.child("image").value as String

                userProfileName.text = displayName
                userProfileStatus.text = status

                Picasso.get().load(image).placeholder(R.drawable.profile_img)
                    .into(userProfilePicture)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}