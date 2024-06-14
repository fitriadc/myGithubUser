package com.example.githubuser.data.remote.retrofit
import com.example.githubuser.data.remote.response.Response
import com.example.githubuser.data.remote.response.UserDetailResponse
import com.example.githubuser.data.remote.response.UserItems
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUser(
        @Query("q") username: String
    ): Call<Response>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getDetailUser(@Path("username") username: String): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getListUserFollower(@Path("username") username: String): Call<List<UserItems>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getListUserFollowing(@Path("username") username: String): Call<List<UserItems>>

}