package com.example.jcarlosvelasco.loopmusic.domain.usecase

import com.example.jcarlosvelasco.loopmusic.domain.interfaces.GivePermissionsRepositoryType

interface GivePermissionsType {
    fun execute(path: String)
}

class GivePermissions(
    private val repo: GivePermissionsRepositoryType
) : GivePermissionsType {
    override fun execute(path: String) {
        repo.givePermissions(path)
    }
}