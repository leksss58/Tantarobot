package db.dao

import db.entity.User
import db.enums.UserGenderType
import db.enums.UserLanguageType

interface UserDao {
    fun insertUser(user: User)
    fun getAllUsers(): List<User>
    fun getUserById(userId: Long?): User
    fun updateUserBirthday(userId: Long?, date: String)
    fun updateUserTarget(userId: Long?, target: String)
    fun updateUserGender(userId: Long?, gender: UserGenderType)
    fun updateUserLanguage(userId: Long?, language: UserLanguageType)
}