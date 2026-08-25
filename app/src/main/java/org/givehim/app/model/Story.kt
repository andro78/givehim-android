package org.givehim.app.model

data class Story(val id: String, val title: String, val summary: String, val nickname: String, val category: String, val publishedAt: String)
data class StoryDraft(val category: String, val title: String, val nickname: String, val contact: String, val story: String, val helpNeeded: String, val consent: Boolean = true)
