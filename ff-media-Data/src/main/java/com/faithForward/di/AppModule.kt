package com.faithForward.di

import android.content.Context
import com.faithForward.network.ApiServiceInterface
import com.faithForward.preferences.UserPreferences
import com.faithForward.repository.NetworkRepository
import com.faithForward.util.Constants
import com.faithForward.util.LenientGsonTypeAdapterFactory
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providesRetrofitInstance(
        okHttpClient: OkHttpClient,
    ): Retrofit {

        val gson = GsonBuilder()
            .registerTypeAdapterFactory(LenientGsonTypeAdapterFactory())
            .create()

        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }


    @Provides
    @Singleton
    fun providesApiServiceInterface(retrofit: Retrofit): ApiServiceInterface {
        return retrofit.create(ApiServiceInterface::class.java)
    }


    @Provides
    @Singleton
    fun provideNetworkRepository(
        apiServiceInterface: ApiServiceInterface,
        userPreferences: UserPreferences,
    ): NetworkRepository {
        return NetworkRepository(
            userPreferences = userPreferences,
            apiServiceInterface = apiServiceInterface
        )
    }

    @Provides
    @Singleton
    fun provideUserPreferences(
        @ApplicationContext context: Context,
    ): UserPreferences = UserPreferences(context)

}