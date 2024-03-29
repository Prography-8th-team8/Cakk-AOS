package org.prography.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<ACTION : BaseAction, STATE : BaseState, SE : BaseSideEffect>(
    initialState: STATE,
) : ViewModel() {
    private val actionChannel: Channel<ACTION> = Channel()
    val state: StateFlow<STATE> = actionChannel.receiveAsFlow()
        .runningFold(initialState, ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, initialState)

    private val sideEffectChannel: Channel<SE> = Channel(Channel.UNLIMITED)
    val sideEffect: Flow<SE> = sideEffectChannel.receiveAsFlow()

    protected fun sendAction(action: ACTION) {
        viewModelScope.launch {
            actionChannel.send(action)
        }
    }

    abstract fun reduceState(currentState: STATE, action: ACTION): STATE

    protected fun sendSideEffect(sideEffect: SE) {
        viewModelScope.launch {
            sideEffectChannel.send(sideEffect)
        }
    }

    fun withState(intent: (STATE) -> Unit) = intent(state.value)

    override fun onCleared() {
        super.onCleared()
        actionChannel.close()
        sideEffectChannel.close()
    }
}
