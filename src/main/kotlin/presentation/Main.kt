package presentation

import di.component.BotComponent
import di.component.DaggerBotComponent
import kotlinx.coroutines.runBlocking

fun main() {

    // Инициализация Dagger компонента
    val appComponent = initAppComponent()

    // Получение репозитория
    val dataRepository = appComponent.getServiceRepository()

    // Использование репозитория
    runBlocking {
        try {
            val response = dataRepository.fetchData()
            println("Инициализация dagger прошла успешна. Ответ: $response")
        } catch (e: Exception) {
            println("Ошибка инициализации dagger: ${e.message}")
        }
    }

    val bot = appComponent.getBot().createBot()
    bot.startPolling()
}

fun initAppComponent(): BotComponent {
    return DaggerBotComponent.create()
}