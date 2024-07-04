package com.example.livesaver.di

import android.app.Application
import com.example.livesaver.app.domain.LocalUserManager
import com.example.livesaver.app.usecases.AppModeUsecases
import com.example.livesaver.app.usecases.ChangeAppMode
import com.example.livesaver.app.usecases.ReadAppMode
import com.example.livesaver.home.permissionsUsecases.PermissionsUsecases
import com.example.livesaver.home.permissionsUsecases.ReadWhatsAppBusinessPermission
import com.example.livesaver.home.permissionsUsecases.ReadWhatsAppPermission
import com.example.livesaver.home.permissionsUsecases.WhatsAppBusinessPermissionGranted
import com.example.livesaver.home.permissionsUsecases.WhatsAppPermissionGranted
import com.example.livesaver.onboarding.usescases.appentry.AppEntryUsecases
import com.example.livesaver.onboarding.usescases.appentry.ReadAppEntry
import com.example.livesaver.onboarding.usescases.appentry.SaveAppEntry
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
    @Provides
    @Singleton
    fun provideAppModeUsecases(
        localUserManager: LocalUserManager
    ):AppModeUsecases{
        return AppModeUsecases(
            readAppMode = ReadAppMode(localUserManager),
            changeAppMode = ChangeAppMode(localUserManager)
        )
    }
    @Provides
    @Singleton
    fun providePermissionUsecases(
        localUserManager: LocalUserManager
    ):PermissionsUsecases{
        return PermissionsUsecases(
            whatsAppPermissionGranted = WhatsAppPermissionGranted(localUserManager),
            whatsAppBusinessPermissionGranted = WhatsAppBusinessPermissionGranted(localUserManager),
            readWhatappPermission = ReadWhatsAppPermission(localUserManager),
            readWhatappBusinessPermission = ReadWhatsAppBusinessPermission(localUserManager)
        )
    }
}