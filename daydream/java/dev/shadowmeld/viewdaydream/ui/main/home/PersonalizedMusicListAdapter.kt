package dev.shadowmeld.viewdaydream.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.shadowmeld.viewdaydream.databinding.ItemPersonalizedMusicListBinding
import dev.shadowmeld.viewdaydream.model.PersonalizedMusicListInfo

class PersonalizedMusicListAdapter(
    private val onClick: (PersonalizedMusicListInfo) -> Unit
) : ListAdapter<PersonalizedMusicListInfo, PersonalizedMusicListAdapter.ViewHolder>(NotCurrentDiffCallback()) {

    class ViewHolder(private val binding: ItemPersonalizedMusicListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: PersonalizedMusicListInfo, currentPosition: Int, onClick: (PersonalizedMusicListInfo) -> Unit) {
            binding.musicName.text = data.name
            Glide.with(binding.cover).load(data.picUrl).into(binding.cover)
            binding.root.setOnClickListener {
                onClick(data)
            }
        }
    }

    class NotCurrentDiffCallback : DiffUtil.ItemCallback<PersonalizedMusicListInfo>() {
        override fun areItemsTheSame(oldItem: PersonalizedMusicListInfo, newItem: PersonalizedMusicListInfo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PersonalizedMusicListInfo, newItem: PersonalizedMusicListInfo): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemPersonalizedMusicListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position), position, onClick = onClick)
    }

    override fun submitList(list: List<PersonalizedMusicListInfo>?) {
        super.submitList(list?.let { ArrayList(it) })
    }
}