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
public final class RestauranteViewModel_Factory implements Factory<RestauranteViewModel> {
  private final Provider<RestauranteRepository> restauranteRepositoryProvider;

  public RestauranteViewModel_Factory(
      Provider<RestauranteRepository> restauranteRepositoryProvider) {
    this.restauranteRepositoryProvider = restauranteRepositoryProvider;
  }

  @Override
  public RestauranteViewModel get() {
    return newInstance(restauranteRepositoryProvider.get());
  }

  public static RestauranteViewModel_Factory create(
      Provider<RestauranteRepository> restauranteRepositoryProvider) {
    return new RestauranteViewModel_Factory(restauranteRepositoryProvider);
  }

  public static RestauranteViewModel newInstance(RestauranteRepository restauranteRepository) {
    return new RestauranteViewModel(restauranteRepository);
  }
}
