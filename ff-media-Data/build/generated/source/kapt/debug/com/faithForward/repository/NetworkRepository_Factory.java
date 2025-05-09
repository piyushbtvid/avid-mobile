package com.faithForward.repository;

import com.faithForward.network.ApiServiceInterface;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class NetworkRepository_Factory implements Factory<NetworkRepository> {
  private final Provider<ApiServiceInterface> apiServiceInterfaceProvider;

  public NetworkRepository_Factory(Provider<ApiServiceInterface> apiServiceInterfaceProvider) {
    this.apiServiceInterfaceProvider = apiServiceInterfaceProvider;
  }

  @Override
  public NetworkRepository get() {
    return newInstance(apiServiceInterfaceProvider.get());
  }

  public static NetworkRepository_Factory create(
      Provider<ApiServiceInterface> apiServiceInterfaceProvider) {
    return new NetworkRepository_Factory(apiServiceInterfaceProvider);
  }

  public static NetworkRepository newInstance(ApiServiceInterface apiServiceInterface) {
    return new NetworkRepository(apiServiceInterface);
  }
}
