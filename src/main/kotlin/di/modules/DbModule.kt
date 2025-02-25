package di.modules

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dagger.Module
import dagger.Provides
import db.dao.UserDao
import db.repository.UserDaoImpl
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import javax.inject.Singleton

@Module
class DbModule {

    @Provides
    @Singleton
    fun provideSessionFactory(): SessionFactory {
        val config = Configuration().configure("hibernate.cfg.xml") // <-- ????????? ????
        config.properties.forEach { key, value -> println("$key = $value") }
        return config.buildSessionFactory()
    }

    @Provides
    @Singleton
    fun provideUserDao(sessionFactory: SessionFactory): UserDao {
        return UserDaoImpl(sessionFactory)
    }
}

