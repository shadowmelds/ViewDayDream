package dev.shadowmeld.viewdaydream.ui.main

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.fragment.app.*
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.google.android.material.composethemeadapter.MdcTheme
import dev.shadowmeld.viewdaydream.R
import dev.shadowmeld.viewdaydream.databinding.ActivityMainBinding
import dev.shadowmeld.viewdaydream.media.PlayList
import dev.shadowmeld.viewdaydream.media.PlayListListener
import dev.shadowmeld.viewdaydream.ui.main.explore.ExploreFragment
import dev.shadowmeld.viewdaydream.ui.main.home.HomeFragment
import dev.shadowmeld.viewdaydream.ui.main.library_music.LibraryMusicFragment
import dev.shadowmeld.viewdaydream.ui.main.local_home.LocalHomeFragment
import dev.shadowmeld.viewdaydream.ui.main.local_home.LocalMusicInfo
import dev.shadowmeld.viewdaydream.ui.now_player.ParentPlayer
import dev.shadowmeld.viewdaydream.ui.settings.SettingsActivity
import dev.shadowmeld.viewdaydream.util.logger


enum class DaydreamMode {
    DAYDREAM_LOCAL_MODE,
    DAYDREAM_ONLINE_MODE
}

enum class ScreenFragments {
    HOME,
    EXPLORE,
    LIBRARY_MUSIC,
    LOCAL_HOME
}

@ExperimentalMaterialApi
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    private var homeFragment: HomeFragment? = null
    private var exploreFragment: ExploreFragment? = null
    private var libraryMusicFragment: LibraryMusicFragment? = null
    private var localHomeFragment: LocalHomeFragment? = null

    private var currentFragment: ScreenFragments? = null

    private var currentDaydreamMode = DaydreamMode.DAYDREAM_LOCAL_MODE

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == SettingsActivity.RESULT_DAYDREAM_MODE) {
            val data: Intent? = it.data
            logger("成功返回DaydreamMode Change")
            viewModel.getDayDreamMode()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("ShadowmeldMainViewModel", "Activity1:${viewModel.hashCode()}")

        viewModel.loadData(getDefaultSharedPreferences(this))

        viewModel.isReady = false
        // Set up an OnPreDrawListener to the root view.
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // Check if the initial data is ready.
                    return if (viewModel.isReady) {
                        // The content is ready; start drawing.
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        // The content is not ready; suspend.
                        false
                    }
                }
            }
        )

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        viewModel.daydreamMode.observe(this) {
            it?.let {
                setDaydreamMode(if (it) DaydreamMode.DAYDREAM_ONLINE_MODE else DaydreamMode.DAYDREAM_LOCAL_MODE)
            }
        }

        initComposeView()
        initData()

        PlayList.get().addUpdateListener(object : PlayListListener {
            override fun onCurrentPlayUpdate(currentPlayMusic: LocalMusicInfo) {

                Log.d("ShadowmeldMainViewModel", "MainActivity:${viewModel.hashCode()}")
                viewModel.currentMusic?.invoke(currentPlayMusic)
            }
        },)
    }

    private fun initData() {
        viewModel.getDayDreamMode()
    }

    private fun setDaydreamMode(currentDaydreamMode: DaydreamMode) {

        // 可以开始准备绘制第一帧
        viewModel.isReady = true
        this.currentDaydreamMode = currentDaydreamMode

        setFragmentContainer(if (currentDaydreamMode == DaydreamMode.DAYDREAM_LOCAL_MODE) ScreenFragments.LOCAL_HOME else ScreenFragments.HOME)
        setupBottomNavigationBar(currentDaydreamMode)
    }

    private fun setFragmentContainer(currentFragment: ScreenFragments) {
        this.currentFragment = currentFragment
        supportFragmentManager.commit {

            when (currentFragment) {
                ScreenFragments.HOME -> {
                    if (homeFragment == null) {
                        homeFragment = HomeFragment.newInstance()
                        homeFragment?.let {
                            setReorderingAllowed(true)
                            add(R.id.fragment_container, it)
                        }
                    }

                    homeFragment?.let { show(it) }
                    exploreFragment?.let { hide(it) }
                    libraryMusicFragment?.let { hide(it) }
                    localHomeFragment?.let { hide(it) }
                }
                ScreenFragments.EXPLORE -> {
                    if (exploreFragment == null) {
                        exploreFragment = ExploreFragment.newInstance()
                        exploreFragment?.let {
                            setReorderingAllowed(true)
                            add(R.id.fragment_container, it)
                        }
                    }

                    homeFragment?.let { hide(it) }
                    exploreFragment?.let { show(it) }
                    libraryMusicFragment?.let { hide(it) }
                    localHomeFragment?.let { hide(it) }
                }
                ScreenFragments.LIBRARY_MUSIC -> {
                    if (libraryMusicFragment == null) {
                        libraryMusicFragment = LibraryMusicFragment.newInstance()
                        libraryMusicFragment?.let {
                            setReorderingAllowed(true)
                            add(R.id.fragment_container, it)
                        }
                    }

                    homeFragment?.let { hide(it) }
                    exploreFragment?.let { hide(it) }
                    libraryMusicFragment?.let { show(it) }
                    localHomeFragment?.let { hide(it) }
                }
                ScreenFragments.LOCAL_HOME -> {
                    if (localHomeFragment == null) {
                        localHomeFragment = LocalHomeFragment.newInstance()
                        localHomeFragment?.let {
                            setReorderingAllowed(true)
                            add(R.id.fragment_container, it)
                        }
                    }

                    homeFragment?.let { hide(it) }
                    exploreFragment?.let { hide(it) }
                    libraryMusicFragment?.let { hide(it) }
                    localHomeFragment?.let { show(it) }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                logger("Settings Menu 被点击了")
                resultLauncher.launch(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    private fun setupBottomNavigationBar(currentDaydreamMode: DaydreamMode) {
        val menu: Menu = binding.navigation.menu

        menu.removeItem(0)
        menu.removeItem(1)
        menu.removeItem(2)

        when (currentDaydreamMode) {
            DaydreamMode.DAYDREAM_LOCAL_MODE -> {

                menu.add(0, 0, 0, R.string.home) //设置菜单标题
                menu.findItem(0).setIcon(R.drawable.nav_home) //设置菜单图片

                menu.add(0, 1, 1, R.string.library_music) //设置菜单标题
                menu.findItem(1).setIcon(R.drawable.nav_library_music) //设置菜单图片


                binding.navigation.setOnItemSelectedListener {
                    when (it.itemId) {
                        0 -> setFragmentContainer(ScreenFragments.LOCAL_HOME)
                        1 -> setFragmentContainer(ScreenFragments.LIBRARY_MUSIC)
                    }
                    return@setOnItemSelectedListener true
                }
            }
            DaydreamMode.DAYDREAM_ONLINE_MODE -> {

                menu.add(0, 0, 0, R.string.home) //设置菜单标题
                menu.findItem(0).setIcon(R.drawable.nav_home) //设置菜单图片

                menu.add(0, 1, 1, R.string.explore) //设置菜单标题
                menu.findItem(1).setIcon(R.drawable.nav_explore) //设置菜单图片

                menu.add(0, 2, 2, R.string.library_music) //设置菜单标题
                menu.findItem(2).setIcon(R.drawable.nav_library_music) //设置菜单图片

                binding.navigation.setOnItemSelectedListener {
                    when (it.itemId) {
                        0 -> setFragmentContainer(ScreenFragments.HOME)
                        1 -> setFragmentContainer(ScreenFragments.EXPLORE)
                        2 -> setFragmentContainer(ScreenFragments.LIBRARY_MUSIC)
                    }
                    return@setOnItemSelectedListener true
                }
            }
        }
    }


    private fun initComposeView() {

        binding.currentPlay.setContent {
            MdcTheme {
                ParentPlayer(viewModel = viewModel)
            }
        }
    }
}