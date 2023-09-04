package com.example.mywithingsapp.domain.entities

import android.os.Parcelable
import com.example.mywithingsapp.commons.utils.Constants.Companion.IMAGE_TAG
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PixabayApiResponse(@field:Json(name = IMAGE_TAG) val hits: List<Image>?) : Parcelable
