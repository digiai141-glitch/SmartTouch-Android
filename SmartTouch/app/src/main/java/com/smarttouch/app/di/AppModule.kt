package com.smarttouch.app.di

import com.smarttouch.app.data.repository.GestureRepository
import com.smarttouch.app.data.repository.GestureRepositoryImpl
import com.smarttouch.app.data.repository.SettingsRepository
import com.smarttouch.app.data.repository.SettingsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindGestureRepository(
        impl: GestureRepositoryImpl,
    ): GestureRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl,
    ): SettingsRepository
}
