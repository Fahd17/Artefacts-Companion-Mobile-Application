package com.example.csc_306_cw

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csc_306_cw.database.DBManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class MainActivity : AppCompatActivity() {

    private var atrefacts: ArrayList<Artefact> = ArrayList<Artefact>()
    private var auth = Firebase.auth
    private var currentUser = auth.currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.artefact_menu)

        var toolbar = findViewById<Toolbar>(R.id.artefact_menu_toolbar)
        setSupportActionBar(toolbar)

        populateRecycleView()

        navigationItemSelectedListener()
    }

    override fun onPause() {
        super.onPause()
        populateRecycleView()
        toolbarOptions()

    }

    override fun onRestart() {
        super.onRestart()
        populateRecycleView()
        toolbarOptions()
    }

    private fun displayMassage(view: View, mess: String) {
        val sb = Snackbar.make(view, mess, Snackbar.LENGTH_SHORT)
        sb.anchorView = view
        sb.show()
    }

    private fun isUserLoggedIn(): Boolean {
        currentUser = auth.currentUser
        val currentUserEmail = currentUser?.email

        return currentUserEmail != null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar_menu, menu)
        toolbarOptions()
        return super.onCreateOptionsMenu(menu)
    }

    private fun toolbarOptions() {
        var toolbar = findViewById<Toolbar>(R.id.artefact_menu_toolbar)
        val loginMenuItem: MenuItem = toolbar.menu.findItem(R.id.login_button)
        val logoutMenuItem: MenuItem = toolbar.menu.findItem(R.id.logout_button)

        if (isUserLoggedIn()) {
            loginMenuItem.setVisible(false)
            logoutMenuItem.setVisible(true)
        } else {
            loginMenuItem.setVisible(true)
            logoutMenuItem.setVisible(false)
        }
    }

    private fun populateRecycleView() {
        val db = DBManager(this)

        atrefacts = db.populateArtefactsList("ready")
        val textRecyclerView = findViewById<View>(R.id.artefacts_menu) as RecyclerView
        val layoutManger = LinearLayoutManager(this)
        textRecyclerView.layoutManager = layoutManger

        val artefactMenuAdapter = ArtefactRowAdapter(atrefacts)
        textRecyclerView.adapter = artefactMenuAdapter

    }

    fun readQRCode(view: View) {

        var options = ScanOptions()
        options.setCaptureActivity(ScanActivity::class.java)
        launcher.launch(options)
    }

    val launcher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            var id = result.contents
            val db = DBManager(this)
            if (db.isArtefactPresent(id)) {
                val artefactPage = Intent(this, ArtefactPageActivity::class.java)
                artefactPage.putExtra("id", id.toInt())
                startActivity(artefactPage)
            } else {
                val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation_bar)
                displayMassage(bottomNavigationView, getString(R.string.invalid_QR))
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var toolbar = findViewById<Toolbar>(R.id.artefact_menu_toolbar)
        when (item.itemId) {
            R.id.create_artefact -> {
                if (isUserLoggedIn()) {
                    val newArtefact = Intent(this, NewArtefactFormActivity::class.java)
                    startActivity(newArtefact)
                } else {

                    val bottomNavigationView =
                        findViewById<BottomNavigationView>(R.id.navigation_bar)
                    displayMassage(bottomNavigationView, getString(R.string.login_first))
                }
                true
            }
            R.id.login_button -> {
                val login = Intent(this, LoginActivity::class.java)
                startActivity(login)
                true
            }
            R.id.logout_button -> {
                auth.signOut()
                toolbarOptions()
                true
            }
            else -> false
        }
        return super.onOptionsItemSelected(item)
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