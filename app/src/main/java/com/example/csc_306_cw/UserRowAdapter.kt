package com.example.csc_306_cw

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.csc_306_cw.database.DBManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class UserRowAdapter (private val users: ArrayList<String>): RecyclerView.Adapter<UserRowAdapter.ViewHolder>(){

    private lateinit var context: Context
    private var auth =  Firebase.auth
    private var currentUser = auth.currentUser

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var userId = itemView.findViewById<View>(R.id.user_id) as TextView
        var switch = itemView.findViewById<View>(R.id.role_switch) as Switch

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserRowAdapter.ViewHolder {

        context = parent.context
        var inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.user_row, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserRowAdapter.ViewHolder, position: Int) {
        val userId = users[position]

        val db = DBManager(context)
        holder.userId.text = userId
        holder.switch.isChecked = db.isAdmin(userId)

        holder.switch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener {
                buttonView, isChecked ->
            db.changeRole(userId, isChecked.toString())
        })

    }


}