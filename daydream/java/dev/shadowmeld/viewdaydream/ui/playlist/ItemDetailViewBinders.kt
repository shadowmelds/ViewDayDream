package dev.shadowmeld.viewdaydream.ui.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.shadowmeld.viewdaydream.R
import dev.shadowmeld.viewdaydream.databinding.ItemDetailMusicListBinding
import dev.shadowmeld.viewdaydream.model.MusicListDetailInfo
import dev.shadowmeld.viewdaydream.model.MusicListTracks


class ItemDetailViewHolder(private val binding: ItemDetailMusicListBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: MusicListDetailInfo, currentPosition: Int, onClick: (MusicListTracks) -> Unit) {
        Glide.with(binding.playListCover.context).load(item.playlist?.coverImgUrl).into(binding.playListCover)
        binding.playListName.text = item.playlist?.name
        binding.playListCreator.text = "创建者：${item.playlist?.creator?.nickname}"
        binding.playListInfo.text = "${item.playlist?.trackCount} 首音乐"
        binding.playListDescription.text = item.playlist?.description
    }
}

class ItemDetailViewBinders : FeedItemViewBinder<MusicListDetailInfo, ItemDetailViewHolder>(
    MusicListDetailInfo::class.java
) {
    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ItemDetailViewHolder(ItemDetailMusicListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun bindViewHolder(model: MusicListDetailInfo, viewHolder: ItemDetailViewHolder, position: Int, onClick: (Any) -> Unit) {
        viewHolder.bind(model, position, onClick = onClick)
    }

    override fun getFeedItemType() = R.layout.item_detail_music_list

    override fun areItemsTheSame(oldItem: MusicListDetailInfo, newItem: MusicListDetailInfo) = true

    override fun areContentsTheSame(oldItem: MusicListDetailInfo, newItem: MusicListDetailInfo) = true
}