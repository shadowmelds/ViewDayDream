package dev.shadowmeld.viewdaydream.ui.main

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Size
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.*
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import coil.compose.rememberImagePainter
import com.bumptech.glide.Glide
import com.google.accompanist.glide.rememberGlidePainter
import com.google.android.material.composethemeadapter.MdcTheme
import com.google.android.material.snackbar.Snackbar
import dev.shadowmeld.viewdaydream.R
import dev.shadowmeld.viewdaydream.databinding.ActivityMainBinding
import dev.shadowmeld.viewdaydream.media.PlayList
import dev.shadowmeld.viewdaydream.media.PlayListListener
import dev.shadowmeld.viewdaydream.ui.main.explore.ExploreFragment
import dev.shadowmeld.viewdaydream.ui.main.home.HomeFragment
import dev.shadowmeld.viewdaydream.ui.main.library_music.LibraryMusicFragment
import dev.shadowmeld.viewdaydream.ui.main.local_home.LocalHomeFragment
import dev.shadowmeld.viewdaydream.ui.main.local_home.LocalMusicInfo
import dev.shadowmeld.viewdaydream.ui.player.PlayerActivity
import dev.shadowmeld.viewdaydream.ui.settings.SettingsActivity
import dev.shadowmeld.viewdaydream.util.logger
import java.io.FileNotFoundException


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
class MainActivity : AppCompatActivity(), PlayListListener {

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
        PlayList.addUpdateListener(
            this
        )
    }

    private fun initData() {
        viewModel.getDayDreamMode()
    }

    private var updateCurrentMusic: ((musicData: LocalMusicInfo) -> Unit)? = null
    override fun onCurrentPlayUpdate(currentPlayMusic: LocalMusicInfo) {
        updateCurrentMusic?.invoke(currentPlayMusic)
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
                NowPlayer()
            }
        }
    }

    @Composable
    private fun NowPlayer(
        viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    ) {

        val musicData = remember {
            mutableStateOf<LocalMusicInfo?>(null)
        }
        updateCurrentMusic = {
            musicData.value = it
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {

            Card(
                onClick = {

                },
                modifier = Modifier
                    .width(200.dp)
                    .align(alignment = Alignment.BottomEnd)
                    .padding(0.dp, 0.dp, 16.dp, 72.dp),
                backgroundColor = colorResource(id = R.color.color_primary)
            ) {
                ConstraintLayout(
                    modifier = Modifier
                        .align(alignment = Alignment.BottomEnd)
                ) {

                    val context = LocalContext.current
                    var thumbnail: Bitmap? = null
                    musicData.value?.uri?.let {
                        try {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                thumbnail = context.contentResolver.loadThumbnail(
                                    it, Size(640, 480), null)
                            }

                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        }
                    }


                    val (musicCover, musicInfo, musicDuration) = createRefs()
                    Image(
                        painter = rememberGlidePainter(thumbnail?: R.mipmap.ic_launcher),
                        contentDescription = "Contact profile picture",
                        modifier = Modifier
                            .padding(8.dp)
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .constrainAs(musicCover) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                            }
                    )

                    Column(
                        modifier = Modifier
                            .constrainAs(musicInfo) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(musicCover.end)
                                end.linkTo(musicDuration.start)
                                width = Dimension.fillToConstraints
                            }
                    ) {
                        Text(
                            text = musicData.value?.musicName ?: "Music Name",
                            fontSize = 12.sp
                        )
                    }

                    Text(
                        text = "${musicData.value?.musicDuration ?: "0"}",
                        style = MaterialTheme.typography.body2,
                        fontSize = 10.sp,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .constrainAs(musicDuration) {
                                bottom.linkTo(musicInfo.bottom)
                                end.linkTo(parent.end)
                            }
                    )
                }
            }
        }
    }

    @Preview
    @Composable
    private fun PreviewNowPlayer() {
        NowPlayer()
    }

    @Composable
    private fun NowPlayerScreen() {

    }

    @Preview
    @Composable
    private fun PreviewGreeting() {
        NowPlayerScreen()
    }
}