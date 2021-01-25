package ir.maghsoodi.myvenues.data.models

import ir.maghsoodi.myvenues.data.models.Meta
import ir.maghsoodi.myvenues.data.models.Response

data class SearchResponse(
    val meta: Meta,
    val response: Response
)