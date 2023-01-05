package org.rb.dao

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.rb.database.DatabaseFactory.dbQuery
import org.rb.models.Link
import org.rb.models.Links
import org.rb.utils.getRandomString

class DAOFacadeImpl : DAOFacade {
    private fun resultRowToLink(row: ResultRow) = Link(
        id = row[Links.id],
        code = row[Links.code],
        url = row[Links.url]
    )

    override suspend fun link(code: String): Link? = dbQuery {
        Links
            .select { Links.code eq code }
            .map(::resultRowToLink)
            .singleOrNull()
    }

    override suspend fun addNewLink(url: String): Link? = dbQuery {
        var codeIsValid = false
        var code = ""

        while (!codeIsValid) {
            val generatedCode = getRandomString(8)
            val link = Links
                .select { Links.code eq generatedCode }
                .map(::resultRowToLink)
                .singleOrNull()

            if (link == null) {
                codeIsValid = true
                code = generatedCode
            }
        }

        val insertStatement = Links.insert {
            it[Links.code] = code
            it[Links.url] = url
        }

        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToLink)
    }
}

val dao: DAOFacade = DAOFacadeImpl()
