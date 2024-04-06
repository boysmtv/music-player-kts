package com.example.musicplayerkts.activity.music.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayerkts.activity.music.model.MusicResultModel
import com.example.musicplayerkts.activity.music.presentation.SearchActivity
import com.example.musicplayerkts.activity.music.presentation.SearchOnClickListener
import com.example.musicplayerkts.databinding.ActivitySearchListItemBinding
import com.example.musicplayerkts.helper.InterfaceDialog

class SongAdapter : RecyclerView.Adapter<SongAdapter.AddressHolder>() {

    private var listModel = mutableListOf<MusicResultModel>()
    private lateinit var listener: SearchOnClickListener<MusicResultModel>
    private lateinit var MusicResultModel: MusicResultModel

    @SuppressLint("NotifyDataSetChanged")
    fun provided(
        model: List<MusicResultModel>,
        musicResultModel: MusicResultModel,
        context: SearchActivity,
        interfaceDialog: InterfaceDialog,
    ) {
        this.listModel = model.toMutableList()
        this.listener = context
        this.MusicResultModel = musicResultModel
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongAdapter.AddressHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = ActivitySearchListItemBinding.inflate(inflater, parent, false)
        return AddressHolder(binding)
    }

    override fun getItemCount(): Int {
        return this.listModel.size
    }

    override fun onBindViewHolder(holder: SongAdapter.AddressHolder, position: Int) {
        val model = this.listModel[position]
        holder.bind(position, model, listener)
    }

    inner class AddressHolder(binding: ActivitySearchListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val binding: ActivitySearchListItemBinding

        fun bind(
            position: Int,
            model: MusicResultModel,
            listener: SearchOnClickListener<MusicResultModel>
        ) {
            Glide.with(binding.root).load(model.artworkUrl100).into(binding.icPhoto)

            binding.tvSongName.text = model.trackName
            binding.tvArtist.text = model.artistName
            binding.tvAlbum.text = model.collectionName

            if (MusicResultModel.previewUrl != null) {
                if (!MusicResultModel.previewUrl.equals(model.previewUrl)) {
                    binding.vmMusic.visibility = View.GONE
                } else {
                    binding.vmMusic.visibility = View.VISIBLE
                }
            }

            binding.layoutContent.setOnClickListener {
                listener.onItemClick(
                    binding,
                    position,
                    model
                )
            }
        }

        init {
            this.binding = binding
        }
    }
}