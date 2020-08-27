package whtsAppClone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import com.kush.learningkotlin.R
import kotlinx.android.synthetic.main.activity_status.*
import org.w3c.dom.Text

class StatusActivity : AppCompatActivity() {

    var mDatabase: DatabaseReference? = null
    var mCurrentUser: FirebaseUser? = null
    var mStorageReference:  StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)

        supportActionBar!!.title = "Status"

        if (intent.extras != null) {
            val oldStatus = intent.extras!!.get("Status")
            newStatusET.setText(oldStatus.toString())
        }

        if (intent.extras!!.equals(null)) {
            newStatusET.setText("Enter your new Status")
        }

        updateStatus.setOnClickListener{

            mCurrentUser = FirebaseAuth.getInstance().currentUser
            val userID = mCurrentUser!!.uid

            mDatabase = FirebaseDatabase.getInstance().reference
                .child("Users")
                .child(userID)

            val status = newStatusET.text.toString().trim()

            mDatabase!!.child("Status")
                .setValue(status).addOnCompleteListener{
                    task: Task<Void> ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Status Updated", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, SettingsActivity::class.java))
                    } else {
                        Toast.makeText(this, "Status Not Updated", Toast.LENGTH_LONG).show()
                    }
                }
        }

    }
}