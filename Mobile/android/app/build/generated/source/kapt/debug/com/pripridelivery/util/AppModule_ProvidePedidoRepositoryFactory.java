package com.pripridelivery.util;

import com.google.firebase.firestore.FirebaseFirestore;
import com.pripridelivery.data.repository.PedidoRepository;
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
public final class AppModule_ProvidePedidoRepositoryFactory implements Factory<PedidoRepository> {
  private final Provider<FirebaseFirestore> dbProvider;

  public AppModule_ProvidePedidoRepositoryFactory(Provider<FirebaseFirestore> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public PedidoRepository get() {
    return providePedidoRepository(dbProvider.get());
  }

  public static AppModule_ProvidePedidoRepositoryFactory create(
      Provider<FirebaseFirestore> dbProvider) {
    return new AppModule_ProvidePedidoRepositoryFactory(dbProvider);
  }

  public static PedidoRepository providePedidoRepository(FirebaseFirestore db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.providePedidoRepository(db));
  }
}
