package com.pripridelivery.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pripridelivery.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth, db: FirebaseFirestore): AuthRepository =
        AuthRepository(auth, db)

    @Provides
    @Singleton
    fun provideEnderecoRepository(db: FirebaseFirestore): EnderecoRepository =
        EnderecoRepository(db)

    @Provides
    @Singleton
    fun provideRestauranteRepository(db: FirebaseFirestore): RestauranteRepository =
        RestauranteRepository(db)

    @Provides
    @Singleton
    fun provideProdutoRepository(db: FirebaseFirestore): ProdutoRepository =
        ProdutoRepository(db)

    @Provides
    @Singleton
    fun provideCarrinhoRepository(db: FirebaseFirestore): CarrinhoRepository =
        CarrinhoRepository(db)

    @Provides
    @Singleton
    fun providePedidoRepository(db: FirebaseFirestore): PedidoRepository =
        PedidoRepository(db)
}
