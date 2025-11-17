package com.faithForward.media.viewModel.uiModels

import android.util.Log
import com.faithForward.media.ui.commanComponents.CategoryComposeDto
import com.faithForward.media.ui.commanComponents.PosterCardDto
import com.faithForward.media.ui.sections.common_ui.carousel.CarouselContentRowDto
import com.faithForward.media.ui.sections.common_ui.carousel.CarouselItemDto
import com.faithForward.media.ui.sections.common_ui.category.CategoryRowDto
import com.faithForward.media.ui.sections.common_ui.content.PosterRowDto
import com.faithForward.media.ui.sections.creator.card.CreatorCardDto
import com.faithForward.media.util.formatDuration
import com.faithForward.network.dto.ContentItem
import com.faithForward.network.dto.HomeSectionApiResponse
import com.faithForward.network.dto.SectionContentResponse
import com.faithForward.network.dto.creator.UserData
import com.faithForward.network.dto.myList.MyListResponse
import com.faithForward.util.MyFavList

sealed interface HomePageItem {
    data class CarouselRow(val dto: CarouselContentRowDto) : HomePageItem
    data class PosterRow(val dto: PosterRowDto) : HomePageItem
    data class CategoryRow(val dto: CategoryRowDto) : HomePageItem
    data class CreatorGrid(val dto: List<CreatorCardDto>) : HomePageItem
}


fun List<UserData>.toCreatorCardDtoList(): List<CreatorCardDto> {
    return this.map { user ->
        CreatorCardDto(
            creatorImageUrl = user.profileImg.orEmpty(), // fallback to empty string if null
            channelBannerImage = user.channelBanner,
            creatorName = user.name,
            creatorSubscriberText = "${user.channelSubscribers} Subscribers",
            channelDescription = user.bio
                ?: "", // using bio instead of channel_description as it is coming null
            id = user.id
        )
    }
}

fun HomeSectionApiResponse.toHomePageItems(): List<HomePageItem> {
    Log.e("HOME_DATA_LOAD", "to HomePageItems called in uiModel ")
    val sections = data
    val homePageItems = mutableListOf<HomePageItem>()
    var carouselAdded = false
    var carouselSectionIndex: Int? = null

    val pillCategories = sections?.pillCategories?.mapNotNull { category ->
        category.name?.takeIf { it.isNotBlank() }?.let {
            CategoryComposeDto(it, id = category.id?.toString().orEmpty())
        }
    }?.takeIf { it.isNotEmpty() }

    val genreCategories = sections?.genres?.mapNotNull { genre ->
        genre.name?.takeIf { it.isNotBlank() }?.let {
            CategoryComposeDto(it, id = genre.id?.toString().orEmpty())
        }
    }?.takeIf { it.isNotEmpty() }

    val categoryRowItems = pillCategories ?: genreCategories
    if (categoryRowItems != null) {
        homePageItems.add(HomePageItem.CategoryRow(CategoryRowDto(categoryRowItems)))
    }
    // Find the first section with a non-null/non-empty first item for carousel
    sections?.sections?.forEachIndexed { index, section ->
        if (!carouselAdded && section.content?.isNotEmpty() == true && section.content?.first() != null && section.content?.first()
                ?.toCarouselItemDto() != null
        ) {
            section.content?.first()?.toCarouselItemDto()?.run {
                homePageItems.add(
                    HomePageItem.CarouselRow(
                        CarouselContentRowDto(
                            listOf(this)
                        )
                    )
                )
            }
            carouselAdded = true
            carouselSectionIndex = index
        }
    }

    // Process all sections for PosterRow
    sections?.sections?.forEachIndexed { index, section ->
        // Skip if section or items are null/empty
        if (section.content.isNullOrEmpty()) return@forEachIndexed

        // Determine which items to include in PosterRow
        val posterItems = if (index == carouselSectionIndex) {
            // For the carousel section, include all items except the first
            section.content?.drop(1)?.filterNotNull()?.map { it.toPosterCardDto() }
        } else {
            // For other sections, include all non-null items
            section.content?.filterNotNull()?.map { it.toPosterCardDto() }
        }

        // Add PosterRow if there are items to display
        if (!posterItems.isNullOrEmpty()) {
            homePageItems.add(
                HomePageItem.PosterRow(
                    PosterRowDto(
                        heading = section.title ?: "",
                        dtos = posterItems,
                        rowId = section.id ?: ""
                    )
                )
            )
        }
    }

    return homePageItems
}

fun SectionContentResponse.toHomePageItems(
    rowHeading: String,
): List<HomePageItem> {
    val homePageItems = mutableListOf<HomePageItem>()

    if (data.isNotEmpty()) {
        // First item → Carousel
        val firstItem = data.first().toCarouselItemDto()
        homePageItems.add(
            HomePageItem.CarouselRow(
                CarouselContentRowDto(listOf(firstItem))
            )
        )

        // Remaining items → PosterRow (combined)
        val posterItems = data.drop(1).map { it.toPosterCardDto() }
        if (posterItems.isNotEmpty()) {
            homePageItems.add(
                HomePageItem.PosterRow(
                    PosterRowDto(
                        heading = rowHeading, // Or dynamic heading if available
                        dtos = posterItems,
                        rowId = rowHeading
                    )
                )
            )
        }
    }

    return homePageItems
}

fun MyListResponse.toHomePageItems(): List<HomePageItem> {
    val homePageItems = mutableListOf<HomePageItem>()

    var hasAddedCarousel = false

    data.forEach { category ->
        val items = category.content

        if (items.isNotEmpty()) {
            val adjustedItems = if (!hasAddedCarousel) {
                // Add CarouselRow with the first item
                homePageItems.add(
                    HomePageItem.CarouselRow(
                        CarouselContentRowDto(listOf(items.first().toCarouselItemDto()))
                    )
                )
                hasAddedCarousel = true
                items.drop(1) // drop only from first non-empty list
            } else {
                items
            }

            // Convert remaining items to PosterCardDto
            val posterItems = adjustedItems.map { it.toPosterCardDto() }

            if (posterItems.isNotEmpty()) {
                homePageItems.add(
                    HomePageItem.PosterRow(
                        PosterRowDto(
                            heading = category.title,
                            dtos = posterItems,
                            rowId = category.id
                        )
                    )
                )
            }
        }
    }

    return homePageItems
}


fun ContentItem.toCarouselItemDto(): CarouselItemDto {
    val likedList = MyFavList.likedList
    val myList = MyFavList.myFavList
    val disLikeList = MyFavList.disLikedList

    Log.e("CAROUSEL_CONTENT", "content type in Carsouel is $contentType")

// check based on .content (flattening the nested list)
    val isFavourite = myList?.flatMap { it.content }?.any {
        it.id.toString() == this.id.toString()
    } == true

    val isLiked =
        likedList?.flatMap { it.content }?.any { it.id.toString() == this.id.toString() } == true


    val isDisliked = disLikeList?.flatMap { it.content }
        ?.any { it.id.toString() == this.id.toString() } == true && !isLiked

    return CarouselItemDto(
        description = description,
        duration = null,
        imdbRating = rating,
        imgSrc = landscape,
        releaseDate = uploadedYear,
        title = name,
        slug = slug,
        genre = genres?.mapNotNull { it.name }  // safely extract non-null names
            ?.joinToString(", "),
        isLiked = isLiked,
        isDisliked = isDisliked,
        isFavourite = isFavourite,
        contentType = contentType,
        seriesSlug = seriesSlug
    )

}


fun ContentItem.toPosterCardDto(): PosterCardDto =
    PosterCardDto(
        posterImageSrc = portrait ?: landscape ?: "",
        id = id?.toString().orEmpty(),
        title = name ?: "",
        description = description ?: "",
        genre = genres?.mapNotNull { it.name }  // safely extract non-null names
            ?.joinToString(", "),
        seasons = seasons?.size,
        duration = duration?.toLongOrNull()?.let { formatDuration(it) } ?: "",
        imdbRating = rating,
        releaseDate = dateUploaded,
        videoHlsUrl = videoLink,
        slug = slug,
        seriesSlug = seriesSlug,
        progress = progressSeconds,
        contentType = contentType,
        uploadYear = uploadedYear,
        landScapeImg = landscape ?: ""
    )

fun CreatorCardDto.toCarouselItemDto(): CarouselItemDto {
    return CarouselItemDto(
        id = id.toString(),
        imgSrc = channelBannerImage,
        description = channelDescription,
        subscribers = creatorSubscriberText,
        title = creatorName,
        isCreator = true
    )
}

fun CarouselItemDto.toPosterCardDto(): PosterCardDto {
    return PosterCardDto(
        id = id,
        slug = slug,
        posterImageSrc = imgSrc ?: "",
        title = title ?: "",
        description = description ?: "",
        genre = genre,
        seasons = seasons,
        duration = duration,
        imdbRating = imdbRating,
        releaseDate = releaseDate,
        contentType = contentType,
        seriesSlug = seriesSlug,
        landScapeImg = imgSrc ?: ""
    )
}


sealed class CarouselClickUiState {
    data object Idle : CarouselClickUiState()
    data class NavigateToPlayer(
        val posterCardDto: PosterCardDto,
        val isFromContinueWatching: Boolean = false,
    ) :
        CarouselClickUiState()
}

sealed class ToPlayerBackDetailUiState {
    data object Idle : ToPlayerBackDetailUiState()
    data class NavigateToPlayer(val posterCardDto: PosterCardDto) : ToPlayerBackDetailUiState()
}


