package com.faithForward.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0086@\u00a2\u0006\u0002\u0010\bJ\u001c\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u00062\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\rJ\u001c\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\n0\u00062\u0006\u0010\u000f\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\rR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/faithForward/repository/NetworkRepository;", "", "apiServiceInterface", "Lcom/faithForward/network/ApiServiceInterface;", "(Lcom/faithForward/network/ApiServiceInterface;)V", "getCategories", "Lretrofit2/Response;", "Lcom/faithForward/network/dto/CategoryResponse;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getGivenCategoryDetail", "Lcom/faithForward/network/dto/SectionApiResponse;", "categoryId", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getGivenSectionData", "sectionId", "ff-media-Data_debug"})
public final class NetworkRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.faithForward.network.ApiServiceInterface apiServiceInterface = null;
    
    @javax.inject.Inject()
    public NetworkRepository(@org.jetbrains.annotations.NotNull()
    com.faithForward.network.ApiServiceInterface apiServiceInterface) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getGivenSectionData(int sectionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.faithForward.network.dto.SectionApiResponse>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getCategories(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.faithForward.network.dto.CategoryResponse>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getGivenCategoryDetail(int categoryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.faithForward.network.dto.SectionApiResponse>> $completion) {
        return null;
    }
}