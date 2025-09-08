package com.faithForward.media.ui.player.relatedContent

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PlayerRelatedContentMobile(
    modifier: Modifier = Modifier,
    isVisible: Boolean = false,
    onItemClick: (RelatedContentItemDto?, List<RelatedContentItemDto>?, index: Int?) -> Unit,
    onDismiss: () -> Unit,
    playerRelatedContentRowDto: PlayerRelatedContentRowDto,
) {
    val density = LocalDensity.current
    val screenHeight = LocalContext.current.resources.displayMetrics.heightPixels
    val screenHeightDp = with(density) { screenHeight.toDp() }
    
    var offsetY by remember { mutableFloatStateOf(screenHeightDp.value) }
    var isDragging by remember { mutableStateOf(false) }
    
    // Animate offset when visibility changes
    val animatedOffsetY by animateFloatAsState(
        targetValue = if (isVisible) 0f else screenHeightDp.value,
        animationSpec = tween(300),
        label = "bottomSheetOffset"
    )
    
    // Update offset when not dragging
    LaunchedEffect(animatedOffsetY, isDragging) {
        if (!isDragging) {
            offsetY = animatedOffsetY
        }
    }
    
    // Show/hide based on visibility
    if (isVisible || offsetY < screenHeightDp.value) {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            // Semi-transparent background overlay
            if (isVisible) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { isDragging = true },
                                onDragEnd = { 
                                    isDragging = false
                                    // If user swipes down on background, dismiss
                                    if (offsetY > screenHeightDp.value * 0.1f) {
                                        onDismiss()
                                    } else {
                                        offsetY = 0f
                                    }
                                }
                            ) { _, dragAmount ->
                                val dragAmountDp = with(density) { dragAmount.y.toDp().value }
                                val newOffset = offsetY + dragAmountDp
                                
                                // Only allow downward swipes on background
                                if (dragAmountDp > 0) {
                                    offsetY = newOffset.coerceAtLeast(0f)
                                }
                            }
                        }
                )
            }
            
            // Bottom sheet card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .offset(y = with(density) { offsetY.dp })
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.Black),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeightDp * 0.6f) // 60% of screen height
                ) {
                    // Drag handle at the top
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragStart = { isDragging = true },
                                    onDragEnd = { 
                                        isDragging = false
                                        // If dragged down more than 1/3 of screen height, dismiss
                                        if (offsetY > screenHeightDp.value * 0.3f) {
                                            onDismiss()
                                        } else {
                                            // Snap back to top
                                            offsetY = 0f
                                        }
                                    }
                                ) { _, dragAmount ->
                                    val dragAmountDp = with(density) { dragAmount.y.toDp().value }
                                    val newOffset = offsetY + dragAmountDp
                                    
                                    // Allow dragging down to dismiss, but prevent dragging up beyond the top
                                    offsetY = newOffset.coerceAtLeast(0f)
                                    
                                    // If user is dragging down significantly, provide immediate feedback
                                    if (dragAmountDp > 0 && newOffset > screenHeightDp.value * 0.2f) {
                                        // User is dragging down - allow it to continue
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(4.dp)
                                .background(
                                    Color.White.copy(alpha = 0.5f),
                                    RoundedCornerShape(2.dp)
                                )
                        )
                    }
                    
                    // Use existing PlayerRelatedContentRow component
                    PlayerRelatedContentRow(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 28.dp), // Add padding to account for drag handle
                        playerRelatedContentRowDto = playerRelatedContentRowDto,
                        onItemClick = onItemClick,
                        onUp = {
                            onDismiss()
                            true
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlayerRelatedContentMobilePreview() {
    PlayerRelatedContentMobile(
        isVisible = true,
        onItemClick = { _, _, _ -> },
        onDismiss = { },
        playerRelatedContentRowDto = PlayerRelatedContentRowDto(
            title = "Related Videos",
            rowList = listOf(
                RelatedContentItemDto(
                    image = "",
                    id = "1",
                    slug = "test-1",
                    title = "Test Video 1",
                    description = "This is a test video description",
                    contentType = "Movie"
                ),
                RelatedContentItemDto(
                    image = "",
                    id = "2",
                    slug = "test-2",
                    title = "Test Video 2",
                    description = "Another test video description",
                    contentType = "Series"
                )
            )
        )
    )
}
