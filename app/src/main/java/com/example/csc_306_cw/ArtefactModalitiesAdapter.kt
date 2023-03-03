package com.example.csc_306_cw

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

public class ArtefactModalitiesAdapter( private var modalities: ArrayList<Int>): RecyclerView.Adapter<ArtefactModalitiesAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var modalityView = itemView.findViewById<View>(R.id.modality) as ImageView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtefactModalitiesAdapter.ViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.artefact_modality, parent, false)

        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: ArtefactModalitiesAdapter.ViewHolder, position: Int) {
        val info = modalities[position]

        holder.modalityView.setImageResource(info)

    }

    override fun getItemCount(): Int {
        return modalities.size
    }
}