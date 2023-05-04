package com.example.fufu.data.repository

import com.example.fufu.data.network.RetrofitHelper
import com.example.fufu.data.network.restaurant.RestaurantApi

class RestaurantRepository {
    private val restaurantApi: RestaurantApi = RetrofitHelper.getInstance().create(RestaurantApi::class.java)

    suspend fun getRestaurantByResId(resId: String) {
        restaurantApi.getRestaurantByResId(resId)
    }
}