package di.modules

import dagger.Module
import dagger.Provides
import data.api.ServiceApi
import data.repository.ServiceApiImpl
import domain.repository.ServiceRepository
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideServiceRepository(serviceApi: ServiceApi): ServiceRepository = ServiceApiImpl(serviceApi)
}