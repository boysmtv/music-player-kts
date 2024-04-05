package com.example.musicplayerkts.activity.music.presentation

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayerkts.R
import com.example.musicplayerkts.activity.music.adapter.SongAdapter
import com.example.musicplayerkts.activity.music.model.MusicReqModel
import com.example.musicplayerkts.activity.music.model.MusicResultModel
import com.example.musicplayerkts.activity.music.vm.MusicViewModel
import com.example.musicplayerkts.databinding.ActivitySearchBinding
import com.example.musicplayerkts.databinding.ActivitySearchListItemBinding
import com.example.musicplayerkts.helper.InterfaceDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class SearchActivity : AppCompatActivity(), SearchOnClickListener<MusicResultModel> {

    val musicVM by viewModel<MusicViewModel>()

    private lateinit var mediaPlayer: MediaPlayer

    private val TAG = this::class.java.simpleName
    private val thisContext = this@SearchActivity

    private lateinit var songAdapter: SongAdapter
    private lateinit var musicResultModel: MusicResultModel

    private lateinit var binding: ActivitySearchBinding
    private lateinit var interfaceDialog: InterfaceDialog

    private lateinit var listMusic: List<MusicResultModel>

    private var currPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)

        interfaceDialog = InterfaceDialog(thisContext)
        songAdapter = SongAdapter()
        mediaPlayer = MediaPlayer()
        musicResultModel = MusicResultModel()

        setContentView(binding.root)
        supportActionBar!!.hide()

        binding.etSearchName.setText("justin beiber")
        binding.rvSearch.requestFocus()

        setupViewModel(musicVM, binding, songAdapter)
        setRequest(musicVM, binding, songAdapter)

        setOnListener()
    }

    /**
     * set view model
     */
    private fun setupViewModel(
        VM: MusicViewModel,
        binding: ActivitySearchBinding,
        adapter: SongAdapter
    ) {
        with(VM) {
            onSuccess.observe(thisContext) {
                it?.let {
                    if (it.isEmpty()) {
                        binding.tvEmpty.visibility = VISIBLE
                    } else {
                        binding.tvEmpty.visibility = GONE
                    }

                    listMusic = it
                    adapter.provided(it, musicResultModel, thisContext, interfaceDialog)
                }
            }
            onError.observe(thisContext) {
                interfaceDialog.dismisDialogLoading()
                interfaceDialog.showDialogWarningConfirm("Please try again!", it, "OK!")
            }

            onProgress.observe(thisContext) { isVisible ->
                binding.progressBar.visibility = if (isVisible) VISIBLE else GONE
            }
        }
    }

    private fun setRequest(
        VM: MusicViewModel,
        binding: ActivitySearchBinding,
        adapter: SongAdapter
    ) {
        // set loading on ui
        binding.rvSearch.adapter = adapter
        VM.doIt(MusicReqModel(term = binding.etSearchName.text.toString()))
    }

    private fun setOnListener() {
        binding.ivMusicPlay.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                binding.ivMusicPlay.setImageBitmap(null)
                binding.ivMusicPlay.setImageResource(android.R.color.transparent)
                binding.ivMusicPlay.setBackgroundResource(R.drawable.ic_music_play)
                musicResultModel.isPlay = false
            } else {
                mediaPlayer.start()
                binding.ivMusicPlay.setImageBitmap(null)
                binding.ivMusicPlay.setImageResource(android.R.color.transparent)
                binding.ivMusicPlay.setBackgroundResource(R.drawable.ic_music_pause_2)
                musicResultModel.isPlay = true
            }
        }

        binding.ivMusicNext.setOnClickListener {
            binding.rvSearch.postDelayed({
                if (binding.rvSearch.findViewHolderForAdapterPosition(0) != null) {
                    binding.rvSearch.findViewHolderForAdapterPosition(currPosition + 1)!!.itemView.performClick()
                }
            }, 0)
        }

        binding.ivMusicPrevious.setOnClickListener {
            binding.rvSearch.postDelayed({
                if (binding.rvSearch.findViewHolderForAdapterPosition(0) != null) {
                    if (currPosition > 0)
                        binding.rvSearch.findViewHolderForAdapterPosition(currPosition - 1)!!.itemView.performClick()
                }
            }, 0)
        }

        binding.ivSearch.setOnClickListener {
            setRequest(musicVM, binding, songAdapter)
        }

        binding.etSearchName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    setRequest(musicVM, binding, songAdapter)
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemClick(
        itemBinding: ActivitySearchListItemBinding,
        position: Int,
        model: MusicResultModel
    ) {
        musicResultModel = model
        currPosition = position
        binding.tvPlaySong.text = model.trackName
        songAdapter.provided(listMusic, musicResultModel, thisContext, interfaceDialog)
        songAdapter.notifyDataSetChanged()

        if (musicResultModel.isPlay) {
            itemBinding.vmMusic.visibility = View.INVISIBLE
            musicResultModel.isPlay = false
            mediaPlayer.stop()
        } else {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            mediaPlayer = MediaPlayer()
            try {
                mediaPlayer.setDataSource(model.previewUrl)
                mediaPlayer.prepare()
                mediaPlayer.start()

                binding.sbProgress.max = mediaPlayer.duration
                binding.ivMusicPlay.setImageBitmap(null)
                binding.ivMusicPlay.setImageResource(android.R.color.transparent)
                binding.ivMusicPlay.setBackgroundResource(R.drawable.ic_music_pause_2)

                itemBinding.vmMusic.visibility = View.VISIBLE

                musicResultModel.isPlay = true
                StartSongTrack(itemBinding).start()
            } catch (ex: Exception) {
                Timber.tag("MySongAdapter").e("Error: " + ex.message)
            }
        }
    }

    inner class StartSongTrack(binding: ActivitySearchListItemBinding) : Thread() {

        private var itemBinding: ActivitySearchListItemBinding

        init {
            itemBinding = binding
        }

        override fun run() {
            var currentPosition = mediaPlayer.currentPosition
            val total = mediaPlayer.duration

            while (mediaPlayer.isPlaying && currentPosition < total) {
                currentPosition = try {
                    mediaPlayer.currentPosition
                } catch (e: InterruptedException) {
                    return
                } catch (e: java.lang.Exception) {
                    return
                }
                binding.sbProgress.progress = currentPosition

                if (!mediaPlayer.isPlaying && currentPosition >= total) {
                    runOnUiThread {
                        itemBinding.vmMusic.visibility = View.INVISIBLE
                        musicResultModel.isPlay = false
                    }
                }
            }
        }
    }
}