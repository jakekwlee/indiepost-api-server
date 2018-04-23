package com.indiepost.service;

import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.ImageSet;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.model.legacy.Contentlist;
import com.indiepost.model.legacy.Detaillist;
import com.indiepost.repository.LegacyContentListRepository;
import com.indiepost.repository.LegacyDetailListRepository;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by jake on 11/22/16.
 */
@Service
@Transactional
public class LegacyPostServiceImpl implements LegacyPostService {

    private final LegacyContentListRepository legacyContentListRepository;

    private final LegacyDetailListRepository legacyDetailListRepository;

    @Autowired
    public LegacyPostServiceImpl(LegacyContentListRepository legacyContentListRepository, LegacyDetailListRepository legacyDetailListRepository) {
        this.legacyContentListRepository = legacyContentListRepository;
        this.legacyDetailListRepository = legacyDetailListRepository;
    }

    @Override
    public Contentlist save(Post post) {
        Contentlist contentlist = new Contentlist();
        Detaillist detaillist = new Detaillist();

        copyNewToLegacy(post, contentlist, detaillist);

        Long id = legacyContentListRepository.save(contentlist);
        detaillist.setParent(id);

        legacyDetailListRepository.save(detaillist);
        return contentlist;
    }

    @Override
    public void update(Post post) {
        Contentlist contentlist = post.getLegacyPost();
        if (contentlist == null) {
            return;
        }
        List<Detaillist> detaillistList = legacyDetailListRepository.findByParent(contentlist.getNo());
        for (Detaillist detaillist : detaillistList) {
            legacyDetailListRepository.delete(detaillist);
        }
        Detaillist detaillist = new Detaillist();
        copyNewToLegacy(post, contentlist, detaillist);

        legacyContentListRepository.update(contentlist);
        detaillist.setParent(contentlist.getNo());
        legacyDetailListRepository.save(detaillist);
    }

    @Override
    public void delete(Post post) {
        Contentlist contentlist = post.getLegacyPost();
        this.deleteContentAndDetail(contentlist);
    }

    @Override
    public void deleteById(Long id) {
        Contentlist contentlist = legacyContentListRepository.findByNo(id);
        this.deleteContentAndDetail(contentlist);
    }

    private void copyNewToLegacy(Post post, Contentlist contentlist, Detaillist detaillist) {
        int status;

        // TODO
        if (post.getStatus() == PostStatus.PENDING || post.getStatus() == PostStatus.TRASH) {
            status = 0;
        } else {
            status = 1;
        }
        String regDatePattern = "yyyyMMdd";
        String modifyDatePattern = "yyyyMMddHH";

        contentlist.setRegdate(post.getCreatedAt().format(DateTimeFormatter.ofPattern(regDatePattern)));
        contentlist.setModifydate(post.getPublishedAt().format(DateTimeFormatter.ofPattern(modifyDatePattern)));
        contentlist.setMenuno(post.getCategory().getId().intValue());
        contentlist.setContentname(StringEscapeUtils.escapeHtml4(post.getTitle()));
        contentlist.setContenttext(post.getExcerpt());
        contentlist.setWriterid(post.getAuthor().getUsername());
        contentlist.setWritername(post.getDisplayName());
        String imageUrl = "";
        if (post.getTitleImage() != null) {
            ImageSet titleImageSet = post.getTitleImage();
            if (titleImageSet.getOptimized() != null) {
                imageUrl = titleImageSet.getOptimized().getFilePath();
            } else {
                imageUrl = titleImageSet.getOriginal().getFilePath();
            }
        }
        contentlist.setImageurl(imageUrl);
        contentlist.setImageurl2("");
        contentlist.setDataurl("");
        contentlist.setSubs(0);
        contentlist.setIsdisplay(status);
        contentlist.setIsmain(status);
        contentlist.setPrice(0);
        contentlist.setDataurl("");
        contentlist.setIsarticleservice(0);
        contentlist.setIsstreamingservice(0);
        contentlist.setIsdownloadservice(0);
        contentlist.setPrice(0);
        if (contentlist.getHit() == null || contentlist.getHit() == 0) {
            contentlist.setGoods(0);
            contentlist.setUv(0);
            contentlist.setJjim(0);
            contentlist.setHit(0);
        }
        contentlist.setListseq(0);
        contentlist.setListseqmain(0);
        contentlist.setOs(0);
        contentlist.setPlatform(0);
        contentlist.setType1no(1);
        contentlist.setType2no(1);

        List<Tag> tags = post.getTags();

        if (tags != null) {
            String[] tagNameArray = new String[tags.size()];
            Tag[] tagArray = tags.toArray(new Tag[tags.size()]);
            for (int i = 0; i < tagArray.length; ++i) {
                tagNameArray[i] = tagArray[i].getName();
            }
            contentlist.setKeyword(String.join(", ", tagNameArray));
        }

        detaillist.setIorder(1);
        detaillist.setData(post.getContent());
        detaillist.setType(9);
        detaillist.setIspay(0);
    }

    private void deleteContentAndDetail(Contentlist contentlist) {
        if (contentlist == null) {
            return;
        }
        Long parentId = contentlist.getNo();
        legacyContentListRepository.delete(contentlist);
        List<Detaillist> detaillistList = legacyDetailListRepository.findByParent(parentId);
        for (Detaillist detaillist : detaillistList) {
            legacyDetailListRepository.delete(detaillist);
        }
    }
}
