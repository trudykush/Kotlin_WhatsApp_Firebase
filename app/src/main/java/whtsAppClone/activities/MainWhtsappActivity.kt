package whtsAppClone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kush.learningkotlin.R

class MainWhtsappActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var user: FirebaseUser? = null
    var mAuthListener: FirebaseAuth.AuthStateListener? =  null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_whtsapp)

        mAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener {
            firebaseAuth: FirebaseAuth ->
                user = firebaseAuth.currentUser

            if (user != null) {
                startActivity(Intent(this, Dashboard::class.java))
                finish()
            } else {
                Toast.makeText(this, "Not Signed In", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun goToLoginPage(view: View) {
        startActivity(Intent(this, Chat_Login::class.java))
    }
    fun goToRegisterPage(view: View) {
        startActivity(Intent(this, CreateAccount::class.java))
    }

    override fun onStart() {
        super.onStart()
        mAuthListener?.let { mAuth!!.addAuthStateListener(it) }
    }

    override fun onStop() {
        super.onStop()

        if (mAuthListener != null) {
            mAuth?.removeAuthStateListener(mAuthListener!!)
        }
    }
}