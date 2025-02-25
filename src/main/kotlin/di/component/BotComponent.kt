package di.component

import dagger.Component
import db.dao.UserDao
import di.modules.ApiModule
import di.modules.BotModule
import di.modules.DataModule
import di.modules.DbModule
import domain.repository.ServiceRepository
import presentation.bot.Bot
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApiModule::class,
        DataModule::class,
        BotModule::class,
        DbModule::class
    ]
)
interface BotComponent {
    fun getServiceRepository(): ServiceRepository
    fun getBot(): Bot
    fun getUserDao(): UserDao
}