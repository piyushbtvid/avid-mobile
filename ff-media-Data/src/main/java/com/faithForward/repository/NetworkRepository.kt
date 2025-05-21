package com.faithForward.repository

import com.faithForward.network.ApiServiceInterface
import javax.inject.Inject

class NetworkRepository @Inject constructor(
    private val apiServiceInterface: ApiServiceInterface
) {

    suspend fun getGivenSectionData(sectionId: Int) =
        apiServiceInterface.getGivenSectionData()

    suspend fun getCategories() = apiServiceInterface.getCategories()

//    suspend fun getGivenCategoryDetail(categoryId: Int) =
//        apiServiceInterface.getGivenSectionData(categoryId)

    suspend fun getCreatorsList() = apiServiceInterface.getCreatorsList()

}