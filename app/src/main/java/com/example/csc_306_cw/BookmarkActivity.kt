package com.example.csc_306_cw

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csc_306_cw.database.DBManager
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

    }
}