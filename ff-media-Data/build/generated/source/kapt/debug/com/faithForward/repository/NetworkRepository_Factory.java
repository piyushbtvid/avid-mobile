package com.faithForward.repository;

import com.faithForward.network.ApiServiceInterface;
import com.faithForward.preferences.UserPreferences;
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
  private final Provider<UserPreferences> userPreferencesProvider;

  private final Provider<ApiServiceInterface> apiServiceInterfaceProvider;

  public NetworkRepository_Factory(Provider<UserPreferences> userPreferencesProvider,
      Provider<ApiServiceInterface> apiServiceInterfaceProvider) {
    this.userPreferencesProvider = userPreferencesProvider;
    this.apiServiceInterfaceProvider = apiServiceInterfaceProvider;
  }

  @Override
  public NetworkRepository get() {
    return newInstance(userPreferencesProvider.get(), apiServiceInterfaceProvider.get());
  }

  public static NetworkRepository_Factory create(Provider<UserPreferences> userPreferencesProvider,
      Provider<ApiServiceInterface> apiServiceInterfaceProvider) {
    return new NetworkRepository_Factory(userPreferencesProvider, apiServiceInterfaceProvider);
  }

  public static NetworkRepository newInstance(UserPreferences userPreferences,
      ApiServiceInterface apiServiceInterface) {
    return new NetworkRepository(userPreferences, apiServiceInterface);
  }
}
