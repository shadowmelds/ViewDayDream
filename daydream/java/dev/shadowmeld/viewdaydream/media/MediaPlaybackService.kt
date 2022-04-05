package dev.shadowmeld.viewdaydream.media

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.MediaBrowserServiceCompat
import dev.shadowmeld.viewdaydream.R


private const val MY_MEDIA_ROOT_ID = "media_root_id"
private const val MY_EMPTY_MEDIA_ROOT_ID = "empty_root_id"

class MediaPlaybackService : MediaBrowserServiceCompat() {

    private var mediaSession: MediaSessionCompat? = null
    private lateinit var stateBuilder: PlaybackStateCompat.Builder

    override fun onCreate() {
        super.onCreate()

        // Create a MediaSessionCompat
        mediaSession = MediaSessionCompat(baseContext,
            MediaPlaybackService::class.java.simpleName
        ).apply {

            // 启用来自 MediaButtons 和 TransportControls 的回调
            setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                    or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
            )
            // 使用 ACTION_PLAY 设置初始 PlaybackState，以便媒体按钮可以启动播放器
            stateBuilder = PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY
                        or PlaybackStateCompat.ACTION_PLAY_PAUSE
                )

            setPlaybackState(stateBuilder.build())

            // MySessionCallback() 具有处理来自媒体控制器的回调的方法
            setCallback(MediaSessionCallback())

            // 设置会话的令牌，以便客户端活动可以与之通信。
            setSessionToken(sessionToken)
        }
    }

    /**
     * onGetRoot() 控制对服务的访问
     * 如果该方法返回 null，则会拒绝连接
     * 要允许客户端连接到您的服务并浏览其媒体内容，onGetRoot() 必须返回非 null 的 BrowserRoot，这是代表您的内容层次结构的根 ID。
     */
    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot(
            getString(R.string.app_name),  // Name visible in Android Auto
            null
        )
    }

    /**
     * onLoadChildren() 使客户端能够构建和显示 内容层次结构菜单。
     */
    override fun onLoadChildren(
        parentId: String,
        result: Result<List<MediaBrowserCompat.MediaItem>>
    ) {
        // 不允许浏览
        if (MY_EMPTY_MEDIA_ROOT_ID == parentId) {
            result.sendResult(null)
            return
        }

        // 例如假设音乐目录已经加载/缓存。

        val mediaItems = listOf<MediaBrowserCompat.MediaItem>()

        // 检查这是否是根菜单
        if (MY_MEDIA_ROOT_ID == parentId) {
            // 为顶层构建 MediaItem 对象，
            // 并将它们放在 mediaItems 列表中......
        } else {
            // 检查传递的 parentMediaId 以查看我们所在的子菜单，
            // 并将该菜单的子项放在 mediaItems 列表中...
        }
        result.sendResult(mediaItems)
    }

}

class MediaSessionCallback : MediaSessionCompat.Callback() {

}