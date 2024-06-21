package com.deymer.database.managers.firestore

import com.deymer.database.DataBaseConstants.DEFAULT_STRING
import com.deymer.database.DataBaseConstants.Names.KEY_NAME_DATABASE
import com.deymer.database.DataBaseConstants.Tables.ACCOUNTS_TABLE
import com.deymer.database.DataBaseConstants.Tables.TRANSACTIONS_TABLE
import com.deymer.database.DataBaseConstants.Tables.USERS_TABLE
import com.deymer.database.entities.AccountEntity
import com.deymer.database.entities.TransactionEntity
import com.deymer.database.entities.UserEntity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirestoreManager @Inject constructor(
    private val firestoreDatabase: FirebaseFirestore
): IFirestoreManager {

    override suspend fun <T : Any> save(
        entity: T
    ): Boolean = suspendCoroutine { continuation ->
        val tableName = getTableName(entity::class.java)
        firestoreDatabase.collection(KEY_NAME_DATABASE)
            .document(tableName)
            .collection(tableName)
            .document()
            .set(entity)
            .addOnCompleteListener { task ->
                continuation.resume(task.isSuccessful)
            }
    }

    override suspend fun <T : Any> save(
        entity: T, entityId: String
    ): Boolean = suspendCoroutine { continuation ->
        val tableName = getTableName(entity::class.java)
        firestoreDatabase.collection(KEY_NAME_DATABASE)
            .document(tableName)
            .collection(tableName)
            .document(entityId)
            .set(entity)
            .addOnCompleteListener { task ->
                continuation.resume(task.isSuccessful)
            }
    }

    override suspend fun <T : Any> save(
        entityList: List<T>
    ): Boolean = coroutineScope {
        val tasks = entityList.map { entity ->
            async {
                val table = getTableName(entity::class.java)
                firestoreDatabase.collection(KEY_NAME_DATABASE)
                    .document(table)
                    .collection(table)
                    .add(entity)
                    .await()
                true
            }
        }
        tasks.awaitAll().all { it }
    }

    override suspend fun <T : Any> updateValueById(
        documentId: String,
        keyField: String,
        valueField: T,
        table: String
    ): Boolean = suspendCoroutine { continuation ->
        firestoreDatabase.collection(KEY_NAME_DATABASE)
            .document(table)
            .collection(table)
            .document(documentId)
            .update(keyField, valueField)
            .addOnCompleteListener { task ->
                continuation.resume(task.isSuccessful)
            }
    }

    override suspend fun <T : Any> updateValue(
        keyId: String,
        valueId: String,
        keyField: String,
        valueField: T,
        table: String
    ): Boolean {
        val documentSnapshot = firestoreDatabase.collection(KEY_NAME_DATABASE)
            .document(table)
            .collection(table)
            .whereEqualTo(keyId, valueId)
            .get()
            .await()
            .documents.firstOrNull()
        return documentSnapshot?.let {
            documentSnapshot.reference.update(keyField, valueField).isSuccessful
        } ?: run { false }
    }

    override suspend fun <T : Any> fetchAll(entity: T): List<DocumentSnapshot> {
        val tableName = getTableName(entity::class.java)
        return firestoreDatabase.collection(KEY_NAME_DATABASE)
            .document(tableName)
            .collection(tableName)
            .get()
            .await()
            .documents
    }

    override suspend fun getById(
        id: String,
        table: String
    ): DocumentSnapshot? {
        return firestoreDatabase
            .collection(KEY_NAME_DATABASE)
            .document(table)
            .collection(table)
            .document(id)
            .get()
            .await()
    }

    override suspend fun getByValue(
        key: String,
        value: String,
        table: String
    ): DocumentSnapshot? {
        return firestoreDatabase.collection(KEY_NAME_DATABASE)
            .document(table)
            .collection(table)
            .whereEqualTo(key, value)
            .get()
            .await()
            .documents.firstOrNull()
    }

    override suspend fun getListByValue(
        key: String,
        value: String,
        table: String
    ): List<DocumentSnapshot> {
        return firestoreDatabase.collection(KEY_NAME_DATABASE)
            .document(table)
            .collection(table)
            .whereEqualTo(key, value)
            .get()
            .await()
            .documents
    }

    private fun <T> getTableName(value: Class<T>): String {
        return when (value) {
            UserEntity::class.java -> USERS_TABLE
            AccountEntity::class.java -> ACCOUNTS_TABLE
            TransactionEntity::class.java -> TRANSACTIONS_TABLE
            else -> DEFAULT_STRING
        }
    }
}