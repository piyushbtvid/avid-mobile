package com.faithForward.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\f\u001a\u00020\rH\u0086@\u00a2\u0006\u0002\u0010\u000eJ\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010H\u0086@\u00a2\u0006\u0002\u0010\u000eJ\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\u0010H\u0086@\u00a2\u0006\u0002\u0010\u000eJ\b\u0010\u0014\u001a\u0004\u0018\u00010\tJ\u001c\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u00102\u0006\u0010\u0017\u001a\u00020\u0018H\u0086@\u00a2\u0006\u0002\u0010\u0019J\u001c\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00160\u00102\u0006\u0010\u001b\u001a\u00020\u0018H\u0086@\u00a2\u0006\u0002\u0010\u0019J$\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001d0\u00102\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u001fH\u0086@\u00a2\u0006\u0002\u0010!J\u0016\u0010\"\u001a\u00020\r2\u0006\u0010#\u001a\u00020\tH\u0086@\u00a2\u0006\u0002\u0010$R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006%"}, d2 = {"Lcom/faithForward/repository/NetworkRepository;", "", "userPreferences", "Lcom/faithForward/preferences/UserPreferences;", "apiServiceInterface", "Lcom/faithForward/network/ApiServiceInterface;", "(Lcom/faithForward/preferences/UserPreferences;Lcom/faithForward/network/ApiServiceInterface;)V", "userSession", "Lkotlinx/coroutines/flow/Flow;", "Lcom/faithForward/network/dto/login/LoginData;", "getUserSession", "()Lkotlinx/coroutines/flow/Flow;", "clearSession", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCategories", "Lretrofit2/Response;", "Lcom/faithForward/network/dto/CategoryResponse;", "getCreatorsList", "Lcom/faithForward/network/dto/creator/CreatorsListApiResponse;", "getCurrentSession", "getGivenCategoryDetail", "Lcom/faithForward/network/dto/SectionApiResponse;", "categoryId", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getGivenSectionData", "sectionId", "loginUser", "Lcom/faithForward/network/dto/login/LoginResponse;", "email", "", "password", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveUserSession", "session", "(Lcom/faithForward/network/dto/login/LoginData;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "ff-media-Data_debug"})
public final class NetworkRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.faithForward.preferences.UserPreferences userPreferences = null;
    @org.jetbrains.annotations.NotNull()
    private final com.faithForward.network.ApiServiceInterface apiServiceInterface = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.faithForward.network.dto.login.LoginData> userSession = null;
    
    @javax.inject.Inject()
    public NetworkRepository(@org.jetbrains.annotations.NotNull()
    com.faithForward.preferences.UserPreferences userPreferences, @org.jetbrains.annotations.NotNull()
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
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.faithForward.network.dto.login.LoginData> getUserSession() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object saveUserSession(@org.jetbrains.annotations.NotNull()
    com.faithForward.network.dto.login.LoginData session, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object clearSession(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.faithForward.network.dto.login.LoginData getCurrentSession() {
        return null;
    }
}