-- MySQL dump 10.13  Distrib 5.1.47, for redhat-linux-gnu (i386)
--
-- Host: localhost    Database: mycozBranch
-- ------------------------------------------------------
-- Server version	5.1.47

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
-- Table structure for table `Blog`
--

DROP TABLE IF EXISTS `Blog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Blog` (
  `ID` int(11) NOT NULL DEFAULT '0',
  `UserID` int(11) NOT NULL DEFAULT '0',
  `CategoryID` int(11) NOT NULL DEFAULT '0',
  `Title` varchar(50) DEFAULT 'NULL',
  `Description` text,
  `LastDate` date DEFAULT NULL,
  `Date` date DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Title` (`Title`),
  KEY `Blog_ibfk_1` (`UserID`),
  KEY `Blog_ibfk_2` (`CategoryID`),
  CONSTRAINT `Blog_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `User` (`ID`),
  CONSTRAINT `Blog_ibfk_2` FOREIGN KEY (`CategoryID`) REFERENCES `mycozShared`.`BlogCategory` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Download`
--

DROP TABLE IF EXISTS `Download`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Download` (
  `ID` int(11) NOT NULL DEFAULT '0',
  `TypeID` int(11) NOT NULL DEFAULT '0',
  `Name` varchar(20) DEFAULT NULL,
  `DownloadPath` varchar(200) DEFAULT NULL,
  `ImagePath` varchar(200) DEFAULT NULL,
  `Date` date DEFAULT NULL,
  `Description` text,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Name` (`Name`),
  KEY `TypeID` (`TypeID`),
  CONSTRAINT `Download_ibfk_1` FOREIGN KEY (`TypeID`) REFERENCES `mycozShared`.`DownloadType` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Example`
--

DROP TABLE IF EXISTS `Example`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Example` (
  `id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(50) DEFAULT 'NULL',
  `age` int(11) NOT NULL DEFAULT '0',
  `school` varchar(50) DEFAULT 'NULL',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `Example_ibfk_1` (`age`),
  KEY `Example_ibfk_2` (`school`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JobAccounting`
--

DROP TABLE IF EXISTS `JobAccounting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `JobAccounting` (
  `ID` int(11) NOT NULL DEFAULT '0',
  `JobNoteID` int(11) NOT NULL DEFAULT '0',
  `ItemName` varchar(100) DEFAULT NULL,
  `ItemRate` decimal(25,4) DEFAULT NULL,
  `ItemUnit` varchar(10) DEFAULT NULL,
  `ItemQuantity` double DEFAULT '1',
  `CurrencyID` int(11) NOT NULL DEFAULT '0',
  `AccountID` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ItemName` (`ItemName`,`JobNoteID`,`AccountID`),
  KEY `JobNoteID` (`JobNoteID`),
  KEY `CurrencyID` (`CurrencyID`),
  KEY `JobAccounting_ibfk_3` (`AccountID`),
  CONSTRAINT `JobAccounting_ibfk_1` FOREIGN KEY (`JobNoteID`) REFERENCES `JobNote` (`ID`),
  CONSTRAINT `JobAccounting_ibfk_2` FOREIGN KEY (`CurrencyID`) REFERENCES `mycozShared`.`Currency` (`ID`),
  CONSTRAINT `JobAccounting_ibfk_3` FOREIGN KEY (`AccountID`) REFERENCES `mycozShared`.`Account` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JobJournal`
--

DROP TABLE IF EXISTS `JobJournal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `JobJournal` (
  `ID` int(11) NOT NULL DEFAULT '0',
  `AccountID` int(11) NOT NULL DEFAULT '0',
  `JobNoteID` int(11) NOT NULL DEFAULT '0',
  `DCType` enum('Credit','Debit') DEFAULT NULL,
  `Amount` decimal(25,4) DEFAULT NULL,
  `OrgCurrencyID` int(11) NOT NULL DEFAULT '0',
  `OrgRate` double DEFAULT NULL,
  `JobAccountingID` int(11) NOT NULL DEFAULT '0',
  `ExchangeRate` double DEFAULT '1',
  PRIMARY KEY (`ID`),
  KEY `AccountID` (`AccountID`),
  KEY `JobNoteID` (`JobNoteID`),
  KEY `OrgCurrencyID` (`OrgCurrencyID`),
  KEY `JobAccountingID` (`JobAccountingID`),
  CONSTRAINT `JobJournal_ibfk_1` FOREIGN KEY (`AccountID`) REFERENCES `mycozShared`.`Account` (`ID`),
  CONSTRAINT `JobJournal_ibfk_2` FOREIGN KEY (`JobNoteID`) REFERENCES `JobNote` (`ID`),
  CONSTRAINT `JobJournal_ibfk_3` FOREIGN KEY (`OrgCurrencyID`) REFERENCES `mycozShared`.`Currency` (`ID`),
  CONSTRAINT `JobJournal_ibfk_4` FOREIGN KEY (`JobAccountingID`) REFERENCES `JobAccounting` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `JobNote`
--

DROP TABLE IF EXISTS `JobNote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `JobNote` (
  `ID` int(11) NOT NULL DEFAULT '0',
  `NoteNo` varchar(60) DEFAULT 'NULL',
  `NoteTypeID` int(11) NOT NULL DEFAULT '0',
  `OperatorID` int(11) NOT NULL DEFAULT '0',
  `ChargeToID` int(11) NOT NULL DEFAULT '0',
  `IsPosted` enum('Y','N','O','A') DEFAULT NULL,
  `PostDate` date DEFAULT NULL,
  `PrintCount` smallint(6) DEFAULT '0',
  `Date` date DEFAULT NULL,
  `DueDate` date DEFAULT NULL,
  `Description` text,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `NoteNo` (`NoteNo`),
  KEY `NoteTypeID` (`NoteTypeID`),
  KEY `OperatorID` (`OperatorID`),
  KEY `ChargeToID` (`ChargeToID`),
  CONSTRAINT `JobNote_ibfk_1` FOREIGN KEY (`NoteTypeID`) REFERENCES `mycozShared`.`NoteType` (`ID`),
  CONSTRAINT `JobNote_ibfk_2` FOREIGN KEY (`OperatorID`) REFERENCES `mycozShared`.`OperatorUser` (`ID`),
  CONSTRAINT `JobNote_ibfk_3` FOREIGN KEY (`ChargeToID`) REFERENCES `User` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Message`
--

DROP TABLE IF EXISTS `Message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Message` (
  `ID` int(11) NOT NULL DEFAULT '0',
  `UserID` int(11) NOT NULL DEFAULT '0',
  `BlogID` int(11) NOT NULL DEFAULT '0',
  `MessageDate` date DEFAULT NULL,
  `Title` varchar(50) DEFAULT 'NULL',
  `Description` text,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Title` (`Title`),
  KEY `Message_ibfk_1` (`UserID`),
  KEY `Message_ibfk_2` (`BlogID`),
  CONSTRAINT `Message_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `User` (`ID`),
  CONSTRAINT `Message_ibfk_2` FOREIGN KEY (`BlogID`) REFERENCES `Blog` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `ID` int(11) NOT NULL DEFAULT '0',
  `CountryID` int(11) NOT NULL DEFAULT '0',
  `LanguageID` int(11) NOT NULL DEFAULT '0',
  `CityID` int(11) NOT NULL DEFAULT '0',
  `StateID` int(11) NOT NULL DEFAULT '0',
  `Name` varchar(50) DEFAULT NULL,
  `Password` varchar(50) DEFAULT NULL,
  `Address` varchar(200) DEFAULT NULL,
  `Email` varchar(50) DEFAULT NULL,
  `Tel` varchar(60) DEFAULT NULL,
  `Zip` varchar(20) DEFAULT NULL,
  `MobileNo` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Name` (`Name`),
  KEY `CountryID` (`CountryID`),
  KEY `LanguageID` (`LanguageID`),
  KEY `CityID` (`CityID`),
  KEY `StateID` (`StateID`),
  CONSTRAINT `User_ibfk_1` FOREIGN KEY (`CountryID`) REFERENCES `mycozShared`.`Country` (`ID`),
  CONSTRAINT `User_ibfk_2` FOREIGN KEY (`LanguageID`) REFERENCES `mycozShared`.`Language` (`ID`),
  CONSTRAINT `User_ibfk_3` FOREIGN KEY (`CityID`) REFERENCES `mycozShared`.`City` (`ID`),
  CONSTRAINT `User_ibfk_4` FOREIGN KEY (`StateID`) REFERENCES `mycozShared`.`State` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'mycozBranch'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2010-06-30  8:02:31