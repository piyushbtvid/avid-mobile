package com.faithForward.di;

import com.faithForward.network.ApiServiceInterface;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class AppModule_ProvidesApiServiceInterfaceFactory implements Factory<ApiServiceInterface> {
  private final AppModule module;

  private final Provider<Retrofit> retrofitProvider;

  public AppModule_ProvidesApiServiceInterfaceFactory(AppModule module,
      Provider<Retrofit> retrofitProvider) {
    this.module = module;
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public ApiServiceInterface get() {
    return providesApiServiceInterface(module, retrofitProvider.get());
  }

  public static AppModule_ProvidesApiServiceInterfaceFactory create(AppModule module,
      Provider<Retrofit> retrofitProvider) {
    return new AppModule_ProvidesApiServiceInterfaceFactory(module, retrofitProvider);
  }

  public static ApiServiceInterface providesApiServiceInterface(AppModule instance,
      Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(instance.providesApiServiceInterface(retrofit));
  }
}
