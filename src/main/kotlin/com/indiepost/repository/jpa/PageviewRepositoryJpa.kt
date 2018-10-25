package com.indiepost.repository.jpa

import com.indiepost.model.Post
import com.indiepost.model.analytics.Pageview
import com.indiepost.model.analytics.Visitor
import com.indiepost.repository.PageviewRepository
import org.springframework.stereotype.Repository

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class PageviewRepositoryJpa : PageviewRepository {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun save(pageview: Pageview) {
        if (pageview.postId != null) {
            val postReference = entityManager.getReference(Post::class.java, pageview.postId)
            pageview.post = postReference
        }
        if (pageview.visitorId != null) {
            val visitorReference = entityManager.getReference(Visitor::class.java, pageview.visitorId)
            pageview.visitor = visitorReference
        }
        entityManager.persist(pageview)
    }
}
