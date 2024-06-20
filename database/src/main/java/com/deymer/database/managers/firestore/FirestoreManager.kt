package com.deymer.database.managers.firestore

import com.google.firebase.firestore.DocumentSnapshot

class FirestoreManager: IFirestoreManager {

    override suspend fun <T : Any> save(entity: T): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun <T : Any> save(entity: T, entityId: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun <T : Any> save(entityList: List<T>): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun <T : Any> updateValueById(
        documentId: String,
        keyField: String,
        valueField: T,
        table: String
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun <T : Any> updateValue(
        keyId: String,
        valueId: String,
        keyField: String,
        valueField: T,
        table: String
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun <T : Any> fetchAll(entity: T): List<DocumentSnapshot> {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: String, table: String): DocumentSnapshot? {
        TODO("Not yet implemented")
    }

    override suspend fun getByValue(key: String, value: String, table: String): DocumentSnapshot? {
        TODO("Not yet implemented")
    }

    override suspend fun getListByValue(
        key: String,
        value: String,
        table: String
    ): List<DocumentSnapshot> {
        TODO("Not yet implemented")
    }
}