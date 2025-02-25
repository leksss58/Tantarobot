package db.repository

import db.dao.UserDao
import db.entity.User
import db.enums.UserGenderType
import db.enums.UserLanguageType
import jakarta.transaction.Transactional
import org.hibernate.SessionFactory
import javax.inject.Inject

class UserDaoImpl @Inject constructor(
    private val sessionFactory: SessionFactory
) : UserDao {
    @Transactional
    override fun getAllUsers(): List<User> {
        return sessionFactory.openSession().use { session ->
            session.createQuery("FROM User", User::class.java).resultList
        }
    }

    override fun getUserById(userId: Long?): User {
        return sessionFactory.openSession().use { session ->
            session
                .createQuery("FROM User WHERE id = :userId", User::class.java)
                .setParameter("userId", userId)
                .singleResult
        }
    }

    @Transactional
    override fun insertUser(user: User) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.merge(user)
            session.transaction.commit()
        }
    }

    override fun updateUserBirthday(userId: Long?, date: String) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.createMutationQuery("UPDATE User SET date = :date WHERE id = :userId")
                .setParameter("date", date)
                .setParameter("userId", userId)
                .executeUpdate()
            session.transaction.commit()
        }
    }

    override fun updateUserTarget(userId: Long?, target: String) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.createMutationQuery("UPDATE User SET target = :target WHERE id = :userId")
                .setParameter("target", target)
                .setParameter("userId", userId)
                .executeUpdate()
            session.transaction.commit()
        }
    }

    override fun updateUserGender(userId: Long?, gender: UserGenderType) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.createMutationQuery("UPDATE User SET gender = :gender WHERE id = :userId")
                .setParameter("gender", gender)
                .setParameter("userId", userId)
                .executeUpdate()
            session.transaction.commit()
        }
    }

    override fun updateUserLanguage(userId: Long?, language: UserLanguageType) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.createMutationQuery("UPDATE User SET language = :language WHERE id = :userId")
                .setParameter("language", language)
                .setParameter("userId", userId)
                .executeUpdate()
            session.transaction.commit()
        }
    }
}