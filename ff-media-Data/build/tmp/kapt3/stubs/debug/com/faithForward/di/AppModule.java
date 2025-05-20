package com.faithForward.di;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007J\u0012\u0010\t\u001a\u00020\b2\b\b\u0001\u0010\n\u001a\u00020\u000bH\u0007J\u0010\u0010\f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u000eH\u0007J\b\u0010\u000f\u001a\u00020\u000eH\u0007\u00a8\u0006\u0010"}, d2 = {"Lcom/faithForward/di/AppModule;", "", "()V", "provideNetworkRepository", "Lcom/faithForward/repository/NetworkRepository;", "apiServiceInterface", "Lcom/faithForward/network/ApiServiceInterface;", "userPreferences", "Lcom/faithForward/preferences/UserPreferences;", "provideUserPreferences", "context", "Landroid/content/Context;", "providesApiServiceInterface", "retrofit", "Lretrofit2/Retrofit;", "providesRetrofitInstance", "ff-media-Data_debug"})
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
    com.faithForward.network.ApiServiceInterface apiServiceInterface, @org.jetbrains.annotations.NotNull()
    com.faithForward.preferences.UserPreferences userPreferences) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.faithForward.preferences.UserPreferences provideUserPreferences(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
}