package com.uz.taxation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val manager = supportFragmentManager

    val TAG = "MainActivity"

    //Connection to Firebase
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //variables that connects to firebase
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()


        //Will Show the login layout
        signOut()
    }

    //function to show Login layout
    fun showLogin(){
        val transaction = manager.beginTransaction()
        val fragment = LoginFragment()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }


    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {

            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)

        } else {
            showLogin()
        }
    }

    private fun signOut() {
        auth.signOut()
        updateUI(null)
    }

}
