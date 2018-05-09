package com.user.exokotlin

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter

class VideoPlayerActivity : AppCompatActivity() {
    private lateinit var player: SimpleExoPlayer
    private var shouldAutoPlay: Boolean = false
    private val uri = Uri.parse("http://54.255.155.24:1935//Live/_definst_/amlst:sweetbcha1novD235L240P/playlist.m3u8")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)
        shouldAutoPlay = true
    }

    private fun initPlayer() {
        val simpleExoPlayerView = findViewById<SimpleExoPlayerView>(R.id.player_view)
        val bandwidthMeter = DefaultBandwidthMeter()
        val trackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(trackSelectionFactory)
        val dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "exoplayer2example"), bandwidthMeter)
        val extractorsFactory = DefaultExtractorsFactory()
        val videoSource = HlsMediaSource(uri, dataSourceFactory, 1, null, null)
        val loopingSource =  LoopingMediaSource(videoSource)

        simpleExoPlayerView.requestFocus()
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)
        player.prepare(loopingSource)
        player.playWhenReady = shouldAutoPlay;
        simpleExoPlayerView.player = player
    }

    private fun releasePlayer() {
        player.release()
        shouldAutoPlay = player.playWhenReady
    }

    override fun onStart() {
        super.onStart()
        initPlayer()
    }

    override fun onResume() {
        super.onResume()
        initPlayer()

    }

    override fun onPause() {
        super.onPause()
        releasePlayer()

    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }
}
