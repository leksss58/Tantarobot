package domain.repository

import data.api.ServiceApi
import javax.inject.Inject

interface ServiceRepository  {

    suspend fun fetchData()
}