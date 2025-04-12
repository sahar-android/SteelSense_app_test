package ir.simyapp.steelsensetestapp.DI

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.simyapp.steelsensetestapp.data_layer.ChartRepository
import ir.simyapp.steelsensetestapp.data_layer.ChartRepositoryImpl
import ir.simyapp.steelsensetestapp.data_layer.DataManager
import javax.inject.Singleton

/**
 * @Author: Sahar simyari
 * @Date: 4/11/2025
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideChartRepository(
        dataManager: DataManager
    ): ChartRepository {
        return ChartRepositoryImpl(dataManager)
    }
}