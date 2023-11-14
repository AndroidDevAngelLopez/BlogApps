package org.example.blogapp.api

import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import org.bson.codecs.ObjectIdGenerator
import org.example.blogapp.data.MongoDB
import org.example.blogapp.models.Newsletter

@Api(routeOverride = "subscribe")
suspend fun subscribeNewsletter(context: ApiContext) {
    try {
        val newsletter = context.req.getBody<Newsletter>()
        val newNewsletter = newsletter?.copy(_id = ObjectIdGenerator().generate().toString())
        context.res.setBody(newNewsletter?.let {
            context.data.getValue<MongoDB>().subscribe(newsletter = it)
        })
    } catch (e: Exception) {
        context.res.setBody(e.message)
    }
}