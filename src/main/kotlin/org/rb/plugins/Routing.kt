package org.rb.plugins

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.rb.dao.dao

fun Application.configureRouting() {
    install(ContentNegotiation) {
        json()
    }

    routing {
        get("create") {
            val url = call.request.queryParameters["url"]
            if (url == null) {
                call.respond(HttpStatusCode.BadRequest, "Query parameter url are missing.")
                return@get
            }

            val link = dao.addNewLink(url)
            if (link == null) {
                call.respond(HttpStatusCode.InternalServerError, "Error while creating link.")
                return@get
            }

            call.respond(mapOf("url" to "http://localhost:8080/${link.code}"))
        }

        get("{code}") {
            val code = call.parameters["code"]
            if (code == null) {
                call.respond(HttpStatusCode.BadRequest, "Path parameter code are missing.")
                return@get
            }

            val link = dao.link(code)
            if (link == null) {
                call.respond(HttpStatusCode.NotFound, "Link not found.")
                return@get
            }

            call.respondRedirect(link.url)
        }
    }
}
