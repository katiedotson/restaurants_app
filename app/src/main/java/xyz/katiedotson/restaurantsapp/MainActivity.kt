package xyz.katiedotson.restaurantsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import dagger.hilt.android.AndroidEntryPoint
import xyz.katiedotson.restaurantsapp.features.restaurantdetails.RestaurantDetailsScreen
import xyz.katiedotson.restaurantsapp.features.restaurantslist.RestaurantsScreen
import xyz.katiedotson.restaurantsapp.features.restaurantslist.RestaurantsViewModel
import xyz.katiedotson.restaurantsapp.ui.theme.RestaurantsAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RestaurantsAppTheme {
                RestaurantsApp()
            }
        }
    }
}

@Composable
private fun RestaurantsApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "restaurants") {
        composable(route = "restaurants") {
            val viewModel: RestaurantsViewModel = hiltViewModel()
            RestaurantsScreen(state = viewModel.state.value,
                onItemClick = { id ->
                    navController.navigate("restaurants/$id")
                },
                onFavoriteClick = { id, oldValue ->
                    viewModel.toggleFavorite(id, oldValue)
                }
            )
        }
        composable(
            route = "restaurants/{restaurant_id}",
            arguments = listOf(
                navArgument("restaurant_id") { type = NavType.IntType }
            ),
            deepLinks = listOf(
                navDeepLink { uriPattern = "www.restaurantsapp.details.com/{restaurant_id}" }
            )
        ) {
            RestaurantDetailsScreen()
        }
    }
}
