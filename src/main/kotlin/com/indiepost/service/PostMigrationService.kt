package com.indiepost.service

import com.indiepost.model.Profile

interface PostMigrationService {
    fun migrateProfiles()

    fun findProfileByEtc(text: String): Profile?
}