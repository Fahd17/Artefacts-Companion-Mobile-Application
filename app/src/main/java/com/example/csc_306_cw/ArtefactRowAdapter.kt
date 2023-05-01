package com.example.csc_306_cw

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.csc_306_cw.database.DBManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ArtefactRowAdapter(private val artefacts: ArrayList<Artefact>): RecyclerView.Adapter<ArtefactRowAdapter.ViewHolder>(){

    private lateinit var context: Context
    private var auth =  Firebase.auth
    private var currentUser = auth.currentUser
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var artefactImage = itemView.findViewById<View>(R.id.atrefact_image) as ImageView
        var artefactName = itemView.findViewById<View>(R.id.artefacr_name) as TextView
        var artefactMeta = itemView.findViewById<View>(R.id.artefact_meta_data) as TextView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtefactRowAdapter.ViewHolder {
        context = parent.context
        var inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.artefact_row, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return artefacts.size
    }

    override fun onBindViewHolder(holder: ArtefactRowAdapter.ViewHolder, position: Int) {
        val info = artefacts[position]

        val imageBitmap = BitmapFactory.decodeByteArray(info.getImage(), 0, info.getImage()!!.size)
        holder.artefactImage.setImageBitmap(imageBitmap)
        holder.artefactName.text = info.getName()
        holder.artefactMeta.text = info.getMeta()

        holder.itemView.setOnClickListener(View.OnClickListener {
            val newIntent = Intent(context, ArtefactPageActivity::class.java)
            newIntent.putExtra("id", info.getId())
            Log.d("testing",info.getId().toString() )
            context.startActivity(newIntent)
        })
        var addToBookmark = holder.itemView.findViewById<Button>(R.id.add_to_bookmark)as Button
        addToBookmark.setOnClickListener(View.OnClickListener {

            val db = DBManager(context)
            var userId = currentUser?.uid

            if (userId != null) {
                db.addUserBookmark(userId, info.getId())
            }
        })
    }


}