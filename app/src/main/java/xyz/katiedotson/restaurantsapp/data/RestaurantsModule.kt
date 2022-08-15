package xyz.katiedotson.restaurantsapp.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.katiedotson.restaurantsapp.data.api.RestaurantsApiService
import xyz.katiedotson.restaurantsapp.data.room.RestaurantDao
import xyz.katiedotson.restaurantsapp.data.room.RestaurantsDb
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RestaurantsModule {

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .also { it.level = HttpLoggingInterceptor.Level.BODY }
            ).build()
    }

    @Provides
    @Singleton
    fun provideRestaurantsApiService(client: OkHttpClient): RestaurantsApiService =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl("https://restaurants-app-c013d-default-rtdb.firebaseio.com/")
            .build().create(RestaurantsApiService::class.java)

    @Singleton
    @Provides
    fun provideRoomDatabase(
        @ApplicationContext appContext: Context
    ): RestaurantsDb {
        return Room.databaseBuilder(
            appContext,
            RestaurantsDb::class.java,
            "restaurants_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideRoomDao(database: RestaurantsDb): RestaurantDao {
        return database.dao
    }

}
