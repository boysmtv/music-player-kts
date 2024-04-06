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
import io.gresse.hugo.vumeterlibrary.VuMeterView
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class SearchActivity : AppCompatActivity(), SearchOnClickListener<MusicResultModel> {

    private val TAG = this::class.java.simpleName

    val musicVM by viewModel<MusicViewModel>()

    private lateinit var mediaPlayer: MediaPlayer

    private val thisContext = this@SearchActivity

    private lateinit var songAdapter: SongAdapter
    private lateinit var musicResultModel: MusicResultModel

    private lateinit var binding: ActivitySearchBinding
    private lateinit var interfaceDialog: InterfaceDialog

    private lateinit var listMusic: List<MusicResultModel>

    private var currPosition = 0
    private lateinit var musicVuMeterView: VuMeterView

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
                        binding.tvEmptySearch.visibility = VISIBLE
                    } else {
                        binding.tvEmpty.visibility = GONE
                        binding.tvEmptySearch.visibility = GONE
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

    private fun setOnListener() = with(binding) {
        ivMusicPlay.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                musicResultModel.isPlay = false
                setupStartOrStop(false)
            } else {
                mediaPlayer.start()
                musicResultModel.isPlay = true
                setupStartOrStop(true)
                startMusicMeter()
            }
        }

        ivMusicNext.setOnClickListener {
            rvSearch.postDelayed({
                if (rvSearch.findViewHolderForAdapterPosition(0) != null) {
                    rvSearch.findViewHolderForAdapterPosition(currPosition + 1)!!.itemView.performClick()
                }
            }, 0)
        }

        ivMusicPrevious.setOnClickListener {
            rvSearch.postDelayed({
                if (rvSearch.findViewHolderForAdapterPosition(0) != null) {
                    if (currPosition > 0)
                        rvSearch.findViewHolderForAdapterPosition(currPosition - 1)!!.itemView.performClick()
                }
            }, 0)
        }

        ivSearch.setOnClickListener {
            setRequest(musicVM, binding, songAdapter)
        }

        ivCancel.setOnClickListener {
            etSearchName.setText("")
            setRequest(musicVM, binding, songAdapter)
        }

        etSearchName.addTextChangedListener(object : TextWatcher {
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
        musicVuMeterView = itemBinding.vmMusic
        musicResultModel = model
        currPosition = position
        binding.tvPlaySong.text = model.trackName
        songAdapter.provided(listMusic, musicResultModel, thisContext, interfaceDialog)
        songAdapter.notifyDataSetChanged()

        if (musicResultModel.isPlay) {
            musicVuMeterView.visibility = GONE
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

                musicVuMeterView.visibility = VISIBLE
                musicResultModel.isPlay = true

                startMusicMeter()
            } catch (ex: Exception) {
                Timber.tag("MySongAdapter").e("Error: " + ex.message)
            }
        }
    }

    private fun startMusicMeter() {
        Thread {
            var currentPosition = mediaPlayer.currentPosition
            val total = mediaPlayer.duration

            while (mediaPlayer.isPlaying && currentPosition < total) {
                currentPosition = try {
                    mediaPlayer.currentPosition
                } catch (e: InterruptedException) {
                    return@Thread
                } catch (e: java.lang.Exception) {
                    return@Thread
                }
                binding.sbProgress.progress = currentPosition

                if (currentPosition == total) {
                    stopMusic()
                }
            }
        }.start()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun stopMusic() {
        runOnUiThread {
            musicVuMeterView.visibility = GONE
            songAdapter.notifyItemChanged(currPosition)
        }
        musicResultModel.isPlay = false
        mediaPlayer.stop()
        setupStartOrStop(false)
    }

    private fun setupStartOrStop(isStart: Boolean) = with(binding) {
        if (isStart) {
            ivMusicPlay.setImageBitmap(null)
            ivMusicPlay.setImageResource(android.R.color.transparent)
            ivMusicPlay.setBackgroundResource(R.drawable.ic_music_pause_2)
        } else {
            ivMusicPlay.setImageBitmap(null)
            ivMusicPlay.setImageResource(android.R.color.transparent)
            ivMusicPlay.setBackgroundResource(R.drawable.ic_music_play)
        }
    }
}