package co.youverify.youhr.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import co.youverify.youhr.data.AuthRepositoryImpl
import co.youverify.youhr.data.PreferencesRepositoryImpl
import co.youverify.youhr.data.repository.auth.AuthRemoteDataSource
import co.youverify.youhr.data.repository.auth.AuthRemoteDataSourceImpl
import co.youverify.youhr.domain.repository.AuthRepository
import co.youverify.youhr.domain.repository.PreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
 object DataModule {

    @Module
    @InstallIn(SingletonComponent::class)
    interface RepoDataModule{
        @Binds
        @Singleton
        abstract fun bindAuthRemoteDataSource(authRemoteDataSourceImpl: AuthRemoteDataSourceImpl):AuthRemoteDataSource

        @Binds
        @Singleton
        abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl):AuthRepository

        @Binds
        @Singleton
        abstract fun bindPreferencesRepository(preferencesRepositoryImpl: PreferencesRepositoryImpl):PreferencesRepository
    }


    @Provides
    @Singleton
    fun providePreferencesDataStore( @ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(produceFile = {
            context.preferencesDataStoreFile("app_pref")
        })
}