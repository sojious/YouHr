package co.youverify.youhr.di


import co.youverify.youhr.data.local.DBTask
import co.youverify.youhr.data.mapper.DbToDomainLeaveListMapper
import co.youverify.youhr.data.mapper.DbToDomainLeaveMapper
import co.youverify.youhr.data.mapper.DbToDomainLeaveSummaryMapper
import co.youverify.youhr.data.mapper.DbToDomainProfileMapper
import co.youverify.youhr.data.mapper.DbToDomainTaskListMapper
import co.youverify.youhr.data.mapper.DbToDomainTaskMapper
import co.youverify.youhr.data.mapper.DtoToDbLeaveListMapper
import co.youverify.youhr.data.mapper.DtoToDbLeaveMapper
import co.youverify.youhr.data.mapper.DtoToDbLeaveSummaryMapper
import co.youverify.youhr.data.mapper.DtoToDbProfileMapper
import co.youverify.youhr.data.mapper.DtoToDbTaskListMapper
import co.youverify.youhr.data.mapper.DtoToDbTaskMapper
import co.youverify.youhr.data.model.ApiTask
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
    fun provideDtoToDbTaskListMapper(taskMapper:DtoToDbTaskMapper): DtoToDbTaskListMapper<ApiTask,DBTask> = DtoToDbTaskListMapper(taskMapper)

    @Provides
    @Singleton
    fun provideDbToDomainTaskListMapper(taskMapper: DbToDomainTaskMapper): DbToDomainTaskListMapper = DbToDomainTaskListMapper(taskMapper)

    @Provides
    @Singleton
    fun provideDtoToDbProfileMapper(): DtoToDbProfileMapper = DtoToDbProfileMapper()

    @Provides
    @Singleton
    fun provideDbToDomainProfileMapper(): DbToDomainProfileMapper = DbToDomainProfileMapper()


    @Provides
    @Singleton
    fun provideDtoToDbLeaveMapper(): DtoToDbLeaveMapper = DtoToDbLeaveMapper()

    @Provides
    @Singleton
    fun provideDbToDomainLeaveMapper(): DbToDomainLeaveMapper = DbToDomainLeaveMapper()

    @Provides
    @Singleton
    fun provideDtoToDbLeaveListMapper(mapper:DtoToDbLeaveMapper): DtoToDbLeaveListMapper = DtoToDbLeaveListMapper(mapper)

    @Provides
    @Singleton
    fun provideDbToDomainLeaveListMapper(mapper: DbToDomainLeaveMapper): DbToDomainLeaveListMapper = DbToDomainLeaveListMapper(mapper)

    @Provides
    @Singleton
    fun provideDtoToDbLeaveSummaryMapper(): DtoToDbLeaveSummaryMapper = DtoToDbLeaveSummaryMapper()

    @Provides
    @Singleton
    fun provideDbToDomainLeaveSummaryMapper(): DbToDomainLeaveSummaryMapper = DbToDomainLeaveSummaryMapper()



}