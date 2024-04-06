package com.example.musicplayerkts.activity.music.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayerkts.activity.music.model.MusicModel
import com.example.musicplayerkts.activity.music.model.MusicReqModel
import com.example.musicplayerkts.activity.music.model.MusicResultModel
import com.example.musicplayerkts.activity.music.usecase.MusicUC
import com.example.musicplayerkts.domain.model.ApiError
import com.example.musicplayerkts.domain.usecase.UseCaseResponse
import com.google.gson.Gson
import timber.log.Timber

class MusicViewModel(
    private val musicUC: MusicUC,
) : ViewModel() {
    private val TAG = this::class.java.simpleName

    val onSuccess = MutableLiveData<List<MusicResultModel>?>()
    val onError = MutableLiveData<String>()
    val onProgress = MutableLiveData<Boolean>()

    fun doIt(reqModel: MusicReqModel) {
        onProgress.value = true
        musicUC.invoke(
            viewModelScope, reqModel,
            object : UseCaseResponse<MusicModel> {
                override fun onSuccess(result: MusicModel) {
                    onSuccess.value = result.results
                    onProgress.value = false
                    Timber.tag(TAG).e("Model: ${Gson().toJson(result.results)}")
                }

                override fun onError(apiError: ApiError) {
                    onError.value = apiError.getErrorMessage()
                    onProgress.value = false
                }
            },
        )
    }

}