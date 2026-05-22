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
public final class RestauranteRepository_Factory implements Factory<RestauranteRepository> {
  private final Provider<FirebaseFirestore> dbProvider;

  public RestauranteRepository_Factory(Provider<FirebaseFirestore> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public RestauranteRepository get() {
    return newInstance(dbProvider.get());
  }

  public static RestauranteRepository_Factory create(Provider<FirebaseFirestore> dbProvider) {
    return new RestauranteRepository_Factory(dbProvider);
  }

  public static RestauranteRepository newInstance(FirebaseFirestore db) {
    return new RestauranteRepository(db);
  }
}
