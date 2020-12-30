package com.zhiyu.dao;

import com.zhiyu.domain.SysLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SysLogDao extends JpaRepository<SysLog, Long>, JpaSpecificationExecutor<SysLog> {

}
