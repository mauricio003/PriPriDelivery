package com.pripridelivery.util;

import com.google.firebase.firestore.FirebaseFirestore;
import com.pripridelivery.data.repository.CarrinhoRepository;
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
public final class AppModule_ProvideCarrinhoRepositoryFactory implements Factory<CarrinhoRepository> {
  private final Provider<FirebaseFirestore> dbProvider;

  public AppModule_ProvideCarrinhoRepositoryFactory(Provider<FirebaseFirestore> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public CarrinhoRepository get() {
    return provideCarrinhoRepository(dbProvider.get());
  }

  public static AppModule_ProvideCarrinhoRepositoryFactory create(
      Provider<FirebaseFirestore> dbProvider) {
    return new AppModule_ProvideCarrinhoRepositoryFactory(dbProvider);
  }

  public static CarrinhoRepository provideCarrinhoRepository(FirebaseFirestore db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideCarrinhoRepository(db));
  }
}
