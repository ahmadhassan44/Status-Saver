package com.example.livesaver.onboarding.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.livesaver.AppUtils
import com.example.livesaver.R
import de.hdodenhof.circleimageview.CircleImageView

class LanguageAdapter(
    private val languages: Array<String>
): RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    inner class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView = itemView.findViewById<CircleImageView>(R.id.circleImageView)
        private val languageTextView = itemView.findViewById<TextView>(R.id.textView5)

        fun bind(language: String) {
            // Set language name in TextView
            languageTextView.text = language

            // Set language image based on language name
            val drawableId = AppUtils().getLanguageDrawableId(language)
            imageView.setImageResource(drawableId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.language_item, parent, false)
        return LanguageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return languages.size
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val language = languages[position]
        holder.bind(language)
    }

}
