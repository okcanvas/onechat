/*
 Source Server         : local-mysql
 Source Server Type    : MySQL
 Source Server Version : 
 Source Host           : localhost:3306
 Source Schema         : bizbeeChat
*/
create
database `bizbeeChat` default character set utf8mb4 collate utf8mb4_general_ci;
USE
bizbeeChat;

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for chat_session
-- ----------------------------
DROP TABLE IF EXISTS `chat_session`;
CREATE TABLE `chat_session`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `own_id`      bigint                                                        DEFAULT NULL COMMENT '사용자 id',
    `target_id`   bigint                                                        DEFAULT NULL COMMENT '상대 id',
    `chat_type`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '채팅형식',
    `update_time` bigint                                                        DEFAULT NULL COMMENT '수정시간',
    `top_flag`    int                                                           DEFAULT NULL COMMENT '',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of chat_session
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for im_friend
-- ----------------------------
DROP TABLE IF EXISTS `im_friend`;
CREATE TABLE `im_friend`
(
    `id`                bigint       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id`           bigint       NOT NULL COMMENT '사용자d',
    `friend_id`         bigint       NOT NULL COMMENT '친구id',
    `friend_nick_name`  varchar(255) NOT NULL COMMENT '친구닉네임',
    `friend_head_image` varchar(255) DEFAULT '' COMMENT '친구이미지',
    `created_time`      datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    PRIMARY KEY (`id`),
    KEY                 `idx_user_id` (`user_id`),
    KEY                 `idx_friend_id` (`friend_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='친구';

-- ----------------------------
-- Records of im_friend
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for im_group
-- ----------------------------
DROP TABLE IF EXISTS `im_group`;
CREATE TABLE `im_group`
(
    `id`               bigint       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name`             varchar(255) NOT NULL COMMENT '그룸명',
    `owner_id`         bigint       NOT NULL COMMENT '생성자id',
    `head_image`       varchar(255)  DEFAULT '' COMMENT '이미지',
    `head_image_thumb` varchar(255)  DEFAULT '' COMMENT '섬네일',
    `notice`           varchar(1024) DEFAULT '' COMMENT '공지문',
    `remark`           varchar(255)  DEFAULT '' COMMENT '비고',
    `deleted`          tinyint(1) DEFAULT '0' COMMENT '',
    `created_time`     datetime      DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    `group_type`       int           DEFAULT '0' COMMENT '유형:0정상,1익명',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3 COMMENT='그룹';

-- ----------------------------
-- Records of im_group
-- ----------------------------
BEGIN;
INSERT INTO `im_group` (`id`, `name`, `owner_id`, `head_image`, `head_image_thumb`, `notice`,
                        `remark`, `deleted`, `created_time`, `group_type`)
VALUES (5, '대화방', 1, 'http://127.30.0.1:9000/bizbeeChat/image/20230702/1688284083704.png',
        'http://127.30.0.1:9000/bizbeeChat/image/20230702/1688284083704.png',
        '공지사항',
        '특이사항\n',
        0, '2024-08-22 11:43:40', 1);
COMMIT;

-- ----------------------------
-- Table structure for im_group_member
-- ----------------------------
DROP TABLE IF EXISTS `im_group_member`;
CREATE TABLE `im_group_member`
(
    `id`           bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
    `group_id`     bigint NOT NULL COMMENT '그룹id',
    `user_id`      bigint NOT NULL COMMENT '사용자id',
    `alias_name`   varchar(255) DEFAULT '' COMMENT '그룹내닉내임',
    `head_image`   varchar(255) DEFAULT '' COMMENT '그룹내이미지',
    `remark`       varchar(255) DEFAULT '' COMMENT '비고',
    `quit`         tinyint(1) DEFAULT '0' COMMENT '종료(퇴장/퇴출...)',
    `created_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    PRIMARY KEY (`id`),
    KEY            `idx_group_id` (`group_id`),
    KEY            `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COMMENT='멤버';

-- ----------------------------
-- Records of im_group_member
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for im_group_message
-- ----------------------------
DROP TABLE IF EXISTS `im_group_message`;
CREATE TABLE `im_group_message`
(
    `id`        bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
    `group_id`  bigint NOT NULL COMMENT '그룹id',
    `send_id`   bigint NOT NULL COMMENT '전송자id',
    `content`   text COMMENT '내용',
    `type`      tinyint(1) NOT NULL COMMENT '메세지종류 0:문자 1:이미지 2:파일 3:음성 10:알림',
    `status`    tinyint(1) DEFAULT '0' COMMENT '상태 0:정상  2:취소',
    `send_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '전송시간',
    PRIMARY KEY (`id`),
    KEY         `idx_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='그룹메세지';

-- ----------------------------
-- Records of im_group_message
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for im_private_message
-- ----------------------------
DROP TABLE IF EXISTS `im_private_message`;
CREATE TABLE `im_private_message`
(
    `id`        bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
    `send_id`   bigint NOT NULL COMMENT '전송자id',
    `recv_id`   bigint NOT NULL COMMENT '수신자id',
    `content`   text COMMENT '내용',
    `type`      tinyint(1) NOT NULL COMMENT '메세지종류 0:문자 1:이미지 2:파일 3:음성 10:알림',
    `status`    tinyint(1) NOT NULL COMMENT '상태 0:읽지않음 1:읽음 2:취소',
    `send_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '전송시간',
    PRIMARY KEY (`id`),
    KEY         `idx_send_recv_id` (`send_id`,`recv_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='개별메세지';

-- ----------------------------
-- Records of im_private_message
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for im_user
-- ----------------------------
DROP TABLE IF EXISTS `im_user`;
CREATE TABLE `im_user`
(
    `id`               bigint       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_name`        varchar(255) NOT NULL COMMENT '이름',
    `nick_name`        varchar(255) NOT NULL COMMENT '닉네임',
    `head_image`       varchar(255)  DEFAULT '' COMMENT '이미지',
    `head_image_thumb` varchar(255)  DEFAULT '' COMMENT '섬내일',
    `password`         varchar(255) NOT NULL COMMENT '비밀번호',
    `sex`              tinyint(1) DEFAULT '0' COMMENT '성별 0:남 1::여',
    `signature`        varchar(1024) DEFAULT '' COMMENT '전자서명',
    `last_login_time`  datetime      DEFAULT NULL COMMENT '최근로그인시간',
    `created_time`     datetime      DEFAULT CURRENT_TIMESTAMP COMMENT '등록시간',
    `register_from`    int           DEFAULT '0' COMMENT '가입정보:0기본,1gitee,2github....',
    `oauth_src`        text COMMENT 'oauth인증결과',
    `account_type`     int           DEFAULT '0' COMMENT '개정구분:0정상,1익명',
    `anonymou_id`      varchar(255)  DEFAULT NULL COMMENT '익명id',
    `last_login_ip`    varchar(30)   DEFAULT NULL COMMENT '최근로그인IP',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_user_name` (`user_name`),
    KEY                `idx_nick_name` (`nick_name`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8mb3 COMMENT='사용자';

-- ----------------------------
-- Records of im_user
-- ----------------------------
BEGIN;
INSERT INTO `im_user` (`id`, `user_name`, `nick_name`, `head_image`, `head_image_thumb`, `password`,
                       `sex`, `signature`, `last_login_time`, `created_time`, `register_from`,
                       `oauth_src`, `account_type`, `anonymou_id`, `last_login_ip`)
VALUES (1, 'admin', 'admin', '', '', '$2a$10$MEHf4SDJi6.Wfxj0KDQ4EeUc47YIymx7vMzWKIyz6YR7hKiE1Rnl2',
        0, '', NULL, '2023-06-12 13:12:43', 0, NULL, 0, NULL, '127.0.0.1');
COMMIT;

SET
FOREIGN_KEY_CHECKS = 1;
