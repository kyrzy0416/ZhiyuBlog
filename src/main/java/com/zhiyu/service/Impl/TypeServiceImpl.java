package com.zhiyu.service.Impl;

import com.zhiyu.dao.TypeDao;
import com.zhiyu.domain.Type;
import com.zhiyu.exception.myNotFoundException;
import com.zhiyu.service.TypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeDao typeDao;

    @Override
    @Transactional
    public Type saveType(Type type) {
        return typeDao.save(type);
    }

    @Override
    @Transactional
    public Type getType(Long id) {
        return typeDao.getOne(id);
    }

    @Override
    public Type getTypeByName(String name) {
        return typeDao.findByName(name);
    }

    @Override
    @Transactional
    public Page<Type> listType(Pageable pageable) {
        return typeDao.findAll(pageable);
    }

    @Override
    public List<Type> listType() {
        return typeDao.findAll();
    }

    @Override
    public List<Type> listTypeTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0, size, sort);
        return typeDao.findTop(pageable);

    }

    @Override
    @Transactional
    public Type updateType(Long id, Type type) {
        Type t = typeDao.getOne(id);
        if (t == null) {
            throw new myNotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(type, t);
        return typeDao.save(t);
    }

    @Override
    @Transactional
    public void deleteType(Long id) {
        typeDao.deleteById(id);
    }

    @Override
    public Long getCount() {
        return typeDao.count();
    }
}
