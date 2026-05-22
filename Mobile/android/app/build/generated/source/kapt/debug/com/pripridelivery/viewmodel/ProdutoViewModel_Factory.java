package com.pripridelivery.viewmodel;

import com.pripridelivery.data.repository.ProdutoRepository;
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
public final class ProdutoViewModel_Factory implements Factory<ProdutoViewModel> {
  private final Provider<ProdutoRepository> produtoRepositoryProvider;

  public ProdutoViewModel_Factory(Provider<ProdutoRepository> produtoRepositoryProvider) {
    this.produtoRepositoryProvider = produtoRepositoryProvider;
  }

  @Override
  public ProdutoViewModel get() {
    return newInstance(produtoRepositoryProvider.get());
  }

  public static ProdutoViewModel_Factory create(
      Provider<ProdutoRepository> produtoRepositoryProvider) {
    return new ProdutoViewModel_Factory(produtoRepositoryProvider);
  }

  public static ProdutoViewModel newInstance(ProdutoRepository produtoRepository) {
    return new ProdutoViewModel(produtoRepository);
  }
}
