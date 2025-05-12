package com.faithForward.media.viewModel

import com.faithForward.media.PosterCardDto
import com.faithForward.media.PosterRowDto
import com.faithForward.media.carousel.CarouselContentRowDto
import com.faithForward.media.carousel.CarouselItemDto
import com.faithForward.network.dto.Item
import com.faithForward.network.dto.Section
import com.faithForward.network.dto.SectionApiResponse

sealed interface HomePageItem {
    data class CarouselRow(val dto: CarouselContentRowDto) : HomePageItem
    data class PosterRow(val dto: PosterRowDto) : HomePageItem
}


fun SectionApiResponse.toHomePageItems(): List<HomePageItem> {
    return data
        .sortedBy { if (it.type == "Carousel") 0 else 1 } // Prioritize Carousel
        .map { it.toHomePageItem() }
}

fun Section.toHomePageItem(): HomePageItem {
    return when (type) {
        "Carousel" -> {
            val carouselItemsDto = items.map { item: Item ->
                // Assuming Item has a method or property to convert to CarouselItemDto
                item.toCarouselItemDto() // Replace with actual conversion logic
            }
            HomePageItem.CarouselRow(CarouselContentRowDto(carouselItemsDto))
        }

        else -> {
            val posterItemsDto = items.map { item: Item ->
                // Assuming Item has a method or property to convert to PosterCardDto
                item.toPosterCardDto() // Replace with actual conversion logic
            }
            HomePageItem.PosterRow(
                PosterRowDto(
                    heading = title,
                    dtos = posterItemsDto
                )
            ) // Replace 'title' with actual heading source
        }
    }
}

fun Item.toCarouselItemDto(): CarouselItemDto =
    CarouselItemDto(
        description = description,
        duration = duration,
        imdbRating = imdbRating,
        imgSrc = posterImage,
    )


fun Item.toPosterCardDto(): PosterCardDto = PosterCardDto(posterImageSrc = posterImage)

