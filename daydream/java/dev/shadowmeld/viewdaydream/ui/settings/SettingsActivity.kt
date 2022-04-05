package dev.shadowmeld.viewdaydream.ui.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceFragmentCompat
import dev.shadowmeld.viewdaydream.R
import dev.shadowmeld.viewdaydream.data.SharedPreferencesValues
import dev.shadowmeld.viewdaydream.databinding.SettingsActivityBinding
import dev.shadowmeld.viewdaydream.ui.main.MainViewModel
import dev.shadowmeld.viewdaydream.util.logger


class SettingsActivity : AppCompatActivity() {

    companion object {
        const val RESULT_DAYDREAM_MODE = 2
    }

    private lateinit var binding: SettingsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                logger("Settings Menu 被点击了")
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        private val listener: SharedPreferences.OnSharedPreferenceChangeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { _: SharedPreferences, key: String ->
                if (key == SharedPreferencesValues.DAYDREAM_MODE) {
                    activity?.setResult(RESULT_DAYDREAM_MODE, Intent())
                }
            }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        override fun onResume() {
            super.onResume()
            preferenceManager.sharedPreferences?.registerOnSharedPreferenceChangeListener(listener)
        }

        override fun onPause() {
            super.onPause()
            preferenceManager.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(listener)
        }

    }
}