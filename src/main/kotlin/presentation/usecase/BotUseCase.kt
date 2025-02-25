package presentation.usecase

import db.dao.UserDao
import db.entity.User
import db.enums.UserGenderType
import db.enums.UserLanguageType
import domain.repository.ServiceRepository
import javax.inject.Inject

class BotUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository,
    private val userDao: UserDao
) {
    suspend fun fetchData() = serviceRepository.fetchData()

    fun insertUser(user: User) {
        userDao.insertUser(
            user
        )
    }

    fun getUser(userId: Long?) = userDao.getUserById(userId)


    fun updateUserTarget(userId: Long?, target: String) {
        userDao.updateUserTarget(userId, target)
    }

    fun updateUserGender(userId: Long?, gender: UserGenderType) {
        userDao.updateUserGender(userId, gender)
    }

    fun updateUserBirthday(userId: Long?, date: String) {
        userDao.updateUserBirthday(userId, date)
    }

    fun updateUserLanguage(userId: Long?, language: UserLanguageType) {
        userDao.updateUserLanguage(userId, language)
    }
}