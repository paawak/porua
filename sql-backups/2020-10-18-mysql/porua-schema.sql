-- phpMyAdmin SQL Dump
-- version 4.6.6deb5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Oct 18, 2020 at 05:58 PM
-- Server version: 8.0.21
-- PHP Version: 7.2.24-0ubuntu0.18.04.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `porua`
--

-- --------------------------------------------------------

--
-- Table structure for table `book`
--

CREATE TABLE `book` (
  `id` int NOT NULL,
  `name` varchar(200) NOT NULL,
  `language` varchar(25) NOT NULL
) ENGINE=InnoDB ;

-- --------------------------------------------------------

--
-- Table structure for table `ocr_word`
--

CREATE TABLE `ocr_word` (
  `book_id` int NOT NULL,
  `page_image_id` int NOT NULL,
  `word_sequence_id` int NOT NULL,
  `raw_text` longtext NOT NULL,
  `corrected_text` longtext,
  `x1` int NOT NULL,
  `y1` int NOT NULL,
  `x2` int NOT NULL,
  `y2` int NOT NULL,
  `confidence` double NOT NULL,
  `line_number` int DEFAULT NULL,
  `IGNORED` bit(1) NOT NULL DEFAULT b'0'
) ENGINE=InnoDB ;

ALTER TABLE ocr_word MODIFY raw_text longtext CHARACTER SET utf8;
ALTER TABLE ocr_word MODIFY corrected_text longtext CHARACTER SET utf8;


-- --------------------------------------------------------

--
-- Table structure for table `page_image`
--

CREATE TABLE `page_image` (
  `id` int NOT NULL,
  `book_id` int NOT NULL,
  `name` varchar(200) NOT NULL,
  `page_number` int NOT NULL,
  `correction_completed` bit(1) NOT NULL DEFAULT b'0',
  `ignored` bit(1) NOT NULL DEFAULT b'0'
) ENGINE=InnoDB ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `SYS_CT_10112` (`name`);

--
-- Indexes for table `ocr_word`
--
ALTER TABLE `ocr_word`
  ADD KEY `SYS_IDX_SYS_FK_10143_10156` (`book_id`),
  ADD KEY `SYS_IDX_SYS_FK_10144_10158` (`page_image_id`);

--
-- Indexes for table `page_image`
--
ALTER TABLE `page_image`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `SYS_CT_10124` (`name`),
  ADD KEY `SYS_IDX_SYS_FK_10123_10130` (`book_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `book`
--
ALTER TABLE `book`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `page_image`
--
ALTER TABLE `page_image`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=721;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `ocr_word`
--
ALTER TABLE `ocr_word`
  ADD CONSTRAINT `SYS_FK_10143` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`),
  ADD CONSTRAINT `SYS_FK_10144` FOREIGN KEY (`page_image_id`) REFERENCES `page_image` (`id`);

--
-- Constraints for table `page_image`
--
ALTER TABLE `page_image`
  ADD CONSTRAINT `SYS_FK_10123` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
