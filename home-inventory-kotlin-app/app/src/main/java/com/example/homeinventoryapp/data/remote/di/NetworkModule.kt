package com.example.homeinventoryapp.data.remote.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.homeinventoryapp.BuildConfig
import com.example.homeinventoryapp.data.model.furniture.FurnitureService
import com.example.homeinventoryapp.data.remote.services.CompartmentService
import com.example.homeinventoryapp.data.remote.services.HomeService
import com.example.homeinventoryapp.data.remote.services.InvitationService
import com.example.homeinventoryapp.data.remote.services.ItemService
import com.example.homeinventoryapp.data.remote.services.RoomService
import com.example.homeinventoryapp.data.remote.services.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_store")

    @Singleton
    @Provides
    fun provideHTTPLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }

    @Singleton
    @Provides
    fun provideHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory =
        GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideHomeService(retrofit: Retrofit): HomeService =
        retrofit.create(HomeService::class.java)

    @Singleton
    @Provides
    fun provideUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Singleton
    @Provides
    fun provideInvitationService(retrofit: Retrofit): InvitationService =
        retrofit.create(InvitationService::class.java)

    @Singleton
    @Provides
    fun provideRoomService(retrofit: Retrofit): RoomService =
        retrofit.create(RoomService::class.java)

    @Singleton
    @Provides
    fun provideFurnitureService(retrofit: Retrofit): FurnitureService =
        retrofit.create(FurnitureService::class.java)

    @Singleton
    @Provides
    fun provideCompartmentService(retrofit: Retrofit): CompartmentService =
        retrofit.create(CompartmentService::class.java)

    @Singleton
    @Provides
    fun provideItemService(retrofit: Retrofit): ItemService =
        retrofit.create(ItemService::class.java)


}