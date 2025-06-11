package com.faithForward.media.home.creator.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faithForward.media.viewModel.CreatorDetailViewModel
import com.faithForward.media.viewModel.uiModels.CreatorDetailItem
import com.faithForward.util.Resource

@Composable
fun CreatorDetailScreen(
    modifier: Modifier = Modifier,
    creatorDetailViewModel: CreatorDetailViewModel,
) {
    val creatorDetail by creatorDetailViewModel.creatorDetailPageData.collectAsStateWithLifecycle()


    when (creatorDetail) {

        is Resource.Loading, is Resource.Unspecified -> {
            // Add loading indicator here if required
            return
        }

        is Resource.Error -> {
            // Error UI here if required
            return
        }

        is Resource.Success -> {
            val creatorDetailPageDto = creatorDetail.data as? CreatorDetailItem.CreatorDetail

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                if (creatorDetailPageDto?.creatorDetailDto != null) {
                    CreatorDetailPage(
                        modifier = Modifier.padding(start = 31.dp, top = 20.dp),
                        creatorDetailDto = creatorDetailPageDto.creatorDetailDto
                    )
                }


            }
        }

    }

}