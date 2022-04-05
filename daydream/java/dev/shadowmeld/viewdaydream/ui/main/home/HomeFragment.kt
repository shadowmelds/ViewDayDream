package dev.shadowmeld.viewdaydream.ui.main.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dev.shadowmeld.viewdaydream.R
import dev.shadowmeld.viewdaydream.databinding.FragmentHomeBinding
import dev.shadowmeld.viewdaydream.ui.playlist.PlayListActivity
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPersonalizedMusicList()

        val jumpIntent = Intent(view.context, PlayListActivity::class.java)

        val adapter = PersonalizedMusicListAdapter {
            jumpIntent.putExtra("id", it.id)
            jumpIntent.putExtra("type", it.type)
            jumpIntent.putExtra("name", it.name)
            jumpIntent.putExtra("copywriter", it.copywriter)
            jumpIntent.putExtra("picUrl", it.picUrl)
            jumpIntent.putExtra("canDislike", it.canDislike)
            jumpIntent.putExtra("trackNumberUpdateTime", it.trackNumberUpdateTime)
            jumpIntent.putExtra("playCount", it.playCount)
            jumpIntent.putExtra("trackCount", it.trackCount)
            jumpIntent.putExtra("highQuality", it.highQuality)
            jumpIntent.putExtra("alg", it.alg)
            startActivity(jumpIntent)
        }
        binding.personalizedMusicList.layoutManager = LinearLayoutManager(binding.personalizedMusicList.context).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        binding.personalizedMusicList.adapter = adapter

        viewModel.liveData.observe(viewLifecycleOwner) {
            adapter.submitList(it?.result)
        }

        binding.updateData.setOnClickListener {
            adapter.submitList(viewModel.liveData.value?.result)
        }
    }
}