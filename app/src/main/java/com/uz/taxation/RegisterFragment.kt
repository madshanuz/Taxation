package com.uz.taxation

import android.content.Context
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
import kotlinx.android.synthetic.main.activity_register.*

class RegisterFragment : Fragment() {

    val TAG = "RegisterFragment"

    //Connection to Firebase
    private lateinit var mDatabase: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.activity_register, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //variables that connects to firebase
        database = FirebaseDatabase.getInstance()
        mDatabase = database!!.reference!!.child("Users")
        auth = FirebaseAuth.getInstance()

        btn_register.setOnClickListener({

            val email = reg_Email.text.toString()
            val password = reg_Pass.text.toString()
            createAccount(email, password)
        })

        //If user click Login Button
        txt_login.setOnClickListener({

            //Will Show the login layout
            showLogin()
        })

    }

    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }

        // [START create_user_with_email]
        this.activity?.let {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val userId = auth!!.currentUser!!.uid
                        val currentUserDb = mDatabase!!.child(userId)
                        currentUserDb.child("firstName").setValue(reg_FIrstName.text.toString())
                        currentUserDb.child("lastName").setValue(reg_LastName.text.toString())
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(context, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }

                }
        }

        // [END create_user_with_email]
    }


    private fun validateForm(): Boolean {
        var valid = true

        val email = reg_Email.text.toString()
        if (TextUtils.isEmpty(email)) {
            reg_Email.error = "Required."
            valid = false
        } else {
            reg_Email.error = null
        }

        val password = reg_Pass.text.toString()
        if (TextUtils.isEmpty(password)) {
            reg_Pass.error = "Required."
            valid = false
        } else {
            reg_Pass.error = null
        }

        val confirmPassword = reg_ConfirmPass.text.toString()
        if (TextUtils.isEmpty(confirmPassword)) {
            reg_ConfirmPass.error = "Required."
            valid = false
        }else if(password != confirmPassword){
            reg_ConfirmPass.error = "Password not Match."
            valid = false
        } else {
            reg_ConfirmPass.error = null
        }

        val fName = reg_FIrstName.text.toString()
        if (TextUtils.isEmpty(fName)) {
            reg_FIrstName.error = "Required."
            valid = false
        } else {
            reg_FIrstName.error = null
        }

        val lName = reg_LastName.text.toString()
        if (TextUtils.isEmpty(lName)) {
            reg_LastName.error = "Required."
            valid = false
        } else {
            reg_LastName.error = null
        }

        return valid
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {

            val intent = Intent(activity, AdminActivity::class.java)
            startActivity(intent)

        } else {

        }
    }
    //function to show Login layout
    fun showLogin(){
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        val fragment = LoginFragment()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }

}