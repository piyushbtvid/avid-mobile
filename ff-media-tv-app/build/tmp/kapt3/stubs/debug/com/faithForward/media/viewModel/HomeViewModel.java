package com.faithForward.media.viewModel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010R\u001c\u0010\u0005\u001a\u0010\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001f\u0010\t\u001a\u0010\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\u00070\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0011"}, d2 = {"Lcom/faithForward/media/viewModel/HomeViewModel;", "Landroidx/lifecycle/ViewModel;", "networkRepository", "Lcom/faithForward/repository/NetworkRepository;", "(Lcom/faithForward/repository/NetworkRepository;)V", "_sectionData", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/faithForward/util/Resource;", "Lcom/faithForward/network/dto/SectionApiResponse;", "sectionData", "Lkotlinx/coroutines/flow/StateFlow;", "getSectionData", "()Lkotlinx/coroutines/flow/StateFlow;", "getGivenSectionData", "", "sectionId", "", "ff-media-tv-app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class HomeViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.faithForward.repository.NetworkRepository networkRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.faithForward.util.Resource<com.faithForward.network.dto.SectionApiResponse>> _sectionData = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.faithForward.util.Resource<com.faithForward.network.dto.SectionApiResponse>> sectionData = null;
    
    @javax.inject.Inject()
    public HomeViewModel(@org.jetbrains.annotations.NotNull()
    com.faithForward.repository.NetworkRepository networkRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.faithForward.util.Resource<com.faithForward.network.dto.SectionApiResponse>> getSectionData() {
        return null;
    }
    
    public final void getGivenSectionData(int sectionId) {
    }
}