package com.indiepost.repository

import com.indiepost.NewIndiepostApplication
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.inject.Inject

/**
 * Created by jake on 10/12/17.
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = arrayOf(NewIndiepostApplication::class))
@WebAppConfiguration
@Transactional
class ImageRepositoryTests {

    @Inject
    private lateinit var imageRepository: ImageRepository

    @Test
    fun findByPrefixShouldReturnProperImageSet() {
        val prefix = "2016/06/KykQo6TS"
        val imageSet = imageRepository.findByPrefix(prefix)
        val actual = imageSet?.prefix
        assertThat(actual).isEqualTo(prefix)
        for ((_, filePath) in imageSet!!.images!!) {
            if (filePath != null) {
                assertThat(filePath.contains(prefix)).isTrue()
            }
        }
    }

    @Test
    fun findByPrefixesShouldReturnProperImageSetList() {
        val prefixList = LinkedHashSet<String>()
        val prefixArray = arrayOf("2016/06/KykQo6TS", "2016/06/hLAxroNO", "2016/06/cfCVxlur", "2016/06/BEIn5p5X", "2016/06/IVHRNugU", "2016/05/r4zY248T", "2016/05/yHCGyfUH", "2016/05/VyXUUey2", "2016/05/El6gGSXo", "2016/05/MdCH5F7x")
        Collections.addAll(prefixList, *prefixArray)
        val imageSetList = imageRepository.findByPrefixes(prefixList)
        assertThat(imageSetList.size).isEqualTo(10)

        for (imageSet in imageSetList) {
            val images = imageSet.images
            val prefix = imageSet.prefix
            if (images != null) {
                for ((_, filePath) in images) {
                    if (filePath != null) {
                        assertThat(filePath.contains(prefix!!)).isTrue()
                    }
                }
            }
        }
    }
}
