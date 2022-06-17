package de.app.core.db

import de.app.core.success
import de.app.data.model.entities.UserDao
import de.app.data.model.User
import de.app.data.model.UserHeader
import de.app.data.model.Address
import de.app.data.model.UserType
import de.app.data.model.entities.UserEntity
import de.app.data.model.entities.UserCredentials
import de.app.data.model.entities.CurrentUser

class UserDataSource(
    private val userDao: UserDao,
) {

    suspend fun getUsers(): List<UserHeader> {
        return userDao.getAllUsers().map {deserializeUser(it)}
    }

    suspend fun getCurrentUser(): Result<User>{
        val currentUser = userDao.getCurrentUser()
        if (currentUser.isEmpty()) return Result.failure(IllegalStateException("No logged in user"))
        val userId = currentUser.first().userId

        return getUserById(userId)
    }

    suspend fun setCurrentUser(userId: String) {
        val currentUser = userDao.getCurrentUser()
        val newCurrentUser = CurrentUser(userId)
        if (currentUser.isNotEmpty()){
            userDao.setCurrentUser(currentUser.first(), newCurrentUser)
        }else {
            userDao.insertCurrentUser(newCurrentUser)
        }
    }

    suspend fun unsetCurrent(){
        val currentUser = userDao.getCurrentUser()
        if (currentUser.isNotEmpty()){
            userDao.deleteCurrentUser(currentUser.first())
        }
    }

    suspend fun remove(userId: String): Result<Unit> {
        return getUserEntityById(userId).map {
            val credentials = userDao.getCredentialsById(userId) !!
            val currentUser = userDao.getCurrentUser()
            if (currentUser.isNotEmpty() && currentUser.first().userId == userId) {
                userDao.deleteAllInfo(it, credentials, currentUser.first())
            }else{
                userDao.deleteUserWithCredentials(it, credentials)
            }
        }
    }

    suspend fun login(userId: String, pin: String): Result<User> {
        val credentials =
            userDao.getCredentialsById(userId) ?: return userNotFoundError(userId)

        if (credentials.pin == pin){
            return getUserById(userId)
        }
        return Result.failure(IllegalStateException("Invalid pin"))
    }

    suspend fun add(user: User, pin: String) {
        userDao.insertUserWithCredentials(
            serializeUser(user),
            UserCredentials(user.userId, pin)
        )
    }

    private suspend fun getUserEntityById(userId: String): Result<UserEntity> {
        return userDao.getUserById(userId)?.success() ?: userNotFoundError(userId)
    }

    suspend fun getUserById(userId: String): Result<User> {
        return getUserEntityById(userId).map { deserializeUser(it) }
    }

    private fun <T> userNotFoundError(userId: String): Result<T> =
        Result.failure(IllegalArgumentException("User with id $userId not found"))

    private fun serializeUser(user: User): UserEntity {
        return UserEntity(
            user.userId,
            user.displayName,
            user.accountSecretToken,
            user.address.city,
            user.address.country,
            user.address.postalCode,
            user.type.name
        )
    }

    private fun deserializeUser(user: UserEntity): User {
        return User(
            user.userId,
            user.displayName,
            user.accountSecretToken,
            Address(user.city, user.country, user.postalCode),
            UserType.valueOf(user.type)
        )
    }
}