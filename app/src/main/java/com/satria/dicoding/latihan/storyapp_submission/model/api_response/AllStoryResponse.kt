package com.satria.dicoding.latihan.storyapp_submission.model.api_response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AllStoryResponse(

    @field:SerializedName("listStory")
    val listStory: List<ListStoryItem> = arrayListOf(),

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
) : Parcelable

@Parcelize
@Entity(tableName = "stories")
data class ListStoryItem(

    @field:SerializedName("photoUrl")
    val photoUrl: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("lon")
    val lon: String? = null,

    @PrimaryKey
    @field:SerializedName("id")
    val id: String = "0",

    @field:SerializedName("lat")
    val lat: String? = null
) : Parcelable
