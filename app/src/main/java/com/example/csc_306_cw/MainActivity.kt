package com.example.csc_306_cw

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csc_306_cw.database.DBManager

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

    }

    fun launch2(view: View){
        val newIntent2 = Intent(this, LoginActivity::class.java)
        startActivity(newIntent2)
    }

    /**
    fun addDate(view: View): ArrayList<Artefact>{

        var list = populateList()

        val db = DBManager(this)

        for (i in 0 .. 6 ){
            var artifact = list.get(i)
            db.addArtefact(artifact.getName(), artifact.getImage(), artifact.getMeta(),
                artifact.paragraphsToJson(artifact.getArtefactParagraphs()), artifact.modalitiesToJson(artifact.getArtefactModalities()))
        }

        return list


    }
    **/

    private fun populateList(): ArrayList<Artefact>{
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