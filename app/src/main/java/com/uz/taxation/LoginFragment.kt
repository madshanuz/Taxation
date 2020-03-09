package com.uz.taxation

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginFragment : Fragment() {


    val TAG = "LoginFragment"

    //Connection to Firebase
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_login, container, false)
    }


    private fun validateForm(): Boolean {
        var valid = true

        val email = input_email.text.toString()
        if (TextUtils.isEmpty(email)) {
            input_email.error = "Required."
            valid = false
        } else {
            input_email.error = null
        }

        val password = input_pass.text.toString()
        if (TextUtils.isEmpty(password)) {
            input_pass.error = "Required."
            valid = false
        } else {
            input_pass.error = null
        }

        return valid
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //variables that connects to firebase
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()


        //If Login button is selected..
        //It will authenticate to Firebase either user exist or not.
        btn_login.setOnClickListener({

            val email = input_email.text.toString()
            val pass = input_pass.text.toString()
            signIn(email,pass)
        })

        //if user click register button
        txt_create.setOnClickListener({
            showRegister()
        })
    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }

        // [START sign_in_with_email]
        this.activity?.let {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(context, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }

                    // [END_EXCLUDE]
                }
        }
        // [END sign_in_with_email]
    }


    //function to show register layout
    fun showRegister(){
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        val fragment = RegisterFragment()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {

            val intent = Intent(activity, AdminActivity::class.java)
            startActivity(intent)

        } else {

        }
    }
}