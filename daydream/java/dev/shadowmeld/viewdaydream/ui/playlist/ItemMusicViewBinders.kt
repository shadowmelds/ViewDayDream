package dev.shadowmeld.viewdaydream.ui.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.shadowmeld.viewdaydream.R
import dev.shadowmeld.viewdaydream.databinding.ItemMusicListBinding
import dev.shadowmeld.viewdaydream.model.MusicListTracks


class ItemMusicViewHolder(private val binding: ItemMusicListBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: MusicListTracks, currentPosition: Int, onClick: (MusicListTracks) -> Unit) {
        Glide.with(binding.cover.context).load(item.al?.picUrl).into(binding.cover)
        binding.musicName.text = item.name
        binding.musicProducer.text = item.ar?.get(0)?.name
        binding.number.text = "${currentPosition + 1}"

        item.dt?.let {
            val minutes = it / 1000 / 60
            val seconds = it / 1000 % 60
            binding.time.text = "$minutes:$seconds"
        }
    }
}

class ItemMusicViewBinders : FeedItemViewBinder<MusicListTracks, ItemMusicViewHolder>(
    MusicListTracks::class.java
) {
    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ItemMusicViewHolder(ItemMusicListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun bindViewHolder(model: MusicListTracks, viewHolder: ItemMusicViewHolder, position: Int, onClick: (Any) -> Unit) {
        viewHolder.bind(model, position, onClick = onClick)
    }

    override fun getFeedItemType() = R.layout.item_music_list

    override fun areItemsTheSame(oldItem: MusicListTracks, newItem: MusicListTracks) = true

    override fun areContentsTheSame(oldItem: MusicListTracks, newItem: MusicListTracks) = true
}