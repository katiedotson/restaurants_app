package xyz.katiedotson.restaurantsapp.features.restaurantslist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import xyz.katiedotson.restaurantsapp.data.MainDispatcher
import xyz.katiedotson.restaurantsapp.domain.usecase.GetInitialRestaurantsUseCase
import xyz.katiedotson.restaurantsapp.domain.usecase.ToggleRestaurantUseCase
import javax.inject.Inject

@HiltViewModel
class RestaurantsViewModel @Inject constructor(
    private val getInitialRestaurantsUseCase: GetInitialRestaurantsUseCase,
    private val toggleRestaurantsUseCase: ToggleRestaurantUseCase,
    @MainDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state = mutableStateOf(
        RestaurantsScreenState(
            restaurants = listOf(),
            isLoading = true
        )
    )
    val state: State<RestaurantsScreenState> get() = _state
    private val errorHandler =
        CoroutineExceptionHandler { _, exception ->
            exception.printStackTrace()
            _state.value = _state.value.copy(
                error = exception.message,
                isLoading = false
            )
        }

    init {
        viewModelScope.launch(context = errorHandler + dispatcher) {
            _state.value = RestaurantsScreenState(restaurants = getInitialRestaurantsUseCase(), isLoading = false)
        }
    }

    fun toggleFavorite(id: Int, oldValue: Boolean) {
        viewModelScope.launch(context = errorHandler + dispatcher) {
            val updatedRestaurants = toggleRestaurantsUseCase(id, oldValue)
            _state.value = _state.value.copy(restaurants = updatedRestaurants)
        }
    }

}
