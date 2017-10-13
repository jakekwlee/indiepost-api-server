package com.indiepost.repository;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.model.Image;
import com.indiepost.model.ImageSet;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jake on 10/12/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
@Transactional
public class ImageRepositoryTest {

    @Autowired
    private ImageRepository imageRepository;

    @Test
    public void findByPrefixShouldReturnProperImageSet() {
        String prefix = "2016/06/KykQo6TS";
        ImageSet imageSet = imageRepository.findByPrefix(prefix);
        String actual = imageSet.getPrefix();
        Assert.assertEquals(prefix, actual);
        for (Image image : imageSet.getImages()) {
            Assert.assertTrue(image.getFilePath().contains(prefix));
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
        Assert.assertTrue(imageSetList.size() == 10);

        for (ImageSet imageSet : imageSetList) {
            Set<Image> images = imageSet.getImages();
            String prefix = imageSet.getPrefix();
            for (Image image : images) {
                Assert.assertTrue(image.getFilePath().contains(prefix));
            }
        }
    }
}
