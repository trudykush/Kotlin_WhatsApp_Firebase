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
import com.kush.learningkotlin.R
import kotlinx.android.synthetic.main.activity_create_account.*

class Chat_Login : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat__login)

        mAuth = FirebaseAuth.getInstance()

    }

    fun userLogin(view: View) {
        var email = userEmailRegister.text.toString().trim()
        var password = userPasswordRegister.text.toString().trim()

        if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {
            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, passowrd: String) {
        mAuth!!.signInWithEmailAndPassword(email, passowrd)
            .addOnCompleteListener{
                task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    var userName = email.split("@")[0]
                    Toast.makeText(this, "User Created", Toast.LENGTH_LONG).show()
                    var dashBoardIntent = Intent(this, Dashboard::class.java)
                    dashBoardIntent.putExtra("DisplayName", userName)
                    startActivity(dashBoardIntent)
                    finish()

                } else {
                    Toast.makeText(this, "Login Error" + task.exception, Toast.LENGTH_LONG).show()
                }
            }
    }
}