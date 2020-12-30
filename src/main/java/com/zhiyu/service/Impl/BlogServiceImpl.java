package com.zhiyu.service.Impl;

import com.zhiyu.dao.BlogDao;
import com.zhiyu.domain.Blog;
import com.zhiyu.domain.BlogQuery;
import com.zhiyu.domain.Type;
import com.zhiyu.exception.myNotFoundException;
import com.zhiyu.service.BlogService;
import com.zhiyu.util.MarkdownUtils;
import com.zhiyu.util.ZhiyuUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogDao blogDao;

    @Override
    public Blog getBlog(Long id) {
        return blogDao.getOne(id);
    }

    @Override
    @Transactional
    public Blog getAndConvert(Long id) {
        Blog blog = blogDao.getOne(id);
        if (blog == null)
            throw new myNotFoundException("该博客不存在");
//        待优化
        Blog blogToHtml = new Blog();
        BeanUtils.copyProperties(blog, blogToHtml);
        String content = blogToHtml.getContent();
        blogToHtml.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        blogDao.updateViews(id);
        return blogToHtml;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogDao.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicates.add(cb.like(root.<String>get("title"), "%"+blog.getTitle()+"%"));
                }
                if (blog.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if (blog.isRecommend()) {
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogDao.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogDao.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"), tagId);
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogDao.findByQuery(query, pageable);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogDao.findTop(pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogDao.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            map.put(year, blogDao.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogDao.count();
    }

    @Override
    public Long getAllViews() {
        List<Blog> blogs = blogDao.findAll();
        long views = 0;
        for (Blog blog : blogs) {
            views += blog.getViews();
        }
        return views;
    }

    @Override
    @Transactional
    public Blog saveBlog(Blog blog) {
        if (blog.getCreateTime() == null) { // 判断Blog是否为新创建的Blog
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
            if ("".equals(blog.getFlag()))
                blog.setFlag("原创");
        } else {
            blog.setUpdateTime(new Date());
        }
        return blogDao.save(blog);
    }

    @Override
    @Transactional
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogDao.getOne(id);
        if (b == null)
            throw new myNotFoundException("该博客不存在");
        BeanUtils.copyProperties(blog, b, ZhiyuUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogDao.save(b);
    }

    @Override
    @Transactional
    public void deleteBlog(Long id) {
        blogDao.deleteById(id);
    }
}
