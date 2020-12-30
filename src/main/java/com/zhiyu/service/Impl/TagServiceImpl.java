package com.zhiyu.service.Impl;

import com.zhiyu.dao.TagDao;
import com.zhiyu.domain.Tag;
import com.zhiyu.exception.myNotFoundException;
import com.zhiyu.service.TagService;
import com.zhiyu.util.ZhiyuUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagDao tagDao;

    @Override
    public Tag saveTag(Tag tag) {
        return tagDao.save(tag);
    }

    @Override
    public Tag getTag(Long id) {
        return tagDao.getOne(id);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagDao.findByName(name);
    }

    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagDao.findAll(pageable);
    }

    @Override
    public List<Tag> listTag() {
        return tagDao.findAll();
    }

    @Override
    public List<Tag> listTag(String ids) {
        return tagDao.findAllById(ZhiyuUtils.convertToList(ids));
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0, size, sort);
        return tagDao.findTop(pageable);
    }

    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t = tagDao.getOne(id);
        if (t == null)
            throw new myNotFoundException("不存在该标签");
        BeanUtils.copyProperties(tag, t);
        return tagDao.save(t);
    }

    @Override
    public void deleteTag(Long id) {
        tagDao.deleteById(id);
    }

    @Override
    public Long getCount() {
        return tagDao.count();
    }
}
