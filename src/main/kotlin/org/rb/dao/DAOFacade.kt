package org.rb.dao

import org.rb.models.Link

interface DAOFacade {
    suspend fun link(code: String): Link?
    suspend fun addNewLink(url: String): Link?
}