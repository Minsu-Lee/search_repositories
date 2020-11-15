package com.jackson.repositories.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RepositoriesResponse (

    @SerializedName("total_count")
    var totalCount: Int = 0,

    @SerializedName("incomplete_results")
    var incompleteResults: Boolean

): Parcelable