-- 创建库
create database if not exists bi;

-- 切换库
use bi;

-- 用户表
create table if not exists user
(
    id            bigint auto_increment comment 'id' primary key,
    user_account  varchar(256)                       not null comment '账号',
    user_password varchar(512)                       not null comment '密码',
    create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete     tinyint  default 0                 not null comment '是否删除',
    index idx_user_account (user_account)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 图表表
create table if not exists chart
(
    id           bigint auto_increment comment 'id' primary key,
    goal         text                               null comment '分析目标',
    `name`       varchar(128)                       null comment '图表名称',
    chart_data   text                               null comment '图表数据',
    chart_type   varchar(128)                       null comment '图表类型',
    gen_chart    text                               null comment '生成的图表数据',
    gen_result   text                               null comment '生成的分析结论',
    status       varchar(128)                       not null default 'wait' comment 'wait,running,succeed,failed',
    exec_message text                               null comment '执行信息',
    user_id      bigint                             null comment '创建用户 id',
    create_time  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete    tinyint  default 0                 not null comment '是否删除'
) comment '图表信息表' collate = utf8mb4_unicode_ci;
