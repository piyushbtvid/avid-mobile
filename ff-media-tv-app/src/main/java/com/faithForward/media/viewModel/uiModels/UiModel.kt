package com.faithForward.media.viewModel.uiModels

import com.faithForward.media.commanComponents.CategoryComposeDto
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.home.carousel.CarouselContentRowDto
import com.faithForward.media.home.carousel.CarouselItemDto
import com.faithForward.media.home.category.CategoryRowDto
import com.faithForward.media.home.content.PosterRowDto
import com.faithForward.media.home.creator.card.CreatorCardDto
import com.faithForward.network.dto.CategoryResponse
import com.faithForward.network.dto.ContentItem
import com.faithForward.network.dto.HomeSectionApiResponse
import com.faithForward.network.dto.SectionContentResponse
import com.faithForward.network.dto.creator.UserData
import com.faithForward.network.dto.myList.MyListResponse

sealed interface HomePageItem {
    data class CarouselRow(val dto: CarouselContentRowDto) : HomePageItem
    data class PosterRow(val dto: PosterRowDto) : HomePageItem
    data class CategoryRow(val dto: CategoryRowDto) : HomePageItem
    data class CreatorGrid(val dto: List<CreatorCardDto>) : HomePageItem
}


fun CategoryResponse.toCategoryRow(): HomePageItem.CategoryRow {
    val categoryDtos = data.map {
        CategoryComposeDto(it.name, id = it.id.toString())
    }
    return HomePageItem.CategoryRow(dto = CategoryRowDto(categoryDtos))
}

fun List<UserData>.toCreatorCardDtoList(): List<CreatorCardDto> {
    return this.map { user ->
        CreatorCardDto(
            creatorImageUrl = user.profileImg.orEmpty(), // fallback to empty string if null
            creatorName = user.name,
            creatorSubscriberText = "${user.channelSubscribers} Subscribers",
            channelDescription = user.channelDescription ?: ""
        )
    }
}

fun HomeSectionApiResponse.toHomePageItems(): List<HomePageItem> {
    val sections = data
    val homePageItems = mutableListOf<HomePageItem>()
    var carouselAdded = false
    var carouselSectionIndex: Int? = null

    val category = sections?.genres?.mapIndexed { index, genre ->
        CategoryComposeDto(genre.name ?: "", id = genre.id ?: "")
    }
    if (category != null) {
        homePageItems.add(HomePageItem.CategoryRow(CategoryRowDto(category)))
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
                        heading = section.title ?: "", dtos = posterItems
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
                        dtos = posterItems
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
                            heading = category.title, dtos = posterItems
                        )
                    )
                )
            }
        }
    }

    return homePageItems
}


fun ContentItem.toCarouselItemDto(): CarouselItemDto {
    // Return the DTO
    return CarouselItemDto(
        description = description,
        duration = null,
        imdbRating = rating,
        imgSrc = landscape,
        releaseDate = uploadedYear,
        title = name
    )
}


fun ContentItem.toPosterCardDto(): PosterCardDto =
    PosterCardDto(posterImageSrc = landscape ?: portrait ?: "",
        id = id ?: "",
        title = name ?: "",
        description = description ?: "",
        genre = genres?.mapNotNull { it.name }  // safely extract non-null names
            ?.joinToString(", "),
        seasons = seasons?.size,
        duration = duration.toString(),
        imdbRating = rating,
        releaseDate = dateUploaded,
        videoHlsUrl = video_link)

fun CreatorCardDto.toCarouselItemDto(): CarouselItemDto {
    return CarouselItemDto(
        imgSrc = creatorImageUrl,
        description = channelDescription,
        subscribers = creatorSubscriberText,
        title = creatorName,
        isCreator = true
    )
}


