package dev.shadowmeld.viewdaydream.ui.player

import android.content.ComponentName
import android.os.Bundle
import android.os.RemoteException
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import dev.shadowmeld.viewdaydream.databinding.ActivityPlayerBinding
import dev.shadowmeld.viewdaydream.media.MediaItemData
import dev.shadowmeld.viewdaydream.ui.main.MainActivity


class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    private lateinit var mMediaBrowser: MediaBrowserCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     binding = ActivityPlayerBinding.inflate(layoutInflater)
     setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener { view ->
            initMediaBrowser()
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    private fun playMedia(mediaItem: MediaItemData) {

    }

    private fun initMediaBrowser() {
        val componentName = ComponentName("dev.shadowmeld.viewdaydream","dev.shadowmeld.viewdaydream.media.MusicService")
        mMediaBrowser = MediaBrowserCompat(this, componentName, object : MediaBrowserCompat.ConnectionCallback() {
            override fun onConnected() {
                try {
                    // Ah, here’s our Token again
                    val token = mMediaBrowser.sessionToken
                    // This is what gives us access to everything
                    val controller = MediaControllerCompat(this@PlayerActivity, token)
                    // Convenience method to allow you to use
                    // MediaControllerCompat.getMediaController() anywhere
                    MediaControllerCompat.setMediaController(
                        this@PlayerActivity, controller
                    )
                } catch (e: RemoteException) {
                    Log.e(
                        PlayerActivity::class.java.simpleName,
                        "Error creating controller", e
                    )
                }
            }

            override fun onConnectionSuspended() {
                // We were connected, but no longer :-(
            }

            override fun onConnectionFailed() {
                // The attempt to connect failed completely.
                // Check the ComponentName!
            }
        }, null)
        mMediaBrowser.connect()
    }


    override fun onDestroy() {
        super.onDestroy()
//        mMediaBrowser.disconnect()
    }
}