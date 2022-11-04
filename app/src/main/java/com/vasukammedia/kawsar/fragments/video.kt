package com.vasukammedia.kawsar.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import com.vasukammedia.kawsar.R
import com.vasukammedia.kawsar.adapter.adapterAudio
import com.vasukammedia.kawsar.adapter.adapterVideo
import com.vasukammedia.kawsar.dataModel.dataAudio
import com.vasukammedia.kawsar.dataModel.dataVideo
import com.vasukammedia.kawsar.videoPlayer

class video : Fragment() {

    private lateinit var audioArrayList: ArrayList<dataVideo>
    private lateinit var audioAdapter: adapterVideo
    private lateinit var audioRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_video, container, false)

        audioArrayList = arrayListOf()
        audioRecyclerView = view.findViewById<RecyclerView>(R.id.videoRecyclerView)
        audioRecyclerView.layoutManager =
            GridLayoutManager(requireActivity(),2, GridLayoutManager.VERTICAL, false)
        audioRecyclerView.setHasFixedSize(false)

        dataCall()
        return view
    }

    fun dataCall() {
        val db = FirebaseFirestore.getInstance()
        db.collection("MediaCollection").document("video").collection("CustomUpload")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Toast.makeText(requireContext(), "$error", Toast.LENGTH_SHORT).show()
                } else {
                    for (dc: DocumentChange in value!!.documentChanges) {
                        when (dc.type) {
                            DocumentChange.Type.ADDED -> {
                                audioArrayList.add(dc.document.toObject(dataVideo::class.java))
                            }
                            DocumentChange.Type.MODIFIED -> {
                                val i = dc.oldIndex
                                audioArrayList.removeAt(i)
                                audioArrayList.add(dc.document.toObject(dataVideo::class.java))
                            }
                            DocumentChange.Type.REMOVED -> {
                                val i = dc.oldIndex
                                audioArrayList.removeAt(i)
                            }
                        }
                    }

                    audioAdapter = adapterVideo(audioArrayList)
                    audioRecyclerView.adapter = audioAdapter
                    audioAdapter.setOnClickListener(object : adapterVideo.onItemClickListener {
                        override fun onItemClick(position: Int) {

                            val i = Intent(requireContext(),videoPlayer::class.java)
                            i.putExtra("url",audioArrayList[position].url)
                            startActivity(i)

                        }

                    })

                }
            }
    }

}