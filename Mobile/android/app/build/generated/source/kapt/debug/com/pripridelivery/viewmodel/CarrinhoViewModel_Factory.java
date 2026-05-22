package com.pripridelivery.viewmodel;

import com.pripridelivery.data.repository.CarrinhoRepository;
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
public final class CarrinhoViewModel_Factory implements Factory<CarrinhoViewModel> {
  private final Provider<CarrinhoRepository> carrinhoRepositoryProvider;

  public CarrinhoViewModel_Factory(Provider<CarrinhoRepository> carrinhoRepositoryProvider) {
    this.carrinhoRepositoryProvider = carrinhoRepositoryProvider;
  }

  @Override
  public CarrinhoViewModel get() {
    return newInstance(carrinhoRepositoryProvider.get());
  }

  public static CarrinhoViewModel_Factory create(
      Provider<CarrinhoRepository> carrinhoRepositoryProvider) {
    return new CarrinhoViewModel_Factory(carrinhoRepositoryProvider);
  }

  public static CarrinhoViewModel newInstance(CarrinhoRepository carrinhoRepository) {
    return new CarrinhoViewModel(carrinhoRepository);
  }
}
