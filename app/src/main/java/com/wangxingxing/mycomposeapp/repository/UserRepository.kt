package com.wangxingxing.mycomposeapp.repository

import com.wangxingxing.mycomposeapp.model.User
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor() {
    
    private val fakeUsers = listOf(
        User(1, "John Doe", "john.doe@example.com"),
        User(2, "Jane Smith", "jane.smith@example.com"),
        User(3, "Bob Johnson", "bob.johnson@example.com"),
        User(4, "Alice Williams", "alice.williams@example.com"),
        User(5, "Charlie Brown", "charlie.brown@example.com"),
        User(6, "Diana Prince", "diana.prince@example.com"),
        User(7, "Edward Norton", "edward.norton@example.com"),
        User(8, "Fiona Apple", "fiona.apple@example.com")
    )
    
    suspend fun getUsers(): List<User> {
        delay(2000) // Simulate network delay
        return fakeUsers
    }
    
    suspend fun getUserById(id: Int): User? {
        delay(1000) // Simulate network delay
        return fakeUsers.find { it.id == id }
    }
    
    fun login(email: String, password: String): Boolean {
        // Simple fake login - accept any email/password combination
        return email.isNotBlank() && password.isNotBlank()
    }
    
    fun getCurrentUser(): User? {
        // In a real app, this would come from a session/token
        return fakeUsers.firstOrNull()
    }
}

