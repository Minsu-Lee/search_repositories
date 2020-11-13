package com.jackson.repositoriestest.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RepositoriesPermissions (

    @SerializedName("admin")
    var admin: Boolean,

    @SerializedName("push")
    var push: Boolean,

    @SerializedName("pull")
    var pull: Boolean,

): Parcelable