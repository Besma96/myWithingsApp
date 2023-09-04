package com.example.mywithingsapp.domain.entities

import android.os.Parcelable
import com.example.mywithingsapp.commons.utils.Constants
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(
    @field:Json(name = Constants.ID_TAG) val id: Long?,
    @field:Json(name = Constants.LARGE_IMAGE_URL_TAG) val largeImageURL: String?,
    @field:Json(name = Constants.LIKES_TAG) val likes: Long?,
    @field:Json(name = Constants.COMMENTS_TAG) val comments: Long?,
    @field:Json(name = Constants.TAGS_TAG) val tags: String?,
    @field:Json(name = Constants.USER_TAG) val user: String?,
    @field:Json(name = Constants.FAVORITES_TAG) val favorites: Long?,
    @field:Json(name = Constants.PREVIEW_URL_TAG) val previewURL: String?
) : Parcelable {
    var isSelected: Boolean? = false
}
