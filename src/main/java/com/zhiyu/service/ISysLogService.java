package com.zhiyu.service;

import com.zhiyu.domain.SysLog;

import java.util.List;

public interface ISysLogService {
    void insertLog(SysLog entity);
    List<SysLog> listSysLog();
}
