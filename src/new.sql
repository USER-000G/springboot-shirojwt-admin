DROP DATABASE IF EXISTS mydb;

create database mydb;

use mydb;

create table user (
  id BIGINT primary key auto_increment COMMENT "ID",
  username varchar(20) not null unique COMMENT "帐号",
  password varchar(80) not null COMMENT "密码",
  nickname varchar(20) not null COMMENT "昵称",
  reg_time datetime not null COMMENT "注册时间"
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT "用户表";

CREATE TABLE role (
  id BIGINT primary key auto_increment COMMENT "ID",
  name varchar(128) not null unique COMMENT "角色名称"
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT "角色表";


CREATE TABLE permission (
  id BIGINT primary key auto_increment COMMENT "ID",
  name varchar(128) COMMENT '资源名称',
  per_code varchar(128) not null unique COMMENT '权限代码字符串'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT "资源表";

CREATE TABLE user_role (
  id BIGINT primary key auto_increment COMMENT "ID",
  user_id BIGINT not null COMMENT '用户id',
  role_id BIGINT not null COMMENT '角色id',
  foreign key (user_id) references user (id),
  foreign key (role_id) references role (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT "用户角色表";

CREATE TABLE role_permission (
  id BIGINT primary key auto_increment COMMENT "ID",
  role_id BIGINT not null COMMENT '角色id',
  permission_id BIGINT not null COMMENT '权限id',
  foreign key (role_id) references role (id),
  foreign key (permission_id) references permission (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT "角色资源表";

insert into user values(null, "admin", "Q0ZFRkIzQ0ZFQzMzRkU0NjFERkNGMERGMUJDRDc4NDk=", "admin", now());
insert into user values(null, "高丹", "QjM0QzEyNTVEMDNCMjQwRjgzMzFCMEE2N0JBMUE0MEY=", "gd", now());
insert into user values(null, "guest", "123222", "guest", now());

insert into role values(null, "admin");
insert into role values(null, "customer");

insert into user_role values(null, 1, 1);
insert into user_role values(null, 2, 2);

insert into permission values(null, "查看用户", "user:view");
insert into permission values(null, "操作用户", "user:edit");

insert into role_permission values(null, 1, 1);
insert into role_permission values(null, 1, 2);
insert into role_permission values(null, 2, 1);

