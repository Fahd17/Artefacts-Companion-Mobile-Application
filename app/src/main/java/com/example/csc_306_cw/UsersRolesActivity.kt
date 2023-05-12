package com.example.csc_306_cw

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csc_306_cw.database.DBManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UsersRolesActivity : AppCompatActivity(){

    var users: ArrayList<String> = ArrayList<String>()
    private var auth =  Firebase.auth
    private var currentUser = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.artefact_second_menu)

        populateRecycleView()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation_bar)
        bottomNavigationView.selectedItemId= R.id.settings
        navigationItemSelectedListener()

    }

    private fun populateRecycleView(){
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation_bar)
        if (isUserLoggedIn()) {
            val db = DBManager(this)

            if (db.isAdmin(currentUser!!.uid)) {
                users = db.populateUsersList()
                val textRecyclerView =
                    findViewById<View>(R.id.artefacts_secondly_menu) as RecyclerView
                val layoutManger = LinearLayoutManager(this)
                textRecyclerView.layoutManager = layoutManger

                val artefactMenuAdapter = UserRowAdapter(users)
                textRecyclerView.adapter = artefactMenuAdapter
            }else {
                displayMassage(bottomNavigationView, getString(R.string.admin_role_required))
            }
        }else{

            displayMassage(bottomNavigationView, getString(R.string.login_first))
        }

    }

    private fun displayMassage(view: View, mess: String) {
        val sb = Snackbar.make(view, mess, Snackbar.LENGTH_SHORT)
        sb.anchorView = view
        sb.show()
    }

    private fun isUserLoggedIn():Boolean {
        currentUser = auth.currentUser
        val currentUserEmail = currentUser?.email

        return currentUserEmail != null
    }

    private fun navigationItemSelectedListener(){
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
                } R.id.settings -> {
                val role = Intent(this, UsersRolesActivity::class.java)
                startActivity(role)
                true
            }
                else -> false
            }
        }
    }
}