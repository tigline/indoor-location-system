create table user_info
(
    user_id  bigint      not null
        primary key,
    username varchar(64) null,
    password varchar(64) null,
    nickname varchar(50) null,
    phone    varchar(11) null,
    role     varchar(20) null
);

create table aoa_data_info
(
    id            bigint auto_increment
        primary key,
    gateway       varchar(64) null,
    node_id       varchar(64) null,
    system_id     varchar(64) null,
    type          varchar(64) null,
    mac           varchar(64) null,
    motion        varchar(64) null,
    opt_scale     float       null,
    position_type varchar(64) null,
    x             float       null,
    y             float       null,
    timestamp     bigint      null
);

alter table user_info
    add email varchar(64) null after nickname;

alter table aoa_data_info
    change x pos_x float null;

alter table aoa_data_info
    change y pos_y float null;

create table gateway_info
(
    gateway      varchar(64)   not null
        primary key,
    group_id     varchar(64)   null,
    name         varchar(100)  null,
    product_name varchar(100)  null,
    system_id    varchar(64)   null,
    type         varchar(64)   null,
    status       varchar(64)   null,
    e_fence_ids  varchar(200)  null,
    set_x        float         null,
    set_y        float         null,
    set_z        float         null,
    update_time  bigint        null,
    extra_info   varchar(1024) null
);

create table beacon_info
(
    device_id     varchar(64)   not null
        primary key,
    mac           varchar(64)   null,
    gateway       varchar(64)   null,
    group_id      varchar(64)   null,
    name          varchar(100)  null,
    product_name  varchar(100)  null,
    system_id     varchar(64)   null,
    type          varchar(64)   null,
    status        varchar(64)   null,
    e_fence_ids   varchar(200)  null,
    motion        varchar(64)   null,
    opt_scale     float         null,
    position_type varchar(64)   null,
    pos_x         float         null,
    pos_y         float         null,
    update_time   bigint        null,
    extra_info    varchar(1024) null
);

alter table aoa_data_info
    change node_id device_id varchar(64) null;

alter table aoa_data_info
    modify gateway varchar(64) null after device_id;

alter table gateway_info
    add map_id varchar(64) null after gateway;

alter table beacon_info
    add map_id varchar(64) null after gateway;

-- 2023-02-10
alter table user_info
    modify user_id varchar(64) not null;

-- 2023-02-11
alter table aoa_data_info
    change system_id map_id varchar(64) null;

alter table aoa_data_info
    drop column gateway;

alter table aoa_data_info
    drop column type;

alter table aoa_data_info
    drop column mac;

alter table aoa_data_info
    drop column motion;

alter table aoa_data_info
    drop column position_type;

alter table gateway_info
    add ip varchar(64) null;

create table map_info
(
    map_id   varchar(64)  not null
        primary key,
    name     varchar(100) null,
    location varchar(100) null,
    picture  text         null
);

-- 2023-02-13
alter table map_info
    drop column location;

alter table map_info
    add width float null after name;

alter table map_info
    add height float null after width;

alter table map_info
    add pixel_width float null after height;

alter table map_info
    add pixel_height float null after pixel_width;

alter table map_info
    add remark varchar(1024) null;

create table fence_info
(
    fence_id varchar(64)       not null
        primary key,
    name     varchar(100)      null,
    map_id   varchar(64)       null,
    type     varchar(20)       null,
    points   varchar(1024)     null,
    enabled  tinyint default 0 null
);

alter table gateway_info
    change e_fence_ids fence_ids varchar(1024) null;

alter table beacon_info
    change e_fence_ids fence_ids varchar(1024) null;

-- 2023-02-14
create table department_info
(
    dep_id    bigint auto_increment
        primary key,
    name      varchar(200)     null,
    parent_id bigint default 0 null
);

create table personnel_type_info
(
    type_id     bigint auto_increment
        primary key,
    type_name   varchar(50) null,
    picture     text        null,
    create_time bigint      null
);

create table personnel_info
(
    personnel_id bigint auto_increment
        primary key,
    name         varchar(100) null,
    sex          varchar(20)  null,
    tag          varchar(64)  null,
    email        varchar(100) null,
    phone        varchar(20)  null,
    id_number    varchar(64)  null,
    type_id      bigint       null,
    dep_id       bigint       null,
    avatar       text         null,
    create_time  bigint       null
);

-- 2023-02-15
create table building_info
(
    building_id varchar(64)  null,
    name        varchar(100) null,
    address     varchar(200) null,
    picture     text         null
);

alter table map_info
    add building_id varchar(64) null after name;

alter table map_info
    drop column width;

alter table map_info
    add floor varchar(100) null after building_id;

alter table map_info
    drop column height;

alter table map_info
    drop column pixel_width;

alter table map_info
    drop column pixel_height;

alter table map_info
    drop column remark;

alter table personnel_info
    drop column email;

alter table personnel_info
    drop column phone;

alter table personnel_info
    drop column id_number;

create table thing_info
(
    thing_id bigint auto_increment
        primary key,
    name     varchar(100) null,
    tag      varchar(64)  null,
    type_id  bigint       null,
    picture  text         null
);

create table thing_type_info
(
    type_id     bigint auto_increment
        primary key,
    type_name   varchar(100) null,
    picture     text         null,
    create_time bigint       null
);

-- 2023-02-16
alter table gateway_info
    add his_x float null after set_z;

alter table gateway_info
    add his_y float null after his_x;

alter table gateway_info
    add his_z float null after his_y;

-- 2023-02-17
alter table map_info
    add width float null;

alter table map_info
    add length float null;

alter table map_info
    add width_px float null;

alter table map_info
    add length_px float null;

-- 2023-02-18
alter table beacon_info
    add online tinyint default 0 null after status;

-- 2023-02-19
alter table gateway_info
    add angle float null after set_z;

-- 2023-02-20
alter table aoa_data_info
    add type varchar(64) null after device_id;

-- 2023-02-21
alter table building_info
    modify picture longtext null;

alter table map_info
    modify picture longtext null;

alter table personnel_info
    modify avatar longtext null;

alter table personnel_type_info
    modify picture longtext null;

alter table thing_info
    modify picture longtext null;

alter table thing_type_info
    modify picture longtext null;

SELECT @@event_scheduler;

CREATE EVENT dealWithOffline
    ON SCHEDULE every 60 second
    DO UPDATE beacon_info SET online=0 WHERE (online = 1 AND update_time <= (unix_timestamp() - 60))
;

-- 2023-02-22
create table alarm_info
(
    alarm_id    bigint auto_increment
        primary key,
    device_id   varchar(64) null,
    fence_id    varchar(64) null,
    type        varchar(64) null,
    content     text        null,
    status      varchar(64) null,
    update_time bigint      null,
    create_time bigint      null
);

-- 2023-02-27
alter table alarm_info
    add name varchar(200) null after device_id;

alter table alarm_info
    add map_id varchar(64) null after name;

alter table alarm_info
    add point varchar(200) null after status;

-- 2023-04-28
CREATE TABLE `t_company` (
     `id` int NOT NULL AUTO_INCREMENT COMMENT '公司ID',
     `company_code` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '公司编码',
     `company_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '企业名称',
     `simple_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '企业别名',
     `contact_name` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '联系人',
     `contact_phone` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '联系人电话',
     `begin_create_time` timestamp NULL DEFAULT NULL COMMENT '开通日期',
     `active` tinyint DEFAULT '1' COMMENT '1有效，0无效',
     `create_time` datetime DEFAULT NULL COMMENT '创建时间',
     `update_time` datetime DEFAULT NULL COMMENT '修改时间',
     PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

alter table building_info
    add company_id int(11) null after building_id;

DROP TABLE IF EXISTS `t_model`;
CREATE TABLE `t_model` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '模型ID',
   `model_code` varchar(64) NOT NULL COMMENT '模型编码',
   `model_name` varchar(50) NOT NULL COMMENT '物模型名',
   `model_version` varchar(20) DEFAULT NULL COMMENT '物模型版本',
   `company_id` int(11) DEFAULT NULL COMMENT '所属公司id',
   `properties` json DEFAULT NULL COMMENT '屬性',
   `configs` json DEFAULT NULL COMMENT '配置',
   `events` json DEFAULT NULL COMMENT '事件',
   `commands` json DEFAULT NULL COMMENT '指令',
   `active` tinyint(1) DEFAULT '1' COMMENT '1有效，0无效',
   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
   `update_time` datetime DEFAULT NULL COMMENT '修改时间',
   PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 2023-5-9
CREATE TABLE `t_model_device` (
  `id` int(11) NOT NULL COMMENT 'id',
  `device_id` varchar(64) NOT NULL COMMENT '网关设备ID',
  `company_id` int(11) DEFAULT NULL COMMENT '所属公司ID',
  `model_id` varchar(64) NOT NULL COMMENT '物模型ID',
  `active` tinyint(2) DEFAULT NULL COMMENT '1有效，0无效',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 2023-5-11
ALTER TABLE `t_model_device` CHANGE device_id client_id varchar(64) COMMENT '设备ID';
ALTER TABLE `t_model_device` ADD device_id VARCHAR(64) NULL COMMENT '网关ID' AFTER client_id;
ALTER TABLE `t_model` ADD model_code VARCHAR(64) NOT NULL COMMENT '模型编码' AFTER id;
ALTER TABLE `t_model_device` MODIFY model_id Int(11) COMMENT '模型ID';
ALTER TABLE `t_model` CHANGE model_version version_name VARCHAR(20) COMMENT '物模型版本名称';
ALTER TABLE `t_model` ADD version_code Int(11) NULL COMMENT '物模型版本号' AFTER version_name;

-- 2023-5-22
CREATE TABLE `t_command_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) DEFAULT NULL COMMENT '模板名称',
  `title` varchar(100) DEFAULT NULL COMMENT '模板标题，用于前端展示使用',
  `company_id` int(11) DEFAULT NULL COMMENT '公司ID',
  `model_id` int(11) DEFAULT NULL COMMENT '模型ID',
  `content` varchar(1024) DEFAULT NULL COMMENT '命令内容,如果有参数，要包含占位符',
  `param` varchar(200) DEFAULT NULL COMMENT '命令参数格式为JSONARRAY',
  `ack_check` tinyint(1) DEFAULT '0' COMMENT '是否ack确认',
  `enable_check` tinyint(1) DEFAULT '0' COMMENT '是否启动禁用',
  `category` varchar(20) DEFAULT NULL COMMENT '类目',
  `on_version` int(11) DEFAULT NULL COMMENT '可用版本',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `active` tinyint(1) DEFAULT '1' COMMENT '是否禁用(1:启用状态 0:禁用状态)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='指令模板表';

CREATE TABLE `t_command_record` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `client_id` varchar(64) DEFAULT NULL COMMENT '设备ID',
    `template_id` int(11) DEFAULT NULL COMMENT '模板id',
    `alias` varchar(64) DEFAULT NULL COMMENT '别名',
    `content` varchar(255) DEFAULT NULL COMMENT 'content不包括占位符,不供用户查看',
    `param` varchar(255) DEFAULT NULL COMMENT '命令参数格式为key:value:type多个用分号分割',
    `immediately` tinyint(1) DEFAULT '1' COMMENT '是否立即执行1立即2延时',
    `plan_execute_time` datetime DEFAULT NULL COMMENT '计划执行时间',
    `timeout` int(11) DEFAULT NULL COMMENT '超时时间(单位秒)',
    `description` varchar(255) DEFAULT NULL COMMENT '描述',
    `status` tinyint(1) DEFAULT '0' COMMENT '命令状态(0:ready 1:processing 2:completed)',
    `execute_end_time` datetime DEFAULT NULL COMMENT '执行结束时间',
    `creator` varchar(50) DEFAULT NULL COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `updater` varchar(50) DEFAULT NULL COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='指令执行记录表';

-- 2023-6-27
ALTER TABLE `beacon_info` ADD company_code VARCHAR(100) NULL COMMENT '厂商编码' AFTER product_name;
ALTER TABLE `beacon_info` ADD pos_z FLOAT(10) NULL COMMENT 'z' AFTER pos_y;
ALTER TABLE `aoa_data_info` ADD gateway_id VARCHAR(64) NULL COMMENT '网关设备ID' AFTER device_id;
ALTER TABLE `aoa_data_info` ADD pos_z FLOAT(10) NULL COMMENT 'z' AFTER pos_y;
ALTER TABLE `aoa_data_info` ADD create_time datetime NULL COMMENT '创建时间' AFTER timestamp;

-- 2023-6-28
ALTER TABLE `gateway_info` ADD zone_id VARCHAR(30) NULL COMMENT 'zone' AFTER map_id;
ALTER TABLE `gateway_info` ADD company_code VARCHAR(100) NULL COMMENT '厂商编码' AFTER group_id;
-- 2023-6-29
ALTER TABLE `beacon_info` ADD zone_id VARCHAR(30) NULL COMMENT 'zone' AFTER map_id;
ALTER TABLE `beacon_info` ADD company_code VARCHAR(100) NULL COMMENT '厂商编码' AFTER mac;
ALTER TABLE `map_info` ADD company_code VARCHAR(100) NULL COMMENT '厂商编码' AFTER building_id;
ALTER TABLE `map_info` ADD coordinate_type VARCHAR(64) NULL COMMENT '厂商编码' AFTER company_code;

-- 2023-6-30
ALTER TABLE `aoa_data_info` ADD zone_id VARCHAR(30) NULL COMMENT 'zone' AFTER map_id;

-- 2023-6-30
ALTER TABLE `beacon_info` MODIFY `pos_x` DOUBLE;
ALTER TABLE `beacon_info` MODIFY `pos_y` DOUBLE;
ALTER TABLE `beacon_info` MODIFY `pos_z` DOUBLE;
ALTER TABLE `aoa_data_info` MODIFY `pos_x` DOUBLE;
ALTER TABLE `aoa_data_info` MODIFY `pos_y` DOUBLE;
ALTER TABLE `aoa_data_info` MODIFY `pos_z` DOUBLE;
ALTER TABLE `gateway_info` MODIFY `set_x` DOUBLE;
ALTER TABLE `gateway_info` MODIFY `set_y` DOUBLE;
ALTER TABLE `gateway_info` MODIFY `set_z` DOUBLE;
ALTER TABLE `gateway_info` MODIFY `angle` DOUBLE;
ALTER TABLE `gateway_info` MODIFY `his_x` DOUBLE;
ALTER TABLE `gateway_info` MODIFY `his_y` DOUBLE;
ALTER TABLE `gateway_info` MODIFY `his_z` DOUBLE;