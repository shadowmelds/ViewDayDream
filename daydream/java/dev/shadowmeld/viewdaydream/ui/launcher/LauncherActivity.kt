package dev.shadowmeld.viewdaydream.ui.launcher

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.shadowmeld.viewdaydream.databinding.ActivityLauncherBinding
import dev.shadowmeld.viewdaydream.ui.main.MainActivity
import dev.shadowmeld.viewdaydream.ui.onboarding.OnboardingActivity
import dev.shadowmeld.viewdaydream.util.logger
import kotlinx.coroutines.*

class LauncherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLauncherBinding

    private val viewModel: LaunchViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)
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


        viewModel.loadData(getPreferences(Context.MODE_PRIVATE))
        viewModel.getDaydreamMode {
            when (it) {
                null -> {}
                is LaunchNavigatonAction.NavigateToMainActivityAction -> startActivity(
                    Intent(this@LauncherActivity, MainActivity::class.java)
                )
                is LaunchNavigatonAction.NavigateToOnboardingAction -> startActivity(
                    Intent(this@LauncherActivity, OnboardingActivity::class.java)
                )
            }
        }

        MainScope().launch {

            withContext(Dispatchers.IO) {
                delay(500L)
            }
            finish()
        }
        viewModel.isReady = true
    }
}
sealed class LaunchNavigatonAction {
    object NavigateToOnboardingAction : LaunchNavigatonAction()
    object NavigateToMainActivityAction : LaunchNavigatonAction()
}