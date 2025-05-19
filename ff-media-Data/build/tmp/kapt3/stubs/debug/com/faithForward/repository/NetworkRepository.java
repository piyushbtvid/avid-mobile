package com.faithForward.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0086@\u00a2\u0006\u0002\u0010\bJ\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0006H\u0086@\u00a2\u0006\u0002\u0010\bJ\u001c\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u00062\u0006\u0010\r\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010\u000fJ\u001c\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\f0\u00062\u0006\u0010\u0011\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010\u000fJ$\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\u00062\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0015H\u0086@\u00a2\u0006\u0002\u0010\u0017R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/faithForward/repository/NetworkRepository;", "", "apiServiceInterface", "Lcom/faithForward/network/ApiServiceInterface;", "(Lcom/faithForward/network/ApiServiceInterface;)V", "getCategories", "Lretrofit2/Response;", "Lcom/faithForward/network/dto/CategoryResponse;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCreatorsList", "Lcom/faithForward/network/dto/creator/CreatorsListApiResponse;", "getGivenCategoryDetail", "Lcom/faithForward/network/dto/SectionApiResponse;", "categoryId", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getGivenSectionData", "sectionId", "loginUser", "Lcom/faithForward/network/dto/login/LoginResponse;", "email", "", "password", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "ff-media-Data_debug"})
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
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getCreatorsList(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.faithForward.network.dto.creator.CreatorsListApiResponse>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object loginUser(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.faithForward.network.dto.login.LoginResponse>> $completion) {
        return null;
    }
}