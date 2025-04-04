package com.example.contactroomapp

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactroomapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), ContactAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: ContactDatabase
    private var contactList = mutableListOf<Contact>()
    private lateinit var adapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = ContactDatabase.getDatabase(this)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ContactAdapter(contactList, this)
        binding.recyclerView.adapter = adapter
        loadContacts()

        binding.fab.setOnClickListener {
            showContactDialog(null)
        }
    }

    private fun loadContacts() {
        lifecycleScope.launch {
            contactList.clear()
            contactList.addAll(db.contactDao().getAllContacts())
            adapter.notifyDataSetChanged()
        }
    }

    private fun showContactDialog(contact: Contact?) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_contact, null)
        val nameEdit = dialogView.findViewById<EditText>(R.id.etName)
        val contactEdit = dialogView.findViewById<EditText>(R.id.etContact)
        val emailEdit = dialogView.findViewById<EditText>(R.id.etEmail)

        if (contact != null) {
            nameEdit.setText(contact.name)
            contactEdit.setText(contact.contactNumber)
            emailEdit.setText(contact.email)
        }

        AlertDialog.Builder(this)
            .setTitle(if (contact == null) "Add Contact" else "Edit Contact")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val updatedContact = Contact(
                    id = contact?.id ?: 0,
                    name = nameEdit.text.toString(),
                    contactNumber = contactEdit.text.toString(),
                    email = emailEdit.text.toString()
                )
                lifecycleScope.launch {
                    if (contact == null) {
                        db.contactDao().insertContact(updatedContact)
                    } else {
                        db.contactDao().updateContact(updatedContact)
                    }
                    loadContacts()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onItemLongClick(contact: Contact) {
        val options = arrayOf("Edit", "Delete")
        AlertDialog.Builder(this)
            .setTitle("Choose option")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showContactDialog(contact)
                    1 -> {
                        lifecycleScope.launch {
                            db.contactDao().deleteContact(contact)
                            loadContacts()
                        }
                    }
                }
            }
            .show()
    }
}