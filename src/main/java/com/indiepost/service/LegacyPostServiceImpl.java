package com.indiepost.service;

import com.indiepost.enums.PostEnum;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by jake on 11/22/16.
 */
@Service
@Transactional
public class LegacyPostServiceImpl implements LegacyPostService {

    @Autowired
    private LegacyContentListRepository legacyContentListRepository;

    @Autowired
    private LegacyDetailListRepository legacyDetailListRepository;

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
        Long status;

        // TODO
        if (post.getStatus() == PostEnum.Status.PENDING || post.getStatus() == PostEnum.Status.TRASH) {
            status = 0L;
        } else {
            status = 1L;
        }
        DateFormat modifyDateFormat = new SimpleDateFormat("yyyyMMddHH");
        DateFormat regDateFormat = new SimpleDateFormat("yyyyMMdd");

        contentlist.setRegdate(regDateFormat.format(post.getCreatedAt()));
        contentlist.setModifydate(modifyDateFormat.format(post.getPublishedAt()));
        contentlist.setMenuno(post.getCategory().getId());
        contentlist.setContentname(StringEscapeUtils.escapeHtml4(post.getTitle()));
        contentlist.setContenttext(post.getExcerpt());
        contentlist.setWriterid(post.getAuthor().getUsername());
        contentlist.setWritername(post.getDisplayName());
        String imageUrl = "";
        if (post.getTitleImage() != null) {
            ImageSet titleImageSet = post.getTitleImage();
            if (titleImageSet.getOptimized() != null) {
                imageUrl = titleImageSet.getOptimized().getFileUrl();
            } else {
                imageUrl = titleImageSet.getOriginal().getFileUrl();
            }
        }
        contentlist.setImageurl(imageUrl);
        contentlist.setImageurl2("");
        contentlist.setDataurl("");
        contentlist.setSubs(0L);
        contentlist.setIsdisplay(status);
        contentlist.setIsmain(status);
        contentlist.setPrice(0L);
        contentlist.setDataurl("");
        contentlist.setIsarticleservice(0L);
        contentlist.setIsstreamingservice(0L);
        contentlist.setIsdownloadservice(0L);
        contentlist.setPrice(0L);
        if (contentlist.getHit() == null || contentlist.getHit() == 0) {
            contentlist.setGoods(0L);
            contentlist.setUv(0L);
            contentlist.setJjim(0L);
            contentlist.setHit(0L);
        }
        contentlist.setListseq(0L);
        contentlist.setListseqmain(0L);
        contentlist.setOs(0L);
        contentlist.setPlatform(0L);
        contentlist.setType1no(1L);
        contentlist.setType2no(1L);

        List<Tag> tags = post.getTags();

        if (tags != null) {
            String[] tagNameArray = new String[tags.size()];
            Tag[] tagArray = tags.toArray(new Tag[tags.size()]);
            for (int i = 0; i < tagArray.length; ++i) {
                tagNameArray[i] = tagArray[i].getName();
            }
            contentlist.setKeyword(String.join(", ", tagNameArray));
        }

        detaillist.setIorder(1L);
        detaillist.setData(post.getContent());
        detaillist.setType(9L);
        detaillist.setIspay(0L);
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
