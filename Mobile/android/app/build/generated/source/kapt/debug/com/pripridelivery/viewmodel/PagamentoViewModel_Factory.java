package com.pripridelivery.viewmodel;

import com.pripridelivery.data.repository.CarrinhoRepository;
import com.pripridelivery.data.repository.EnderecoRepository;
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
public final class PagamentoViewModel_Factory implements Factory<PagamentoViewModel> {
  private final Provider<EnderecoRepository> enderecoRepositoryProvider;

  private final Provider<CarrinhoRepository> carrinhoRepositoryProvider;

  private final Provider<PedidoRepository> pedidoRepositoryProvider;

  public PagamentoViewModel_Factory(Provider<EnderecoRepository> enderecoRepositoryProvider,
      Provider<CarrinhoRepository> carrinhoRepositoryProvider,
      Provider<PedidoRepository> pedidoRepositoryProvider) {
    this.enderecoRepositoryProvider = enderecoRepositoryProvider;
    this.carrinhoRepositoryProvider = carrinhoRepositoryProvider;
    this.pedidoRepositoryProvider = pedidoRepositoryProvider;
  }

  @Override
  public PagamentoViewModel get() {
    return newInstance(enderecoRepositoryProvider.get(), carrinhoRepositoryProvider.get(), pedidoRepositoryProvider.get());
  }

  public static PagamentoViewModel_Factory create(
      Provider<EnderecoRepository> enderecoRepositoryProvider,
      Provider<CarrinhoRepository> carrinhoRepositoryProvider,
      Provider<PedidoRepository> pedidoRepositoryProvider) {
    return new PagamentoViewModel_Factory(enderecoRepositoryProvider, carrinhoRepositoryProvider, pedidoRepositoryProvider);
  }

  public static PagamentoViewModel newInstance(EnderecoRepository enderecoRepository,
      CarrinhoRepository carrinhoRepository, PedidoRepository pedidoRepository) {
    return new PagamentoViewModel(enderecoRepository, carrinhoRepository, pedidoRepository);
  }
}
