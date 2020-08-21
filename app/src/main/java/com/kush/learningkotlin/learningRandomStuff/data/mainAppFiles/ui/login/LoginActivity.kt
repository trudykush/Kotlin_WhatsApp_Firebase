package com.kush.learningkotlin.learningRandomStuff.data.mainAppFiles.ui.login

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kush.learningkotlin.R
import com.kush.learningkotlin.learningRandomStuff.data.mainAppFiles.MainKotlinActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    private var mAuth: FirebaseAuth? = null
    private var currentUser: FirebaseUser? = null

    override fun onStart() {
        super.onStart()

        currentUser = mAuth?.currentUser
        if(currentUser != null) {
            Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show()
        } else
            Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
        val loading = findViewById<ProgressBar>(R.id.loading)

        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                //loginViewModel.login(username.text.toString(), password.text.toString())
                mAuth!!.signInWithEmailAndPassword(username.text.toString(), password.text.toString())
                    .addOnCompleteListener{
                            task: Task<AuthResult> ->
                        if (task.isSuccessful) {
                            loading.visibility = View.INVISIBLE
                            startActivity(Intent(this@LoginActivity, MainKotlinActivity::class.java))
                        } else {
                            loading.visibility = View.INVISIBLE

                            var alertBuilder =  AlertDialog.Builder(this@LoginActivity)
                            alertBuilder.setTitle("No Registered")
                            alertBuilder.setMessage("Do you wish to register yourself?")
                            alertBuilder.setPositiveButton("Yes"){
                                    _, _ ->
                                mAuth!!.createUserWithEmailAndPassword(username.text.toString(), password.text.toString())
                                    .addOnCompleteListener{
                                        Toast.makeText(this@LoginActivity, "Registered", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this@LoginActivity, MainKotlinActivity::class.java))
                                    }
                                    .addOnFailureListener{
                                        Toast.makeText(this@LoginActivity, "Registration Failed", Toast.LENGTH_SHORT).show()
                                    }
                            }

                            val alertDialog: AlertDialog = alertBuilder.create()
                            alertDialog.show()


                            Toast.makeText(this@LoginActivity,"Invalid", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}