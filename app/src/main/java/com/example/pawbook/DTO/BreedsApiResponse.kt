package com.example.pawbook.DTO

data class BreedsApiResponse(
    val message: Map<String, List<String>>,
    val status: String
)