-- MySQL dump 10.13  Distrib 8.0.37, for Win64 (x86_64)
--
-- Host: localhost    Database: reclamations
-- ------------------------------------------------------
-- Server version	8.1.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `audit_logs`
--

DROP TABLE IF EXISTS `audit_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audit_logs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `table_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `operation` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `record_id` int NOT NULL,
  `user_id` int DEFAULT NULL,
  `timestamp` datetime DEFAULT CURRENT_TIMESTAMP,
  `old_data` longtext COLLATE utf8mb4_unicode_ci,
  `new_data` longtext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `audit_logs_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `utilisateurs` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_logs`
--


--
-- Table structure for table `niveaux`
--

DROP TABLE IF EXISTS `niveaux`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `niveaux` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `niveaux`
--

LOCK TABLES `niveaux` WRITE;
/*!40000 ALTER TABLE `niveaux` DISABLE KEYS */;
INSERT INTO `niveaux` VALUES (1,'مسؤول إداري '),(2,'مهندس درجة 1\n'),(3,'مهندس درجة 2\n'),(4,'موظف إدخال البيانات\n'),(5,'ناسخ\n');
/*!40000 ALTER TABLE `niveaux` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pieces_jointes`
--

DROP TABLE IF EXISTS `pieces_jointes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pieces_jointes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `num_inscription` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nom` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pieces_jointes`
--

LOCK TABLES `pieces_jointes` WRITE;
/*!40000 ALTER TABLE `pieces_jointes` DISABLE KEYS */;
/*!40000 ALTER TABLE `pieces_jointes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reclamations`
--

DROP TABLE IF EXISTS `reclamations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reclamations` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date_depot` datetime NOT NULL,
  `date_inscription` datetime NOT NULL,
  `num_inscription` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type_identifiant` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `identifiant` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nom_complet` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `commune` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `adresse` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `telephone` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `id_type_reclamation` int NOT NULL DEFAULT '1',
  `objet_reclamation` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `piece_jointe_reclamation` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `annee` int NOT NULL,
  `instructions_gouverneur` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `piece_jointe_envoi_autorite` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `piece_jointe_retour_reclamant` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `id_user_saisie` int NOT NULL,
  `id_type_requete` int NOT NULL DEFAULT '1',
  `observation1` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `observation2` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `observation3` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reference_bo` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `piece_jointe` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `id_source_reclamation` int DEFAULT NULL,
  `date_envoi_autorite` datetime(6) DEFAULT NULL,
  `date_envoi_reclamant` datetime(6) DEFAULT NULL,
  `date_retour_autorite` datetime(6) DEFAULT NULL,
  `destinataire` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `flag_envoi_autorite` int DEFAULT NULL,
  `flag_envoi_reclamant` int DEFAULT NULL,
  `flag_retour_autorite` int DEFAULT NULL,
  `reference_envoi_autorite` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reference_envoi_reclamant` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reference_retour_autorite` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `id_type_destinataire` int DEFAULT NULL,
  `id_user_envoi_autorite` int DEFAULT NULL,
  `id_user_envoi_reclamant` int DEFAULT NULL,
  `id_user_retour_autorite` int DEFAULT NULL,
  `voie_envoi_autorite` int DEFAULT NULL,
  `voie_envoi_reclamant` int DEFAULT NULL,
  `voie_retour_autorite` int DEFAULT NULL,
  `flag_cloture` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `num_inscription` (`num_inscription`),
  KEY `id_user_saisie` (`id_user_saisie`),
  KEY `reclamations_ibfk_1` (`id_type_requete`),
  KEY `reclamations_ibfk_2` (`id_type_reclamation`),
  KEY `fk_source_reclamation` (`id_source_reclamation`),
  KEY `FKf62pkn67sq2mmwheiodtoiyni` (`id_type_destinataire`),
  KEY `FK9mlyxqxhxr22pvkp5iiegnf58` (`id_user_envoi_autorite`),
  KEY `FK6et1te0r3cj5l9xhtqx1f19es` (`id_user_envoi_reclamant`),
  KEY `FKr3uqq084rv4nx1j1nyq57vvbi` (`id_user_retour_autorite`),
  KEY `FKj6psmo5h9kxwmio9s7uoc6k72` (`voie_envoi_autorite`),
  KEY `FKomenbo73mey3f9ki2ynppvvxh` (`voie_envoi_reclamant`),
  KEY `FK34exj2mhtus5wyx4kvld4d2e5` (`voie_retour_autorite`),
  CONSTRAINT `FK34exj2mhtus5wyx4kvld4d2e5` FOREIGN KEY (`voie_retour_autorite`) REFERENCES `voie_reponse` (`id`),
  CONSTRAINT `FK6et1te0r3cj5l9xhtqx1f19es` FOREIGN KEY (`id_user_envoi_reclamant`) REFERENCES `utilisateurs` (`id`),
  CONSTRAINT `FK9mlyxqxhxr22pvkp5iiegnf58` FOREIGN KEY (`id_user_envoi_autorite`) REFERENCES `utilisateurs` (`id`),
  CONSTRAINT `fk_source_reclamation` FOREIGN KEY (`id_source_reclamation`) REFERENCES `source_reclamation` (`id`),
  CONSTRAINT `FKf62pkn67sq2mmwheiodtoiyni` FOREIGN KEY (`id_type_destinataire`) REFERENCES `type_destinataire` (`id`),
  CONSTRAINT `FKj6psmo5h9kxwmio9s7uoc6k72` FOREIGN KEY (`voie_envoi_autorite`) REFERENCES `voie_reponse` (`id`),
  CONSTRAINT `FKomenbo73mey3f9ki2ynppvvxh` FOREIGN KEY (`voie_envoi_reclamant`) REFERENCES `voie_reponse` (`id`),
  CONSTRAINT `FKr3uqq084rv4nx1j1nyq57vvbi` FOREIGN KEY (`id_user_retour_autorite`) REFERENCES `utilisateurs` (`id`),
  CONSTRAINT `reclamations_ibfk_1` FOREIGN KEY (`id_type_requete`) REFERENCES `type_requete` (`id`) ON DELETE CASCADE,
  CONSTRAINT `reclamations_ibfk_2` FOREIGN KEY (`id_type_reclamation`) REFERENCES `type_reclamation` (`id`) ON DELETE CASCADE,
  CONSTRAINT `reclamations_ibfk_7` FOREIGN KEY (`id_user_saisie`) REFERENCES `utilisateurs` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reclamations`
--

LOCK TABLES `reclamations` WRITE;
/*!40000 ALTER TABLE `reclamations` DISABLE KEYS */;
INSERT INTO `reclamations` VALUES (1,'2025-03-13 00:00:00','2025-03-12 00:00:00','250312.0001','CIN','Q186424','الاسم الكامل','عامر السفلية','العنوان ','0661122225',16,'موضوع الشكاية',NULL,2025,'تعليمات سيد العامل',NULL,NULL,1,2,'ملاحظة jh',NULL,NULL,'250312.0001',NULL,3,NULL,NULL,NULL,NULL,1,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2,'2025-03-12 00:00:00','2025-03-12 00:00:00','250312.0002','CIN','Q186424','الاسم الكامل :','بنمنصور','الاسم الكامل :','066666666',18,'موضوع الشكاية :',NULL,2025,'موضوع الشكاية :',NULL,NULL,1,3,'موضوع الشكاية :',NULL,NULL,'250312.0002',NULL,3,NULL,NULL,NULL,NULL,1,1,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1);
/*!40000 ALTER TABLE `reclamations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reclamations_detail`
--

DROP TABLE IF EXISTS `reclamations_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reclamations_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_reclamation` int NOT NULL,
  `num_inscription` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `flag_envoi_autorite` int DEFAULT NULL,
  `date_envoi_autorite` datetime DEFAULT NULL,
  `id_type_destinataire` int DEFAULT NULL,
  `reference_envoi_autorite` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `flag_retour_autorite` int DEFAULT NULL,
  `date_retour_autorite` datetime DEFAULT NULL,
  `voie_retour_autorite` int DEFAULT NULL,
  `reference_retour_autorite` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `flag_envoi_reclamant` int DEFAULT NULL,
  `date_envoi_reclamant` datetime DEFAULT NULL,
  `voie_envoi_reclamant` int DEFAULT NULL,
  `reference_envoi_reclamant` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `id_user_envoi_autorite` int DEFAULT NULL,
  `id_user_retour_autorite` int DEFAULT NULL,
  `id_user_envoi_reclamant` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_reclamation` (`id_reclamation`),
  KEY `id_type_destinataire` (`id_type_destinataire`),
  KEY `id_user_envoi_autorite` (`id_user_envoi_autorite`),
  KEY `id_user_retour_autorite` (`id_user_retour_autorite`),
  KEY `id_user_envoi_reclamant` (`id_user_envoi_reclamant`),
  KEY `FK6m3lsl2anynmk2up0ar4rnmx` (`voie_envoi_reclamant`),
  CONSTRAINT `FK6m3lsl2anynmk2up0ar4rnmx` FOREIGN KEY (`voie_envoi_reclamant`) REFERENCES `voie_reponse` (`id`),
  CONSTRAINT `reclamations_detail_ibfk_1` FOREIGN KEY (`id_reclamation`) REFERENCES `reclamations` (`id`),
  CONSTRAINT `reclamations_detail_ibfk_2` FOREIGN KEY (`id_type_destinataire`) REFERENCES `type_destinataire` (`id`),
  CONSTRAINT `reclamations_detail_ibfk_3` FOREIGN KEY (`id_user_envoi_autorite`) REFERENCES `utilisateurs` (`id`),
  CONSTRAINT `reclamations_detail_ibfk_4` FOREIGN KEY (`id_user_retour_autorite`) REFERENCES `utilisateurs` (`id`),
  CONSTRAINT `reclamations_detail_ibfk_5` FOREIGN KEY (`id_user_envoi_reclamant`) REFERENCES `utilisateurs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reclamations_detail`
--

LOCK TABLES `reclamations_detail` WRITE;
/*!40000 ALTER TABLE `reclamations_detail` DISABLE KEYS */;
INSERT INTO `reclamations_detail` VALUES (1,1,'250312.0001',1,'2025-03-14 00:00:00',2,'REF001',1,'2025-03-18 00:00:00',NULL,'REFT',NULL,NULL,NULL,NULL,1,1,NULL),(2,1,'250312.0001',1,'2025-03-14 00:00:00',2,'REF001',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL),(3,1,'250312.0001',1,'2025-03-14 00:00:00',4,'REF002',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL),(4,1,'250312.0001',1,'2025-03-14 00:00:00',6,'REF004',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL),(7,1,'250312.0001',1,'2025-03-14 00:00:00',6,'15552',1,'2025-03-15 00:00:00',NULL,'REFRET0107',NULL,NULL,NULL,NULL,1,1,NULL),(8,2,'250312.0002',1,'2025-03-14 00:00:00',6,'REF 2002',1,'2025-03-15 00:00:00',NULL,'REFRET01',NULL,NULL,NULL,NULL,1,1,NULL),(9,2,'250312.0002',1,'2025-03-15 00:00:00',2,'REGENV01',1,'2025-03-17 00:00:00',NULL,'REFRET',NULL,NULL,NULL,NULL,1,1,NULL),(10,2,'250312.0002',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'2025-03-15 00:00:00',3,'REF',NULL,NULL,1);
/*!40000 ALTER TABLE `reclamations_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scans_pieces`
--

DROP TABLE IF EXISTS `scans_pieces`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `scans_pieces` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fileName` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `language` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `extractedText` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dateUploaded` datetime DEFAULT NULL,
  `num_inscription` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `date_uploaded` datetime(6) DEFAULT NULL,
  `extracted_text` varchar(10000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `file_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scans_pieces`
--

LOCK TABLES `scans_pieces` WRITE;
/*!40000 ALTER TABLE `scans_pieces` DISABLE KEYS */;
/*!40000 ALTER TABLE `scans_pieces` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sequence_generator`
--

DROP TABLE IF EXISTS `sequence_generator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sequence_generator` (
  `id` int NOT NULL AUTO_INCREMENT,
  `annee` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dernier_numero` int NOT NULL,
  `mois` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sequence_generator`
--

LOCK TABLES `sequence_generator` WRITE;
/*!40000 ALTER TABLE `sequence_generator` DISABLE KEYS */;
INSERT INTO `sequence_generator` VALUES (1,'2025',2,'');
/*!40000 ALTER TABLE `sequence_generator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `source_reclamation`
--

DROP TABLE IF EXISTS `source_reclamation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `source_reclamation` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom_langue_1` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nom_langue_2` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `source_reclamation`
--

LOCK TABLES `source_reclamation` WRITE;
/*!40000 ALTER TABLE `source_reclamation` DISABLE KEYS */;
INSERT INTO `source_reclamation` VALUES (1,'مكتب الضبط','مكتب الضبط'),(2,'عبر البريد','عبر البريد'),(3,'المفتشية العامة بوزارة الداخلية','المفتشية العامة بوزارة الداخلية'),(4,'مصالح اخرى بوزارة الداخلية','مصالح اخرى بوزارة الداخلية'),(5,'جهات أخرى','جهات أخرى'),(6,'دائرة القنيطرة','دائرة القنيطرة');
/*!40000 ALTER TABLE `source_reclamation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `territoires`
--

DROP TABLE IF EXISTS `territoires`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `territoires` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code_ville` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nom_ville_langue_1` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nom_ville_langue_2` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `code_commune` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nom_commune_langue_1` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nom_commune_langue_2` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `code_arrondissement` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nom_arrondissement_langue_1` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nom_arrondissement_langue_2` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `territoires`
--

LOCK TABLES `territoires` WRITE;
/*!40000 ALTER TABLE `territoires` DISABLE KEYS */;
INSERT INTO `territoires` VALUES (1,'KEN','Kénitra','القنيطرة','C01','Kénitra','القنيطرة','A01','Maâmora','المعمورة'),(2,'KEN','Kénitra','القنيطرة','C01','Kénitra','القنيطرة','A02','Oulad Oujih','أولاد اوجيه'),(3,'KEN','Kénitra','القنيطرة','C01','Kénitra','القنيطرة','A03','Saknia','الساكنية'),(4,'KEN','Kénitra','القنيطرة','C02','Mehdia','المهدية','A04','Mehdia','المهدية'),(5,'KEN','Kénitra','القنيطرة','C03','Sidi Taibi','سيدي الطيبي','A05','Sidi Taibi','سيدي الطيبي'),(6,'KEN','Kénitra','القنيطرة','C04','Oulad Slama','أولاد سلامة','A06','Oulad Slama','أولاد سلامة'),(7,'KEN','Kénitra','القنيطرة','C05','Ben Mansour','بنمنصور','A07','Ben Mansour','بنمنصور'),(8,'KEN','Kénitra','القنيطرة','C06','Moulay Bousselham','مولاي بوسلهام','A08','Moulay Bousselham','مولاي بوسلهام'),(9,'KEN','Kénitra','القنيطرة','C07','Sidi Boubker Haj','سيدي بوبكر الحاج','A09','Sidi Boubker Haj','سيدي بوبكر الحاج'),(10,'KEN','Kénitra','القنيطرة','C08','Lalla Mimouna','لالة ميمونة','A10','Lalla Mimouna','لالة ميمونة'),(11,'KEN','Kénitra','القنيطرة','C09','Souk El Arbaa','سوق الأربعاء','A11','Souk El Arbaa','سوق الأربعاء'),(12,'KEN','Kénitra','القنيطرة','C10','Souk Tlet','سوق الثلاثاء','A12','Souk Tlet','سوق الثلاثاء'),(13,'KEN','Kénitra','القنيطرة','C11','Sidi Mohammed Lahmar','سيدي محمد لحمر','A13','Sidi Mohammed Lahmar','سيدي محمد لحمر'),(14,'KEN','Kénitra','القنيطرة','C12','Ain Sbâa','عين السبع','A14','Ain Sbâa','عين السبع'),(15,'KEN','Kénitra','القنيطرة','C13','Sidi Yahia','سيدي يحيى','A15','Sidi Yahia','سيدي يحيى'),(16,'KEN','Kénitra','القنيطرة','C14','Amer Seflia','عامر السفلية','A16','Amer Seflia','عامر السفلية'),(17,'KEN','Kénitra','القنيطرة','C15','Chouafaa','الشوافع','A17','Chouafaa','الشوافع'),(18,'KEN','Kénitra','القنيطرة','C16','Oulad Ambarak','أولاد أمبارك','A18','Oulad Ambarak','أولاد أمبارك');
/*!40000 ALTER TABLE `territoires` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_destinataire`
--

DROP TABLE IF EXISTS `type_destinataire`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_destinataire` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom_langue_1` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nom_langue_2` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `libelle` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_destinataire`
--

LOCK TABLES `type_destinataire` WRITE;
/*!40000 ALTER TABLE `type_destinataire` DISABLE KEYS */;
INSERT INTO `type_destinataire` VALUES (1,'رئيس دائرة','رئيس دائرة',NULL),(2,'رئيس قسم','رئيس قسم',NULL),(3,'رئيس مصلحة','رئيس مصلحة',NULL),(4,'دائرة','دائرة',NULL),(5,'قسم','قسم',NULL),(6,'مصلحة','مصلحة',NULL);
/*!40000 ALTER TABLE `type_destinataire` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_reclamation`
--

DROP TABLE IF EXISTS `type_reclamation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_reclamation` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom_langue_1` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nom_langue_2` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `libelle` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_reclamation`
--

LOCK TABLES `type_reclamation` WRITE;
/*!40000 ALTER TABLE `type_reclamation` DISABLE KEYS */;
INSERT INTO `type_reclamation` VALUES (1,'العقار والملكيات','العقار والملكيات',NULL),(2,'التزوير والفساد','التزوير والفساد',NULL),(3,'التجارة والأنشطة التجارية','التجارة والأنشطة التجارية',NULL),(4,'نزع الملكية والإفراغ','نزع الملكية والإفراغ',NULL),(5,'حقوق الجماعات المحلية','حقوق الجماعات المحلية',NULL),(6,'طلبات الاستفادة من الأراضي','طلبات الاستفادة من الأراضي',NULL),(7,'الأسواق والأماكن العامة','الأسواق والأماكن العامة',NULL),(8,'التعمير والسكن','التعمير والسكن',NULL),(9,'المخالفات التعميرية','المخالفات التعميرية',NULL),(10,'التعدي على الممتلكات','التعدي على الممتلكات',NULL),(11,'الصحة والضمان الاجتماعي','الصحة والضمان الاجتماعي',NULL),(12,'التراخيص والشواهد الإدارية','التراخيص والشواهد الإدارية',NULL),(13,'المساعدات والدعم','المساعدات والدعم',NULL),(14,'الاستغلال غير القانوني للموارد','الاستغلال غير القانوني للموارد',NULL),(15,'النزاعات الأسرية والاجتماعية','النزاعات الأسرية والاجتماعية',NULL),(16,'الملك العام والخاص للدولة','الملك العام والخاص للدولة',NULL),(17,'تنفيذ الأحكام القضائية','تنفيذ الأحكام القضائية',NULL),(18,'النزاعات الإدارية','النزاعات الإدارية',NULL),(19,'المشاكل البيئية','المشاكل البيئية',NULL),(20,'النقل والتنقل','النقل والتنقل',NULL),(21,'شكايات ضد المنتخبين والممثلين','شكايات ضد المنتخبين والممثلين',NULL),(22,'استعمال الفضاءات العمومية','استعمال الفضاءات العمومية',NULL),(23,'النزاعات العقارية','النزاعات العقارية',NULL),(24,'التوظيف والتشغيل','التوظيف والتشغيل',NULL),(25,'أنواع أخرى من الشكايات','أنواع أخرى من الشكايات',NULL);
/*!40000 ALTER TABLE `type_reclamation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_requete`
--

DROP TABLE IF EXISTS `type_requete`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_requete` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom_langue_1` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nom_langue_2` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `libelle` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_requete`
--

LOCK TABLES `type_requete` WRITE;
/*!40000 ALTER TABLE `type_requete` DISABLE KEYS */;
INSERT INTO `type_requete` VALUES (1,'طلب استفسار','طلب استفسار',NULL),(2,'شكوى رسمية','شكوى رسمية',NULL),(3,'طلب مقابلة','طلب مقابلة',NULL);
/*!40000 ALTER TABLE `type_requete` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utilisateurs`
--

DROP TABLE IF EXISTS `utilisateurs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utilisateurs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `login` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pass` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `niveau` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` enum('ADMIN','UTILISATEUR','CONSULTATION') COLLATE utf8mb4_unicode_ci NOT NULL,
  `must_change_password` tinyint(1) DEFAULT '1',
  `profile_picture` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reset_token` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reset_token_expiry` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utilisateurs`
--

LOCK TABLES `utilisateurs` WRITE;
/*!40000 ALTER TABLE `utilisateurs` DISABLE KEYS */;
INSERT INTO `utilisateurs` VALUES (1,'My Isam Mhamedi','imhamedi','$2a$10$5CVThNYnCx1wiZD6lQEfTeN8YOiVUvkjpdoPeXAdLLbUuuoTaTOgS','مسؤول إداري','mhamedi.issam@gmail.com','ADMIN',1,'imhamedi_1.png','67bd581f-52e5-4dc4-98da-8aed6e946b01','2189-05-28 11:56:06.327555'),(5,'issam mhamedi','issam','$2a$10$593DKq/T3BDEaW38o6M9A.DVK8h7Tz9LMv8GHGFU5p7ScGoU.TFsG',NULL,'myisam.mhamedi@gmail.com','ADMIN',1,NULL,NULL,NULL),(7,'Issam 2','user01','$2a$10$ssoapDr1.1igfRHTmoXtM.nFX/3FcE69e7aN7aOsA48tMXQ0PlgWC',NULL,'issam.mhamedi@cdgcapital.ma','UTILISATEUR',1,NULL,NULL,NULL),(8,'issa','iss','$2a$10$mGKu2TqnSoi7OyfFzpRSze/2Sh/pXRJ1TYAcghaHjtJlwPVi4bp0a',NULL,'iss@gmail.com','UTILISATEUR',1,NULL,NULL,NULL),(9,'iss3.','issam3','$2a$10$aKTs8H1YdfAjKAAitObcseeR7VsKKAzm9HeWtmAbcfOjCptNnYYOq',NULL,'aiss@gmail.com','UTILISATEUR',1,NULL,'5f563649-bd0c-4cd0-b1f2-20989ec66ad8','2189-05-31 09:46:30.578411'),(11,'iss6','iss5','$2a$10$7LpWM9BVfYDpbVvg/bAqNO32.J2h9DbGkaGdgQ1cJMyf9c3YqH/5S',NULL,'iss5@gmail.com','UTILISATEUR',1,NULL,NULL,NULL);
/*!40000 ALTER TABLE `utilisateurs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `voie_reponse`
--

DROP TABLE IF EXISTS `voie_reponse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `voie_reponse` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom_langue_1` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nom_langue_2` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `libelle` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `voie_reponse`
--

LOCK TABLES `voie_reponse` WRITE;
/*!40000 ALTER TABLE `voie_reponse` DISABLE KEYS */;
INSERT INTO `voie_reponse` VALUES (1,'مكتب الضبط','مكتب الضبط',NULL),(2,'عبر البريد','عبر البريد',NULL),(3,'المفتشية العامة بوزارة الداخلية','المفتشية العامة بوزارة الداخلية',NULL),(4,'مصالح اخرى بوزارة الداخلية','مصالح اخرى بوزارة الداخلية',NULL),(5,'جهات أخرى','جهات أخرى',NULL),(6,'دائرة القنيطرة','دائرة القنيطرة',NULL);
/*!40000 ALTER TABLE `voie_reponse` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-20 10:57:46
