package com.vasukammedia.kawsar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import com.vasukammedia.kawsar.R
import com.vasukammedia.kawsar.dataModel.dataAudio

class adapterAudio(private var audioArrayList:ArrayList<dataAudio>):RecyclerView.Adapter<adapterAudio.MyViewHolder>() {

    private lateinit var Listener : onItemClickListener
    interface onItemClickListener {

        fun onItemClick(position: Int)

    }

    fun setOnClickListener(listener: onItemClickListener){
        Listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.audio_layout_design,parent,false)


        return MyViewHolder(view,Listener)
    }

    override fun onBindViewHolder(h: MyViewHolder, position: Int) {

        val data : dataAudio = audioArrayList[position]
        h.title.text=data.title
        h.filmName.text=data.filmName

    }

    override fun getItemCount(): Int {
        return audioArrayList.size
    }

    class MyViewHolder(view: View, listener:onItemClickListener):RecyclerView.ViewHolder(view){

        val title : TextView = view.findViewById(R.id.audioTitle)
        val filmName : TextView = view.findViewById(R.id.audioCategory)
        val progressBar : ProgressBar = view.findViewById(R.id.audioProgress)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}
