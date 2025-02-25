package db.entity

import db.enums.UserGenderType
import db.enums.UserLanguageType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
data class User(
    @Id
    val id: Long?,

    @Column(name = "user_name")
    val userName: String?,

    @Column(name = "date")
    val date: String = "",

    @Column(name = "photo")
    val photo: String = "",

    @Column(name = "target")
    val target: String = "",

    @Column(name = "gender")
    val gender: UserGenderType = UserGenderType.NONE,

    @Column(name = "language")
    val language: UserLanguageType = UserLanguageType.NONE

) {
    constructor() : this(null, null, "", "", "", UserGenderType.NONE, UserLanguageType.NONE)
}
