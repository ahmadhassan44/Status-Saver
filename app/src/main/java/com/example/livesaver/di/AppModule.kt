package com.example.livesaver.di

import android.app.Application
import com.example.livesaver.onboarding.domain.LocalUserManager
import com.example.livesaver.onboarding.usescase.appentry.AppEntryUsecases
import com.example.livesaver.onboarding.usescase.appentry.ReadAppEntry
import com.example.livesaver.onboarding.usescase.appentry.SaveAppEntry
import com.loc.newsapp.data.manger.LocalUserMangerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideLocalUserManager(
        application: Application
    ): LocalUserManager {
        return LocalUserMangerImpl(application)
    }
    @Provides
    @Singleton
    fun provideAppEntryUsecases(
        localUserManager: LocalUserManager
    ): AppEntryUsecases {
        return AppEntryUsecases(
            readAppEntry = ReadAppEntry(localUserManager),
            saveAppEntry = SaveAppEntry(localUserManager)
        )
    }
}