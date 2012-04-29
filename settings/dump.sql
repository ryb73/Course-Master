-- MySQL dump 10.13  Distrib 5.5.21, for Win64 (x86)
--
-- Host: localhost    Database: coursemasterdb
-- ------------------------------------------------------
-- Server version	5.5.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `course` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `prof` int(11) NOT NULL,
  `dept` varchar(128) NOT NULL,
  `num` int(11) NOT NULL,
  `sect` int(11) NOT NULL,
  `cred` int(11) NOT NULL,
  `sem` varchar(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `prof` (`prof`),
  CONSTRAINT `course_ibfk_1` FOREIGN KEY (`prof`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (1,'Data Structures',5,'CS',351,1,3,'Spring 2012'),(2,'Modern Programming Languages',5,'CS',431,1,3,'Spring 2012'),(3,'Intro to Databases',7,'CS',557,1,3,'Spring 2012'),(4,'System Programming',6,'CS',337,1,3,'Spring 2012'),(5,'Intro to Software Engineering',6,'CS',361,1,3,'Spring 2012'),(6,'Computer Archicture',9,'CS',458,1,3,'Spring 2012'),(7,'Intro to Operating Systems',8,'CS',537,1,3,'Spring 2012'),(8,'Computer Networks',8,'CS',520,1,3,'Spring 2012'),(9,'Computer Networks',9,'CS',520,2,3,'Spring 2012');
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `discussion_board`
--

DROP TABLE IF EXISTS `discussion_board`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `discussion_board` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `course` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `course` (`course`),
  CONSTRAINT `discussion_board_ibfk_1` FOREIGN KEY (`course`) REFERENCES `course` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discussion_board`
--

LOCK TABLES `discussion_board` WRITE;
/*!40000 ALTER TABLE `discussion_board` DISABLE KEYS */;
INSERT INTO `discussion_board` VALUES (1,'a board',1,1),(2,'hurr',1,1),(3,'ldskfhaskdf',5,1),(4,'Board',4,1),(5,'hello hello',5,1),(6,'comments',4,1),(7,'aspodifhposdf',4,1),(8,'123',4,1),(9,'qwer',4,1),(10,'wqerwer',4,1);
/*!40000 ALTER TABLE `discussion_board` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `discussion_post`
--

DROP TABLE IF EXISTS `discussion_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `discussion_post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `owner` int(11) NOT NULL,
  `parent` int(11) NOT NULL,
  `dte` datetime NOT NULL,
  `content` varchar(1024) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `owner` (`owner`),
  KEY `parent` (`parent`),
  CONSTRAINT `discussion_post_ibfk_1` FOREIGN KEY (`owner`) REFERENCES `user` (`id`),
  CONSTRAINT `discussion_post_ibfk_2` FOREIGN KEY (`parent`) REFERENCES `discussion_post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discussion_post`
--

LOCK TABLES `discussion_post` WRITE;
/*!40000 ALTER TABLE `discussion_post` DISABLE KEYS */;
/*!40000 ALTER TABLE `discussion_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `discussion_topic`
--

DROP TABLE IF EXISTS `discussion_topic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `discussion_topic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `board` int(11) NOT NULL,
  `root` int(11) NOT NULL,
  `name` varchar(256) NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `board` (`board`),
  KEY `root` (`root`),
  CONSTRAINT `discussion_topic_ibfk_1` FOREIGN KEY (`board`) REFERENCES `discussion_board` (`id`),
  CONSTRAINT `discussion_topic_ibfk_2` FOREIGN KEY (`root`) REFERENCES `discussion_post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discussion_topic`
--

LOCK TABLES `discussion_topic` WRITE;
/*!40000 ALTER TABLE `discussion_topic` DISABLE KEYS */;
/*!40000 ALTER TABLE `discussion_topic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `enrollment`
--

DROP TABLE IF EXISTS `enrollment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `enrollment` (
  `user` int(11) NOT NULL,
  `course` int(11) NOT NULL,
  `role` int(11) NOT NULL,
  KEY `user` (`user`),
  KEY `course` (`course`),
  CONSTRAINT `enrollment_ibfk_1` FOREIGN KEY (`user`) REFERENCES `user` (`id`),
  CONSTRAINT `enrollment_ibfk_2` FOREIGN KEY (`course`) REFERENCES `course` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enrollment`
--

LOCK TABLES `enrollment` WRITE;
/*!40000 ALTER TABLE `enrollment` DISABLE KEYS */;
INSERT INTO `enrollment` VALUES (1,1,1),(1,3,1),(1,5,1),(1,7,1),(1,9,1),(2,1,1),(2,2,1),(2,4,1),(2,6,1),(2,8,1),(3,8,1),(3,9,1),(3,1,1),(3,2,1),(4,3,1),(4,4,1),(4,5,1),(4,6,1),(5,1,2),(5,2,2),(6,4,2),(6,5,2),(7,3,2),(8,7,2),(8,8,2),(9,6,2),(9,9,2);
/*!40000 ALTER TABLE `enrollment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `descr` varchar(512) NOT NULL,
  `start` datetime NOT NULL,
  `end` datetime NOT NULL,
  `disp` datetime NOT NULL,
  `owner` int(11) NOT NULL,
  `course` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `owner` (`owner`),
  KEY `course` (`course`),
  CONSTRAINT `event_ibfk_1` FOREIGN KEY (`owner`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
INSERT INTO `event` VALUES (1,'A personal Event','','2012-04-10 00:00:00','2012-04-10 01:00:00','2012-04-16 15:45:59',1,0,1),(2,'A Future Event','','2012-04-17 00:00:00','2012-04-17 01:00:00','2012-04-16 15:54:04',1,0,1),(4,'Homework 3','Homework 3 due at beginning of class','2012-04-13 15:00:00','2012-04-13 15:00:00','2012-04-06 00:00:00',6,5,0),(5,'Homework 5','Homework 5 due at beginning of class','2012-04-20 15:00:00','2012-04-20 15:00:00','2012-04-13 00:00:00',6,5,0);
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `folder`
--

DROP TABLE IF EXISTS `folder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `folder` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `course` int(11) NOT NULL,
  `ext` varchar(256) DEFAULT NULL,
  `start` datetime NOT NULL,
  `end` datetime NOT NULL,
  `disp` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `course` (`course`),
  CONSTRAINT `folder_ibfk_1` FOREIGN KEY (`course`) REFERENCES `course` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `folder`
--

LOCK TABLES `folder` WRITE;
/*!40000 ALTER TABLE `folder` DISABLE KEYS */;
/*!40000 ALTER TABLE `folder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `submission`
--

DROP TABLE IF EXISTS `submission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `submission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `folder` int(11) NOT NULL,
  `path` int(11) NOT NULL,
  `name` varchar(128) NOT NULL,
  `owner` int(11) NOT NULL,
  `dte` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `folder` (`folder`),
  KEY `owner` (`owner`),
  CONSTRAINT `submission_ibfk_1` FOREIGN KEY (`folder`) REFERENCES `folder` (`id`),
  CONSTRAINT `submission_ibfk_2` FOREIGN KEY (`owner`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `submission`
--

LOCK TABLES `submission` WRITE;
/*!40000 ALTER TABLE `submission` DISABLE KEYS */;
/*!40000 ALTER TABLE `submission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fullname` varchar(32) NOT NULL,
  `email` varchar(32) NOT NULL,
  `password` varchar(256) NOT NULL,
  `role` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Graham Mueller','muell372@uwm.edu','Mþ]:ÓVí[†»{:‚Ÿ9v�C',1),(2,'Ryan Biwer','rpbiwer@uwm.edu','Žh”éèÅ�¦°‡ ðÆlm£¸=',1),(3,'Rohit Dhiman','rdhiman@uwm.edu','°ëGsyJÌ¨ã+?Ç8àÔPû-¿ú',1),(4,'Mikhail Arndt','mvarndt@uwm.edu','m™¡Ðùj*ˆ{©;hPÖvÓeïÜK',1),(5,'John Boyland','boyland@uwm.edu','ÐjáNï¶™Ú¬‚ûN#üÇ³ý_',2),(6,'Jayson Rock','rock@uwm.edu','ùOUæ—:°|u«ûuÅA#Qy1 ',2),(7,'Joseph Bockhorst','bockhorst@uwm.edu','ÔeôŸã…í\"»IåjõÒ~u<+œ',2),(8,'Mukal Goyal','goyal@uwm.edu','TpäHbG5/*i«Ì[?#O\"4',2),(9,'Hossein Hosseini','hosseini@uwm.edu','ç&¦õßtíp8ß�˜�R¯��ü',2),(10,'admin','admin','Ý”p•(»ƒÐ�0ˆÔ?GB‰O',3),(11,'Jeff Kothor','jkothor@uwm.edu','nëò�¾þ(ÃËIØ9ÁÚÝ“ºy',1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-04-25 23:53:07
