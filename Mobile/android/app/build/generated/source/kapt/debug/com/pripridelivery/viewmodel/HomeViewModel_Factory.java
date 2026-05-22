package com.pripridelivery.viewmodel;

import com.pripridelivery.data.repository.RestauranteRepository;
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
    "KotlinInternalInJava"
})
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<RestauranteRepository> restauranteRepositoryProvider;

  public HomeViewModel_Factory(Provider<RestauranteRepository> restauranteRepositoryProvider) {
    this.restauranteRepositoryProvider = restauranteRepositoryProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(restauranteRepositoryProvider.get());
  }

  public static HomeViewModel_Factory create(
      Provider<RestauranteRepository> restauranteRepositoryProvider) {
    return new HomeViewModel_Factory(restauranteRepositoryProvider);
  }

  public static HomeViewModel newInstance(RestauranteRepository restauranteRepository) {
    return new HomeViewModel(restauranteRepository);
  }
}
