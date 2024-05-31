package com.project.biovaultwatch.api.model.request

data class UnlockRequestModel(
    var id: String,
    var validation_code: Int
)