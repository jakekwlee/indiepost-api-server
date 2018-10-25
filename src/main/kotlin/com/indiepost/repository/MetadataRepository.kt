package com.indiepost.repository

import com.indiepost.model.Metadata
import org.springframework.data.repository.CrudRepository

/**
 * Created by jake on 10/29/17.
 */
interface MetadataRepository : CrudRepository<Metadata, Long>
