package com.example.csc_306_cw

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity: AppCompatActivity() {

    private var auth =  Firebase.auth
    private var currentUser = auth.currentUser


    lateinit var emailText : EditText
    lateinit var passwordText : EditText
    lateinit var loginbutton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)


        emailText = findViewById<EditText>(R.id.email_text_field)
        passwordText = findViewById<EditText>(R.id.password_text_field)
        loginbutton = findViewById<Button>(R.id.login_button)

    }

    override fun onStart(){
        super.onStart()
    }
    fun update(){

        currentUser = auth.currentUser
        val currentUserEmail = currentUser?.email

        if (currentUserEmail == null){
            // in case they not logged in
        } else {
            redirectToArtefactMenu()
        }
    }

    fun loginClick (view: View) {

        auth.signInWithEmailAndPassword(
            emailText.text.toString(),
            passwordText.text.toString()
        ).addOnCompleteListener(this){
            task ->
            if (task.isSuccessful){
                closeKeyBoard()
                update()
            } else {
                closeKeyBoard()
                displayMassage(
                    loginbutton, task.exception?.message.toString()
                )
            }
        }
    }

    fun logoutClick (view: View){
        auth.signOut()
        update()
    }

    fun loginAsGuest (view: View){
        redirectToArtefactMenu()
    }

    private fun redirectToArtefactMenu () {
        val newIntent = Intent(this, MainActivity::class.java)
        startActivity(newIntent)
    }

    fun goRegister (view: View) {
        val newIntent = Intent(this, RegisterActivity::class.java)
        startActivity(newIntent)
    }

    private fun displayMassage(view: View, msgTxt: String) {
        val sb = Snackbar.make(view, msgTxt, Snackbar.LENGTH_SHORT)
        sb.show()


    }

    private fun closeKeyBoard() {

        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}