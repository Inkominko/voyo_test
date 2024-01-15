package com.niriko.voyotest.model

data class ApiResponse(
    val data: FrontData
)

data class FrontData(
    val front: FrontItem,
)

data class FrontItem(
    val nbDataParts: Int,
    val data: List<DataItem>
)

data class DataItem(
    val name: String,
    val payload: List<PayloadItem>
)

data class PayloadItem(
    val portraitImage: ImageUrl
)

data class ImageUrl(
    val src: String?
){
    fun getFormattedUrl(): String {
        return src?.replace("PLACEHOLDER", "298x441") ?: ""
    }
}