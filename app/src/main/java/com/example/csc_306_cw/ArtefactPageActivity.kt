package com.example.csc_306_cw

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csc_306_cw.database.DBManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ArtefactPageActivity : FragmentActivity(), OnMapReadyCallback {

    lateinit var map: GoogleMap
    lateinit var atrefact: Artefact


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.artefact_page)

        var mapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        val db = DBManager(this)
        val extras = intent.extras
        atrefact = extras?.let { db.findArtefact(it.getInt("id")) }!!


        // setting the name
        var name = findViewById<View>(R.id.Title) as TextView
        name.text = atrefact.getName()


        // setting the MainImage
        var mainImage = findViewById<View>(R.id.main_image) as ImageView
        val imageBitmap = BitmapFactory.decodeByteArray(atrefact.getImage(), 0, atrefact.getImage()!!.size)
        mainImage.setImageBitmap(imageBitmap)

        //Setting the text recycler View
        val textRecyclerView = findViewById<View>(R.id.text_holder) as RecyclerView
        val layoutManger1 = LinearLayoutManager(this)
        textRecyclerView.layoutManager = layoutManger1

        val textSectionArtefactTextAdapter = ArtefactTextAdapter(atrefact.getArtefactParagraphs())
        textRecyclerView.adapter = textSectionArtefactTextAdapter

        //Setting the modalities recycler View
        val modalitiesRecyclerView = findViewById<View>(R.id.modalities_holder) as RecyclerView
        val layoutManger = LinearLayoutManager(this)
        modalitiesRecyclerView.layoutManager = layoutManger

        val modalitiesSectionArtefactModalitiesAdapter =
            ArtefactModalitiesAdapter(atrefact.getArtefactModalities())
        modalitiesRecyclerView.adapter = modalitiesSectionArtefactModalitiesAdapter

    }

    fun update(view: View){
        val newIntent = Intent(this, UpdateArtefactFormActivity::class.java)
        newIntent.putExtra("id", atrefact.getId())
        startActivity(newIntent)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        map = googleMap
        var campus: LatLng = LatLng(51.619296, -3.878914)
        map.addMarker(MarkerOptions().position(campus).title("Computational Foundry"))
        map.moveCamera(CameraUpdateFactory.newLatLng(campus))
    }
}