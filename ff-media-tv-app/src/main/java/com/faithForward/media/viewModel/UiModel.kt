package com.faithForward.media.viewModel

import com.faithForward.media.CategoryRowDto
import com.faithForward.media.PosterCardDto
import com.faithForward.media.PosterRowDto
import com.faithForward.media.carousel.CarouselContentRowDto
import com.faithForward.media.carousel.CarouselItemDto
import com.faithForward.media.components.CategoryComposeDto
import com.faithForward.network.dto.CategoryResponse
import com.faithForward.network.dto.Item
import com.faithForward.network.dto.Section
import com.faithForward.network.dto.SectionApiResponse
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

sealed interface HomePageItem {
    data class CarouselRow(val dto: CarouselContentRowDto) : HomePageItem
    data class PosterRow(val dto: PosterRowDto) : HomePageItem
    data class CategoryRow(val dto: CategoryRowDto) : HomePageItem
}

fun CategoryResponse.toCategoryRow(): HomePageItem.CategoryRow {
    val categoryDtos = data.map {
        CategoryComposeDto(it.name)
    }
    return HomePageItem.CategoryRow(dto = CategoryRowDto(categoryDtos))
}


fun SectionApiResponse.toHomePageItems(): List<HomePageItem> {
    return data.sortedBy { if (it.type == "Carousel") 0 else 1 } // Prioritize Carousel
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
                    heading = title, dtos = posterItemsDto
                )
            ) // Replace 'title' with actual heading source
        }
    }
}

fun Item.toCarouselItemDto(): CarouselItemDto {
    // Convert duration (e.g., "1451" seconds → "24:11")
    val formattedDuration = duration?.toIntOrNull()?.let { totalSeconds ->
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        when {
            hours > 0 -> "%d hr %02d min %02d s".format(hours, minutes, seconds)
            minutes > 0 -> "%d min %02d s".format(minutes, seconds)
            else -> "%d s".format(seconds)
        }
    } ?: ""


    // Convert releaseDate (epoch seconds → "dd MMM yyyy")
    val formattedReleaseDate = try {
        val epochMillis = releaseDate * 1000
        val date = Date(epochMillis)
        val formatter = SimpleDateFormat(
            "dd MMM yyyy", Locale.getDefault()
        )
        formatter.format(date)
    } catch (e: Exception) {
        ""
    }


    // Return the DTO
    return CarouselItemDto(
        description = description,
        duration = formattedDuration,
        imdbRating = imdbRating,
        imgSrc = posterImage,
        releaseDate = formattedReleaseDate,
        title = title
    )
}


fun Item.toPosterCardDto(): PosterCardDto = PosterCardDto(posterImageSrc = posterImage)

