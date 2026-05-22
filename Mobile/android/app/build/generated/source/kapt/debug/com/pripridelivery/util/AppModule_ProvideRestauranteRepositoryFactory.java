package com.pripridelivery.util;

import com.google.firebase.firestore.FirebaseFirestore;
import com.pripridelivery.data.repository.RestauranteRepository;
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
public final class AppModule_ProvideRestauranteRepositoryFactory implements Factory<RestauranteRepository> {
  private final Provider<FirebaseFirestore> dbProvider;

  public AppModule_ProvideRestauranteRepositoryFactory(Provider<FirebaseFirestore> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public RestauranteRepository get() {
    return provideRestauranteRepository(dbProvider.get());
  }

  public static AppModule_ProvideRestauranteRepositoryFactory create(
      Provider<FirebaseFirestore> dbProvider) {
    return new AppModule_ProvideRestauranteRepositoryFactory(dbProvider);
  }

  public static RestauranteRepository provideRestauranteRepository(FirebaseFirestore db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideRestauranteRepository(db));
  }
}
