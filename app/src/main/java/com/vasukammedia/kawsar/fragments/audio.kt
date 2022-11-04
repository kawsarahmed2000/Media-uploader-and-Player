package com.vasukammedia.kawsar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.core.View.DocumentChanges
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import com.vasukammedia.kawsar.R
import com.vasukammedia.kawsar.adapter.adapterAudio
import com.vasukammedia.kawsar.dataModel.dataAudio

class audio : Fragment(), Player.Listener {

    private lateinit var audioArrayList: ArrayList<dataAudio>
    private lateinit var audioAdapter: adapterAudio
    private lateinit var audioRecyclerView: RecyclerView

    lateinit var next: ImageView
    lateinit var prev: ImageView
    private val mp3Url = "https://storage.googleapis.com/exoplayer-test-media-0/play.mp3"
    private val mp3Url2 = "https://storage.googleapis.com/exoplayer-test-media-0/play.mp3"

    var playing: Boolean = false
    var open: Boolean = true
    var playingPosition = 0
    var totalAudio = -1

    lateinit var player: SimpleExoPlayer
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_audio, container, false)

        next = requireActivity().findViewById(R.id.audioNext)
        prev = requireActivity().findViewById(R.id.audioPrev)

        audioArrayList = arrayListOf()
        audioRecyclerView = view.findViewById<RecyclerView>(R.id.audioRecyclerView)
        audioRecyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        audioRecyclerView.setHasFixedSize(false)

        dataCall()

        player = SimpleExoPlayer.Builder(requireContext()).build()
        player.addListener(this)

        next.setOnClickListener {
            if (playingPosition < audioArrayList.size - 1) {
                playFun(++playingPosition)

            } else {

            }
        }
        prev.setOnClickListener {
            if (playingPosition > 0) {
                playFun(--playingPosition)

            } else {

            }
        }

        requireActivity().findViewById<ImageView>(R.id.audioPlayPause).setOnClickListener {
            if (playing) {
                player.pause()
                playing = false
                requireActivity().findViewById<ImageView>(R.id.audioPlayPause)
                    .setImageResource(R.drawable.play_arrow_24)
            } else {
                player.playWhenReady = true
                player.play()
                playing = true
                requireActivity().findViewById<ImageView>(R.id.audioPlayPause)
                    .setImageResource(R.drawable.pause_24)
            }
        }

        return view
    }

    fun alphaDecreased() {
        if (playingPosition == 0) {
            prev.alpha = 0.5f
            prev.isClickable = false
        } else {
            prev.alpha = 1f
            prev.isClickable = true
        }
        if (playingPosition == audioArrayList.size - 1) {
            next.alpha = 0.5f
            next.isClickable = false
        } else {
            next.alpha = 1f
            next.isClickable = true
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onPlayerStateChanged(playWhenReady: Boolean, state: Int) {
        if (state == ExoPlayer.STATE_ENDED) {
            //player back ended
            requireActivity().findViewById<ImageView>(R.id.audioPlayPause)
                .setImageResource(R.drawable.play_arrow_24)
        }
    }

    fun dataCall() {
        val db = FirebaseFirestore.getInstance()
        db.collection("MediaCollection").document("audio").collection("CustomUpload")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Toast.makeText(requireContext(), "$error", Toast.LENGTH_SHORT).show()
                } else {
                    for (dc: DocumentChange in value!!.documentChanges) {
                        when (dc.type) {
                            DocumentChange.Type.ADDED -> {
                                audioArrayList.add(dc.document.toObject(dataAudio::class.java))
                            }
                            DocumentChange.Type.MODIFIED -> {
                                val i = dc.oldIndex
                                audioArrayList.removeAt(i)
                                audioArrayList.add(dc.document.toObject(dataAudio::class.java))
                            }
                            DocumentChange.Type.REMOVED -> {
                                val i = dc.oldIndex
                                audioArrayList.removeAt(i)
                            }
                        }
                    }
                    totalAudio = audioArrayList.size
                    audioAdapter = adapterAudio(audioArrayList)
                    audioRecyclerView.adapter = audioAdapter
                    audioAdapter.setOnClickListener(object : adapterAudio.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            if (playingPosition != position) {
                                playingPosition = position
                                if (open) {
                                    requireActivity().findViewById<ConstraintLayout>(R.id.controlBar).visibility =
                                        View.VISIBLE
                                    open = false
                                }

                                playFun(position)
                            }
                        }
                    })
                }
            }
    }

    private fun playFun(position: Int) {
        alphaDecreased()
        requireActivity().findViewById<TextView>(R.id.audioTitleHome).text =
            audioArrayList[position].title
        requireActivity().findViewById<TextView>(R.id.audioCategoryHome).text =
            audioArrayList[position].filmName
        Picasso.get().load(audioArrayList[position].thumbnail)
            .into(requireActivity().findViewById<RoundedImageView>(R.id.audioThumbnailHome))
        player.pause()
        val dataSourceFactory = DefaultDataSourceFactory(
            requireContext(),
            getString(R.string.app_name)
        )
        val extractorsFactory = DefaultExtractorsFactory()

        val mediaSource = ProgressiveMediaSource
            .Factory(dataSourceFactory, extractorsFactory)
            .createMediaSource(MediaItem.fromUri(audioArrayList[position].url.toString()))

        player.prepare(mediaSource)
        player.playWhenReady = true
        player.play()
        playing = true
        requireActivity().findViewById<ImageView>(R.id.audioPlayPause)
            .setImageResource(R.drawable.pause_24)
    }

    override fun onResume() {
        super.onResume()
        if (playing) {
            player.playWhenReady = true
            player.play()
            playing = true
        }
    }

    override fun onPause() {
        super.onPause()
        if (playing) {
            player.pause()
            playing = false
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        player.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}