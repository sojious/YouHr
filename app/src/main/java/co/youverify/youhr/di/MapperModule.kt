package co.youverify.youhr.di


import co.youverify.youhr.data.mapper.DbToDomainTaskListMapper
import co.youverify.youhr.data.mapper.DbToDomainTaskMapper
import co.youverify.youhr.data.mapper.DtoToDbTaskListMapper
import co.youverify.youhr.data.mapper.DtoToDbTaskMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {

    @Provides
    @Singleton
    fun provideDtoToDbTaskMapper(): DtoToDbTaskMapper = DtoToDbTaskMapper()

    @Provides
    @Singleton
    fun provideDbToDomainTaskMapper(): DbToDomainTaskMapper = DbToDomainTaskMapper()

    @Provides
    @Singleton
    fun provideDtoToDbTaskListMapper(taskMapper:DtoToDbTaskMapper): DtoToDbTaskListMapper = DtoToDbTaskListMapper(taskMapper)

    @Provides
    @Singleton
    fun provideDbToDomainTaskListMapper(taskMapper: DbToDomainTaskMapper): DbToDomainTaskListMapper = DbToDomainTaskListMapper(taskMapper)
}