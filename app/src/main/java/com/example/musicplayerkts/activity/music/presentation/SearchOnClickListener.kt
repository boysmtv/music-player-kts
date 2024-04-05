package com.example.musicplayerkts.activity.music.presentation

import com.example.musicplayerkts.activity.music.model.MusicResultModel
import com.example.musicplayerkts.databinding.ActivitySearchListItemBinding

interface SearchOnClickListener<T> {
    fun onItemClick(itemBinding: ActivitySearchListItemBinding, position: Int, model: MusicResultModel)
}