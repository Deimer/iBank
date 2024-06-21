package com.deymer.repository.mappers

import com.deymer.database.entities.UserEntity
import com.deymer.repository.models.SimpleUserModel
import com.deymer.repository.models.UserModel

fun UserModel.toEntity() = UserEntity(
    name = this.name,
    surname = this.surname,
    email = this.email,
    password = this.password,
    documentPhoto = this.documentPhoto
)

fun UserEntity.toModel() = SimpleUserModel(
    simpleName = this.name,
    name = this.name,
    surname = this.surname,
    email = this.email,
    urlPhoto = this.urlPhoto
)