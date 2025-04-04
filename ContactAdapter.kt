package com.example.contactroomapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contactroomapp.databinding.ItemContactBinding

class ContactAdapter(private val contactList: List<Contact>) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        holder.binding.tvName.text = contact.name
        holder.binding.tvContact.text = contact.contactNumber
        holder.binding.tvEmail.text = contact.email
    }

    override fun getItemCount(): Int = contactList.size
}