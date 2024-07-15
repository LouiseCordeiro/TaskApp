package com.example.todoapp.domain.usecases

import android.location.Location
import com.example.todoapp.data.LocationHelper
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class GetLocationUseCase @Inject constructor(
    private val locationHelper: LocationHelper
) : IGetLocationUseCase{
    override fun invoke(): Flow<Location?> = callbackFlow {
        locationHelper.getLastLocation { location ->
            trySend(location).isSuccess
        }
        awaitClose { }
    }
}