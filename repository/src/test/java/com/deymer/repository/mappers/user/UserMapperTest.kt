package com.deymer.repository.mappers.user

import android.net.Uri
import com.deymer.database.entities.UserEntity
import com.deymer.repository.mappers.toEntity
import com.deymer.repository.mappers.toModel
import com.deymer.repository.models.UserModel
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock

class UserMapperTest {

    @Test
    fun `UserModel to UserEntity mapping`() {
        val userModel = UserModel(
            name = "Name",
            surname = "User",
            email = "user.name@example.com",
            password = "securepassword",
            documentPhoto = mock(Uri::class.java)
        )
        val userEntity = userModel.toEntity()
        assertEquals(userModel.name, userEntity.name)
        assertEquals(userModel.surname, userEntity.surname)
        assertEquals(userModel.email, userEntity.email)
        assertEquals(userModel.password, userEntity.password)
        assertEquals(userModel.documentPhoto, userEntity.documentPhoto)
    }

    @Test
    fun `UserEntity to SimpleUserModel mapping`() {
        val userEntity = UserEntity(
            name = "Test User",
            surname = "Test",
            email = "test.user@example.com",
            urlPhoto = "http://example.com/urlPhoto.jpg"
        )
        val simpleUserModel = userEntity.toModel()
        assertEquals("Test", simpleUserModel.simpleName)
        assertEquals(userEntity.name, simpleUserModel.name)
        assertEquals(userEntity.surname, simpleUserModel.surname)
        assertEquals(userEntity.email, simpleUserModel.email)
        assertEquals(userEntity.urlPhoto, simpleUserModel.urlPhoto)
    }

    @Test
    fun `UserModel with default values to UserEntity mapping`() {
        val userModel = UserModel(
            name = "",
            surname = "",
            email = "",
            password = "",
            documentPhoto = mock(Uri::class.java)
        )
        val userEntity = userModel.toEntity()
        assertEquals(userModel.name, userEntity.name)
        assertEquals(userModel.surname, userEntity.surname)
        assertEquals(userModel.email, userEntity.email)
        assertEquals(userModel.password, userEntity.password)
        assertEquals(userModel.documentPhoto, userEntity.documentPhoto)
    }

    @Test
    fun `UserEntity with custom values to SimpleUserModel mapping`() {
        val userEntity = UserEntity(
            name = "Test User",
            surname = "Test",
            email = "test.user@example.com",
            urlPhoto = "http://example.com/urlPhoto.jpg"
        )
        val simpleUserModel = userEntity.toModel()
        assertEquals("Test", simpleUserModel.simpleName)
        assertEquals(userEntity.name, simpleUserModel.name)
        assertEquals(userEntity.surname, simpleUserModel.surname)
        assertEquals(userEntity.email, simpleUserModel.email)
        assertEquals(userEntity.urlPhoto, simpleUserModel.urlPhoto)
    }
}