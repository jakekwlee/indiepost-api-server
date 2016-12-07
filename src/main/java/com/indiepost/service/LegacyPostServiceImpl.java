package com.indiepost.service;

import com.indiepost.enums.PostEnum;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.model.legacy.Contentlist;
import com.indiepost.model.legacy.Detaillist;
import com.indiepost.repository.LegacyContentListRepository;
import com.indiepost.repository.LegacyDetailListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

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
        Long long0 = new Long(0);
        Long long1 = new Long(1);
        Long long9 = new Long(9);

        if (post.getStatus() == PostEnum.Status.PENDING) {
            status = long0;
        } else {
            status = long1;
        }
        DateFormat modifyDateFormat = new SimpleDateFormat("yyyyMMddHH");
        DateFormat regDateFormat = new SimpleDateFormat("yyyyMMdd");

        contentlist.setRegdate(regDateFormat.format(post.getCreatedAt()));
        contentlist.setModifydate(modifyDateFormat.format(post.getPublishedAt()));
        contentlist.setMenuno(post.getCategory().getId());
        contentlist.setContentname(post.getTitle());
        contentlist.setContenttext(post.getExcerpt());
        contentlist.setWriterid(post.getAuthor().getUsername());
        contentlist.setWritername(post.getDisplayName());
        contentlist.setImageurl(post.getFeaturedImage());
        contentlist.setImageurl2("");
        contentlist.setDataurl("");
        contentlist.setSubs(long0);
        contentlist.setIsdisplay(status);
        contentlist.setIsmain(status);
        contentlist.setPrice(long0);
        contentlist.setDataurl("");
        contentlist.setIsarticleservice(long0);
        contentlist.setIsstreamingservice(long0);
        contentlist.setIsdownloadservice(long0);
        contentlist.setPrice(long0);
        if (contentlist.getHit() == null || contentlist.getHit() == 0) {
            contentlist.setGoods(long0);
            contentlist.setUv(long0);
            contentlist.setJjim(long0);
            contentlist.setHit(long0);
        }
        contentlist.setListseq(long0);
        contentlist.setListseqmain(long0);
        contentlist.setOs(long0);
        contentlist.setPlatform(long0);
        contentlist.setType1no(long1);
        contentlist.setType2no(long1);

        Set<Tag> tags = post.getTags();

        if (tags != null) {
            String[] tagNameArray = new String[tags.size()];
            Tag[] tagArray = tags.toArray(new Tag[tags.size()]);
            for (int i = 0; i < tagArray.length; ++i) {
                tagNameArray[i] = tagArray[i].getName();
            }
            contentlist.setKeyword(String.join(", ", tagNameArray));
        }

        detaillist.setIorder(long1);
        detaillist.setData(post.getContent());
        detaillist.setType(long9);
        detaillist.setIspay(long0);
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
