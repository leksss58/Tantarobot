package presentation.bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.*
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.github.kotlintelegrambot.entities.keyboard.KeyboardButton
import db.entity.User
import db.enums.UserGenderType
import db.enums.UserLanguageType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import presentation.usecase.BotUseCase
import util.StringResources

private const val BOT_ANSWER_TIMEOUT = 30
private const val BOT_TOKEN = "7855788973:AAGpnWKTrSC3GqUVomcFeg3BEdMiXS6v4Z8"

class Bot(private val botUseCase: BotUseCase) :
    CoroutineScope by CoroutineScope(Dispatchers.Default + SupervisorJob()) {

    private var _chatId: ChatId? = null
    private val chatId by lazy { requireNotNull(_chatId) }

    fun createBot(): Bot {
        return bot {
            timeout = BOT_ANSWER_TIMEOUT
            token = BOT_TOKEN

            dispatch {
                //Commands
                setupCommands()

                //Callbacks
                setupGenderCallback(UserLanguageType.RUSSIAN, BTN_RU, "ru_change_gender")
                setupGenderCallback(UserLanguageType.ENGLISH, BTN_EN, "en_change_gender")

                setupGenderCallback(BTN_MAN, UserGenderType.MAN)
                setupGenderCallback(BTN_WOMAN, UserGenderType.WOMAN)

                setupDateCallBack()
            }
        }
    }

    private fun Dispatcher.setupGenderCallback(languageType: UserLanguageType, callbackData: String, messageKey: String) {
        callbackQuery(callbackData = callbackData) {
            try {
                botUseCase.updateUserLanguage(userId = callbackQuery.from.id, language = languageType)
            } catch (e: Exception) {
                println("Error updating user language: ${e.message}")
                e.printStackTrace()
            }

            val inlineKeyboardMarkup = InlineKeyboardMarkup.create(
                listOf(
                    InlineKeyboardButton.CallbackData(
                        text = when (languageType) {
                            UserLanguageType.RUSSIAN -> "Мужской \uD83D\uDC68"
                            UserLanguageType.ENGLISH -> "Man \uD83D\uDC68"
                            else -> ""
                        },
                        callbackData = BTN_MAN
                    ),
                    InlineKeyboardButton.CallbackData(
                        text = when (languageType) {
                            UserLanguageType.RUSSIAN -> "Женский \uD83D\uDC69"
                            UserLanguageType.ENGLISH -> "Woman \uD83D\uDC69"
                            else -> ""
                        },
                        callbackData = BTN_WOMAN
                    )
                )
            )


            bot.sendMessage(
                chatId = chatId,
                text = StringResources.get(messageKey),
                replyMarkup = inlineKeyboardMarkup
            )
        }
    }

    private fun Dispatcher.setupGenderCallback(callbackData: String, gender: UserGenderType) {
        callbackQuery(callbackData = callbackData) {
            botUseCase.updateUserGender(userId = callbackQuery.from.id, gender = gender)

            val userLanguage = botUseCase.getUser(callbackQuery.from.id).language

            val inlineKeyboardMarkup = InlineKeyboardMarkup.create(
                listOf(
                    InlineKeyboardButton.CallbackData(
                        text = when (userLanguage) {
                            UserLanguageType.RUSSIAN -> StringResources.get("ru_skip")
                            UserLanguageType.ENGLISH -> StringResources.get("en_skip")
                            UserLanguageType.NONE -> ""
                        },
                        callbackData = BTN_SKIP
                    )
                )
            )

            bot.sendPhoto(
                chatId = chatId,
                photo = TelegramFile.ByUrl("https://i.ibb.co/hJYZVMJT/mock-selfie.jpg"),
                caption = when (userLanguage) {
                    UserLanguageType.RUSSIAN -> StringResources.get("ru_selfie")
                    UserLanguageType.ENGLISH -> StringResources.get("en_selfie")
                    UserLanguageType.NONE -> ""
                },
                parseMode = ParseMode.MARKDOWN,
                replyMarkup = inlineKeyboardMarkup
            )
        }
    }

    private fun Dispatcher.setupDateCallBack() {
        callbackQuery(callbackData = BTN_SKIP) {

            val userId = callbackQuery.from.id
            val userLanguage = botUseCase.getUser(callbackQuery.from.id).language

            bot.sendMessage(
                chatId = chatId,
                text = when (userLanguage) {
                    UserLanguageType.RUSSIAN -> StringResources.get("ru_data_message")
                    UserLanguageType.ENGLISH -> StringResources.get("en_data_message")
                    UserLanguageType.NONE -> ""
                }
            )

            // Ожидаем ввод даты пользователем
            waitForDateInput(userId, userLanguage)
        }
    }

    private fun Dispatcher.waitForDateInput(userId: Long, userLanguage: UserLanguageType) {
        text {
            if (message.from?.id == userId) {
                val dateInput = message.text
                botUseCase.updateUserBirthday(userId, dateInput.orEmpty())

                bot.sendMessage(
                    chatId = ChatId.fromId(userId),
                    text = when (userLanguage) {
                        UserLanguageType.RUSSIAN -> StringResources.get("ru_target_message")
                        UserLanguageType.ENGLISH -> StringResources.get("en_target_message")
                        UserLanguageType.NONE -> ""
                    }
                )
            }
            println("${botUseCase.getUser(userId)}")
        }

    }

    /**
     * Основное меню внизу
     */
    private fun Dispatcher.sendMainMenu() {
        callbackQuery {
            val keyboard = KeyboardReplyMarkup(
                keyboard = listOf(
                    listOf(KeyboardButton(BOTTOM_MENU_GET_CARD)),
                    listOf(KeyboardButton(BOTTOM_MENU_PROFILE)),
                    listOf(KeyboardButton(BOTTOM_MENU_SETTINGS))
                ),
                resizeKeyboard = true
            )

            bot.sendMessage(
                chatId = chatId,
                text = "Выберите действие:",
                replyMarkup = keyboard
            )
        }
    }

    private fun Dispatcher.setupCommands() {
        command("start") {
            _chatId = ChatId.fromId(message.chat.id)

            sendMainMenu()

            val inlineKeyboardMarkup = InlineKeyboardMarkup.create(
                listOf(
                    InlineKeyboardButton.CallbackData(
                        text = "Русский \uD83C\uDDF7\uD83C\uDDFA",
                        callbackData = BTN_RU
                    ),
                    InlineKeyboardButton.CallbackData(
                        text = "English \uD83C\uDDEC\uD83C\uDDE7",
                        callbackData = BTN_EN
                    )
                )
            )

            bot.sendMessage(
                chatId = chatId,
                text = StringResources.get("ru_welcome_message"),
                replyMarkup = inlineKeyboardMarkup
            )

            text(BOTTOM_MENU_GET_CARD) {
                bot.sendMessage(
                    chatId = ChatId.fromId(message.chat.id),
                    text = "Получаем новую карту"
                )
            }

            val profileText = """
        📌 АНКЕТА ПОЛЬЗОВАТЕЛЯ 📌

        ├─ 📝 Имя: ${botUseCase.getUser(message.from?.id).userName}
        ├─ 📅 Дата рождения: ${botUseCase.getUser(message.from?.id).date}
        ├─ ⚤  Пол: ${botUseCase.getUser(message.from?.id).gender}
        └─ 🌍 Язык: ${botUseCase.getUser(message.from?.id).language}
            """.trimIndent()

            text(BOTTOM_MENU_PROFILE) {
                bot.sendMessage(
                    chatId = ChatId.fromId(message.chat.id),
                    text = profileText
                )
            }

            text(BOTTOM_MENU_SETTINGS) {
                bot.sendMessage(
                    chatId = ChatId.fromId(message.chat.id),
                    text = "Настройки в разработке"
                )
            }

            try {
                botUseCase.insertUser(
                    User(
                        id = message.from?.id,
                        userName = message.from?.firstName
                            ?: message.from?.lastName
                            ?: message.from?.username.orEmpty(),
                    )
                )
                println("User inserted successfully!")
            } catch (e: Exception) {
                println("Error inserting user: ${e.message}")
                e.printStackTrace()
            }

            println("Test")

            try {
                println("${message.from?.id}")
                botUseCase.getUser(message.from?.id)
                println(botUseCase.getUser(405315886))
        } catch (e: Exception) {
            println("Ошибка в получении пользователя: ${e.message}")
            e.printStackTrace()
        }
        }
    }

    private companion object {
        const val BTN_RU = "btn_ru"
        const val BTN_EN = "btn_en"

        const val BTN_MAN = "btn_man"
        const val BTN_WOMAN = "btn_woman"

        const val BTN_SKIP = "btn_skip"

        //Нижнее меню
        const val BOTTOM_MENU_GET_CARD = "\uD83D\uDD0D Получить новую карту"
        const val BOTTOM_MENU_PROFILE = "\uD83D\uDC64 Мой профиль"
        const val BOTTOM_MENU_SETTINGS = "⚙ Настройки"
    }
}