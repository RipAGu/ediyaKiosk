package com.example.stageusproject

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.CountDownLatch

object RetrofitClient {

    var instance: Retrofit? = null

    fun initRetrofit(): Retrofit? {
        if(instance == null){
            instance = Retrofit.Builder()
                .baseUrl("http://3.39.66.6:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return instance
    }
}

data class AccountData(
    var message : String,
    var success : Boolean
)

data class AccountLoginData(
    var message : String,
    var success : Boolean
)

data class AccountOverlap(
    var message : String,
    var success: Boolean
)

data class CategoryData(
    var message : String,
    var success : Boolean,
    var data : List<CategoryName>
)

data class CategoryName(
    var category_name : String
)

data class MenuData(
    var message : String,
    var success : Boolean,
    var data : List<MenuList>
)

data class MenuList(
    var menu_name : String,
    var menu_price : Int,
    var menu_image: String
)

data class PostOrder(
    var message: String,
    var success: Boolean
)

data class PostOrderData(
    var id: String,
    var order_list: List<OrderListData>,
    var total_price: Int
)

data class OrderListData(
    var name : String,
    var count : Int,
    var sum_price : Int
)

data class GetOrder(
    var message : String,
    var success : Boolean,
    var data : List<GetOrderData>
)

data class GetOrderData(
   var order_list : List<GetOrderList>,
   var total_price: Int

)

data class GetOrderList(
    var name : String,
    var count : Int,
    var sum_price : Int
)

interface RetrofitService {
    @POST("/account")
    fun postAccount(
        @Body body : HashMap<String, String>
    ) : Call<AccountData>

    @GET("/account/login")
    fun getAccount(
        @Query("id") id: String,
        @Query("pw") pw: String
    ) : Call<AccountLoginData>

    @GET("/account/overlap")
    fun getOverlap(
        @Query("id") id: String
    ): Call<AccountOverlap>

    @GET("/category")
    fun getCategory(
        @Query("lang") lang : String,
    ): Call<CategoryData>

    @GET("/category/menu")
    fun getMenu(
        @Query("category_name") category_name: String,
        @Query("lang") lang : String
    ) : Call<MenuData>

    @POST("/order")
    fun postOrder(
        @Body body : HashMap<String, Any>
    ) : Call<PostOrder>

    @GET("/order")
    fun getOrder(
        @Query("id") id : String
    ) : Call<GetOrder>


}

