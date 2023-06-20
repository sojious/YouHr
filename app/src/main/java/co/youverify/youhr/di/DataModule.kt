package co.youverify.youhr.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import androidx.room.RoomDatabase
import co.youverify.youhr.data.AuthRepositoryImpl
import co.youverify.youhr.data.LeaveRepositoryImpl
import co.youverify.youhr.data.PreferencesRepositoryImpl
import co.youverify.youhr.data.ProfileRepositoryImpl
import co.youverify.youhr.data.TaskRepositoryImpl
import co.youverify.youhr.data.local.YouHrDatabase
import co.youverify.youhr.data.repository.auth.AuthRemoteDataSource
import co.youverify.youhr.data.repository.auth.AuthRemoteDataSourceImpl
import co.youverify.youhr.data.repository.leave.LeaveLocalDataSource
import co.youverify.youhr.data.repository.leave.LeaveLocalDataSourceImpl
import co.youverify.youhr.data.repository.leave.LeaveRemoteDataSource
import co.youverify.youhr.data.repository.leave.LeaveRemoteDatasourceImpl
import co.youverify.youhr.data.repository.profile.ProfileLocalDataSource
import co.youverify.youhr.data.repository.profile.ProfileLocalDataSourceImpl
import co.youverify.youhr.data.repository.profile.ProfileRemoteDataSource
import co.youverify.youhr.data.repository.profile.ProfileRemoteDatasourceImpl
import co.youverify.youhr.data.repository.task.TaskLocalDataSource
import co.youverify.youhr.data.repository.task.TaskLocalDataSourceImpl
import co.youverify.youhr.data.repository.task.TaskRemoteDataSource
import co.youverify.youhr.data.repository.task.TaskRemoteDataSourceImpl
import co.youverify.youhr.domain.repository.AuthRepository
import co.youverify.youhr.domain.repository.LeaveRepository
import co.youverify.youhr.domain.repository.PreferencesRepository
import co.youverify.youhr.domain.repository.ProfileRepository
import co.youverify.youhr.domain.repository.TaskRepository
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

        @Binds
        @Singleton
        abstract fun bindTaskLocalDataSource(taskLocalDataSourceImpl: TaskLocalDataSourceImpl):TaskLocalDataSource

        @Binds
        @Singleton
        abstract fun bindTaskRemoteDataSource(taskRemoteDataSourceImpl: TaskRemoteDataSourceImpl):TaskRemoteDataSource

        @Binds
        @Singleton
        abstract fun bindTaskRepository(taskRepositoryImpl: TaskRepositoryImpl):TaskRepository


        @Binds
        @Singleton
        abstract fun bindProfileLocalDataSource(profileLocalDataSourceImpl: ProfileLocalDataSourceImpl):ProfileLocalDataSource

        @Binds
        @Singleton
        abstract fun bindProfileRemoteDataSource(profileRemoteDataSourceImpl: ProfileRemoteDatasourceImpl):ProfileRemoteDataSource

        @Binds
        @Singleton
        abstract fun bindProfileRepository(profileRepositoryImpl: ProfileRepositoryImpl):ProfileRepository

        @Binds
        @Singleton
        abstract fun bindLeaveRepository(leaveRepositoryImpl: LeaveRepositoryImpl):LeaveRepository
        @Binds
        @Singleton
        abstract fun bindLeaveLocalDataSource(leaveLocalDataSourceImpl: LeaveLocalDataSourceImpl):LeaveLocalDataSource

        @Binds
        @Singleton
        abstract fun bindLeaveRemoteDataSource(leaveRemoteDataSourceImpl: LeaveRemoteDatasourceImpl):LeaveRemoteDataSource
    }


    @Provides
    @Singleton
    fun providePreferencesDataStore( @ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(produceFile = {
            context.preferencesDataStoreFile("app_pref")
        })
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context):YouHrDatabase =
        Room.databaseBuilder(context,YouHrDatabase::class.java,"youHr_db")
            .fallbackToDestructiveMigration()
            .build()
}