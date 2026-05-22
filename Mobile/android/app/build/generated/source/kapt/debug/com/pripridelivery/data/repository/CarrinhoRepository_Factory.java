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
public final class CarrinhoRepository_Factory implements Factory<CarrinhoRepository> {
  private final Provider<FirebaseFirestore> dbProvider;

  public CarrinhoRepository_Factory(Provider<FirebaseFirestore> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public CarrinhoRepository get() {
    return newInstance(dbProvider.get());
  }

  public static CarrinhoRepository_Factory create(Provider<FirebaseFirestore> dbProvider) {
    return new CarrinhoRepository_Factory(dbProvider);
  }

  public static CarrinhoRepository newInstance(FirebaseFirestore db) {
    return new CarrinhoRepository(db);
  }
}
