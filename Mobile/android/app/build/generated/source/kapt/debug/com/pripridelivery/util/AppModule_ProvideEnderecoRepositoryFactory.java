package com.pripridelivery.util;

import com.google.firebase.firestore.FirebaseFirestore;
import com.pripridelivery.data.repository.EnderecoRepository;
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
    "KotlinInternalInJava"
})
public final class AppModule_ProvideEnderecoRepositoryFactory implements Factory<EnderecoRepository> {
  private final Provider<FirebaseFirestore> dbProvider;

  public AppModule_ProvideEnderecoRepositoryFactory(Provider<FirebaseFirestore> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public EnderecoRepository get() {
    return provideEnderecoRepository(dbProvider.get());
  }

  public static AppModule_ProvideEnderecoRepositoryFactory create(
      Provider<FirebaseFirestore> dbProvider) {
    return new AppModule_ProvideEnderecoRepositoryFactory(dbProvider);
  }

  public static EnderecoRepository provideEnderecoRepository(FirebaseFirestore db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideEnderecoRepository(db));
  }
}
