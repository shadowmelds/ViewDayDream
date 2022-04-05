package dev.shadowmeld.viewdaydream.ui.now_player

import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.util.Size
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.glide.rememberGlidePainter
import dev.shadowmeld.viewdaydream.R
import dev.shadowmeld.viewdaydream.ui.main.MainViewModel
import dev.shadowmeld.viewdaydream.ui.main.local_home.LocalMusicInfo
import java.io.FileNotFoundException


@ExperimentalMaterialApi
@Composable
fun ParentPlayer(viewModel: MainViewModel? = null) {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "now_player") {
        composable("now_player") { NowPlayer(navController, viewModel) }
        composable("now_player_screen") {NowPlayerScreen(navController)}
    }

}

@ExperimentalMaterialApi
@Composable
private fun NowPlayer(
    navController: NavController? = null,
    viewModel: MainViewModel? = null
) {

    Log.d("ShadowmeldMainViewModel", "${viewModel.hashCode()}")
    val musicData = remember {
        mutableStateOf<LocalMusicInfo?>(null)
    }

    viewModel?.setUpdateCurrentMusic {
        musicData.value = it
    }
    Log.d("ShadowmeldMainViewModel", "${viewModel?.currentMusic == null}")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        Card(
            onClick = {
                navController?.navigate("now_player_screen")
            },
            modifier = Modifier
                .width(200.dp)
                .align(alignment = Alignment.BottomEnd)
                .padding(0.dp, 0.dp, 16.dp, 72.dp),
            backgroundColor = colorResource(id = R.color.color_primary),
            shape = RoundedCornerShape(16.dp),
            elevation = 8.dp
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

@ExperimentalMaterialApi
@Preview
@Composable
private fun PreviewNowPlayer() {
    NowPlayer()
}

@Composable
private fun NowPlayerScreen(
    navController: NavController? = null
) {

    val musicData = remember {
        mutableStateOf<LocalMusicInfo?>(null)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription =
                            "Back"
                        )
                    }
                },
                actions = {
                    /* RowScope 会将这些 Icon 水平放置 */
                    IconButton(onClick = {}) {
                        Icon(Icons.Rounded.MoreVert, contentDescription = "菜单")
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        },
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(
                    Color.White
                )
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(36.dp, 8.dp, 36.dp, 8.dp)
                    .padding(0.dp, 36.dp, 0.dp, 0.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = 16.dp
            ) {
                Image(
                    painter = rememberGlidePainter(R.mipmap.ic_launcher),
                    contentDescription = "Contact profile picture",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            } // 封面

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(36.dp, 8.dp, 36.dp, 8.dp)
            ) {

                val (favorite, musicInfo, musicDuration) = createRefs()

                Column(
                    modifier = Modifier
                        .constrainAs(musicInfo) {
                            start.linkTo(parent.start)
                            width = Dimension.fillToConstraints
                        }
                ) {
                    Text(
                        text = musicData.value?.musicName ?: "Music Name",
                        style = MaterialTheme.typography.h6
                    )
                    Text(
                        text = musicData.value?.musicArtist ?: "Music Artist",
                        style = MaterialTheme.typography.body2
                    )
                }

                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .constrainAs(favorite) {
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }) {
                    Icon(Icons.Rounded.FavoriteBorder, contentDescription = "喜欢")
                }

            } // 音乐信息
        }
    }

}

@Preview
@Composable
private fun PreviewGreeting() {
    NowPlayerScreen()
}