package whtsAppClone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kush.learningkotlin.R
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccount : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        mAuth = FirebaseAuth.getInstance()

    }

    private fun createAccount(email: String, password: String, displayName: String) {
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    var currentUserID = mAuth!!.currentUser
                    var userId = currentUserID!!.uid

                    var userDetails = HashMap<String, String>()
                    userDetails["DisplayName"] = displayName
                    userDetails["Status"] = "Hello from Kush"
                    userDetails["image"] = "default"
                    userDetails["thumb_image"] = "default"

                    mDatabase = FirebaseDatabase.getInstance().reference
                        .child("Users").child(userId)

                    mDatabase!!.setValue(userDetails).addOnCompleteListener{
                        task: Task<Void> ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "User Created", Toast.LENGTH_LONG).show()
                            var dashBoardIntent = Intent(this, Dashboard::class.java)
                            dashBoardIntent.putExtra("DisplayName", displayName)
                            startActivity(dashBoardIntent)
                            finish()

                        } else {
                            Toast.makeText(this, "User Not Created", Toast.LENGTH_LONG).show()
                        }
                    }

                } else {
                    Toast.makeText(this, "Error " + task.exception, Toast.LENGTH_LONG).show()
                }
            }
    }

    fun registerUser(view: View) {
        var email = userEmailRegister.text.toString().trim()
        var password = userPasswordRegister.text.toString().trim()
        var displayName = userDisplayNameRegister.text.toString().trim()

        if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)
            || !TextUtils.isEmpty(displayName)) {
            createAccount(email, password, displayName)
        } else {
            Toast.makeText(this, "Fields can't be empty", Toast.LENGTH_LONG).show()
        }
    }
}