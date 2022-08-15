package xyz.katiedotson.restaurantsapp.features.restaurantdetails

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.katiedotson.restaurantsapp.data.api.RemoteRestaurant
import xyz.katiedotson.restaurantsapp.data.api.RestaurantsApiService
import xyz.katiedotson.restaurantsapp.domain.model.Restaurant

class RestaurantDetailsViewModel(private val stateHandle: SavedStateHandle) : ViewModel() {

    private var restInterface: RestaurantsApiService

    val state = mutableStateOf<Restaurant?>(null)

    private val errorHandler =
        CoroutineExceptionHandler { _, exception ->
            exception.printStackTrace()
        }

    init {
        val retrofit: Retrofit =
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://restaurants-app-c013d-default-rtdb.firebaseio.com/")
                .build()
        restInterface = retrofit.create(RestaurantsApiService::class.java)
        val id = stateHandle.get<Int>("restaurant_id") ?: 0
        viewModelScope.launch(errorHandler) {
            val restaurant = getRemoteRestaurant(id)
            state.value = Restaurant(id = restaurant.id, title = restaurant.title, description = restaurant.description, isFavorite = false)
        }
    }

    private suspend fun getRemoteRestaurant(id: Int): RemoteRestaurant {
        return withContext(Dispatchers.IO) {
            val responseMap = restInterface.getRestaurant(id)
            return@withContext responseMap.values.first()
        }
    }

}
