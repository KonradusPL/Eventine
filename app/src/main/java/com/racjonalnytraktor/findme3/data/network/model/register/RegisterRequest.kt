package com.racjonalnytraktor.findme3.data.network.model.register

data class RegisterRequest(val email: String, val fullName: String, val password: String){
    override fun toString(): String{
        return "email: $email, name: $fullName, password: $password"
    }
}