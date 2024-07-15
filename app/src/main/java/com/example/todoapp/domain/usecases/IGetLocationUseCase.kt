package com.example.todoapp.domain.usecases

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface IGetLocationUseCase {
    operator fun invoke(): Flow<Location?>
}