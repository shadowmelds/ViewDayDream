package dev.shadowmeld.viewdaydream.ui.main.library_music

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.shadowmeld.viewdaydream.R

class LibraryMusicFragment : Fragment() {

    companion object {
        fun newInstance() = LibraryMusicFragment()
    }

    private lateinit var viewModel: LibraryMusicViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_library_music, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}