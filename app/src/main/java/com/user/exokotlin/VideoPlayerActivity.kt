package com.user.exokotlin

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.extractor.ts.TsExtractor.MODE_SINGLE_PMT
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.UdpDataSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.user.exokotlin.R


class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var player: SimpleExoPlayer
    private var shouldAutoPlay: Boolean = false
    private val uriString = "udp://@239.255.0.1:5004" // multicast streaming & IP & port

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)
        shouldAutoPlay = true

    }


    private fun initPlayer(){
        val simpleExoPlayerView = findViewById<SimpleExoPlayerView>(R.id.player_view)
        val bandwidthMeter = DefaultBandwidthMeter()
        val extractorsFactory = DefaultExtractorsFactory()
        val trackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(trackSelectionFactory)
        val udpDataSource = DefaultDataSourceFactory(this, Util.getUserAgent(this, "MainActivity"), bandwidthMeter)
        val mediaSource = ExtractorMediaSource(Uri.parse(uriString), udpDataSource, extractorsFactory, null, null)

        simpleExoPlayerView.requestFocus()


        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)

        simpleExoPlayerView.player = player
        player.playWhenReady = shouldAutoPlay;
        player.prepare(mediaSource)


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
