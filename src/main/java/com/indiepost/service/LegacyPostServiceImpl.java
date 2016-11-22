package com.indiepost.service;

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
    public Long saveOrUpdate(Post post) {
        Contentlist contentlist = legacyContentListRepository.findByNo(post.getId());
        if (contentlist != null) {
            update(post);
            return contentlist.getNo();
        }
        Detaillist detaillist = new Detaillist();

        copyNewToLegacy(post, contentlist, detaillist);
        legacyContentListRepository.save(contentlist);
        legacyDetailListRepository.save(detaillist);
        return post.getId();
    }

    @Override
    public void update(Post post) {
        Contentlist contentlist = legacyContentListRepository.findByNo(post.getId());
        List<Detaillist> detaillistList = legacyDetailListRepository.findByParent(contentlist.getNo());
        for (Detaillist detaillist : detaillistList) {
            legacyDetailListRepository.delete(detaillist);
        }
        Detaillist detaillist = new Detaillist();
        copyNewToLegacy(post, contentlist, detaillist);
        legacyContentListRepository.update(contentlist);
        legacyDetailListRepository.save(detaillist);
    }

    @Override
    public void delete(Post post) {
        deleteById(post.getId());
    }

    @Override
    public void deleteById(Long id) {
        Contentlist contentlist = legacyContentListRepository.findByNo(id);
        legacyContentListRepository.delete(contentlist);
        List<Detaillist> detaillistList = legacyDetailListRepository.findByParent(contentlist.getNo());
        for (Detaillist detaillist : detaillistList) {
            legacyDetailListRepository.delete(detaillist);
        }
    }

    private void copyNewToLegacy(Post post, Contentlist contentlist, Detaillist detaillist) {
        DateFormat modifyDateFormat = new SimpleDateFormat("yyyyMMddHH");
        DateFormat regDateFormat = new SimpleDateFormat("yyyyMMdd");

        contentlist.setNo(post.getId());
        contentlist.setRegdate(regDateFormat.format(post.getCreatedAt()));
        contentlist.setModifydate(modifyDateFormat.format(post.getPublishedAt()));
        contentlist.setMenuno(post.getCategory().getId());
        contentlist.setContentname(post.getTitle());
        contentlist.setContenttext(post.getExcerpt());
        contentlist.setWriterid(post.getAuthor().getUsername());
        contentlist.setWritername(post.getDisplayName());
        contentlist.setImageurl(post.getFeaturedImage());
        contentlist.setIsdisplay(new Long(1));
        contentlist.setIsmain(new Long(1));
        contentlist.setPrice(new Long(0));
        contentlist.setDataurl("");
        contentlist.setIsarticleservice(new Long(0));
        contentlist.setIsstreamingservice(new Long(0));
        contentlist.setIsdownloadservice(new Long(0));
        contentlist.setPrice(new Long(0));
        contentlist.setGoods(new Long(0));
        contentlist.setListseq(new Long(0));
        contentlist.setListseqmain(new Long(0));
        contentlist.setOs(new Long(0));
        contentlist.setPlatform(new Long(0));
        contentlist.setType1no(new Long(1));
        contentlist.setType2no(new Long(1));

        Set<Tag> tags = post.getTags();

        if (tags != null) {
            String[] tagNameArray = new String[tags.size()];
            Tag[] tagArray = tags.toArray(new Tag[tags.size()]);
            for (int i = 0; i < tagArray.length; ++i) {
                tagNameArray[i] = tagArray[i].getName();
            }
            contentlist.setKeyword(String.join(", ", tagNameArray));
        }

        detaillist.setParent(post.getId());
        detaillist.setIorder(new Long(1));
        detaillist.setData(post.getContent());
        detaillist.setType(new Long(9));
        detaillist.setIspay(new Long(0));
    }
}
