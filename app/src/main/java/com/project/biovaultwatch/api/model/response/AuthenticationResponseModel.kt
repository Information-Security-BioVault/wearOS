package com.project.biovaultwatch.api.model.response

data class AuthenticationResponseModel(
    var message: String,
    var watch_id: String,
    var result: String,
    var validation_code: Int
)