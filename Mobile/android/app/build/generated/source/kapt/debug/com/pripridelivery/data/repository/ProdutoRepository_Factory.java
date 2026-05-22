package com.pripridelivery.data.repository;

import com.google.firebase.firestore.FirebaseFirestore;
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
public final class ProdutoRepository_Factory implements Factory<ProdutoRepository> {
  private final Provider<FirebaseFirestore> dbProvider;

  public ProdutoRepository_Factory(Provider<FirebaseFirestore> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ProdutoRepository get() {
    return newInstance(dbProvider.get());
  }

  public static ProdutoRepository_Factory create(Provider<FirebaseFirestore> dbProvider) {
    return new ProdutoRepository_Factory(dbProvider);
  }

  public static ProdutoRepository newInstance(FirebaseFirestore db) {
    return new ProdutoRepository(db);
  }
}
