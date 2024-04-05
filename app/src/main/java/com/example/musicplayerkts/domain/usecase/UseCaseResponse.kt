package com.example.musicplayerkts.domain.usecase

import com.example.musicplayerkts.domain.model.ApiError

interface UseCaseResponse<Type> {

    fun onSuccess(result: Type)

    fun onError(apiError: ApiError)

}

