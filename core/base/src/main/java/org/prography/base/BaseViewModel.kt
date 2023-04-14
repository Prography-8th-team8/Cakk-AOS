import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import org.prography.base.BaseIntent
import org.prography.base.BaseSideEffect
import org.prography.base.BaseState

abstract class BaseViewModel<INTENT : BaseIntent, STATE : BaseState, SE : BaseSideEffect> : ViewModel() {
    protected val intentChannel: Channel<INTENT> = Channel()
    private val sideEffectsChannel: Channel<SE> = Channel()

    abstract val state: StateFlow<STATE>
    val sideEffects = sideEffectsChannel.receiveAsFlow()

    suspend fun sendIntent(intent: INTENT){
        intentChannel.send(intent)
    }

    abstract fun reduceState(state: STATE, intent: INTENT): STATE

    protected suspend fun sendSideEffect(sideEffect: SE){
        sideEffectsChannel.send(sideEffect)
    }
}
