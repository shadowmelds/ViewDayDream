package dev.shadowmeld.viewdaydream.ui.main.local_home

import android.graphics.Bitmap
import android.os.Build
import android.util.Size
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.shadowmeld.viewdaydream.R
import dev.shadowmeld.viewdaydream.databinding.ItemBaseMusicListBinding
import java.io.FileNotFoundException

class LocalMusicAdapter(
    private val onClick: (LocalMusicInfo) -> Unit) : ListAdapter<LocalMusicInfo, LocalMusicAdapter.ViewHolder>(LocalMusicDiffCallback()) {

    class ViewHolder(private val binding: ItemBaseMusicListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: LocalMusicInfo, currentPosition: Int, onClick: (LocalMusicInfo) -> Unit) {
            binding.number.text = "${currentPosition + 1}"
            binding.musicName.text = data.musicName
            binding.musicArtist.text = data.musicArtist

            // Load thumbnail of a specific media item.

            var thumbnail: Bitmap? = null
                try {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        thumbnail = binding.cover.context.contentResolver.loadThumbnail(
                            data.uri, Size(640, 480), null)
                    }

                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }

            Glide.with(binding.cover).load(thumbnail).error(R.mipmap.ic_launcher).into(binding.cover)

            binding.container.setOnClickListener {
                onClick(data)
            }
        }
    }

    class LocalMusicDiffCallback : DiffUtil.ItemCallback<LocalMusicInfo>() {
        override fun areItemsTheSame(oldItem: LocalMusicInfo, newItem: LocalMusicInfo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: LocalMusicInfo, newItem: LocalMusicInfo): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemBaseMusicListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position), position, onClick)
    }

    override fun submitList(list: List<LocalMusicInfo>?) {
        super.submitList(list?.let { ArrayList(it) })
    }
}