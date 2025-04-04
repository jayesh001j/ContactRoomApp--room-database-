package com.example.contactroomapp

import androidx.room.*

@Dao
interface ContactDao {
    @Insert
    suspend fun insertContact(contact: Contact)

    @Query("SELECT * FROM contacts")
    suspend fun getAllContacts(): List<Contact>
}