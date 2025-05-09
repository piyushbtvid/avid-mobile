package com.faithForward.di;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\tH\u0007J\b\u0010\n\u001a\u00020\tH\u0007\u00a8\u0006\u000b"}, d2 = {"Lcom/faithForward/di/AppModule;", "", "()V", "provideNetworkRepository", "Lcom/faithForward/repository/NetworkRepository;", "apiServiceInterface", "Lcom/faithForward/network/ApiServiceInterface;", "providesApiServiceInterface", "retrofit", "Lretrofit2/Retrofit;", "providesRetrofitInstance", "ff-media-Data_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class AppModule {
    
    public AppModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final retrofit2.Retrofit providesRetrofitInstance() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.faithForward.network.ApiServiceInterface providesApiServiceInterface(@org.jetbrains.annotations.NotNull()
    retrofit2.Retrofit retrofit) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.faithForward.repository.NetworkRepository provideNetworkRepository(@org.jetbrains.annotations.NotNull()
    com.faithForward.network.ApiServiceInterface apiServiceInterface) {
        return null;
    }
}