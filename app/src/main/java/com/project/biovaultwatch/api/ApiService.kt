package com.project.biovaultwatch.api

import com.project.biovaultwatch.api.model.request.AuthenticationRequestModel
import com.project.biovaultwatch.api.model.request.EnrollRequestModel
import com.project.biovaultwatch.api.model.response.AuthenticationResponseModel
import com.project.biovaultwatch.api.model.response.EnrollResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    // 심박수 데이터 등록
    @POST("/api/register")
    fun enroll(
        @Body parameters: EnrollRequestModel
    ): Call<EnrollResponseModel>

    // 심박수 데이터를 통한 인증
    @POST("/api/authenticate")
    fun authenticate(
        @Body parameters: AuthenticationRequestModel
    ): Call<AuthenticationResponseModel>


}