package dev.shadowmeld.viewdaydream.ui.main.local_home

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.BaseColumns
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import dev.shadowmeld.viewdaydream.util.logger
import java.util.concurrent.TimeUnit
import android.provider.MediaStore.Audio.AudioColumns;
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import dev.shadowmeld.viewdaydream.databinding.FragmentLocalHomeBinding
import dev.shadowmeld.viewdaydream.media.PlayList
import dev.shadowmeld.viewdaydream.ui.main.MainViewModel

class LocalHomeFragment : Fragment() {

    companion object {
        fun newInstance() = LocalHomeFragment()
    }

    private lateinit var binding: FragmentLocalHomeBinding

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLocalHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("ShadowmeldMainViewModel", "Fragment1:${viewModel.hashCode()}")
        val localMusicAdapter = LocalMusicAdapter {
            PlayList.get().setCurrentPlayMusic(it)
            Log.d("ShadowmeldMainViewModel", "Fragment:${viewModel.hashCode()}")
        }

        binding.localMusicList.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = localMusicAdapter
        }

        val musicList = mutableListOf<LocalMusicInfo>()

        val projection = arrayOf(
            BaseColumns._ID,
            AudioColumns.TITLE,
            AudioColumns.TRACK,
            AudioColumns.YEAR,
            AudioColumns.DURATION,
            AudioColumns.DATA,
            AudioColumns.DATE_MODIFIED,
            AudioColumns.ALBUM_ID,
            AudioColumns.ALBUM,
            AudioColumns.ARTIST_ID,
            AudioColumns.ARTIST,
            AudioColumns.SIZE
        )

        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Audio.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )
            } else {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

        // Show only videos that are at least 5 seconds in duration.
        val selection = "${MediaStore.Audio.Media.DURATION} >= ?"
        val selectionArgs = arrayOf(
            TimeUnit.MILLISECONDS.convert(5, TimeUnit.SECONDS).toString()
        )

        // Display videos in alphabetical order based on their display name.
        val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

        val query = context?.contentResolver?.query(
            collection,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        query?.use { cursor ->
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)

            logger("musicList 有: ${musicList.size}")
            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val artist = cursor.getString(artistColumn)
                val duration = cursor.getInt(durationColumn)
                val size = cursor.getInt(sizeColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                logger("musicList 循环: ${musicList.size}")
                musicList += LocalMusicInfo(contentUri, name, artist, duration, size)
            }
        }

        logger("musicList Size: ${musicList.size}")

        localMusicAdapter.submitList(musicList)
    }

}