package com.faithForward.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
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
public final class AppModule_ProvidesRetrofitInstanceFactory implements Factory<Retrofit> {
  private final AppModule module;

  public AppModule_ProvidesRetrofitInstanceFactory(AppModule module) {
    this.module = module;
  }

  @Override
  public Retrofit get() {
    return providesRetrofitInstance(module);
  }

  public static AppModule_ProvidesRetrofitInstanceFactory create(AppModule module) {
    return new AppModule_ProvidesRetrofitInstanceFactory(module);
  }

  public static Retrofit providesRetrofitInstance(AppModule instance) {
    return Preconditions.checkNotNullFromProvides(instance.providesRetrofitInstance());
  }
}
