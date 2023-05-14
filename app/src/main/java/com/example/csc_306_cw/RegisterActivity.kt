package com.example.csc_306_cw

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.csc_306_cw.database.DBManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity: AppCompatActivity()  {

    private var auth =  Firebase.auth
    private var currentUser = auth.currentUser

    lateinit var emailText : EditText
    lateinit var passwordText : EditText
    lateinit var passwordVerifyText : EditText
    lateinit var regiterbutton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)


        emailText = findViewById<EditText>(R.id.email_text_field)
        passwordText = findViewById<EditText>(R.id.password_text_field)
        passwordVerifyText = findViewById<EditText>(R.id.password_verify_text_field)
        regiterbutton = findViewById<Button>(R.id.register_button)
    }

    override fun onStart(){
        super.onStart()
    }

    fun update(){

        currentUser = auth.currentUser
        val currentUserEmail = currentUser?.email
        val currentUserId = currentUser?.uid

        if (currentUserEmail == null){
            // in case they not logged in
        } else {
            val db = DBManager(this)
            if (db.populateUsersList().size == 0) {
                db.registerRole(currentUserId!!, "true")
            } else {
                db.registerRole(currentUserId!!, "false")
            }
            val newIntent = Intent(this, MainActivity::class.java)
            startActivity(newIntent)
        }
    }

    fun registerClick(view: View) {
        currentUser = auth.currentUser
        if (currentUser != null){
            displayMassage(view, getString(R.string.register_while_logged_in))
        } else {
            if (passwordText.text.toString().equals(passwordVerifyText.text.toString())){
                auth.createUserWithEmailAndPassword(
                emailText.text.toString(),
                passwordText.text.toString()
                ).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        closeKeyBoard()
                        update()
                    } else {
                        closeKeyBoard()
                        displayMassage(
                            regiterbutton, task.exception?.message.toString()
                        )
                    }
                }
            } else {
                displayMassage(
                    regiterbutton, R.string.password_not_matching.toString()
                )
            }

        }

    }

    private fun displayMassage(view: View, msgTxt: String) {
        val sb = Snackbar.make(view, msgTxt, Snackbar.LENGTH_SHORT)
        sb.show()


    }

    fun goLogin (view: View) {
        val newIntent = Intent(this, LoginActivity::class.java)
        startActivity(newIntent)
    }

    private fun closeKeyBoard() {

        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}