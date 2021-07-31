package com.example.swudolistapp

import retrofit2.Call
import retrofit2.http.*

interface MyAPI {

    @FormUrlEncoded
    @POST("/app_login/")
    fun requestLogin(
            @Field("user_id") user_id: String,
            @Field("user_pw") user_pw: String,
    ): Call<PostItem>

    @FormUrlEncoded
    @POST("/app_register/")
    fun requestRegister(
        @Field("user_r_id") user_r_id: String,
        @Field("user_r_pw") user_r_pw: String,
        @Field("user_r_email") user_r_email: String,
        @Field("user_tot_subject") user_tot_subject: String,
    ): Call<PostItem>

    @FormUrlEncoded
    @POST("/app_logout/")
    fun requestLogout(
        @Field("user_lo_id") user_lo_id: String,
    ): Call<PostItem>

    @FormUrlEncoded
    @PUT("/app_update/")
    fun requestChange(
        @Field("user_update_id") user_update_id: String,
        @Field("user_update_subject") user_update_subject: String,
    ): Call<PostItem>

    @GET("/app_get_subject/")
    fun getSubjects(
//        @Query("user_subjects") user_subjects: String,
    ): Call<RequestSubject>

}