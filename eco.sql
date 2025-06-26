/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.7.19 : Database - ecommerce
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`ecommerce` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `ecommerce`;

/*Table structure for table `ausertable` */

DROP TABLE IF EXISTS `ausertable`;

CREATE TABLE `ausertable` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `aname` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `apwd` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

/*Data for the table `ausertable` */

insert  into `ausertable`(`id`,`aname`,`apwd`) values (1,'admin','admin');

/*Table structure for table `busertable` */

DROP TABLE IF EXISTS `busertable`;

CREATE TABLE `busertable` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bemail` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `bpwd` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

/*Data for the table `busertable` */

insert  into `busertable`(`id`,`bemail`,`bpwd`) values (1,'chenheng@126.com','78f8a7ae700c91db09facb05a675432b'),(2,'chenhengdl@126.com','78f8a7ae700c91db09facb05a675432b'),(3,'chenh@126.com','78f8a7ae700c91db09facb05a675432b'),(4,'chen@126.com','78f8a7ae700c91db09facb05a675432b'),(6,'123456@qq.com','78f8a7ae700c91db09facb05a675432b');

/*Table structure for table `carttable` */

DROP TABLE IF EXISTS `carttable`;

CREATE TABLE `carttable` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `busertable_id` int(11) NOT NULL,
  `goodstable_id` int(11) NOT NULL,
  `shoppingnum` int(11) NOT NULL,
  `selected` int(11) DEFAULT '1' COMMENT '0-未选中, 1-已选中',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `bid` (`busertable_id`) USING BTREE,
  KEY `gno` (`goodstable_id`) USING BTREE,
  CONSTRAINT `bid` FOREIGN KEY (`busertable_id`) REFERENCES `busertable` (`id`),
  CONSTRAINT `gno` FOREIGN KEY (`goodstable_id`) REFERENCES `goodstable` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

/*Data for the table `carttable` */

/*Table structure for table `focustable` */

DROP TABLE IF EXISTS `focustable`;

CREATE TABLE `focustable` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `goodstable_id` int(11) NOT NULL,
  `busertable_id` int(11) NOT NULL,
  `focustime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `gno1` (`goodstable_id`) USING BTREE,
  KEY `bid1` (`busertable_id`) USING BTREE,
  CONSTRAINT `bid1` FOREIGN KEY (`busertable_id`) REFERENCES `busertable` (`id`),
  CONSTRAINT `gno1` FOREIGN KEY (`goodstable_id`) REFERENCES `goodstable` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

/*Data for the table `focustable` */

insert  into `focustable`(`id`,`goodstable_id`,`busertable_id`,`focustime`) values (2,33,1,'2023-04-10 23:01:11'),(3,34,1,'2023-04-25 13:04:46'),(4,35,1,'2023-04-29 13:06:35'),(5,39,3,'2025-06-05 05:44:52'),(6,42,4,'2025-06-06 08:45:13'),(7,40,6,'2025-06-24 08:40:44');

/*Table structure for table `goodstable` */

DROP TABLE IF EXISTS `goodstable`;

CREATE TABLE `goodstable` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gname` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `goprice` double DEFAULT NULL,
  `grprice` double DEFAULT NULL,
  `gstore` int(11) DEFAULT NULL,
  `gpicture` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `is_advertisement` tinyint(4) DEFAULT NULL,
  `goodstype_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `typeid` (`goodstype_id`) USING BTREE,
  CONSTRAINT `typeid` FOREIGN KEY (`goodstype_id`) REFERENCES `goodstype` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

/*Data for the table `goodstable` */

insert  into `goodstable`(`id`,`gname`,`goprice`,`grprice`,`gstore`,`gpicture`,`is_advertisement`,`goodstype_id`) values (32,'毕业花束',20000,88.88,9999986,'816671063.png',2,5),(33,'冰箱',3000,2000,29985,'223815272.png',1,6),(34,'游戏本',8000,6999,49983,'174460810.png',1,13),(35,'荔枝',10,4.99,10000,'481066792.png',1,8),(36,'黑神话悟空',40000,20000,499997,'903644285.png',1,15),(37,'手办',800,188,5995,'432318181.png',2,16),(38,'苹果',9.9,6.6,4998,'290804662.png',2,8),(39,'香蕉',6.6,3.3,8866,'921602784.png',2,8),(40,'抽油烟机',3333,2222,66663,'548266842.png',2,6),(42,'吹风机',20,18,9985,'296961076.png',2,7),(43,'洗衣机',8000,3000,9000,'369427752.png',2,6);

/*Table structure for table `goodstype` */

DROP TABLE IF EXISTS `goodstype`;

CREATE TABLE `goodstype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `typename` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

/*Data for the table `goodstype` */

insert  into `goodstype`(`id`,`typename`) values (5,'鲜花花'),(6,'家电'),(7,'电器'),(8,'水果'),(13,'电脑'),(15,'游戏'),(16,'二次元');

/*Table structure for table `orderbasetable` */

DROP TABLE IF EXISTS `orderbasetable`;

CREATE TABLE `orderbasetable` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `busertable_id` int(11) NOT NULL,
  `amount` double NOT NULL,
  `status` tinyint(4) NOT NULL,
  `orderdate` datetime NOT NULL,
  `is_deleted` int(11) DEFAULT '0' COMMENT '0-未删除, 1-已删除（用户端不可见）',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `bid2` (`busertable_id`) USING BTREE,
  CONSTRAINT `bid2` FOREIGN KEY (`busertable_id`) REFERENCES `busertable` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

/*Data for the table `orderbasetable` */

insert  into `orderbasetable`(`id`,`busertable_id`,`amount`,`status`,`orderdate`,`is_deleted`) values (6,1,317000,1,'2023-03-01 20:31:34',0),(7,1,480,1,'2023-05-05 20:41:19',0),(8,1,30000,1,'2023-07-23 20:48:48',0),(9,1,20000,1,'2023-06-09 20:49:31',0),(10,1,200000,1,'2023-02-09 14:09:02',0),(11,1,30000,1,'2023-08-19 14:09:36',0),(12,1,20000,1,'2023-11-29 14:10:23',0),(13,1,20000,1,'2023-04-29 14:11:04',0),(14,1,48000,1,'2023-04-29 14:12:47',0),(15,1,30000,1,'2023-05-20 14:23:46',0),(16,1,144000,1,'2023-01-29 15:00:42',0),(17,3,25591,1,'2025-06-05 05:05:13',0),(18,3,55550,1,'2025-06-05 05:33:31',0),(19,3,5555,1,'2025-06-05 05:35:15',0),(20,4,59550,1,'2025-06-06 08:43:47',0),(21,4,180,0,'2025-06-06 08:45:57',0),(23,6,2222,1,'2025-06-22 21:14:06',1),(24,6,11461,1,'2025-06-24 11:45:01',0),(25,6,188,0,'2025-06-24 12:41:51',1);

/*Table structure for table `orderdetail` */

DROP TABLE IF EXISTS `orderdetail`;

CREATE TABLE `orderdetail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `orderbasetable_id` int(11) NOT NULL,
  `goodstable_id` int(11) NOT NULL,
  `shoppingnum` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `odsn` (`orderbasetable_id`) USING BTREE,
  KEY `gno3` (`goodstable_id`) USING BTREE,
  CONSTRAINT `gno3` FOREIGN KEY (`goodstable_id`) REFERENCES `goodstable` (`id`),
  CONSTRAINT `odsn` FOREIGN KEY (`orderbasetable_id`) REFERENCES `orderbasetable` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

/*Data for the table `orderdetail` */

insert  into `orderdetail`(`id`,`orderbasetable_id`,`goodstable_id`,`shoppingnum`) values (8,6,32,14),(9,6,34,13),(10,6,33,13),(11,7,37,1),(12,8,34,1),(13,9,36,1),(14,10,36,1),(15,11,34,1),(16,12,33,1),(17,13,33,1),(18,14,37,1),(19,15,34,1),(20,16,37,3),(21,17,42,2),(22,17,39,1),(23,17,36,1),(24,18,39,10),(25,19,39,1),(26,20,39,10),(27,20,38,2),(28,21,42,10),(30,23,40,1),(31,24,40,2),(32,24,42,1),(33,24,34,1),(34,25,37,1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
