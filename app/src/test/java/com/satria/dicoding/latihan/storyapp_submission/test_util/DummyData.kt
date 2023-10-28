package com.satria.dicoding.latihan.storyapp_submission.test_util

import com.satria.dicoding.latihan.storyapp_submission.model.api_response.ListStoryItem

object DummyData {
    fun generateDummyStory(): List<ListStoryItem> {
        val items = arrayListOf<ListStoryItem>()
        for (i in 0..100) {
            val storyItem = ListStoryItem(
                "", "", "Name",
                "Description $i", "", "$i", ""
            )
            items.add(storyItem)
        }
        return items
    }
}