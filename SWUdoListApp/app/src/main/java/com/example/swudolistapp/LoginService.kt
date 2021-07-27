package com.example.swudolistapp

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService {

    @FormUrlEncoded
    @POST("/app_login/")
    fun requestLogin(
            @Field("user_id") user_id: String,
            @Field("user_pw") user_pw: String,
    ): Call<Login>

}

interface RegisterService{
    @FormUrlEncoded
    @POST("/app_register/")
    fun requestRegister(
            @Field("user_r_id") user_r_id: String,
            @Field("user_r_pw") user_r_pw: String,
            @Field("user_r_email") user_r_email:String,
    ): Call<Register>
}