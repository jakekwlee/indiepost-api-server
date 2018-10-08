package com.indiepost.repository;

import com.indiepost.NewIndiepostApplicationKt;
import com.indiepost.model.Image;
import com.indiepost.model.ImageSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Created by jake on 10/12/17.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NewIndiepostApplicationKt.class)
@WebAppConfiguration
@Transactional
public class ImageRepositoryTests {

    @Inject
    private ImageRepository imageRepository;

    @Test
    public void findByPrefixShouldReturnProperImageSet() {
        String prefix = "2016/06/KykQo6TS";
        ImageSet imageSet = imageRepository.findByPrefix(prefix);
        String actual = imageSet.getPrefix();
        assertThat(actual).isEqualTo(prefix);
        for (Image image : imageSet.getImages()) {
            assertThat(image.getFilePath().contains(prefix)).isTrue();
        }
    }

    @Test
    public void findByPrefixesShouldReturnProperImageSetList() {
        Set<String> prefixList = new LinkedHashSet<>();
        String[] prefixArray = {
                "2016/06/KykQo6TS",
                "2016/06/hLAxroNO",
                "2016/06/cfCVxlur",
                "2016/06/BEIn5p5X",
                "2016/06/IVHRNugU",
                "2016/05/r4zY248T",
                "2016/05/yHCGyfUH",
                "2016/05/VyXUUey2",
                "2016/05/El6gGSXo",
                "2016/05/MdCH5F7x"
        };
        Collections.addAll(prefixList, prefixArray);
        List<ImageSet> imageSetList = imageRepository.findByPrefixes(prefixList);
        assertThat(imageSetList.size()).isEqualTo(10);

        for (ImageSet imageSet : imageSetList) {
            Set<Image> images = imageSet.getImages();
            String prefix = imageSet.getPrefix();
            for (Image image : images) {
                assertThat(image.getFilePath().contains(prefix)).isTrue();
            }
        }
    }
}
