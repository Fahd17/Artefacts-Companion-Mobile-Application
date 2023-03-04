package com.example.csc_306_cw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : FragmentActivity(), OnMapReadyCallback{

     lateinit var map: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.artefactsmenu)

        var mapFragment: SupportMapFragment  = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        var atrefacts = popilateList()
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
            list.add(artefact)
        }

        return list


    }
    private fun popilateParagraphs(): HashMap<String, String>{

        var paragraphs : HashMap<String, String> = HashMap<String, String> ()

        paragraphs.put("Fahd", "Each artifact will have a page and as done in Wikipedia (Figure 1) a section of the artifact page will be dedicated for non-text content laid one by one vertically and the remaining part of the page will be used to display the text [].")

        paragraphs.put("Fahd1", "Each artifact will have a page and as done in Wikipedia (Figure 1) a section of the artifact page will be dedicated for non-text content laid one by one vertically and the remaining part of the page will be used to display the text [].")

        paragraphs.put("Fahd2", "Each artifact will have a page and as done in Wikipedia (Figure 1) a section of the artifact page will be dedicated for non-text content laid one by one vertically and the remaining part of the page will be used to display the text [].")

        paragraphs.put("Fahd3", "Each artifact will have a page and as done in Wikipedia (Figure 1) a section of the artifact page will be dedicated for non-text content laid one by one vertically and the remaining part of the page will be used to display the text [].")
        paragraphs.put("Fahd4", "Each artifact will have a page and as done in Wikipedia (Figure 1) a section of the artifact page will be dedicated for non-text content laid one by one vertically and the remaining part of the page will be used to display the text [].")

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


    override fun onMapReady(googleMap: GoogleMap) {

        map = googleMap
        var campus: LatLng = LatLng(51.619296, -3.878914)
        map.addMarker(MarkerOptions().position(campus).title("Computational Foundry"))
        map.moveCamera(CameraUpdateFactory.newLatLng(campus))
    }
}