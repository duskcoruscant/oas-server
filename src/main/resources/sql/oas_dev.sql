/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50734
 Source Host           : localhost:3306
 Source Schema         : oas_dev

 Target Server Type    : MySQL
 Target Server Version : 50734
 File Encoding         : 65001

 Date: 28/04/2022 20:53:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '员工id',
  `email` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '邮箱',
  `nickname` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '账户名',
  `password` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '密码',
  `register_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `login_time` datetime NULL DEFAULT NULL COMMENT '上一次登录时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ix_account_name`(`nickname`) USING BTREE,
  UNIQUE INDEX `ix_account_email`(`email`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES (1, 'admin@qq.com', 'admin', '$2a$10$wg0f10n.30UbU.9hPpucbef/ya62LdTKs72xJfjxvTFsL0Xaewbra', '2022-02-20 00:00:00', '2022-04-28 20:28:15');
INSERT INTO `account` VALUES (26, 'zhangsanyaya@qq.com', '法外狂徒', '$2a$10$xo6v5t17YtrFifS3mVHeUe9TsiJ8rqbBREv5kMWDIDtAqZcN/j.gW', '2022-03-29 18:40:09', '2022-04-18 20:41:51');
INSERT INTO `account` VALUES (27, 'lisi@163.com', 'lisi', '$2a$10$Dr0vZE31DzhE0ufRYxcFPuvYintQA5yuZM651CZKbfI6BI0AMGA.m', '2022-04-11 12:06:58', '2022-04-27 21:20:33');
INSERT INTO `account` VALUES (28, 'wangwu@163.com', 'wangwu', '$2a$10$KD0k3H3Vom.bVZ0DUSNi9.pHOSf1OiUNvsGqM7diykSoFRC9nZEEi', '2022-04-11 12:08:28', '2022-04-24 16:02:37');
INSERT INTO `account` VALUES (29, 'annajin@qq.com', 'annajin', '$2a$10$ULmknWocWGdQy96yzLeo2O8MbswKlcAVomyK6B2UscSv721yBvfZm', '2022-04-11 13:27:02', '2022-04-27 21:59:04');

-- ----------------------------
-- Table structure for account_role
-- ----------------------------
DROP TABLE IF EXISTS `account_role`;
CREATE TABLE `account_role`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增编号',
  `account_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户Id',
  `role_id` bigint(20) UNSIGNED NOT NULL COMMENT '角色Id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of account_role
-- ----------------------------
INSERT INTO `account_role` VALUES (1, 1, 1);
INSERT INTO `account_role` VALUES (6, 26, 3);
INSERT INTO `account_role` VALUES (7, 27, 2);
INSERT INTO `account_role` VALUES (8, 28, 2);
INSERT INTO `account_role` VALUES (9, 29, 2);
INSERT INTO `account_role` VALUES (10, 29, 3);
INSERT INTO `account_role` VALUES (13, 28, 1);
INSERT INTO `account_role` VALUES (14, 27, 1);

-- ----------------------------
-- Table structure for attendance
-- ----------------------------
DROP TABLE IF EXISTS `attendance`;
CREATE TABLE `attendance`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `emp_id` bigint(20) NOT NULL COMMENT '员工id',
  `clock_date` date NOT NULL COMMENT '打卡日期',
  `clock_in_time` time NULL DEFAULT NULL COMMENT '签到时间',
  `clock_out_time` time NULL DEFAULT NULL COMMENT '签退时间',
  `come_late_minutes` smallint(6) NULL DEFAULT NULL COMMENT '迟到分钟数',
  `leave_early_minutes` smallint(6) NULL DEFAULT NULL COMMENT '早退分钟数',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '考勤' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of attendance
-- ----------------------------
INSERT INTO `attendance` VALUES (3, 1, '2022-04-01', '08:54:10', '17:36:36', NULL, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (4, 1, '2022-04-02', '11:14:10', '11:36:36', 134, 323, NULL, NULL);
INSERT INTO `attendance` VALUES (9, 1, '2022-04-03', '14:35:28', '17:00:30', 335, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (10, 1, '2022-04-04', '11:57:56', '17:09:09', 177, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (11, 1, '2022-04-05', '14:16:23', '19:35:36', 316, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (12, 1, '2022-04-06', '13:24:45', '20:11:17', 264, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (13, 26, '2022-04-06', '16:52:18', NULL, 472, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (14, 1, '2022-04-07', '17:29:01', '22:41:32', 509, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (15, 1, '2022-04-08', '16:28:56', '22:04:05', 448, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (16, 1, '2022-04-09', '09:43:25', '19:34:05', 43, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (17, 1, '2022-04-10', '14:55:45', '19:24:45', 355, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (18, 1, '2022-04-11', '11:45:51', '23:16:23', 165, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (19, 29, '2022-04-11', '21:03:08', '21:03:10', 723, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (20, 29, '2022-04-12', '14:06:39', NULL, 306, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (21, 1, '2022-04-12', '08:58:36', '21:23:45', NULL, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (22, 1, '2022-04-13', '08:54:42', '22:15:33', NULL, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (23, 1, '2022-04-14', '08:49:13', '18:00:08', NULL, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (24, 1, '2022-04-16', '00:45:55', '19:16:26', NULL, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (25, 1, '2022-04-17', '13:19:18', NULL, 259, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (26, 1, '2022-04-18', '16:56:20', NULL, 476, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (27, 29, '2022-04-18', '20:32:05', NULL, 692, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (28, 1, '2022-04-19', '08:12:41', '16:57:37', NULL, 2, NULL, NULL);
INSERT INTO `attendance` VALUES (29, 1, '2022-04-21', '15:48:53', '22:01:24', 408, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (30, 1, '2022-04-23', '13:39:57', '22:45:27', 279, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (31, 1, '2022-04-24', '08:16:14', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (32, 1, '2022-04-26', '11:49:49', NULL, 169, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (33, 1, '2022-04-27', '19:45:40', NULL, 645, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (34, 29, '2022-04-27', '20:35:28', '20:35:34', 695, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (35, 27, '2022-04-27', '21:35:36', NULL, 755, NULL, NULL, NULL);
INSERT INTO `attendance` VALUES (36, 1, '2022-04-28', '08:02:31', '20:46:25', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for conference_equipment
-- ----------------------------
DROP TABLE IF EXISTS `conference_equipment`;
CREATE TABLE `conference_equipment`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会议室设备编号',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会议室设备类型',
  `status` tinyint(4) NOT NULL COMMENT '会议室设备使用状态（0空闲，1使用中，2损坏）',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会议室设备表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of conference_equipment
-- ----------------------------
INSERT INTO `conference_equipment` VALUES (1, 'KT0001', '空调', 1, 'admin', '2022-04-14 15:47:50', 'admin', '2022-04-14 23:06:25', 0);
INSERT INTO `conference_equipment` VALUES (2, 'KT0002', '空调', 1, 'admin', '2022-04-14 23:06:45', 'admin', '2022-04-14 23:06:45', 0);
INSERT INTO `conference_equipment` VALUES (3, 'KT0003', '空调', 1, 'admin', '2022-04-14 23:07:00', 'admin', '2022-04-14 23:07:00', 0);
INSERT INTO `conference_equipment` VALUES (4, 'TYY0001', '投影仪', 1, 'admin', '2022-04-14 23:07:49', 'admin', '2022-04-14 23:07:49', 0);
INSERT INTO `conference_equipment` VALUES (5, 'TYY0002', '投影仪', 1, 'admin', '2022-04-14 23:07:56', 'admin', '2022-04-14 23:07:56', 0);
INSERT INTO `conference_equipment` VALUES (6, 'MKF0001', '麦克风', 1, 'admin', '2022-04-14 23:08:17', 'admin', '2022-04-14 23:08:17', 0);
INSERT INTO `conference_equipment` VALUES (7, 'MKF0002', '麦克风', 0, 'admin', '2022-04-16 18:10:26', 'admin', '2022-04-16 23:51:46', 0);
INSERT INTO `conference_equipment` VALUES (8, 'MKF0003', '麦克风', 1, 'admin', '2022-04-16 19:43:55', 'admin', '2022-04-16 19:43:55', 0);
INSERT INTO `conference_equipment` VALUES (9, 'TYY0003', '投影仪', 0, 'admin', '2022-04-16 21:09:09', 'admin', '2022-04-16 21:09:09', 0);

-- ----------------------------
-- Table structure for conference_reservation
-- ----------------------------
DROP TABLE IF EXISTS `conference_reservation`;
CREATE TABLE `conference_reservation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `room_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会议室编号',
  `subject` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会议主题',
  `date` date NOT NULL COMMENT '会议日期',
  `start_time` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '开始时间',
  `end_time` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '结束时间',
  `res_emp_id` bigint(20) NOT NULL COMMENT '预订人id',
  `creator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会议预定表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of conference_reservation
-- ----------------------------
INSERT INTO `conference_reservation` VALUES (1, 'A1-208', '测试', '2022-04-21', '10:00', '12:00', 1, 'admin', '2022-04-20 16:47:27', 0);
INSERT INTO `conference_reservation` VALUES (2, 'A1-207', '123', '2022-04-21', '12:00', '14:00', 1, 'admin', '2022-04-21 10:25:29', 0);
INSERT INTO `conference_reservation` VALUES (3, 'A1-101', '这是两个半小时的会议', '2022-04-21', '15:00', '17:30', 29, 'admin', '2022-04-21 11:47:27', 0);
INSERT INTO `conference_reservation` VALUES (4, 'A1-101', '周六会议', '2022-04-23', '15:00', '17:30', 1, 'admin', '2022-04-23 12:47:27', 0);
INSERT INTO `conference_reservation` VALUES (5, 'A1-208', '于周六创建的会议', '2022-04-23', '18:00', '19:30', 1, 'admin', '2022-04-23 16:35:27', 0);
INSERT INTO `conference_reservation` VALUES (6, 'A1-208', '会议', '2022-04-23', '21:00', '22:30', 1, 'admin', '2022-04-23 16:43:27', 0);
INSERT INTO `conference_reservation` VALUES (7, 'A1-207', '周日21点会议', '2022-04-23', '21:00', '24:00', 1, 'admin', '2022-04-23 16:47:27', 0);
INSERT INTO `conference_reservation` VALUES (8, 'A1-101', '七点半到九点半', '2022-04-23', '19:30', '21:30', 26, 'admin', '2022-04-23 20:19:34', 0);
INSERT INTO `conference_reservation` VALUES (9, 'A1-207', '王五周日会议', '2022-04-24', '15:00', '15:30', 28, 'admin', '2022-04-23 21:42:42', 0);

-- ----------------------------
-- Table structure for conference_room
-- ----------------------------
DROP TABLE IF EXISTS `conference_room`;
CREATE TABLE `conference_room`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会议室编号',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会议室地址',
  `status` tinyint(4) NOT NULL COMMENT '状态（0正常，1停用）',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会议室表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of conference_room
-- ----------------------------
INSERT INTO `conference_room` VALUES (1, 'A1-208', '北区谦园A1栋208室', 0, 'admin', '2022-04-14 16:47:03', 'admin', '2022-04-24 00:02:33', 0);
INSERT INTO `conference_room` VALUES (2, 'A1-101', '北区谦园A1栋101室', 0, 'admin', '2022-04-14 23:29:26', 'admin', '2022-04-24 14:41:38', 0);
INSERT INTO `conference_room` VALUES (3, 'A1-207', '北区谦园A1栋207室', 0, 'admin', '2022-04-16 00:35:47', 'admin', '2022-04-24 14:36:52', 0);

-- ----------------------------
-- Table structure for conference_room_equipment
-- ----------------------------
DROP TABLE IF EXISTS `conference_room_equipment`;
CREATE TABLE `conference_room_equipment`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `conference_room_id` bigint(20) NOT NULL COMMENT '会议室id',
  `conference_equipment_id` bigint(20) NOT NULL COMMENT '会议室设备id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会议室-设备关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of conference_room_equipment
-- ----------------------------
INSERT INTO `conference_room_equipment` VALUES (1, 2, 1);
INSERT INTO `conference_room_equipment` VALUES (2, 2, 4);
INSERT INTO `conference_room_equipment` VALUES (3, 2, 6);
INSERT INTO `conference_room_equipment` VALUES (4, 1, 2);
INSERT INTO `conference_room_equipment` VALUES (5, 1, 5);
INSERT INTO `conference_room_equipment` VALUES (6, 1, 3);
INSERT INTO `conference_room_equipment` VALUES (7, 3, 8);

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '部门名称',
  `parent_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '父部门id',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '显示顺序',
  `leader_emp_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` tinyint(4) NOT NULL COMMENT '部门状态（0正常 1停用）',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 117 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES (100, 'hwenbin', 0, 0, 1, '15888888888', 'ry@qq.com', 0, 'admin', '2021-01-05 17:03:47', '103', '2022-03-25 20:06:57', b'0');
INSERT INTO `department` VALUES (101, '深圳总公司', 100, 2, 104, '15888888888', 'ry@qq.com', 0, 'admin', '2021-01-05 17:03:47', 'admin', '2022-04-11 11:58:53', b'0');
INSERT INTO `department` VALUES (102, '长沙分公司', 100, 3, 0, '15888888888', 'ry@qq.com', 0, 'admin', '2021-01-05 17:03:47', 'admin', '2022-04-11 11:59:00', b'0');
INSERT INTO `department` VALUES (103, '研发部门', 101, 1, 104, '15888888888', 'ry@qq.com', 0, 'admin', '2021-01-05 17:03:47', '103', '2022-01-14 01:04:14', b'0');
INSERT INTO `department` VALUES (104, '市场部门', 101, 2, NULL, '15888888888', 'ry@qq.com', 0, 'admin', '2021-01-05 17:03:47', '', '2021-12-15 05:01:38', b'0');
INSERT INTO `department` VALUES (105, '测试部门', 101, 3, NULL, '15888888888', 'ry@qq.com', 0, 'admin', '2021-01-05 17:03:47', '', '2021-12-15 05:01:37', b'0');
INSERT INTO `department` VALUES (106, '财务部门', 101, 4, 103, '15888888888', 'ry@qq.com', 0, 'admin', '2021-01-05 17:03:47', '103', '2022-01-15 21:32:22', b'0');
INSERT INTO `department` VALUES (107, '运维部门', 101, 5, NULL, '15888888888', 'ry@qq.com', 0, 'admin', '2021-01-05 17:03:47', '', '2021-12-15 05:01:33', b'0');
INSERT INTO `department` VALUES (108, '市场部门', 102, 1, NULL, '15888888888', 'ry@qq.com', 0, 'admin', '2021-01-05 17:03:47', '1', '2022-02-16 08:35:45', b'0');
INSERT INTO `department` VALUES (109, '财务部门', 102, 2, NULL, '15888888888', 'ry@qq.com', 0, 'admin', '2021-01-05 17:03:47', '', '2021-12-15 05:01:29', b'0');
INSERT INTO `department` VALUES (112, 'hwb测试', 100, 4, 0, '13850469677', NULL, 0, '1', '2022-03-25 16:07:53', 'admin', '2022-04-11 11:59:09', b'0');
INSERT INTO `department` VALUES (113, '摸鱼部', 112, 0, 12, '13159290527', 'moyubu@163.com', 1, 'admin', '2022-03-25 23:16:31', '', '2022-03-25 23:16:31', b'0');
INSERT INTO `department` VALUES (114, '养老部', 112, 1, 1, NULL, NULL, 0, 'admin', '2022-03-25 23:22:23', 'admin', '2022-04-17 13:59:02', b'0');
INSERT INTO `department` VALUES (115, '测试部', 112, 2, 8, NULL, NULL, 0, 'admin', '2022-03-27 13:57:24', 'admin', '2022-03-27 14:34:17', b'1');
INSERT INTO `department` VALUES (116, '董事会', 100, 1, 1, '17850396869', 'hwenbin@163.com', 0, 'admin', '2022-04-11 11:59:45', 'admin', '2022-04-11 11:59:45', b'0');

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '员工id',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '员工姓名',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账户昵称',
  `sex` tinyint(4) NULL DEFAULT 0 COMMENT '员工性别 1女，2男',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '员工电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '员工邮箱',
  `birthday` date NULL DEFAULT NULL COMMENT '员工生日',
  `position_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '职位编号数组',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门id',
  `entry_date` date NULL DEFAULT NULL COMMENT '入职日期',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` tinyint(4) NULL DEFAULT 0 COMMENT '帐号状态 0正常，1停用',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除 0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ix_employee_name`(`name`) USING BTREE,
  UNIQUE INDEX `ix_employee_email`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '员工信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES (1, 'admin', 'admin', NULL, '', 'admin@qq.com', '2022-02-20', '[1]', 116, NULL, NULL, 0, NULL, '2022-03-22 17:31:43', 'admin', '2022-04-11 12:01:25', 0);
INSERT INTO `employee` VALUES (26, '张三', '法外狂徒', 1, NULL, 'zhangsanyaya@qq.com', NULL, '[4]', 114, NULL, '法外狂徒张三', 0, 'admin', '2022-03-29 18:40:09', 'admin', '2022-04-11 12:01:52', 0);
INSERT INTO `employee` VALUES (27, '李四', 'lisi', 1, '13312345678', 'lisi@163.com', '2000-02-02', '[6]', 116, '2022-02-20', '大股东', 0, 'admin', '2022-04-11 12:06:58', 'admin', '2022-04-15 18:23:40', 0);
INSERT INTO `employee` VALUES (28, '王五', 'wangwu', 2, '17812345678', 'wangwu@163.com', '1998-03-12', '[6]', 116, '2022-02-25', '小股东', 0, 'admin', '2022-04-11 12:08:28', 'admin', '2022-04-11 12:08:28', 0);
INSERT INTO `employee` VALUES (29, '安纳金', 'annajin', 2, '15912344321', 'annajin@qq.com', '1987-08-03', '[6]', 116, '2022-03-01', NULL, 0, 'admin', '2022-04-11 13:27:03', 'admin', '2022-04-11 13:27:03', 0);

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路径',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型',
  `size` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '大小',
  `emp_id` bigint(20) NOT NULL COMMENT '员工id',
  `parent_id` bigint(20) NOT NULL COMMENT '父级id 0为根目录',
  `is_shared` tinyint(1) NOT NULL COMMENT '是否公共',
  `content_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'content-type',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` bit(1) NULL DEFAULT b'0' COMMENT '逻辑删除',
  `deleted_batch_num` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除批次号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 79 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of file
-- ----------------------------
INSERT INTO `file` VALUES (62, '这是个人文件夹', NULL, '文件夹', '-', 1, 0, 0, NULL, 'admin', '2022-04-09 00:17:07', 'admin', '2022-04-09 00:17:07', b'1', 'da0b31fa-89ea-46e8-a70e-c26651dd7cf1');
INSERT INTO `file` VALUES (63, '文件夹二号', NULL, '文件夹', '-', 1, 0, 0, NULL, 'admin', '2022-04-09 00:17:30', 'admin', '2022-04-09 00:17:42', b'1', '74a14e72-b209-480d-83a2-fc49640715b0');
INSERT INTO `file` VALUES (64, '1-1', NULL, '文件夹', '-', 1, 62, 0, NULL, 'admin', '2022-04-09 00:17:56', 'admin', '2022-04-09 00:17:56', b'1', 'da0b31fa-89ea-46e8-a70e-c26651dd7cf1');
INSERT INTO `file` VALUES (65, '空文本文件.txt', '1/2022/04/09/1082743空文本文件.txt', 'txt', '0B', 1, 64, 0, 'text/plain', 'admin', '2022-04-09 00:18:03', 'admin', '2022-04-09 00:18:03', b'1', 'da0b31fa-89ea-46e8-a70e-c26651dd7cf1');
INSERT INTO `file` VALUES (66, '空文本文件.txt', '1/2022/04/09/1170486空文本文件.txt', 'txt', '0B', 1, 62, 0, 'text/plain', 'admin', '2022-04-09 00:19:30', 'admin', '2022-04-09 00:19:30', b'1', 'da0b31fa-89ea-46e8-a70e-c26651dd7cf1');
INSERT INTO `file` VALUES (67, '这是共享文件夹', NULL, '文件夹', '-', 1, 0, 1, NULL, 'admin', '2022-04-09 00:20:44', 'admin', '2022-04-09 00:20:44', b'1', '9f2624bb-5c54-4672-9399-e4e197def798');
INSERT INTO `file` VALUES (68, '共享文件夹二号', NULL, '文件夹', '-', 1, 0, 1, NULL, 'admin', '2022-04-09 00:20:53', 'admin', '2022-04-09 13:59:51', b'0', NULL);
INSERT INTO `file` VALUES (69, '空文本文件.txt', '1/2022/04/09/1260248空文本文件.txt', 'txt', '0B', 1, 67, 1, 'text/plain', 'admin', '2022-04-09 00:21:00', 'admin', '2022-04-09 00:21:00', b'1', '9f2624bb-5c54-4672-9399-e4e197def798');
INSERT INTO `file` VALUES (70, '空文本文件.txt', '1/2022/04/09/47685525空文本文件.txt', 'txt', '0B', 1, 63, 0, 'text/plain', 'admin', '2022-04-09 13:14:46', 'admin', '2022-04-09 13:14:46', b'1', '74a14e72-b209-480d-83a2-fc49640715b0');
INSERT INTO `file` VALUES (71, 'share测试', NULL, '文件夹', '-', 1, 67, 1, NULL, 'admin', '2022-04-09 13:46:02', 'admin', '2022-04-09 13:46:02', b'1', '9f2624bb-5c54-4672-9399-e4e197def798');
INSERT INTO `file` VALUES (72, '空文本文件.txt', '1/2022/04/09/49569505空文本文件.txt', 'txt', '0B', 1, 71, 1, 'text/plain', 'admin', '2022-04-09 13:46:10', 'admin', '2022-04-09 13:46:10', b'1', '9f2624bb-5c54-4672-9399-e4e197def798');
INSERT INTO `file` VALUES (76, '个人测试文件夹', NULL, '文件夹', '-', 1, 0, 0, NULL, 'admin', '2022-04-09 15:40:40', 'admin', '2022-04-09 15:40:40', b'0', NULL);
INSERT INTO `file` VALUES (77, '测试文件夹', NULL, '文件夹', '-', 1, 76, 0, NULL, 'admin', '2022-04-09 15:40:48', 'admin', '2022-04-09 15:40:48', b'0', NULL);
INSERT INTO `file` VALUES (78, '测试文件夹下的word文件.docx', '1/2022/04/09/56453155测试文件夹下的word文件.docx', 'docx', '0B', 1, 77, 0, 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 'admin', '2022-04-09 15:40:53', 'admin', '2022-04-09 15:40:53', b'0', NULL);

-- ----------------------------
-- Table structure for missive
-- ----------------------------
DROP TABLE IF EXISTS `missive`;
CREATE TABLE `missive`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '流水号（自增id）',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公文名称',
  `type` tinyint(4) NOT NULL COMMENT '公文类型（1通告、2指示、3议案、4决议、5命令）',
  `secret_level` tinyint(4) NOT NULL COMMENT '机密程度（1公开，2部门）',
  `primary_send_dept_id` bigint(20) NULL DEFAULT NULL COMMENT '主送部门id',
  `copy_send_dept_id` bigint(20) NULL DEFAULT NULL COMMENT '抄送部门id',
  `author_id` bigint(20) NOT NULL COMMENT '拟稿人id',
  `author_dept_id` bigint(20) NOT NULL COMMENT '拟稿部门id',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '正文',
  `attachment_id` bigint(20) NULL DEFAULT NULL COMMENT '附件id',
  `creator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `updater` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10002 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公文表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of missive
-- ----------------------------
INSERT INTO `missive` VALUES (10001, '发布于4月18日并于4月19日进行更新的一份通告', 1, 2, 114, 116, 28, 103, '<p>啦啦啦啦<img class=\"wscnph\" src=\"data:image/jpeg;base64,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\" />哈哈哈哈</p>\n<pre class=\"language-java\"><code>import org.junit.Test;\n\n/**\n * @author hwb\n * @create 2022-03-20\n */\npublic class Test1 {\n\n    @Test\n    public void test() {\n        System.out.println(uniquePaths(3, 3));\n    }\n\n    public int uniquePaths(int m, int n) {\n        int[][] dp = new int[m][n];\n        for (int i = 0; i &lt; m; i++) {\n            for (int j = 0; j &lt; n; j++) {\n                if (i == 0 || j == 0) {\n                    dp[i][j] = 1;\n                } else {\n                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];\n                }\n            }\n        }\n        return dp[m - 1][n - 1];\n    }\n\n}</code></pre>', NULL, 'admin', '2022-04-18 20:36:47', 'admin', '2022-04-19 15:48:41', 0);

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '权限id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限标识',
  `parent_id` bigint(20) NOT NULL COMMENT '父权限id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (1, '管理中心', 'manage', 0);
INSERT INTO `permission` VALUES (2, '员工管理', 'manage:employee:list', 1);
INSERT INTO `permission` VALUES (3, '查询员工', 'manage:employee:query', 2);
INSERT INTO `permission` VALUES (4, '新增员工', 'manage:employee:add', 2);
INSERT INTO `permission` VALUES (5, '修改员工', 'manage:employee:update', 2);
INSERT INTO `permission` VALUES (6, '删除员工', 'manage:employee:delete', 2);
INSERT INTO `permission` VALUES (7, '导入员工', 'manage:employee:import', 2);
INSERT INTO `permission` VALUES (8, '导出员工', 'manage:employee:export', 2);
INSERT INTO `permission` VALUES (9, '重置密码', 'manage:employee:reset-password', 2);
INSERT INTO `permission` VALUES (10, '员工角色分配', 'manage:employee:assign-role', 2);
INSERT INTO `permission` VALUES (11, '部门管理', 'manage:department:list', 1);
INSERT INTO `permission` VALUES (12, '查询部门', 'manage:department:query', 11);
INSERT INTO `permission` VALUES (13, '新增部门', 'manage:department:add', 11);
INSERT INTO `permission` VALUES (14, '修改部门', 'manage:department:update', 11);
INSERT INTO `permission` VALUES (15, '删除部门', 'manage:department:delete', 11);
INSERT INTO `permission` VALUES (16, '职位管理', 'manage:position:list', 1);
INSERT INTO `permission` VALUES (17, '查询职位', 'manage:position:query', 16);
INSERT INTO `permission` VALUES (18, '新增职位', 'manage:position:add', 16);
INSERT INTO `permission` VALUES (19, '修改职位', 'manage:position:update', 16);
INSERT INTO `permission` VALUES (20, '删除职位', 'manage:position:delete', 16);
INSERT INTO `permission` VALUES (21, '导出职位', 'manage:position:export', 16);
INSERT INTO `permission` VALUES (22, '角色管理', 'manage:role:list', 1);
INSERT INTO `permission` VALUES (23, '查询角色', 'manage:role:query', 22);
INSERT INTO `permission` VALUES (24, '新增角色', 'manage:role:add', 22);
INSERT INTO `permission` VALUES (25, '修改角色', 'manage:role:update', 22);
INSERT INTO `permission` VALUES (26, '删除角色', 'manage:role:delete', 22);
INSERT INTO `permission` VALUES (27, '导出角色', 'manage:role:export', 22);
INSERT INTO `permission` VALUES (28, '分配角色菜单权限', 'manage:role:assign-role-menu', 22);

-- ----------------------------
-- Table structure for position
-- ----------------------------
DROP TABLE IF EXISTS `position`;
CREATE TABLE `position`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '职位ID',
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '职位编码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '职位名称',
  `sort` int(11) NOT NULL COMMENT '显示顺序',
  `status` tinyint(4) NOT NULL COMMENT '状态（0正常 1停用）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '岗位信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of position
-- ----------------------------
INSERT INTO `position` VALUES (1, 'ceo', '董事长', 1, 0, '', 'admin', '2021-01-05 17:03:48', '1', '2022-02-04 17:50:40', b'0');
INSERT INTO `position` VALUES (2, 'se', '项目经理', 2, 0, '', 'admin', '2021-01-05 17:03:48', '1', '2021-12-12 10:47:47', b'0');
INSERT INTO `position` VALUES (3, 'hr', '人力资源', 3, 0, '', 'admin', '2021-01-05 17:03:48', '', '2021-12-12 10:47:50', b'0');
INSERT INTO `position` VALUES (4, 'user', '普通员工', 4, 0, '', 'admin', '2021-01-05 17:03:48', '', '2021-12-12 10:47:51', b'0');
INSERT INTO `position` VALUES (5, 'test1', '测试岗位1', 5, 1, '123', 'admin', '2021-01-07 15:07:44', 'admin', '2022-03-28 23:34:58', b'1');
INSERT INTO `position` VALUES (6, 'shareholder', '股东', 5, 1, '股票持有者', 'admin', '2022-04-06 16:28:52', 'admin', '2022-04-11 12:05:23', b'0');
INSERT INTO `position` VALUES (7, 'Java', 'Java工程师', 7, 0, NULL, 'admin', '2022-04-06 16:29:02', 'admin', '2022-04-14 16:10:49', b'0');
INSERT INTO `position` VALUES (8, '8', '8', 8, 1, NULL, 'admin', '2022-04-06 16:29:08', 'admin', '2022-04-06 16:29:08', b'0');
INSERT INTO `position` VALUES (9, '9', '9', 9, 1, NULL, 'admin', '2022-04-06 16:29:13', 'admin', '2022-04-06 16:29:13', b'0');
INSERT INTO `position` VALUES (10, '10', '10', 10, 1, NULL, 'admin', '2022-04-06 16:29:21', 'admin', '2022-04-06 16:29:21', b'0');
INSERT INTO `position` VALUES (11, '11', '11', 11, 1, NULL, 'admin', '2022-04-06 16:29:27', 'admin', '2022-04-06 16:29:27', b'0');
INSERT INTO `position` VALUES (12, '12', '12', 12, 1, NULL, 'admin', '2022-04-06 16:29:33', 'admin', '2022-04-06 16:29:33', b'0');

-- ----------------------------
-- Table structure for recycle_file
-- ----------------------------
DROP TABLE IF EXISTS `recycle_file`;
CREATE TABLE `recycle_file`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `emp_id` bigint(20) NOT NULL COMMENT '员工id',
  `file_id` bigint(20) NOT NULL COMMENT '文件id',
  `deleted_time` datetime NOT NULL COMMENT '删除时间',
  `deleted_batch_num` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '删除批次号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '回收站' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of recycle_file
-- ----------------------------

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '角色Id',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色权限字符串',
  `sort` int(11) NULL DEFAULT NULL COMMENT '显示顺序',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` tinyint(4) NULL DEFAULT 0 COMMENT '角色状态（0正常 1停用）',
  `type` tinyint(4) NULL DEFAULT 1 COMMENT '角色类型（0内置 1自定义）',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` bit(1) NULL DEFAULT b'0' COMMENT '是否删除 0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '超级管理员', 'admin', 1, NULL, 0, 0, 'admin', '2019-07-01 00:00:00', 'admin', '2022-03-30 14:45:19', b'0');
INSERT INTO `role` VALUES (2, '普通用户', 'user', 2, NULL, 0, 0, 'admin', '2019-07-01 00:00:00', 'admin', '2022-03-30 14:45:21', b'0');
INSERT INTO `role` VALUES (3, '测试', 'test', 3, NULL, 0, 1, 'admin', '2019-07-01 00:00:00', 'admin', '2022-03-31 16:12:03', b'0');
INSERT INTO `role` VALUES (4, '法外狂徒', 'invalidOrValid', 4, '张三之流', 0, 1, 'admin', '2022-03-30 14:01:57', 'admin', '2022-03-30 16:14:47', b'1');

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增编号',
  `role_id` bigint(20) UNSIGNED NOT NULL COMMENT '角色Id',
  `permission_id` bigint(20) UNSIGNED NOT NULL COMMENT '权限Id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `permission_id`(`permission_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES (1, 3, 1);
INSERT INTO `role_permission` VALUES (2, 3, 2);
INSERT INTO `role_permission` VALUES (3, 3, 16);
INSERT INTO `role_permission` VALUES (4, 3, 17);
INSERT INTO `role_permission` VALUES (5, 3, 3);
INSERT INTO `role_permission` VALUES (6, 3, 22);
INSERT INTO `role_permission` VALUES (7, 3, 23);
INSERT INTO `role_permission` VALUES (8, 3, 11);
INSERT INTO `role_permission` VALUES (9, 3, 12);

-- ----------------------------
-- Table structure for work_log
-- ----------------------------
DROP TABLE IF EXISTS `work_log`;
CREATE TABLE `work_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `type` tinyint(4) NOT NULL COMMENT '日志类型（1日报，2周报，3月报）',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志标题',
  `today_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '今日工作内容',
  `tomorrow_content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '明日工作内容',
  `question` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '遇到的问题',
  `create_emp_id` bigint(20) NOT NULL COMMENT '创建人id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of work_log
-- ----------------------------
INSERT INTO `work_log` VALUES (1, 1, '2022-04-10', '部署悟空crm', '日志中心 + 公告', '内存不足', 1, '2022-04-10 23:05:18', '2022-04-27 21:46:40');
INSERT INTO `work_log` VALUES (2, 1, '2022-04-12新增websocket用作消息通知', '1、前端引入了全局websocket接收后端通知；\n2、后端新增websocket给客户端发送消息通知；\n3、完事工作日志模块功能。', '考虑公告功能设计与实现。', '有些时候后端发送消息，前端却未收到消息。。。', 1, '2022-04-12 16:28:37', '2022-04-12 16:29:25');
INSERT INTO `work_log` VALUES (4, 1, '2022-04-12hhhhh', 'hhhhh', 'hhhhh', 'hhhhh', 1, '2022-04-12 16:42:19', '2022-04-12 16:44:11');
INSERT INTO `work_log` VALUES (5, 1, '2022-04-13会议室模块', '会议室增删改查', '会议室预定', '暂无', 1, '2022-04-13 15:58:54', NULL);

-- ----------------------------
-- Table structure for work_log_send
-- ----------------------------
DROP TABLE IF EXISTS `work_log_send`;
CREATE TABLE `work_log_send`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `log_id` bigint(20) NOT NULL COMMENT '日志id',
  `send_emp_id` bigint(20) NOT NULL COMMENT '发送人id',
  `is_read` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已读',
  `comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评论',
  `comment_time` datetime NULL DEFAULT NULL COMMENT '评论时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '工作日志-发送人 关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of work_log_send
-- ----------------------------
INSERT INTO `work_log_send` VALUES (26, 2, 29, 0, NULL, NULL);
INSERT INTO `work_log_send` VALUES (31, 4, 28, 0, NULL, NULL);
INSERT INTO `work_log_send` VALUES (32, 5, 28, 1, NULL, NULL);
INSERT INTO `work_log_send` VALUES (33, 1, 27, 1, '写的不错', '2022-04-27 21:36:05');
INSERT INTO `work_log_send` VALUES (35, 1, 29, 1, '内存不足也算事？加就完事了~', '2022-04-27 21:59:57');

SET FOREIGN_KEY_CHECKS = 1;
