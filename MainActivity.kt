package com.example.contactroomapp

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactroomapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: ContactDatabase
    private var contactList = mutableListOf<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = ContactDatabase.getDatabase(this)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        loadContacts()

        binding.fab.setOnClickListener {
            showAddContactDialog()
        }
    }

    private fun loadContacts() {
        lifecycleScope.launch {
            contactList = db.contactDao().getAllContacts().toMutableList()
            binding.recyclerView.adapter = ContactAdapter(contactList)
        }
    }

    private fun showAddContactDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_contact, null)
        val nameEdit = dialogView.findViewById<EditText>(R.id.etName)
        val contactEdit = dialogView.findViewById<EditText>(R.id.etContact)
        val emailEdit = dialogView.findViewById<EditText>(R.id.etEmail)

        AlertDialog.Builder(this)
            .setTitle("Add Contact")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val contact = Contact(
                    name = nameEdit.text.toString(),
                    contactNumber = contactEdit.text.toString(),
                    email = emailEdit.text.toString()
                )
                lifecycleScope.launch {
                    db.contactDao().insertContact(contact)
                    loadContacts()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}