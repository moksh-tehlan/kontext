package com.moksh.kontext.di

import com.moksh.kontext.data.repository.AuthRepositoryImpl
import com.moksh.kontext.data.repository.UserRepositoryImpl
import com.moksh.kontext.domain.repository.AuthRepository
import com.moksh.kontext.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}