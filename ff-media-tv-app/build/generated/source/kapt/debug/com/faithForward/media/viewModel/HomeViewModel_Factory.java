package com.faithForward.media.viewModel;

import com.faithForward.repository.NetworkRepository;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<NetworkRepository> networkRepositoryProvider;

  public HomeViewModel_Factory(Provider<NetworkRepository> networkRepositoryProvider) {
    this.networkRepositoryProvider = networkRepositoryProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(networkRepositoryProvider.get());
  }

  public static HomeViewModel_Factory create(
      Provider<NetworkRepository> networkRepositoryProvider) {
    return new HomeViewModel_Factory(networkRepositoryProvider);
  }

  public static HomeViewModel newInstance(NetworkRepository networkRepository) {
    return new HomeViewModel(networkRepository);
  }
}
