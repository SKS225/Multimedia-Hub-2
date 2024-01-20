package com.example.multimediahubviews

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.multimediahubviews.databinding.ActivityAudioPlayerBinding
import com.example.multimediahubviews.databinding.FragmentNowPlayingBinding


class NowPlaying : Fragment() {

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var binding: FragmentNowPlayingBinding
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_now_playing, container, false)
        binding = FragmentNowPlayingBinding.bind(view)
        binding.root.visibility = View.GONE
        binding.playPauseBtnNP.setOnClickListener {
            if (AudioPlayer.isPlaying) pauseMusic()
            else playMusic()
        }
        binding.nextBtnNP.setOnClickListener {
            setSongPosition(true)
            AudioPlayer.musicService!!.createMediaPlayer()

            Glide.with(this)
                .asBitmap()
                .load(AudioPlayer.musicListPA[AudioPlayer.songPosition].artUri)
                .apply(RequestOptions().placeholder(R.drawable.music).centerCrop())
                .into(binding.songImgNP)

            binding.songNameNP.text = AudioPlayer.musicListPA[AudioPlayer.songPosition].title
            AudioPlayer.musicService!!.showNotification(R.drawable.baseline_pause_24, 1F)
            playMusic()

        }

        binding.root.setOnClickListener{
            val intent = Intent(requireContext(),AudioPlayer::class.java)
            intent.putExtra("index",AudioPlayer.songPosition)
            intent.putExtra("class","NowPlaying")
            ContextCompat.startActivity(requireContext(), intent,null)
        }
        // Inflate the layout for this fragment
        return view
    }

    override fun onResume() {
        super.onResume()
        if (AudioPlayer.musicService != null){
            binding.root.visibility = View.VISIBLE
            binding.songNameNP.isSelected = true

            Glide.with(this)
                .asBitmap()
                .load(AudioPlayer.musicListPA[AudioPlayer.songPosition].artUri)
                .apply(RequestOptions().placeholder(R.drawable.music).centerCrop())
                .into(binding.songImgNP)

            binding.songNameNP.text = AudioPlayer.musicListPA[AudioPlayer.songPosition].title
            if (AudioPlayer.isPlaying) binding.playPauseBtnNP.setIconResource(R.drawable.baseline_pause_24)
            else binding.playPauseBtnNP.setIconResource(R.drawable.baseline_play_arrow_24)
        }
    }

    private fun playMusic(){
        AudioPlayer.musicService!!.mediaPlayer!!.start()
        binding.playPauseBtnNP.setIconResource(R.drawable.baseline_pause_24)
        AudioPlayer.musicService!!.showNotification(R.drawable.baseline_pause_24, 1F)
        AudioPlayer.binding.nextBtnPA.setIconResource(R.drawable.baseline_pause_24)
        AudioPlayer.isPlaying = true
    }
    private fun pauseMusic() {
        AudioPlayer.musicService!!.mediaPlayer!!.pause()
        binding.playPauseBtnNP.setIconResource(R.drawable.baseline_play_arrow_24)
        AudioPlayer.musicService!!.showNotification(R.drawable.baseline_play_arrow_24, 0F)
        AudioPlayer.binding.nextBtnPA.setIconResource(R.drawable.baseline_play_arrow_24)
        AudioPlayer.isPlaying = true
    }
}

