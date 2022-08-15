package xyz.katiedotson.restaurantsapp.features.restaurantslist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import xyz.katiedotson.restaurantsapp.domain.usecase.GetInitialRestaurantsUseCase
import xyz.katiedotson.restaurantsapp.domain.usecase.ToggleRestaurantUseCase
import javax.inject.Inject

@HiltViewModel
class RestaurantsViewModel @Inject constructor(
    private val getInitialRestaurantsUseCase: GetInitialRestaurantsUseCase,
    private val toggleRestaurantsUseCase: ToggleRestaurantUseCase
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
        viewModelScope.launch(errorHandler) {
            _state.value = RestaurantsScreenState(restaurants = getInitialRestaurantsUseCase(), isLoading = false)
        }
    }

    fun toggleFavorite(id: Int, oldValue: Boolean) {
        viewModelScope.launch {
            val updatedRestaurants = toggleRestaurantsUseCase(id, oldValue)
            _state.value = _state.value.copy(restaurants = updatedRestaurants)
        }
    }

}
