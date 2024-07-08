package com.example.livesaver.di

import android.app.Activity
import android.app.Application
import android.content.Context
import com.example.livesaver.app.domain.LocalUserManager
import com.example.livesaver.app.usecases.AppModeUsecases
import com.example.livesaver.app.usecases.ChangeAppMode
import com.example.livesaver.app.usecases.ReadAppMode
import com.example.livesaver.home.data.Repository
import com.example.livesaver.home.usecases.folderUri.FolderUriUsecases
import com.example.livesaver.home.usecases.folderUri.SaveWhatsappBusinessUri
import com.example.livesaver.home.usecases.folderUri.SaveWhatsappFolderUri
import com.example.livesaver.home.usecases.permissions.PermissionsUsecases
import com.example.livesaver.home.usecases.permissions.ReadWhatsAppBusinessPermission
import com.example.livesaver.home.usecases.permissions.ReadWhatsAppPermission
import com.example.livesaver.home.usecases.permissions.WhatsAppBusinessPermissionGranted
import com.example.livesaver.home.usecases.permissions.WhatsAppPermissionGranted
import com.example.livesaver.onboarding.usescases.appentry.AppEntryUsecases
import com.example.livesaver.onboarding.usescases.appentry.ReadAppEntry
import com.example.livesaver.onboarding.usescases.appentry.SaveAppEntry
import com.loc.newsapp.data.manger.LocalUserMangerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }
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
    ): PermissionsUsecases {
        return PermissionsUsecases(
            whatsAppPermissionGranted = WhatsAppPermissionGranted(localUserManager),
            whatsAppBusinessPermissionGranted = WhatsAppBusinessPermissionGranted(localUserManager),
            readWhatappPermission = ReadWhatsAppPermission(localUserManager),
            readWhatappBusinessPermission = ReadWhatsAppBusinessPermission(localUserManager)
        )
    }
    @Provides
    @Singleton
    fun provideFolderUriUseCases(
        localUserManager: LocalUserManager
    ):FolderUriUsecases{
        return FolderUriUsecases(
            whatsappFolderUri = SaveWhatsappFolderUri(localUserManager),
            whatsappBusinessUri = SaveWhatsappBusinessUri(localUserManager)
        )
    }
    @Provides
    @Singleton
    fun provideRepo(
        context: Context,
        localUserManager: LocalUserManager,
    ):Repository{
        return Repository(
            context =context,
            localUserManager = localUserManager
        )
    }
}