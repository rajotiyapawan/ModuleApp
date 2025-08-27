package com.rajotiyapawan.pokedex.domain.model

data class RequestModel(
    val name: String? = null,
    val url: String? = null,
    val offset: Int? = null,
    val limit: Int? = null
)