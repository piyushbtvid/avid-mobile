package com.faithForward.util

object Constants {
     const val BASE_URL = "http://107.180.208.127:8000/api/"

   // const val BASE_URL = "http://107.180.208.127:8001/api/"
    const val CATEGORY_END_POINT = "http://107.180.208.127/api/get-category"
    const val CATEGORY_DETAIL_END_POINT = "category/{id}"
    const val HOME_SECTION_END_POINT = "client/v1/home"
    const val GIVEN_SECTION_END_POINT = "client/v1/{id}"
    const val GIVEN_ITEM_DETAIL_END_POINT = "client/v1/content/{slug}"
    const val SINGLE_GENRE_DETAIL_END_POINT = "client/v1/genres/{id}"
    const val CREATOR_END_POINT = "client/v1/creators/list"
    const val CREATOR_DETAIL_END_POINT = "client/v1/creators/{id}"
    const val CREATOR_CONTENT_LIST_END_POINT = "client/v1/creators/{id}/content"
    const val LOGIN_END_POINT = "client/v1/subscribers/login"
    const val SINGLE_SERIES_DETAIL_API = "client/v1/series/{id}"
    const val MY_LIST_END_POINT = "client/v1/my-list/{slug}"
    const val LIKE_DISLIKE_END_POINT = "client/v1/like-dislike/{slug}"
    const val LIKED_LIST_END_POINT = "client/v1/liked"
    const val DIS_lIKED_LIST_END_POINT = "client/v1/disliked"
}