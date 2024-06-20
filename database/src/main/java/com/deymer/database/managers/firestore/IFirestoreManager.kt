package com.deymer.database.managers.firestore

import com.google.firebase.firestore.DocumentSnapshot

interface IFirestoreManager {

    suspend fun <T: Any> save(
        entity: T
    ): Boolean

    suspend fun <T: Any> save(
        entity: T,
        entityId: String
    ): Boolean

    suspend fun <T: Any> save(
        entityList: List<T>
    ): Boolean

    suspend fun <T: Any> updateValueById(
        documentId: String,
        keyField: String,
        valueField: T,
        table: String
    ): Boolean

    suspend fun <T: Any> updateValue(
        keyId: String,
        valueId: String,
        keyField: String,
        valueField: T,
        table: String
    ): Boolean

    suspend fun <T: Any> fetchAll(
        entity: T
    ): List<DocumentSnapshot>

    suspend fun getById(
        id: String,
        table: String
    ): DocumentSnapshot?

    suspend fun getByValue(
        key: String,
        value: String,
        table: String
    ): DocumentSnapshot?

    suspend fun getListByValue(
        key: String,
        value: String,
        table: String
    ): List<DocumentSnapshot>
}