package org.prography.cakk.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.prography.cakk.data.repository.district.DistrictRepository
import org.prography.cakk.data.repository.district.DistrictRepositoryImpl
import org.prography.cakk.data.repository.store.StoreRepository
import org.prography.cakk.data.repository.store.StoreRepositoryImpl
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
