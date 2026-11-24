package com.ayushi.patientreg

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class RecordsActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val list = mutableListOf<Patient>()
    private lateinit var adapter: PatientAdapter
    private var listener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_records)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = PatientAdapter(list) { patient ->
            AlertDialog.Builder(this)
                .setTitle("Delete patient?")
                .setMessage("Delete ${patient.name}'s record?")
                .setPositiveButton("Delete") { _, _ ->
                    db.collection("patients").document(patient.patientId).delete()
                        .addOnFailureListener {
                            Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
                        }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
        recyclerView.adapter = adapter

        listener = db.collection("patients")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }
                list.clear()
                snapshot?.documents?.forEach { doc ->
                    doc.toObject(Patient::class.java)?.let { list.add(it) }
                }
                adapter.notifyDataSetChanged()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        listener?.remove()
    }
}