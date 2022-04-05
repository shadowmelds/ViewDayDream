package dev.shadowmeld.viewdaydream.ui.playlist

import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dev.shadowmeld.viewdaydream.R
import dev.shadowmeld.viewdaydream.databinding.ActivityPlayListBinding
import dev.shadowmeld.viewdaydream.model.PersonalizedMusicListInfo

class PlayListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayListBinding
    private val viewModel: PlayListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val playListData = PersonalizedMusicListInfo(
            intent.getStringExtra("id"),
            intent.getStringExtra("type"),
            intent.getStringExtra("name"),
            intent.getStringExtra("copywriter"),
            intent.getStringExtra("picUrl"),
            intent.getBooleanExtra("canDislike", false),
            intent.getStringExtra("trackNumberUpdateTime"),
            intent.getStringExtra("playCount"),
            intent.getIntExtra("trackCount", -1),
            intent.getStringExtra("highQuality"),
            intent.getStringExtra("alg")
        )

        val itemMusicViewBinders = ItemMusicViewBinders()
        val itemDetailViewBinders = ItemDetailViewBinders()

        val viewBinders = mapOf(
            itemMusicViewBinders.modelClass to itemMusicViewBinders as FeedItemBinder,
            itemDetailViewBinders.modelClass to itemDetailViewBinders as FeedItemBinder,
        )

        val adapter = FeedAdapter(viewBinders) {

        }

        binding.musicListView.layoutManager = LinearLayoutManager(binding.musicListView.context)
        binding.musicListView.adapter = adapter


        viewModel.liveData.observe(this) {
            val data = mutableListOf<Any?>().apply {
                add(it)
            }

            it.playlist?.tracks?.let { music ->
                for (value in music) {
                    data.add(value)
                }
            }

            adapter.submitList(data)
        }

        playListData.id?.let {
            viewModel.getMusicListDetail(it)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
//        finish()
        onBackPressed()
        // or call onBackPressed()
        return true
    }
}