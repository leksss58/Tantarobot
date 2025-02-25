package di.modules

import dagger.Provides
import dagger.Module
import presentation.bot.Bot
import presentation.usecase.BotUseCase
import javax.inject.Singleton

@Module
class BotModule {

    @Provides
    @Singleton
    fun provideBot(botUseCase: BotUseCase): Bot = Bot(botUseCase)
}