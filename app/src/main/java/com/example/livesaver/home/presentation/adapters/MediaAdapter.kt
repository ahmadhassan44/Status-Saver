package com.example.livesaver.home.presentation.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.livesaver.R
import com.example.livesaver.home.domain.MediaModel
import com.example.livesaver.home.presentation.activities.ImagePreviewActivity

class MediaAdapter(
    private val mediaList: ArrayList<MediaModel>
): RecyclerView.Adapter<MediaAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {

        private val imageView=itemView.findViewById<ImageView>(R.id.imageView)
        private val newLabel=itemView.findViewById<TextView>(R.id.newLabel)
        fun bind(mediaModel: MediaModel) {
            Glide.with(itemView.context).load(mediaModel.pathUri).placeholder(R.drawable
                .baseline_photo_24).into(imageView)
            if (mediaModel.isDownloaded) {
                newLabel.visibility = View.GONE
            } else {
                newLabel.visibility = View.VISIBLE
            }
            itemView.setOnClickListener {
                val intent= Intent(itemView.context,ImagePreviewActivity::class.java)
                intent.putExtra("pathUri",mediaModel.pathUri)
                itemView.context.startActivity(intent)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.images_recycler_view_item, parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return mediaList.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mediaModel=mediaList[position]
        holder.bind(mediaModel)
    }
    fun updateList(newMediaList: ArrayList<MediaModel>) {
        mediaList.clear()
        mediaList.addAll(newMediaList)
        notifyDataSetChanged()
    }

}