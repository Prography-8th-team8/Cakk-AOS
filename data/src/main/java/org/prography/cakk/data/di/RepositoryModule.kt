package org.prography.cakk.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.prography.cakk.data.repository.DistrictRepositoryImpl
import org.prography.cakk.data.repository.StoreRepositoryImpl
import org.prography.domain.repository.DistrictRepository
import org.prography.domain.repository.StoreRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindStoreRepository(
        storeRepositoryImpl: StoreRepositoryImpl,
    ): StoreRepository

    @Binds
    @Singleton
    fun bindDistrictRepository(
        districtRepositoryImpl: DistrictRepositoryImpl,
    ): DistrictRepository
}
