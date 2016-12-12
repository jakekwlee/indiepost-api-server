package com.indiepost.config;

import org.dozer.CustomConverter;
import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jake on 16. 12. 12.
 */
@Configuration
public class DozerConfigurer {

    @Bean(name = "org.dozer.Mapper")
    public DozerBeanMapper dozerBean() {
        List<String> mappingFiles = Arrays.asList(
                "bean-mappings.xml"
        );

        DozerBeanMapper dozerBean = new DozerBeanMapper();
        Map customConverter = new HashMap<String, CustomConverter>();
        customConverter.put("EnumStringConverter", new EnumStringBiDirectionalDozerConverter());
        customConverter.put("TagStringConverter", new TagSetToTagStringListConverter());
        dozerBean.setCustomConvertersWithId(customConverter);
        dozerBean.setMappingFiles(mappingFiles);
        return dozerBean;
    }
}
