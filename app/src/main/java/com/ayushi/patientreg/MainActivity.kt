package com.ayushi.patientreg

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etName = findViewById<EditText>(R.id.etName)
        val etAge = findViewById<EditText>(R.id.etAge)
        val rgGender = findViewById<RadioGroup>(R.id.rgGender)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etAddress = findViewById<EditText>(R.id.etAddress)
        val etDisease = findViewById<EditText>(R.id.etDisease)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnView = findViewById<Button>(R.id.btnView)

        btnSubmit.setOnClickListener {
            val name = etName.text.toString().trim()
            val age = etAge.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val address = etAddress.text.toString().trim()
            val disease = etDisease.text.toString().trim()

            val checkedId = rgGender.checkedRadioButtonId
            if (name.isEmpty() || age.isEmpty() || phone.isEmpty() ||
                address.isEmpty() || disease.isEmpty() || checkedId == -1
            ) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (phone.length != 10) {
                Toast.makeText(this, "Phone must be 10 digits", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val gender = findViewById<RadioButton>(checkedId).text.toString()
            val docRef = db.collection("patients").document()

            val patient = hashMapOf(
                "patientId" to docRef.id,
                "name" to name,
                "age" to age,
                "gender" to gender,
                "phone" to phone,
                "address" to address,
                "disease" to disease,
                "timestamp" to System.currentTimeMillis()
            )

            btnSubmit.isEnabled = false
            docRef.set(patient)
                .addOnSuccessListener {
                    Toast.makeText(this, "Patient registered!", Toast.LENGTH_SHORT).show()
                    etName.text.clear(); etAge.text.clear(); etPhone.text.clear()
                    etAddress.text.clear(); etDisease.text.clear(); rgGender.clearCheck()
                    btnSubmit.isEnabled = true
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    btnSubmit.isEnabled = true
                }
        }

        btnView.setOnClickListener {
            startActivity(Intent(this, RecordsActivity::class.java))
        }
    }
}