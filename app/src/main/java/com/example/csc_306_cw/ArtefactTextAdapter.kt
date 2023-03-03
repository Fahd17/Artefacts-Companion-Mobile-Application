package com.example.csc_306_cw

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

public class ArtefactTextAdapter (private val paragraphs: HashMap<String, String>): RecyclerView.Adapter<ArtefactTextAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var headerView = itemView.findViewById<View>(R.id.paragraph_heading) as TextView
        var bodyView = itemView.findViewById<View>(R.id.paragraph_body) as TextView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtefactTextAdapter.ViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.artefact_paragraph, parent, false)

        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: ArtefactTextAdapter.ViewHolder, position: Int) {
        val infoList = paragraphs.toList()
        val info = infoList[position]

        holder.headerView.text = info.first
        holder.bodyView.text = info.second

    }

    override fun getItemCount(): Int {
        return paragraphs.size
    }
}