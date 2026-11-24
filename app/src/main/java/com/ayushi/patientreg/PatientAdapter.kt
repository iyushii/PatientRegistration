package com.ayushi.patientreg

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PatientAdapter(
    private val patients: List<Patient>,
    private val onLongClick: (Patient) -> Unit
) : RecyclerView.Adapter<PatientAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvDetails: TextView = view.findViewById(R.id.tvDetails)
        val tvDisease: TextView = view.findViewById(R.id.tvDisease)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_patient, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val p = patients[position]
        holder.tvName.text = p.name
        holder.tvDetails.text = "Age: ${p.age} | ${p.gender} | Ph: ${p.phone}\n${p.address}"
        holder.tvDisease.text = "Problem: ${p.disease}"
        holder.itemView.setOnLongClickListener { onLongClick(p); true }
    }

    override fun getItemCount() = patients.size
}