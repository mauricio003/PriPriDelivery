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
public final class PedidoRepository_Factory implements Factory<PedidoRepository> {
  private final Provider<FirebaseFirestore> dbProvider;

  public PedidoRepository_Factory(Provider<FirebaseFirestore> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public PedidoRepository get() {
    return newInstance(dbProvider.get());
  }

  public static PedidoRepository_Factory create(Provider<FirebaseFirestore> dbProvider) {
    return new PedidoRepository_Factory(dbProvider);
  }

  public static PedidoRepository newInstance(FirebaseFirestore db) {
    return new PedidoRepository(db);
  }
}
