package com.example.csc_306_cw

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csc_306_cw.database.DBManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class BookmarkActivity : AppCompatActivity(){

    private var auth =  Firebase.auth
    private var currentUser = auth.currentUser

    var atrefacts: ArrayList<Artefact> = ArrayList<Artefact>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.artefact_bookmark)

        val currentUserId = currentUser?.uid

        if (currentUserId != null) {
            Log.d("testing",currentUserId )
        }

        if (currentUserId != null) {
            val db = DBManager(this)
            atrefacts = db.getUserBookmark(currentUserId)
        }

        val textRecyclerView = findViewById<View>(R.id.artefacts_bookmark) as RecyclerView
        val layoutManger =  LinearLayoutManager(this)
        textRecyclerView.layoutManager = layoutManger

        val artefactMenuAdapter = ArtefactBookmarkRowAdapter(atrefacts)
        textRecyclerView.adapter= artefactMenuAdapter
        navigationItemSelectedListener()
    }

    private fun navigationItemSelectedListener(){
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation_bar)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_page -> {
                    val newIntent2 = Intent(this, MainActivity::class.java)
                    startActivity(newIntent2)
                    true
                }
                R.id.bookmark -> {
                    val newIntent2 = Intent(this, BookmarkActivity::class.java)
                    startActivity(newIntent2)
                    true
                }
                else -> false
            }
        }
    }
}