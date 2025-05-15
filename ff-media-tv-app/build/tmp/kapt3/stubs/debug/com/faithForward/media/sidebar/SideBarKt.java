package com.faithForward.media.sidebar;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000.\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u001at\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000b2\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\r2\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\r2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\rH\u0007\u001a\b\u0010\u0010\u001a\u00020\u0001H\u0007\u00a8\u0006\u0011"}, d2 = {"SideBar", "", "modifier", "Landroidx/compose/ui/Modifier;", "columnList", "", "Lcom/faithForward/media/sidebar/SideBarItem;", "sideBarFocusedIndex", "", "sideBarSelectedPosition", "isSideBarFocusable", "", "onSideBarFocusedIndexChange", "Lkotlin/Function1;", "onSideBarSelectedPositionChange", "onSideBarItemClick", "SideBarPreview", "ff-media-tv-app_debug"})
public final class SideBarKt {
    
    @androidx.compose.runtime.Composable()
    public static final void SideBar(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    java.util.List<com.faithForward.media.sidebar.SideBarItem> columnList, int sideBarFocusedIndex, int sideBarSelectedPosition, boolean isSideBarFocusable, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onSideBarFocusedIndexChange, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onSideBarSelectedPositionChange, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.faithForward.media.sidebar.SideBarItem, kotlin.Unit> onSideBarItemClick) {
    }
    
    @androidx.compose.ui.tooling.preview.Preview(showBackground = true, showSystemUi = true)
    @androidx.compose.runtime.Composable()
    public static final void SideBarPreview() {
    }
}