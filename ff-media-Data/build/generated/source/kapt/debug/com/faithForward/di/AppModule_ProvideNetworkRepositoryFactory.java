package com.faithForward.di;

import com.faithForward.network.ApiServiceInterface;
import com.faithForward.repository.NetworkRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class AppModule_ProvideNetworkRepositoryFactory implements Factory<NetworkRepository> {
  private final AppModule module;

  private final Provider<ApiServiceInterface> apiServiceInterfaceProvider;

  public AppModule_ProvideNetworkRepositoryFactory(AppModule module,
      Provider<ApiServiceInterface> apiServiceInterfaceProvider) {
    this.module = module;
    this.apiServiceInterfaceProvider = apiServiceInterfaceProvider;
  }

  @Override
  public NetworkRepository get() {
    return provideNetworkRepository(module, apiServiceInterfaceProvider.get());
  }

  public static AppModule_ProvideNetworkRepositoryFactory create(AppModule module,
      Provider<ApiServiceInterface> apiServiceInterfaceProvider) {
    return new AppModule_ProvideNetworkRepositoryFactory(module, apiServiceInterfaceProvider);
  }

  public static NetworkRepository provideNetworkRepository(AppModule instance,
      ApiServiceInterface apiServiceInterface) {
    return Preconditions.checkNotNullFromProvides(instance.provideNetworkRepository(apiServiceInterface));
  }
}
