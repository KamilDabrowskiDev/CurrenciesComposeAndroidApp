package com.currencies.compose.app.common.di

import com.currencies.compose.app.common.async.DispatcherProvider
import com.currencies.compose.app.common.async.DispatcherProviderImpl
import com.currencies.compose.app.data.NBPApiRepository
import com.currencies.compose.app.data.NBPApiRepositoryRetrofitImpl
import com.currencies.compose.app.data.NBPApiRetrofitService
import com.currencies.compose.app.data.logging.SimpleLoggingInterceptor
import com.currencies.compose.app.data.validator.CurrencyCodeValidator
import com.currencies.compose.app.data.validator.CurrentExchangeRateApiEntityValidator
import com.currencies.compose.app.data.validator.CurrentExchangeRateTableApiEntityValidator
import com.currencies.compose.app.data.validator.EffectiveDateValidator
import com.currencies.compose.app.data.validator.HistoricalExchangeRateApiEntityValidator
import com.currencies.compose.app.data.validator.HistoricalExchangeRatesResponseApiEntityValidator
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

@Module
@InstallIn(SingletonComponent::class)
class AsyncModule {

    @Provides
    @Singleton
    fun dispatcherProvider(): DispatcherProvider {
        return DispatcherProviderImpl()
    }
}


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    private val NBP_API_BASE_URL = "https://api.nbp.pl/api/"

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Provides
    @Singleton
    fun simpleLoggingInterceptor(): SimpleLoggingInterceptor {
        return SimpleLoggingInterceptor()
    }

    @Provides
    @Singleton
    fun okHttpClient(loggingInterceptor: SimpleLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NBP_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitService(retrofit: Retrofit): NBPApiRetrofitService {
        return retrofit.create(NBPApiRetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun provideNBPApiRepository(retrofitService: NBPApiRetrofitService): NBPApiRepository {
        return NBPApiRepositoryRetrofitImpl(retrofitService)
    }
}

@Module
@InstallIn(SingletonComponent::class)
class ValidatorModule() {

    @Provides
    fun currencyCodeValidator(): CurrencyCodeValidator {
        return CurrencyCodeValidator()
    }

    @Provides
    fun effectiveDateValidator(): EffectiveDateValidator {
        return EffectiveDateValidator()
    }

    @Provides
    fun historicalExchangeRateApiEntityValidator(
        effectiveDateValidator: EffectiveDateValidator
    ): HistoricalExchangeRateApiEntityValidator {
        return HistoricalExchangeRateApiEntityValidator(effectiveDateValidator)
    }

    @Provides
    fun historicalExchangeRatesResponseApiEntityValidator(
        historicalExchangeRateApiEntityValidator: HistoricalExchangeRateApiEntityValidator,
        currencyCodeValidator: CurrencyCodeValidator
    ): HistoricalExchangeRatesResponseApiEntityValidator {
        return HistoricalExchangeRatesResponseApiEntityValidator(
            historicalExchangeRateApiEntityValidator = historicalExchangeRateApiEntityValidator,
            currencyCodeValidator = currencyCodeValidator
        )
    }

    @Provides
    fun currentExchangeRateApiEntityValidator(
        currencyCodeValidator: CurrencyCodeValidator
    ): CurrentExchangeRateApiEntityValidator {
        return CurrentExchangeRateApiEntityValidator(currencyCodeValidator)
    }

    @Provides
    fun currentExchangeRateTableApiEntityValidator(
        effectiveDateValidator: EffectiveDateValidator,
        entityValidator: CurrentExchangeRateApiEntityValidator
    ): CurrentExchangeRateTableApiEntityValidator {
        return CurrentExchangeRateTableApiEntityValidator(
            effectiveDateValidator = effectiveDateValidator,
            entityValidator = entityValidator
        )
    }
}
