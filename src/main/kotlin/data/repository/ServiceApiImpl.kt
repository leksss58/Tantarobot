package data.repository

import data.api.ServiceApi
import domain.repository.ServiceRepository
import javax.inject.Inject

class ServiceApiImpl @Inject constructor(
    private val serApi: ServiceApi
): ServiceRepository {

    override suspend fun fetchData() {
        serApi.fetchData()
    }
}