CREATE TABLE `key_value` (
                             `id` bigint(20) NOT NULL,
                             `key` varchar(100) NOT NULL,
                             `value1` text,
                             `value2` text,
                             `desc` varchar(512) DEFAULT NULL,
                             `hidden_flag` tinyint(4) DEFAULT NULL COMMENT '隐藏标记',
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `NewTable_UN` (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `task_execute_instance` (
                                         `taskExecuteInstanceID` varchar(32) NOT NULL COMMENT 'id',
                                         `serverName` varchar(32) DEFAULT NULL COMMENT '服务名',
                                         `serverId` varchar(64) DEFAULT NULL COMMENT '服务id',
                                         `type` varchar(64) DEFAULT NULL COMMENT '任务类型',
                                         `status` tinyint(4) DEFAULT NULL COMMENT '状态',
                                         `executeCount` int(11) DEFAULT NULL COMMENT '执行次数',
                                         `actionTime` timestamp NULL DEFAULT NULL COMMENT '触发时间',
                                         `executeJSON` text COMMENT '执行json',
                                         `userContext` text COMMENT '上下文对象',
                                         `linkTaskExecuteInstanceID` varchar(32) DEFAULT NULL,
                                         `failRetryTimeSeconds` varchar(1024) DEFAULT NULL COMMENT '重试时间',
                                         PRIMARY KEY (`taskExecuteInstanceID`) USING BTREE,
                                         KEY `task_execute_instance_serverName_IDX` (`serverName`) USING BTREE,
                                         KEY `task_execute_instance_serverId_IDX` (`serverId`) USING BTREE,
                                         KEY `task_execute_instance_status_IDX` (`status`) USING BTREE,
                                         KEY `task_execute_instance_actionTime_IDX` (`actionTime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='任务执行器';

CREATE TABLE `buser` (
                         `id` bigint(20) NOT NULL COMMENT 'id',
                         `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '登录用户名',
                         `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '登录密码',
                         `buser_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '员工编号',
                         `role_id` bigint(20) DEFAULT NULL COMMENT '角色id',
                         PRIMARY KEY (`id`),
                         KEY `buser_username_IDX` (`username`) USING BTREE,
                         KEY `buser_password_IDX` (`password`) USING BTREE,
                         KEY `buser_buser_code_IDX` (`buser_code`) USING BTREE,
                         KEY `buser_role_id_IDX` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='b端用户表';

CREATE TABLE `role` (
                        id BIGINT NOT NULL COMMENT 'id',
                        rolename varchar(32) NULL COMMENT '角色名称',
                        permission_list json NULL COMMENT '权限列表["code1", "code2"]',
                        CONSTRAINT role_pk PRIMARY KEY (id)
)
    ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci
COMMENT='角色权限';
CREATE INDEX role_rolename_IDX USING BTREE ON `role` (rolename);



CREATE TABLE buser_role (
                            id BIGINT NOT NULL COMMENT 'id',
                            buser_id BIGINT NULL COMMENT 'buser_id',
                            role_id BIGINT NULL COMMENT '角色id',
                            CONSTRAINT buser_role_pk PRIMARY KEY (id)
)
    ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci
COMMENT='用户角色关联';
CREATE INDEX buser_role_buser_id_IDX USING BTREE ON buser_role (buser_id);


CREATE TABLE file_cache (
                            id BIGINT NOT NULL,
                            resource_file_uri varchar(256) NULL COMMENT '源码uri',
                            redirect_url varchar(256) NULL COMMENT '资源重定向url',
                            CONSTRAINT file_cache_pk PRIMARY KEY (id)
)
    ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci
COMMENT='文件缓存表';
CREATE INDEX file_cache_resource_file_uri_IDX USING BTREE ON file_cache (resource_file_uri);

CREATE TABLE `custom_page` (
                               `id` bigint(20) NOT NULL,
                               `custom_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
                               `page_value` text COLLATE utf8_unicode_ci,
                               PRIMARY KEY (`id`),
                               KEY `custome_page_custom_name_IDX` (`custom_name`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='自定义页面';

-- 客户
CREATE TABLE `customer` (
                            `id` bigint(20) NOT NULL,
                            `customer_phone` varchar(320) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '手机号',
                            `customer_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '姓名',
                            `password` varchar(100) DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            KEY `customer_phone_IDX` (`customer_phone`) USING BTREE,
                            KEY `customer_name_IDX` (`customer_name`) USING BTREE,
                            KEY `customer_password_IDX` (`password`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `job` (
                       `id` bigint(20) NOT NULL,
                       `job_name` varchar(32) DEFAULT NULL,
                       `cron` varchar(100) DEFAULT NULL,
                       `bean_name` varchar(100) DEFAULT NULL,
                       `method_name` varchar(100) DEFAULT NULL,
                       `status` varchar(100) DEFAULT NULL COMMENT 'enable:启用 disabled:禁用',
                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `job_history` (
                               `id` bigint(20) NOT NULL,
                               `job_id` bigint(20) NOT NULL,
                               `run_time` timestamp(6) NULL DEFAULT NULL COMMENT '启动时间',
                               `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '运行状态 ok-运行成功 ex-运行异常 run-运行中',
                               `stop_time` timestamp(6) NULL DEFAULT NULL COMMENT '停止时间',
                               `exception` text,
                               PRIMARY KEY (`id`),
                               KEY `job_history_job_id_IDX` (`job_id`) USING BTREE,
                               KEY `job_history_run_time_IDX` (`run_time`) USING BTREE,
                               KEY `job_history_stop_time_IDX` (`stop_time`) USING BTREE,
                               KEY `job_history_status_IDX` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `organize` (
                            `id` bigint(20) NOT NULL,
                            `organize_name` varchar(100) DEFAULT NULL COMMENT '组织名称',
                            `father_id` bigint(20) DEFAULT NULL COMMENT '父id',
                            PRIMARY KEY (`id`),
                            KEY `organize_organize_name_IDX` (`organize_name`) USING BTREE,
                            KEY `organize_father_id_IDX` (`father_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='组织';


CREATE TABLE `organize_buser` (
                                  `id` bigint(20) NOT NULL,
                                  `organize_id` bigint(20) NOT NULL COMMENT '组织id',
                                  `buser_id` bigint(20) NOT NULL COMMENT 'buser_id',
                                  PRIMARY KEY (`id`),
                                  KEY `organize_buser_organize_id_IDX` (`organize_id`) USING BTREE,
                                  KEY `organize_buser_buser_id_IDX` (`buser_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `chinese_poetry` (
                                  `id` bigint(20) NOT NULL COMMENT 'id',
                                  `title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '标题',
                                  `author` varchar(64) DEFAULT NULL COMMENT '作者',
                                  `paragraphs` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '内容',
                                  `classify_id` bigint(20) DEFAULT NULL COMMENT '分类id',
                                  `order_num` int(11) NOT NULL AUTO_INCREMENT COMMENT '排序字段',
                                  `cdesc` varchar(2048) DEFAULT NULL,
                                  PRIMARY KEY (`id`),
                                  KEY `chinese_poetry_title_IDX` (`title`) USING BTREE,
                                  KEY `chinese_poetry_classify_id_IDX` (`classify_id`) USING BTREE,
                                  KEY `chinese_poetry_order_num_IDX` (`order_num`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=435774 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='古诗词';

CREATE TABLE `chinese_poetry_classify` (
                                           `id` bigint(20) NOT NULL,
                                           `classify_name` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '分类名称',
                                           `order_num` int(11) NOT NULL AUTO_INCREMENT,
                                           PRIMARY KEY (`id`),
                                           KEY `chinese_poetry_classify_order_num_IDX` (`order_num`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='古诗分类';

CREATE TABLE `chinese_poetry_tag` (
                                      `id` bigint(20) NOT NULL,
                                      `chinese_poetry_id` bigint(20) NOT NULL COMMENT '古诗id',
                                      `tag` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'tag标签',
                                      PRIMARY KEY (`id`),
                                      KEY `chinese_poetry_tag_chinese_poetry_id_IDX` (`chinese_poetry_id`) USING BTREE,
                                      KEY `chinese_poetry_tag_tag_IDX` (`tag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='古诗词 tag';

CREATE TABLE `image` (
                         `id` bigint(20) NOT NULL,
                         `url` varchar(512) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '图片地址',
                         `attname` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '别名',
                         `order_num` int(11) NOT NULL AUTO_INCREMENT,
                         `svg_code` text COLLATE utf8_unicode_ci COMMENT 'svgCode',
                         `base64_code` text COLLATE utf8_unicode_ci COMMENT 'base64Code',
                         `image_type` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '图片类型 url svg base64',
                         PRIMARY KEY (`id`),
                         KEY `images_attname_IDX` (`attname`) USING BTREE,
                         KEY `images_order_num_IDX` (`order_num`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=156 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='图片';

CREATE TABLE `mysql_lock` (
                              `lockkey` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                              `created` timestamp(6) NULL DEFAULT NULL,
                              `overtime` timestamp(6) NULL DEFAULT NULL,
                              PRIMARY KEY (`lockkey`),
                              KEY `mysql_lock_created_IDX` (`created`) USING BTREE,
                              KEY `mysql_lock_overtime_IDX` (`overtime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='锁';
