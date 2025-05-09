package com.faithForward.di

import com.faithForward.network.ApiServiceInterface
import com.faithForward.repository.NetworkRepository
import com.faithForward.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesRetrofitInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Provides
    @Singleton
    fun providesApiServiceInterface(retrofit: Retrofit): ApiServiceInterface {
        return retrofit.create(ApiServiceInterface::class.java)
    }


    @Provides
    @Singleton
    fun provideNetworkRepository(apiServiceInterface: ApiServiceInterface): NetworkRepository {
        return NetworkRepository(apiServiceInterface)
    }

}