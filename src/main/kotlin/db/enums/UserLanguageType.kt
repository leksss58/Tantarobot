package db.enums

enum class UserLanguageType(val value: String) {
    RUSSIAN("???????"),
    ENGLISH("English"),
    NONE("?? ???????");

    companion object {
        fun value(findValue: String): UserLanguageType = entries.first { it.value == findValue }
    }
}