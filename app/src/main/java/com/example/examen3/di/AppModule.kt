package com.example.examen3.di

import android.content.Context
import android.content.SharedPreferences
import com.example.examen3.data.local.preferences.PreferencesConstants
import com.example.examen3.data.local.preferences.SudokuPreferences
import com.example.examen3.data.mapper.SudokuMapper
import com.example.examen3.data.remote.api.SudokuApi
import com.example.examen3.data.repository.SudokuRepositoryImpl
import com.example.examen3.domain.repository.SudokuRepository
import com.example.examen3.domain.usecase.GenerateSudokuUseCase
import com.example.examen3.domain.usecase.LoadSudokuProgressUseCase
import com.example.examen3.domain.usecase.SaveSudokuProgressUseCase
import com.example.examen3.domain.usecase.VerifySudokuUseCase
import com.example.examen3.domain.usecase.ResetGameUseCase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            PreferencesConstants.PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun provideSudokuPreferences(
        @ApplicationContext context: Context,
        gson: Gson
    ): SudokuPreferences {
        return SudokuPreferences(context, gson)
    }

    @Provides
    @Singleton
    fun provideSudokuMapper(): SudokuMapper {
        return SudokuMapper()
    }

    @Provides
    @Singleton
    fun provideResetGameUseCase(repository: SudokuRepository): ResetGameUseCase {
        return ResetGameUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideApiKeyInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-Api-Key", "t0dtg+5aoaF866qafi9HvA==CRiQG3bvFTIGJbsP")
                .build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(apiKeyInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideSudokuApi(okHttpClient: OkHttpClient): SudokuApi {
        return Retrofit.Builder()
            // --- CAMBIO AQUÍ --- Añade v1/ a la URL base
            .baseUrl("https://api.api-ninjas.com/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SudokuApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSudokuRepository(
        api: SudokuApi,
        sudokuPreferences: SudokuPreferences,
        sudokuMapper: SudokuMapper
    ): SudokuRepository {
        return SudokuRepositoryImpl(api, sudokuPreferences, sudokuMapper)
    }

    @Provides
    @Singleton
    fun provideGenerateSudokuUseCase(repository: SudokuRepository): GenerateSudokuUseCase {
        return GenerateSudokuUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSaveSudokuProgressUseCase(repository: SudokuRepository): SaveSudokuProgressUseCase {
        return SaveSudokuProgressUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLoadSudokuProgressUseCase(repository: SudokuRepository): LoadSudokuProgressUseCase {
        return LoadSudokuProgressUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideVerifySudokuUseCase(): VerifySudokuUseCase {
        return VerifySudokuUseCase()
    }
}