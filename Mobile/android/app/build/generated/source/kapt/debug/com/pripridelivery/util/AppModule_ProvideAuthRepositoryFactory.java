package com.pripridelivery.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pripridelivery.data.repository.AuthRepository;
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
public final class AppModule_ProvideAuthRepositoryFactory implements Factory<AuthRepository> {
  private final Provider<FirebaseAuth> authProvider;

  private final Provider<FirebaseFirestore> dbProvider;

  public AppModule_ProvideAuthRepositoryFactory(Provider<FirebaseAuth> authProvider,
      Provider<FirebaseFirestore> dbProvider) {
    this.authProvider = authProvider;
    this.dbProvider = dbProvider;
  }

  @Override
  public AuthRepository get() {
    return provideAuthRepository(authProvider.get(), dbProvider.get());
  }

  public static AppModule_ProvideAuthRepositoryFactory create(Provider<FirebaseAuth> authProvider,
      Provider<FirebaseFirestore> dbProvider) {
    return new AppModule_ProvideAuthRepositoryFactory(authProvider, dbProvider);
  }

  public static AuthRepository provideAuthRepository(FirebaseAuth auth, FirebaseFirestore db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideAuthRepository(auth, db));
  }
}
