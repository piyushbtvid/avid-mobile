package com.faithForward.media.viewModel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u001f\u001a\u00020 J\u000e\u0010!\u001a\u00020 2\u0006\u0010\"\u001a\u00020\u0013J\u000e\u0010#\u001a\u00020 2\u0006\u0010$\u001a\u00020\u0013R\u001c\u0010\u0005\u001a\u0010\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\t\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\f\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\n0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001f\u0010\u000e\u001a\u0010\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\u00070\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R+\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0012\u001a\u00020\u00138F@BX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b\u0019\u0010\u001a\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R#\u0010\u001b\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\u00070\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0011R#\u0010\u001d\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\n0\u00070\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0011R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"Lcom/faithForward/media/viewModel/HomeViewModel;", "Landroidx/lifecycle/ViewModel;", "networkRepository", "Lcom/faithForward/repository/NetworkRepository;", "(Lcom/faithForward/repository/NetworkRepository;)V", "_categoriesList", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/faithForward/util/Resource;", "Lcom/faithForward/network/dto/CategoryResponse;", "_creatorList", "", "Lcom/faithForward/network/dto/creator/UserData;", "_homepageData", "Lcom/faithForward/media/viewModel/HomePageItem;", "categoriesList", "Lkotlinx/coroutines/flow/StateFlow;", "getCategoriesList", "()Lkotlinx/coroutines/flow/StateFlow;", "<set-?>", "", "contentRowFocusedIndex", "getContentRowFocusedIndex", "()I", "setContentRowFocusedIndex", "(I)V", "contentRowFocusedIndex$delegate", "Landroidx/compose/runtime/MutableState;", "creatorList", "getCreatorList", "homePageData", "getHomePageData", "fetchCreatorPageData", "", "fetchHomePageData", "sectionId", "onContentRowFocusedIndexChange", "value", "ff-media-tv-app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class HomeViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.faithForward.repository.NetworkRepository networkRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.faithForward.util.Resource<java.util.List<com.faithForward.media.viewModel.HomePageItem>>> _homepageData = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.faithForward.util.Resource<java.util.List<com.faithForward.media.viewModel.HomePageItem>>> homePageData = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.faithForward.util.Resource<java.util.List<com.faithForward.network.dto.creator.UserData>>> _creatorList = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.faithForward.util.Resource<java.util.List<com.faithForward.network.dto.creator.UserData>>> creatorList = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.faithForward.util.Resource<com.faithForward.network.dto.CategoryResponse>> _categoriesList = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.faithForward.util.Resource<com.faithForward.network.dto.CategoryResponse>> categoriesList = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState contentRowFocusedIndex$delegate = null;
    
    @javax.inject.Inject()
    public HomeViewModel(@org.jetbrains.annotations.NotNull()
    com.faithForward.repository.NetworkRepository networkRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.faithForward.util.Resource<java.util.List<com.faithForward.media.viewModel.HomePageItem>>> getHomePageData() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.faithForward.util.Resource<java.util.List<com.faithForward.network.dto.creator.UserData>>> getCreatorList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.faithForward.util.Resource<com.faithForward.network.dto.CategoryResponse>> getCategoriesList() {
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
    
    public final void fetchCreatorPageData() {
    }
}