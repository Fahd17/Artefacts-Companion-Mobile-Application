package com.example.csc_306_cw

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ArtefactPageActivity: FragmentActivity(), OnMapReadyCallback {

    lateinit var map: GoogleMap
    lateinit var atrefacts: ArrayList<Artefact>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.artefact_page)

    var mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    mapFragment.getMapAsync(this)

    atrefacts = popilateList()
    var atrefact = atrefacts.get(1)


    // setting the name
    var name = findViewById<View>(R.id.Title) as TextView
    name.text = atrefact.getName()

    // setting the MainImage
    var mainImage = findViewById<View>(R.id.main_image) as ImageView
    mainImage.setImageResource(atrefact.getImage())

    //Setting the text recycler View
    val textRecyclerView = findViewById<View>(R.id.text_holder) as RecyclerView
    val layoutManger1 =  LinearLayoutManager(this)
    textRecyclerView.layoutManager = layoutManger1

    val textSectionArtefactTextAdapter = ArtefactTextAdapter(atrefact.getArtefactParagraphs())
    textRecyclerView.adapter= textSectionArtefactTextAdapter

    //Setting the modalities recycler View
    val modalitiesRecyclerView = findViewById<View>(R.id.modalities_holder) as RecyclerView
    val layoutManger =  LinearLayoutManager(this)
    modalitiesRecyclerView.layoutManager = layoutManger

    val modalitiesSectionArtefactModalitiesAdapter = ArtefactModalitiesAdapter(atrefact.getArtefactModalities())
    modalitiesRecyclerView.adapter= modalitiesSectionArtefactModalitiesAdapter

}

override fun onMapReady(googleMap: GoogleMap) {

    map = googleMap
    var campus: LatLng = LatLng(51.619296, -3.878914)
    map.addMarker(MarkerOptions().position(campus).title("Computational Foundry"))
    map.moveCamera(CameraUpdateFactory.newLatLng(campus))
}
    private fun popilateList(): ArrayList<Artefact>{
        var list = ArrayList<Artefact>()

        val mainImages = arrayOf(
            R.drawable.atrefact1, R.drawable.atrefact2,R.drawable.atrefact3,R.drawable.atrefact4,
            R.drawable.atrefact5,R.drawable.atrefact6, R.drawable.atrefact7
        )

        val names = arrayOf(
            "Macintosh Plus", "NeXT Computer", "iMac G3", "apple", "StyleWriter", "PET", "VIC20"
        )

        for (i in 0 .. 6 ){
            val artefact = Artefact()
            artefact.setName(names[i])
            artefact.setImage(mainImages[i])
            artefact.setArtefactParagraphs(popilateParagraphs())
            artefact.setArtefactModalities(popilateModalities())
            artefact.setMeta("Produced: 1992, Author: Fahd")
            list.add(artefact)
        }

        return list


    }
    private fun popilateParagraphs(): HashMap<String, String>{

        var paragraphs : HashMap<String, String> = HashMap<String, String> ()

        paragraphs.put("Context", "NeXT, Inc. (later NeXT Computer, Inc. and NeXT Software, Inc.) was an American technology company that specialized in computer workstations intended for higher education and business use. Based in Redwood City, California, " +
                "and founded by Apple Computer co-founder and CEO Steve Jobs after he was forced out of Apple, the company introduced their first product, the NeXT Computer, in 1988, and then the smaller NeXTcube and NeXTstation in 1990.")
        paragraphs.put("History", "In 1985, Apple co-founder and CEO Steve Jobs led a division campaign called SuperMicro, which was responsible for developing the Macintosh and Lisa computers. They were commercial successes on university campuses because Jobs " +
                "had personally visited a few notable universities to promote his products, and also because of Apple University Consortium, a marketing program that allowed academics to buy them at a discount")
        paragraphs.put("Development", "Each artifact will have a page and as done in Wikipedia (Figure 1) a section of the artifact page will be dedicated for non-text content laid one by one vertically and the remaining part of the page will be used to display the text [].")

        paragraphs.put("Usage", "In 1985, Apple co-founder and CEO Steve Jobs led a division campaign called SuperMicro, which was responsible for developing the Macintosh and Lisa computers. They were commercial successes on university campuses because Jobs " +
                "had personally visited a few notable universities to promote his products, and also because of Apple University Consortium, a marketing program that allowed academics to buy them at a discount")

        return paragraphs

    }

    private fun popilateModalities(): ArrayList<Int>{

        var modalities : ArrayList<Int> = ArrayList<Int>()

        modalities.add(R.drawable.next1)
        modalities.add(R.drawable.next2)
        modalities.add(R.drawable.next4)
        modalities.add(R.drawable.next5)
        return modalities

    }
}