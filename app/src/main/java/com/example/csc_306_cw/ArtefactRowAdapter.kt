package com.example.csc_306_cw

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ArtefactRowAdapter(private val artefacts: ArrayList<Artefact>): RecyclerView.Adapter<ArtefactRowAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var artefactImage = itemView.findViewById<View>(R.id.atrefact_image) as ImageView
        var artefactName = itemView.findViewById<View>(R.id.artefacr_name) as TextView
        var artefactMeta = itemView.findViewById<View>(R.id.artefact_meta_data) as TextView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtefactRowAdapter.ViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.artefact_row, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return artefacts.size
    }

    override fun onBindViewHolder(holder: ArtefactRowAdapter.ViewHolder, position: Int) {
        val info = artefacts[position]

        holder.artefactImage.setImageResource(info.getImage())
        holder.artefactName.text = info.getName()
        holder.artefactMeta.text = info.getMeta()
    }
}