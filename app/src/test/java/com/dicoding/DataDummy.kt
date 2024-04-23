package com.dicoding

import com.dicoding.storyapp.data.response.Story

object DataDummy {

    fun generateDummyStoryResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                i.toString(),
                "createdAt + $i",
                "name $i",
                "desctiption $i",
                i.toFloat(),
                "id $i",
                i.toFloat(),
            )
            items.add(story)
        }
        return items
    }
}
