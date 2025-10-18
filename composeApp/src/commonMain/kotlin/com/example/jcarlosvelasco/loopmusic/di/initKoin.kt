package com.example.jcarlosvelasco.loopmusic.di

import com.example.jcarlosvelasco.loopmusic.di.modules.*
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.ksp.generated.module

fun initKoin(config: KoinAppDeclaration? = null, additionalModules: List<Module> = emptyList()) {
    startKoin {
        config?.invoke(this)
        modules(
            listOf(
                DatabaseModule().module,
                InfrastructureModule().module,
                MapperModule().module,
                RepositoryModule().module,
                UseCaseModule().module,
                ViewModelModule().module
            ) + additionalModules
        )
    }
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            DatabaseModule().module,
            InfrastructureModule().module,
            MapperModule().module,
            RepositoryModule().module,
            UseCaseModule().module,
            ViewModelModule().module
        )
    }
}