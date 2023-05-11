package com.example.csc_306_cw

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csc_306_cw.database.DBManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class RequestActivity : AppCompatActivity() {

    var atrefacts: ArrayList<Artefact> = ArrayList<Artefact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.artefact_second_menu)

        populateRecycleView()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation_bar)
        bottomNavigationView.selectedItemId = R.id.history
        navigationItemSelectedListener()
    }

    override fun onResume() {
        super.onResume()

        populateRecycleView()

    }

    override fun onRestart() {
        super.onRestart()
        populateRecycleView()
    }

    private fun populateRecycleView() {
        val db = DBManager(this)

        atrefacts = db.populateArtefactsList("new")
        val textRecyclerView = findViewById<View>(R.id.artefacts_secondly_menu) as RecyclerView
        val layoutManger = LinearLayoutManager(this)
        textRecyclerView.layoutManager = layoutManger

        val artefactMenuAdapter = RequestRowAdapter(atrefacts)
        textRecyclerView.adapter = artefactMenuAdapter

    }

    private fun navigationItemSelectedListener() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation_bar)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_page -> {
                    val artefactMenu = Intent(this, MainActivity::class.java)
                    startActivity(artefactMenu)
                    true
                }
                R.id.bookmark -> {
                    val bookmark = Intent(this, BookmarkActivity::class.java)
                    startActivity(bookmark)
                    true
                }
                R.id.history -> {
                    val requestsMenu = Intent(this, RequestActivity::class.java)
                    startActivity(requestsMenu)
                    true
                }
                R.id.settings -> {
                    val role = Intent(this, UsersRolesActivity::class.java)
                    startActivity(role)
                    true
                }
                else -> false
            }
        }
    }


}