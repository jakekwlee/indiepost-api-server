package com.indiepost.service

import com.indiepost.model.Profile
import com.indiepost.repository.PostMigrationRepository
import com.indiepost.utils.DomUtil
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.inject.Inject
import javax.transaction.Transactional

@Service
@Transactional
class PostMigrationServiceImpl @Inject constructor(
        val postMigrationRepository: PostMigrationRepository) : PostMigrationService {

    override fun migrateProfiles() {
        val posts = postMigrationRepository.selectAllPostsWhereNotProfileSet()
        var profileAndContentUpdated = 0
        var profileUpdated = 0
        var error = 0

        val bylineNameSet: HashSet<String> = HashSet()
        for (post in posts) {
            val profile = postMigrationRepository.findProfileByEtc(post.displayName)
            if (profile != null && (profile.description.isNullOrEmpty())) {
                val description = DomUtil.findWriterInformationFromContent(post.content)
                if (description != null) {
                    profile.description = description
                    profile.showDescription = true
                    logger.info("profile: ${profile.displayName}, description updated '${profile.description}'")
                }
            }
            val content = DomUtil.findAndRemoveWriterInformationFromContent(post.content)
            if (profile == null) {
                logger.info("post: ${post.id}, byline: ${post.displayName}, no profile, no changed")
                bylineNameSet.add(post.displayName)
                ++error
            } else {
                if (post.postProfile.isNotEmpty()) {
                    val count = post.postProfile.stream().filter { it -> it.profile!!.id == profile.id }.count()
                    if (count == 0L) {
                        post.postProfile.forEach { it.priority = it.priority + 1 }
                        post.addProfile(profile, 0)
                    }
                } else {
                    post.addProfile(profile, 0)
                }
                if (content != null && content != post.content) {
                    logger.info("post: ${post.id}, byline: ${post.displayName}, profile attached: ${profile.fullName}, content changed")
                    post.content = content
                    ++profileAndContentUpdated
                } else {
                    logger.info("post: ${post.id}, byline: ${post.displayName}, profile attached: ${profile.fullName}, content not changed")
                    ++profileUpdated
                }
            }
        }
        logger.info("set profile and content: $profileAndContentUpdated")
        logger.info("profile attached: $profileUpdated")
        logger.info("error: $error")
        logger.info("Profile not found:")
        for (bylineName in bylineNameSet) {
            println(bylineName)
        }
    }

    override fun findProfileByEtc(text: String): Profile? {
        return postMigrationRepository.findProfileByEtc(text)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(PostMigrationServiceImpl::class.java)
    }
}