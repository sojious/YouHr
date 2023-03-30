package co.youverify.youhr.di

import co.youverify.youhr.data.repository.auth.AuthRemoteDataSourceImpl
import dagger.Binds

object DataModule {

    @Binds
    fun bindAuthRemoteDataSource(authRemoteDataSourceImpl: AuthRemoteDataSourceImpl){}
}