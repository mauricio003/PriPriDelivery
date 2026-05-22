package com.pripridelivery.viewmodel;

import com.pripridelivery.data.repository.EnderecoRepository;
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
public final class EnderecoViewModel_Factory implements Factory<EnderecoViewModel> {
  private final Provider<EnderecoRepository> enderecoRepositoryProvider;

  public EnderecoViewModel_Factory(Provider<EnderecoRepository> enderecoRepositoryProvider) {
    this.enderecoRepositoryProvider = enderecoRepositoryProvider;
  }

  @Override
  public EnderecoViewModel get() {
    return newInstance(enderecoRepositoryProvider.get());
  }

  public static EnderecoViewModel_Factory create(
      Provider<EnderecoRepository> enderecoRepositoryProvider) {
    return new EnderecoViewModel_Factory(enderecoRepositoryProvider);
  }

  public static EnderecoViewModel newInstance(EnderecoRepository enderecoRepository) {
    return new EnderecoViewModel(enderecoRepository);
  }
}
