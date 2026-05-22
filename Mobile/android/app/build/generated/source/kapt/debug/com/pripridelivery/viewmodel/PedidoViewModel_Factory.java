package com.pripridelivery.viewmodel;

import com.pripridelivery.data.repository.PedidoRepository;
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
public final class PedidoViewModel_Factory implements Factory<PedidoViewModel> {
  private final Provider<PedidoRepository> pedidoRepositoryProvider;

  public PedidoViewModel_Factory(Provider<PedidoRepository> pedidoRepositoryProvider) {
    this.pedidoRepositoryProvider = pedidoRepositoryProvider;
  }

  @Override
  public PedidoViewModel get() {
    return newInstance(pedidoRepositoryProvider.get());
  }

  public static PedidoViewModel_Factory create(
      Provider<PedidoRepository> pedidoRepositoryProvider) {
    return new PedidoViewModel_Factory(pedidoRepositoryProvider);
  }

  public static PedidoViewModel newInstance(PedidoRepository pedidoRepository) {
    return new PedidoViewModel(pedidoRepository);
  }
}
