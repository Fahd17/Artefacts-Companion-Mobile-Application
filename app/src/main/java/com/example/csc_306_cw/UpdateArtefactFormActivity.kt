package com.example.csc_306_cw

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.csc_306_cw.database.DBManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UpdateArtefactFormActivity: AppCompatActivity(), OnMapReadyCallback {
    private lateinit var nameText: EditText
    private lateinit var authorText: EditText
    private lateinit var yearText: EditText
    private lateinit var paragraphsContainer: LinearLayout
    private lateinit var modalitiesContainer: LinearLayout
    private lateinit var addParagraphButton: Button
    private lateinit var addModalitiesButton: Button
    private lateinit var deleteButton: Button
    private  var image: ByteArray?  = null
    var modalities : ArrayList<Int> = ArrayList<Int>()
    private val PICK_IMAGE_REQUEST = 1
    var mainImageCheck: Boolean = false
    lateinit var targetArtefact: Artefact
    private lateinit var map: GoogleMap
    private var auth =  Firebase.auth
    private var currentUser = auth.currentUser

    //setting the default location to CoFo
    private var artefactLocation: LatLng = LatLng(51.619296, -3.878914)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_artefact_form)

        nameText = findViewById<EditText>(R.id.name_text_field)
        paragraphsContainer = findViewById(R.id.paragraphs_container)
        addParagraphButton = findViewById(R.id.add_paragraph_button)
        modalitiesContainer = findViewById(R.id.modalities_container)

        addParagraphButton.setOnClickListener {
            addParagraphView()
        }

        val db = DBManager(this)
        val extras = intent.extras
        targetArtefact = extras?.let { db.findArtefact(it.getInt("id")) }!!

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fillArtefactData()
        populateModalities(modalities)


        deleteButton = findViewById<Button>(R.id.delete_artefact_button)
        if (!db.isAdmin(currentUser!!.uid)) {
            deleteButton.isVisible = false

        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
       addMarkerToMap(targetArtefact.getLocation()!!)
        map.setOnMapClickListener { point ->
            map.clear()
            addMarkerToMap(point)
            artefactLocation = point
        }
    }

    private fun addMarkerToMap (point: LatLng){
        val marker = MarkerOptions()
            .position(point)
            .title("artefact location")
        map.addMarker(marker)
    }

    private fun fillArtefactData(){

        nameText.setText(targetArtefact.getName())
        image = targetArtefact.getImage()
        modalities = targetArtefact.getArtefactModalities()

        val paragraphs = targetArtefact.getArtefactParagraphs()
        addParagraphView(paragraphs)

        artefactLocation = targetArtefact.getLocation()!!
    }

    fun mainImagePicker(view: View) {
        mainImageCheck = true
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    fun modalityImagePicker(view: View) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    private fun addModality( bytes: ByteArray?) {

        val db = DBManager(this)
        var id = db.addModality(bytes)
        modalities.add(id)
        populateModalities(modalities)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data?.data != null) {
            val uri = data.data
            val inputStream = uri?.let { contentResolver.openInputStream(it) }
            val bytes = inputStream?.readBytes()
            inputStream?.close()
            if(mainImageCheck) {
                image = bytes
                mainImageCheck = false
            } else {
                addModality(bytes)
            }
        }
    }

    private fun populateModalities(modalities: ArrayList<Int>) {
        modalitiesContainer.removeAllViews()
        for (modalityId in modalities) {
            val db = DBManager(this)
            val modality = db.getModality(modalityId)

            val imageBitmap = BitmapFactory.decodeByteArray(modality, 0, modality!!.size)
            val inflater = LayoutInflater.from(this)
            val view = inflater.inflate(R.layout.artefact_modality_editable,
                modalitiesContainer, false)
            view.findViewById<ImageView>(R.id.modality).setImageBitmap(imageBitmap)
            val deleteButton = view.findViewById<Button>(R.id.delete_button)
            modalitiesContainer.addView(view)

            deleteButton.setOnClickListener {
                val modalityIndex = modalities.indexOf(modalityId)
                modalities.removeAt(modalityIndex)
                populateModalities(modalities)
            }
        }
    }

    private fun addParagraphView(paragraphs: HashMap<String, String>) {
        for ((heading, body) in paragraphs) {
            val inflater = LayoutInflater.from(this)
            val view = inflater.inflate(R.layout.artefact_new_paragraph, paragraphsContainer, false)
            view.findViewById<EditText>(R.id.paragraph_heading).setText(heading)
            view.findViewById<EditText>(R.id.paragraph_body).setText(body)
            paragraphsContainer.addView(view)
        }
    }

    private fun addParagraphView() {
        val inflater = LayoutInflater.from(this)
        Log.d("testing", "number of paragraphs " + paragraphsContainer.childCount.toString())

        val view = inflater.inflate(R.layout.artefact_new_paragraph, paragraphsContainer, false)
        paragraphsContainer.addView(view)
    }



    fun createArtefact (view: View) {


        val name = nameText.text.toString()


        val db = DBManager(this)
        val artefact = Artefact()
        var auth =  Firebase.auth
        var currentUser = auth.currentUser
        db.addArtefact(name, image, targetArtefact.getMeta(),
            artefact.paragraphsToJson(populateParagraphs()), artefact.modalitiesToJson(modalities),
            targetArtefact.getId().toString(), currentUser!!.uid, artefact.latLngToJson(artefactLocation))


        startActivity(Intent(this, MainActivity::class.java))
    }

    fun deleteArtefact (view: View) {

        val name = nameText.text.toString()


        val db = DBManager(this)
        db.deleteArtefact(targetArtefact.getId()!!)

        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun populateParagraphs(): HashMap<String, String>{

        var paragraphs : HashMap<String, String> = HashMap<String, String> ()
        for (i in 0 until paragraphsContainer.childCount) {
            val paragraph = paragraphsContainer.getChildAt(i)
            val title = paragraph.findViewById<EditText>(R.id.paragraph_heading)
            val body = paragraph.findViewById<EditText>(R.id.paragraph_body)

            paragraphs.put(title.text.toString(), body.text.toString())
        }
        return paragraphs
    }


}