package dev.shadowmeld.viewdaydream.ui.launcher

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import dev.shadowmeld.viewdaydream.ui.BaseViewModel
import dev.shadowmeld.viewdaydream.util.logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LaunchViewModel : BaseViewModel() {

    private var sharedPref: SharedPreferences? = null

    fun loadData(sharedPref: SharedPreferences) {
        this.sharedPref = sharedPref
    }

    var isReady = false

    private val observerResult: MutableLiveData<LaunchNavigatonAction>by lazy {
        MutableLiveData<LaunchNavigatonAction>()
    }

    fun getDaydreamMode(action: (action: LaunchNavigatonAction?) -> Unit) {

        launch(
            {
                withContext(Dispatchers.IO) {
                    sharedPref?.let {
                        getDaydreamMode(it)
                    }
                }

                // 这里温重启有点问题
                action.invoke(observerResult.value)
            },
            {
                errorLiveData.postValue(it)
            },
            {
                loadingLiveData.postValue(false)
            }
        )
    }

    private fun getDaydreamMode(sharedPref: SharedPreferences) {

        val a = sharedPref.getBoolean("first_start", true)
        logger("$a")
        observerResult.postValue(when (a) {
            false -> LaunchNavigatonAction.NavigateToMainActivityAction
            true -> {
                with (sharedPref.edit()) {
                    putBoolean("first_start", false)
                    apply()
                }
                LaunchNavigatonAction.NavigateToOnboardingAction
            }
        })
    }
}
