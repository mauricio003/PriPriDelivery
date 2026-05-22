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
public final class EnderecoRepository_Factory implements Factory<EnderecoRepository> {
  private final Provider<FirebaseFirestore> dbProvider;

  public EnderecoRepository_Factory(Provider<FirebaseFirestore> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public EnderecoRepository get() {
    return newInstance(dbProvider.get());
  }

  public static EnderecoRepository_Factory create(Provider<FirebaseFirestore> dbProvider) {
    return new EnderecoRepository_Factory(dbProvider);
  }

  public static EnderecoRepository newInstance(FirebaseFirestore db) {
    return new EnderecoRepository(db);
  }
}
