package com.vict.framework.utils.lock.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("mysql_lock")
public class MysqlLock {

    @TableId("lockkey")
    private String lockkey;

    @TableField("created")
    private Timestamp created;

    @TableField("overtime")
    private Timestamp overtime;

}
