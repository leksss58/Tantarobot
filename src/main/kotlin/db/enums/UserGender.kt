package db.enums

enum class UserGenderType(val value: String) {
    MAN("???????"),
    WOMAN("???????"),
    NONE("?? ???????");

    companion object {
        fun value(findValue: String): UserGenderType = entries.first { it.value == findValue }
    }
}