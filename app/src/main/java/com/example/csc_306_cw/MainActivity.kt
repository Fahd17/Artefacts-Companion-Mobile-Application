package com.example.csc_306_cw

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csc_306_cw.database.DBManager
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity(){

    var atrefacts: ArrayList<Artefact> = ArrayList<Artefact>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.artefact_menu)

        val db = DBManager(this)

        atrefacts = db.populateArtefactsList()

        val textRecyclerView = findViewById<View>(R.id.artefacts_menu) as RecyclerView
        val layoutManger =  LinearLayoutManager(this)
        textRecyclerView.layoutManager = layoutManger

        val artefactMenuAdapter = ArtefactRowAdapter(atrefacts)
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
    fun launch2(view: View){
        val newIntent2 = Intent(this, NewArtefactFormAdapter::class.java)
        startActivity(newIntent2)
    }


}