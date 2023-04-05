package co.youverify.youhr.presentation.ui.task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import co.youverify.youhr.presentation.TaskDetail
import co.youverify.youhr.presentation.ui.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(private val stateHandle: SavedStateHandle, private val navigator: Navigator) : ViewModel(){



    var taskMessage by mutableStateOf("")

    fun updateTaskMessage(newValue:String){
        taskMessage=newValue
    }



}
