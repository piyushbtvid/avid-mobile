package com.faithForward.di;

import com.faithForward.network.ApiServiceInterface;
import com.faithForward.preferences.UserPreferences;
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

  private final Provider<UserPreferences> userPreferencesProvider;

  public AppModule_ProvideNetworkRepositoryFactory(AppModule module,
      Provider<ApiServiceInterface> apiServiceInterfaceProvider,
      Provider<UserPreferences> userPreferencesProvider) {
    this.module = module;
    this.apiServiceInterfaceProvider = apiServiceInterfaceProvider;
    this.userPreferencesProvider = userPreferencesProvider;
  }

  @Override
  public NetworkRepository get() {
    return provideNetworkRepository(module, apiServiceInterfaceProvider.get(), userPreferencesProvider.get());
  }

  public static AppModule_ProvideNetworkRepositoryFactory create(AppModule module,
      Provider<ApiServiceInterface> apiServiceInterfaceProvider,
      Provider<UserPreferences> userPreferencesProvider) {
    return new AppModule_ProvideNetworkRepositoryFactory(module, apiServiceInterfaceProvider, userPreferencesProvider);
  }

  public static NetworkRepository provideNetworkRepository(AppModule instance,
      ApiServiceInterface apiServiceInterface, UserPreferences userPreferences) {
    return Preconditions.checkNotNullFromProvides(instance.provideNetworkRepository(apiServiceInterface, userPreferences));
  }
}
