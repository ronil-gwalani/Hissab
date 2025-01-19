package org.ronil.hissab.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PeopleDao {

    @Upsert
    suspend fun upsert(person: UserContacts)

    @Delete
    suspend fun delete(person: UserContacts)

    @Query("SELECT * FROM UserContacts")
   suspend fun getAllPeople(): List<UserContacts>

}