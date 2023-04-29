package com.example.csc_306_cw

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.csc_306_cw.database.DBManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ArtefactBookmarkRowAdapter(private val artefacts: ArrayList<Artefact>): RecyclerView.Adapter<ArtefactBookmarkRowAdapter.ViewHolder>(){

    private lateinit var context: Context
    private var auth =  Firebase.auth
    private var currentUser = auth.currentUser

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var artefactImage = itemView.findViewById<View>(R.id.atrefact_image) as ImageView
        var artefactName = itemView.findViewById<View>(R.id.artefacr_name) as TextView
        var artefactMeta = itemView.findViewById<View>(R.id.artefact_meta_data) as TextView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtefactBookmarkRowAdapter.ViewHolder {
        context = parent.context
        var inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.bookmark_artefact_row, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return artefacts.size
    }

    override fun onBindViewHolder(holder: ArtefactBookmarkRowAdapter.ViewHolder, position: Int) {
        val info = artefacts[position]

        holder.artefactImage.setImageResource(info.getImage())
        holder.artefactName.text = info.getName()
        holder.artefactMeta.text = info.getMeta()

        holder.itemView.setOnClickListener(View.OnClickListener {
            val newIntent = Intent(context, ArtefactPageActivity::class.java)
            newIntent.putExtra("id", info.getId())
            Log.d("testing",info.getId().toString() )
            context.startActivity(newIntent)
        })
        var frmoveFromBookmark = holder.itemView.findViewById<Button>(R.id.remove_from_bookmark)as Button
        frmoveFromBookmark.setOnClickListener(View.OnClickListener {

            val db = DBManager(context)
            var userId = currentUser?.uid

            if (userId != null) {
                db.deleteUserBookmark(userId, info.getId())
            }
        })
    }


}