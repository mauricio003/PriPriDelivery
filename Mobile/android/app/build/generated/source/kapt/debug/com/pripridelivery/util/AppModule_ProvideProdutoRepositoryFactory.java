package com.pripridelivery.util;

import com.google.firebase.firestore.FirebaseFirestore;
import com.pripridelivery.data.repository.ProdutoRepository;
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
public final class AppModule_ProvideProdutoRepositoryFactory implements Factory<ProdutoRepository> {
  private final Provider<FirebaseFirestore> dbProvider;

  public AppModule_ProvideProdutoRepositoryFactory(Provider<FirebaseFirestore> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ProdutoRepository get() {
    return provideProdutoRepository(dbProvider.get());
  }

  public static AppModule_ProvideProdutoRepositoryFactory create(
      Provider<FirebaseFirestore> dbProvider) {
    return new AppModule_ProvideProdutoRepositoryFactory(dbProvider);
  }

  public static ProdutoRepository provideProdutoRepository(FirebaseFirestore db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideProdutoRepository(db));
  }
}
