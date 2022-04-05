package dev.shadowmeld.viewdaydream.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    //异常
    val errorLiveData = MutableLiveData<Throwable>()
    //加载中
    val loadingLiveData = MutableLiveData<Boolean>()

    fun launch(
        block: suspend () -> Unit,
        error: suspend (Throwable) -> Unit,
        complete: suspend () -> Unit
    ) {
        loadingLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.Main) {
            try {
                block()
            } catch (e: Exception) {
                e.printStackTrace()
                error(e)
            } finally {
                complete()
            }
        }
    }
}