package com.example.csc_306_cw

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.csc_306_cw.database.DBManager

class NewArtefactFormAdapter: AppCompatActivity() {
    private lateinit var paragraphsContainer: LinearLayout
    private lateinit var addParagraphButton: Button
    private lateinit var addModalitiesButton: Button
    private  var image: ByteArray?  = null
    private val PICK_IMAGE_REQUEST = 1
    var mainImageCheck: Boolean = false
    var modalities : ArrayList<Int> = ArrayList<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_artefact_form)

        paragraphsContainer = findViewById(R.id.paragraphs_container)
        addParagraphButton = findViewById(R.id.add_paragraph_button)

        addParagraphButton.setOnClickListener {
            addParagraphView()
        }

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



    private fun addParagraphView() {
        val inflater = LayoutInflater.from(this)
        Log.d("testing", "number of paragraphs " + paragraphsContainer.childCount.toString())

        val view = inflater.inflate(R.layout.artefact_new_paragraph, paragraphsContainer, false)
        paragraphsContainer.addView(view)
    }

    private fun addModality( bytes: ByteArray?) {

        val db = DBManager(this)
        var id = db.addModality(bytes)
        modalities.add(id)
    }

    fun createArtefact (view: View) {

        val nameText = findViewById<EditText>(R.id.name_text_field)
        val name = nameText.text.toString()

        val authorText = findViewById<EditText>(R.id.author_text_filed)
        val author = authorText.text.toString()
        val yearText = findViewById<EditText>(R.id.production_year)
        val year = yearText.text.toString()

        val db = DBManager(this)
        val artefact = Artefact()
        db.addArtefact(name, image, "Author: ".plus(author).plus(", Produced: ".plus(year)),
            artefact.paragraphsToJson(populateParagraphs()), artefact.modalitiesToJson(modalities))

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