package com.zhiyu.service.Impl;

import com.zhiyu.dao.SysLogDao;
import com.zhiyu.domain.SysLog;
import com.zhiyu.service.ISysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SysLogServiceImpl implements ISysLogService {

    @Autowired
    private SysLogDao sysLogDao;

    @Override
    public void insertLog(SysLog entity) {
        // TODO Auto-generated method
        long count = sysLogDao.count();
        if (count > 4) {
            sysLogDao.deleteAll();
        }
        sysLogDao.save(entity);
    }

    @Override
    public List<SysLog> listSysLog() {
        return sysLogDao.findAll();
    }
}
