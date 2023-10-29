package com.satria.dicoding.latihan.storyapp_submission.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.satria.dicoding.latihan.storyapp_submission.model.api_response.ListStoryItem

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(storyItems: List<ListStoryItem>)

    @Query("SELECT * FROM stories")
    fun getStories(): PagingSource<Int, ListStoryItem>

    @Query("DELETE FROM stories")
    suspend fun deleteAll()
}