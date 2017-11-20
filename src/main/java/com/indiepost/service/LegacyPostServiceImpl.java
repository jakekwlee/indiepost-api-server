package com.indiepost.service;

import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.ImageSet;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.model.legacy.LegacyPost;
import com.indiepost.model.legacy.LegacyPostContent;
import com.indiepost.repository.LegacyPostContentRepository;
import com.indiepost.repository.LegacyPostRepository;
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

    private final LegacyPostRepository legacyPostRepository;

    private final LegacyPostContentRepository legacyPostContentRepository;

    @Autowired
    public LegacyPostServiceImpl(LegacyPostRepository legacyPostRepository, LegacyPostContentRepository legacyPostContentRepository) {
        this.legacyPostRepository = legacyPostRepository;
        this.legacyPostContentRepository = legacyPostContentRepository;
    }

    @Override
    public LegacyPost save(Post post) {
        LegacyPost legacyPost = new LegacyPost();
        LegacyPostContent legacyPostContent = new LegacyPostContent();

        copyNewToLegacy(post, legacyPost, legacyPostContent);

        Long id = legacyPostRepository.save(legacyPost);
        legacyPostContent.setParent(id);

        legacyPostContentRepository.save(legacyPostContent);
        return legacyPost;
    }

    @Override
    public void update(Post post) {
        LegacyPost legacyPost = post.getLegacyPost();
        if (legacyPost == null) {
            return;
        }
        List<LegacyPostContent> legacyPostContentList = legacyPostContentRepository.findByParent(legacyPost.getNo());
        for (LegacyPostContent legacyPostContent : legacyPostContentList) {
            legacyPostContentRepository.delete(legacyPostContent);
        }
        LegacyPostContent legacyPostContent = new LegacyPostContent();
        copyNewToLegacy(post, legacyPost, legacyPostContent);

        legacyPostRepository.update(legacyPost);
        legacyPostContent.setParent(legacyPost.getNo());
        legacyPostContentRepository.save(legacyPostContent);
    }

    @Override
    public void delete(Post post) {
        LegacyPost legacyPost = post.getLegacyPost();
        this.deleteContentAndDetail(legacyPost);
    }

    @Override
    public void deleteById(Long id) {
        LegacyPost legacyPost = legacyPostRepository.findByNo(id);
        this.deleteContentAndDetail(legacyPost);
    }

    private void copyNewToLegacy(Post post, LegacyPost legacyPost, LegacyPostContent legacyPostContent) {
        Long status;

        // TODO
        if (post.getStatus() == PostStatus.PENDING || post.getStatus() == PostStatus.TRASH) {
            status = 0L;
        } else {
            status = 1L;
        }
        String regDatePattern = "yyyyMMdd";
        String modifyDatePattern = "yyyyMMddHH";

        legacyPost.setRegdate(post.getCreatedAt().format(DateTimeFormatter.ofPattern(regDatePattern)));
        legacyPost.setModifydate(post.getPublishedAt().format(DateTimeFormatter.ofPattern(modifyDatePattern)));
        legacyPost.setMenuno(post.getCategory().getId());
        legacyPost.setContentname(StringEscapeUtils.escapeHtml4(post.getTitle()));
        legacyPost.setContenttext(post.getExcerpt());
        legacyPost.setWriterid(post.getCreator().getUsername());
        legacyPost.setWritername(post.getBylineName());
        String imageUrl = "";
        if (post.getTitleImage() != null) {
            ImageSet titleImageSet = post.getTitleImage();
            if (titleImageSet.getOptimized() != null) {
                imageUrl = titleImageSet.getOptimized().getFilePath();
            } else {
                imageUrl = titleImageSet.getOriginal().getFilePath();
            }
        }
        legacyPost.setImageurl(imageUrl);
        legacyPost.setImageurl2("");
        legacyPost.setDataurl("");
        legacyPost.setSubs(0L);
        legacyPost.setIsdisplay(status);
        legacyPost.setIsmain(status);
        legacyPost.setPrice(0L);
        legacyPost.setDataurl("");
        legacyPost.setIsarticleservice(0L);
        legacyPost.setIsstreamingservice(0L);
        legacyPost.setIsdownloadservice(0L);
        legacyPost.setPrice(0L);
        if (legacyPost.getHit() == null || legacyPost.getHit() == 0) {
            legacyPost.setGoods(0L);
            legacyPost.setUv(0L);
            legacyPost.setJjim(0L);
            legacyPost.setHit(0L);
        }
        legacyPost.setListseq(0L);
        legacyPost.setListseqmain(0L);
        legacyPost.setOs(0L);
        legacyPost.setPlatform(0L);
        legacyPost.setType1no(1L);
        legacyPost.setType2no(1L);

        List<Tag> tags = post.getTags();

        if (tags != null) {
            String[] tagNameArray = new String[tags.size()];
            Tag[] tagArray = tags.toArray(new Tag[tags.size()]);
            for (int i = 0; i < tagArray.length; ++i) {
                tagNameArray[i] = tagArray[i].getName();
            }
            legacyPost.setKeyword(String.join(", ", tagNameArray));
        }

        legacyPostContent.setIorder(1L);
        legacyPostContent.setData(post.getContent());
        legacyPostContent.setType(9L);
        legacyPostContent.setIspay(0L);
    }

    private void deleteContentAndDetail(LegacyPost legacyPost) {
        if (legacyPost == null) {
            return;
        }
        Long parentId = legacyPost.getNo();
        legacyPostRepository.delete(legacyPost);
        List<LegacyPostContent> legacyPostContentList = legacyPostContentRepository.findByParent(parentId);
        for (LegacyPostContent legacyPostContent : legacyPostContentList) {
            legacyPostContentRepository.delete(legacyPostContent);
        }
    }
}
