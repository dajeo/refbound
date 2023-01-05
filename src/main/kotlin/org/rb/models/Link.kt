package org.rb.models

import org.jetbrains.exposed.sql.Table

data class Link(val id: Int, val code: String, val url: String)

object Links : Table() {
    val id = integer("id").autoIncrement()
    val code = varchar("code", 256)
    val url = varchar("url", 256)

    override val primaryKey = PrimaryKey(id)
}
