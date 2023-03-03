package com.example.csc_306_cw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //find text box
        val mainText = findViewById<TextView>(R.id.MainText)

        //update the text
        mainText.text = "heyyyyy"
    }

    fun updateText(view: View) {

        //find text box
        val mainText = findViewById<TextView>(R.id.MainText)

        //update the text
        mainText.text = "heyyyyy2"
    }

    fun updateFromKeyboard (view: View) {

        // acquire the keyboard input
        val kbInput = findViewById<EditText>(R.id.editTextTextPersonName)
        val kbText = kbInput.text

        //update text
        val mainText = findViewById<TextView>(R.id.MainText)
        mainText.text = kbText

    }
}