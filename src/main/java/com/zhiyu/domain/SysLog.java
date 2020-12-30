package com.zhiyu.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_log")
public class SysLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userid;
    private String action;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(length = 0)
    private Date Time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userid;
    }

    public void setUserId(Long user_id) {
        this.userid = user_id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getTime() {
        return Time;
    }

    public void setTime(Date time) {
        Time = time;
    }
}
