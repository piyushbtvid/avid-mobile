package com.faithForward.media.viewModel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u000bJ\u000e\u0010\u001c\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u000bJ\u000e\u0010\u001d\u001a\u00020\u001a2\u0006\u0010\u001e\u001a\u00020\u000bR \u0010\u0005\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R+\u0010\f\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@BX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b\u0011\u0010\u0012\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R#\u0010\u0013\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b0\u00070\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/faithForward/media/viewModel/HomeViewModel;", "Landroidx/lifecycle/ViewModel;", "networkRepository", "Lcom/faithForward/repository/NetworkRepository;", "(Lcom/faithForward/repository/NetworkRepository;)V", "_homepageData", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/faithForward/util/Resource;", "", "Lcom/faithForward/media/viewModel/HomePageItem;", "<set-?>", "", "contentRowFocusedIndex", "getContentRowFocusedIndex", "()I", "setContentRowFocusedIndex", "(I)V", "contentRowFocusedIndex$delegate", "Landroidx/compose/runtime/MutableState;", "homePageData", "Lkotlinx/coroutines/flow/StateFlow;", "getHomePageData", "()Lkotlinx/coroutines/flow/StateFlow;", "sampleCreatorCards", "Lcom/faithForward/media/home/creator/card/CreatorCardDto;", "fetchCreatorData", "", "sectionId", "fetchHomePageData", "onContentRowFocusedIndexChange", "value", "ff-media-tv-app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class HomeViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.faithForward.repository.NetworkRepository networkRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.faithForward.util.Resource<java.util.List<com.faithForward.media.viewModel.HomePageItem>>> _homepageData = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.faithForward.util.Resource<java.util.List<com.faithForward.media.viewModel.HomePageItem>>> homePageData = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState contentRowFocusedIndex$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.faithForward.media.home.creator.card.CreatorCardDto> sampleCreatorCards = null;
    
    @javax.inject.Inject()
    public HomeViewModel(@org.jetbrains.annotations.NotNull()
    com.faithForward.repository.NetworkRepository networkRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.faithForward.util.Resource<java.util.List<com.faithForward.media.viewModel.HomePageItem>>> getHomePageData() {
        return null;
    }
    
    public final int getContentRowFocusedIndex() {
        return 0;
    }
    
    private final void setContentRowFocusedIndex(int p0) {
    }
    
    public final void onContentRowFocusedIndexChange(int value) {
    }
    
    public final void fetchHomePageData(int sectionId) {
    }
    
    public final void fetchCreatorData(int sectionId) {
    }
}