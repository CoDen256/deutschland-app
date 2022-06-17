package de.app.data.model.entities

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<UserEntity>

    @Query("SELECT * FROM user WHERE userId = :id")
    suspend fun getUserById(id: String): UserEntity?

    @Insert
    suspend fun insertUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("SELECT * FROM credentials WHERE userId = :id")
    suspend fun getCredentialsById(id: String): UserCredentials?

    @Insert
    suspend fun insertCredentials(info: UserCredentials)

    @Delete
    suspend fun deleteCredentials(info: UserCredentials)

    @Transaction
    suspend fun insertUserWithCredentials(user: UserEntity, info: UserCredentials){
        insertUser(user)
        insertCredentials(info)
    }

    @Transaction
    suspend fun deleteUserWithCredentials(user: UserEntity, info: UserCredentials){
        deleteCredentials(info)
        deleteUser(user)
    }

    @Transaction
    suspend fun deleteAllInfo(user: UserEntity, info: UserCredentials, currentUser: CurrentUser){
        deleteCredentials(info)
        deleteUser(user)
        deleteCurrentUser(currentUser)
    }

    @Query("SELECT * FROM current_user LIMIT 1")
    suspend fun getCurrentUser(): List<CurrentUser>

    @Insert
    suspend fun insertCurrentUser(user: CurrentUser)

    @Delete
    suspend fun deleteCurrentUser(user: CurrentUser)

    @Transaction
    suspend fun setCurrentUser(oldUser: CurrentUser, newUser: CurrentUser){
        deleteCurrentUser(oldUser)
        insertCurrentUser(newUser)
    }
}

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val userId: String,
    val accountSecretToken: String,
    val displayName: String,
    val city: String,
    val country: String,
    val postalCode: String,
    val type: String,
)

@Entity(
    tableName = "credentials",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["userId"],
        childColumns = ["userId"]
    )]
)
class UserCredentials(
    @PrimaryKey val userId: String,
    val pin: String,
)

@Entity(
    tableName = "current_user",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["userId"],
        childColumns = ["userId"]
    )]
)
data class CurrentUser (
    @PrimaryKey val userId: String
)