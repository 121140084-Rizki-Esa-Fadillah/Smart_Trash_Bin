package com.example.smart_trash_bin

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smart_trash_bin.databinding.ActivityMainBinding
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private var currentWeight: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Mengambil data led_status dan weight
        getLedStatus()
        getWeight()
        getBuzzerStatus()
    }

    private fun getLedStatus() {
        database.child("led_status").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ledStatus = snapshot.getValue(String::class.java) ?: "off"
                updateLedStatus(ledStatus)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun updateLedStatus(status: String) {
        when (status) {
            "green" -> {
                binding.ledStatusTextView.text = "Green"
                binding.ledStatusTextView.setTextColor(Color.GREEN)
                updateTrashStatus()
            }
            "yellow" -> {
                binding.ledStatusTextView.text = "Yellow"
                binding.ledStatusTextView.setTextColor(Color.YELLOW)
                updateTrashStatus()
            }
            "red" -> {
                binding.ledStatusTextView.text = "Red"
                binding.ledStatusTextView.setTextColor(Color.RED)
                binding.trashStatusTextView.text = "Kotak sampah penuh"
            }
            else -> {
                binding.ledStatusTextView.text = "Off"
                binding.ledStatusTextView.setTextColor(Color.GRAY)
                binding.trashStatusTextView.text = ""
            }
        }
    }

    private fun getWeight() {
        database.child("weight").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentWeight = snapshot.getValue(Double::class.java) ?: 0.0
                updateWeightTextView(currentWeight)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun updateWeightTextView(weight: Double) {
        val formattedWeight = if (weight >= 1000) {
            val weightInKg = weight / 1000
            String.format("%.2f kg", weightInKg)
        } else {
            String.format("%.2f g", weight)
        }
        binding.weightTextView.text = formattedWeight

        updateTrashStatus()
    }

    private fun updateTrashStatus() {
        val ledStatus = binding.ledStatusTextView.text.toString()
        if (ledStatus == "Red") {
            binding.trashStatusTextView.text = "Kotak sampah penuh"
        } else if (currentWeight > 1500) {
            binding.trashStatusTextView.text = "Kotak sampah melebihi batas berat"
        } else if (ledStatus == "Green" || ledStatus == "Yellow") {
            binding.trashStatusTextView.text = "Kotak sampah masih ada space"
        } else {
            binding.trashStatusTextView.text = ""
        }
    }

    private fun getBuzzerStatus() {
        database.child("buzzer_status").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val buzzerStatus = snapshot.getValue(String::class.java) ?: "off"
                updateBuzzerStatus(buzzerStatus)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun updateBuzzerStatus(status: String) {
        when (status) {
            "on" -> {
                binding.buzzerStatusTextView.text = "On"
            }
            "off" -> {
                binding.buzzerStatusTextView.text = "Off"
            }
            else -> {
                binding.buzzerStatusTextView.text = "Off"
                binding.trashStatusTextView.text = ""
            }
        }
    }
}
