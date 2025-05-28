package com.faithForward.network.dto


data class HomeSectionApiResponse(
    val status: String?,
    val message: String?,
    val data: ContentData?
)

data class ContentData(
    val genres: List<Genre>?,
    val sections: List<Section>?
)

data class Genre(
    val name: String?,
    val id: String?
)

data class Section(
    val title: String?,
    val id: String?,
    val content: List<ContentItem>?
)


data class Creator(
    val id: Int,
    val name: String
)

data class ContentItem(
    val id: String?,
    val name: String?,
    val description: String?,
    val portrait: String?,
    val landscape: String?,
    val genres: List<Genre>?,
    val rating: String?,
    val dateUploaded: String?,
    val uploadedYear: String?,
    var access: String? = null,
    var video_link: String? = null,
    var views: Int? = null,
    var creator: Creator? = null
)