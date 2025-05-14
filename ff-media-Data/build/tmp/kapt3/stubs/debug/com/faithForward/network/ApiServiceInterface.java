package com.faithForward.network;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0005J\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0005J\u001e\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u00032\b\b\u0001\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u001e\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00032\b\b\u0001\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\f\u00a8\u0006\u000f"}, d2 = {"Lcom/faithForward/network/ApiServiceInterface;", "", "getCategories", "Lretrofit2/Response;", "Lcom/faithForward/network/dto/CategoryResponse;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCreatorsList", "Lcom/faithForward/network/dto/creator/CreatorsListApiResponse;", "getGivenCategoryDetail", "Lcom/faithForward/network/dto/CategoryDetailResponse;", "id", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getGivenSectionData", "Lcom/faithForward/network/dto/SectionApiResponse;", "ff-media-Data_debug"})
public abstract interface ApiServiceInterface {
    
    @retrofit2.http.GET(value = "section/{id}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getGivenSectionData(@retrofit2.http.Path(value = "id")
    int id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.faithForward.network.dto.SectionApiResponse>> $completion);
    
    @retrofit2.http.GET(value = "get-category")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getCategories(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.faithForward.network.dto.CategoryResponse>> $completion);
    
    @retrofit2.http.GET(value = "get-category")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getGivenCategoryDetail(@retrofit2.http.Path(value = "id")
    int id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.faithForward.network.dto.CategoryDetailResponse>> $completion);
    
    @retrofit2.http.GET(value = "http://107.180.208.127:8000/api/client/v1/creators/list?perpage=10")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getCreatorsList(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.faithForward.network.dto.creator.CreatorsListApiResponse>> $completion);
}