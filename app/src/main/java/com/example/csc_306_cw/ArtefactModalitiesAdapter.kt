package com.example.csc_306_cw

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.csc_306_cw.database.DBManager

public class ArtefactModalitiesAdapter( private var modalities: ArrayList<Int>): RecyclerView.Adapter<ArtefactModalitiesAdapter.ViewHolder>(){

    private lateinit var context: Context

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var modalityView = itemView.findViewById<View>(R.id.modality) as ImageView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtefactModalitiesAdapter.ViewHolder {
        context = parent.context
        var inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.artefact_modality, parent, false)

        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: ArtefactModalitiesAdapter.ViewHolder, position: Int) {
        val info = modalities[position]
        val db = DBManager(context)
        val modality = db.getModality(info)
        Log.d("testing","modalities "+info )

        val imageBitmap = BitmapFactory.decodeByteArray(modality, 0, modality!!.size)
        holder.modalityView.setImageBitmap(imageBitmap)

    }

    override fun getItemCount(): Int {
        return modalities.size
    }
}