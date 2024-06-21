package com.deymer.repository.repositories.user

import android.net.Uri
import com.deymer.datasource.remote.user.IUserDataSource
import com.deymer.repository.mappers.toEntity
import com.deymer.repository.mappers.toModel
import com.deymer.repository.models.SimpleUserModel
import com.deymer.repository.models.UserModel
import com.deymer.repository.utils.OnResult
import com.deymer.repository.utils.RepositoryConstants.Tags.TAG_NULL
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException
import java.lang.Exception
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDataSource: IUserDataSource
): IUserRepository {

    override suspend fun registerUser(
        userModel: UserModel
    ): OnResult<Boolean> {
        return try {
            OnResult.Success(userDataSource.saveUser(
                userModel.toEntity())
            )
        } catch (exception: FirebaseAuthException) {
            OnResult.Error(exception)
        } catch (exception: FirebaseFirestoreException) {
            OnResult.Error(exception)
        } catch (exception: Exception) {
            OnResult.Error(exception)
        }
    }

    override suspend fun loginUser(
        email: String,
        password: String
    ): OnResult<Boolean> {
        return try {
            OnResult.Success(userDataSource.login(email, password))
        } catch (exception: FirebaseAuthException) {
            OnResult.Error(exception)
        } catch (exception: FirebaseFirestoreException) {
            OnResult.Error(exception)
        } catch (exception: Exception) {
            OnResult.Error(exception)
        }
    }

    override suspend fun fetchUser(): OnResult<SimpleUserModel> {
        return try {
            return userDataSource.getUser()?.let {
                OnResult.Success(it.toModel())
            } ?: run { OnResult.Error(IllegalArgumentException(TAG_NULL)) }
        } catch (exception: FirebaseFirestoreException) {
            OnResult.Error(exception)
        } catch (exception: Exception) {
            OnResult.Error(exception)
        }
    }

    override suspend fun logout() =
        OnResult.Success(userDataSource.logout())

    override suspend fun inSession() =
        OnResult.Success(userDataSource.inSession())

    override suspend fun uploadPhoto(
        photo: Uri
    ): OnResult<Boolean> {
        return try {
            val urlPhoto = userDataSource.uploadPhoto(photo)
            OnResult.Success(urlPhoto.isNotEmpty())
        } catch (exception: FirebaseFirestoreException) {
            OnResult.Error(exception)
        } catch (exception: Exception) {
            OnResult.Error(exception)
        }
    }
}