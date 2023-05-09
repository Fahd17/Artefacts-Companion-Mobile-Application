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

class RequestRowAdapter(private val artefacts: ArrayList<Artefact>): RecyclerView.Adapter<RequestRowAdapter.ViewHolder>(){

    private lateinit var context: Context
    private var auth =  Firebase.auth
    private var currentUser = auth.currentUser
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var artefactImage = itemView.findViewById<View>(R.id.atrefact_image) as ImageView
        var artefactName = itemView.findViewById<View>(R.id.artefacr_name) as TextView
        var requestInfo = itemView.findViewById<View>(R.id.request_info) as TextView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestRowAdapter.ViewHolder {
        context = parent.context
        var inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.requests_row, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RequestRowAdapter.ViewHolder, position: Int) {
        val info = artefacts[position]

        val imageBitmap = BitmapFactory.decodeByteArray(info.getImage(), 0, info.getImage()!!.size)
        holder.artefactImage.setImageBitmap(imageBitmap)
        holder.artefactName.text = info.getName()
        holder.requestInfo.text = info.getMeta()

        holder.itemView.setOnClickListener(View.OnClickListener {
            val newIntent = Intent(context, ArtefactPageActivity::class.java)
            newIntent.putExtra("id", info.getId())
            Log.d("testing",info.getId().toString() )
            context.startActivity(newIntent)
        })

        var approveButton = holder.itemView.findViewById<Button>(R.id.approve_button) as Button
        approveButton.setOnClickListener(View.OnClickListener {
            val db = DBManager(context)
            var state = db.getState(info.getId()!!)

            if (state.equals("new")){

                db.updateArtefact(info, "ready")
                artefacts.removeAt(position)
                notifyItemRemoved(position)
            } else {
                info.setId(state!!.toInt())
                db.updateArtefact(info, "ready")
                artefacts.removeAt(position)
                notifyItemRemoved(position)
            }

        })
    }

    override fun getItemCount(): Int {
        return artefacts.size
    }

}