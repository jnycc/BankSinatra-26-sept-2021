-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `mydb` ;

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
-- -----------------------------------------------------
-- Schema banksinatra
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `banksinatra` ;

-- -----------------------------------------------------
-- Schema banksinatra
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `banksinatra` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`user` (
  `username` VARCHAR(16) NOT NULL,
  `email` VARCHAR(255) NULL,
  `password` VARCHAR(32) NOT NULL,
  `create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP);

USE `banksinatra` ;

-- -----------------------------------------------------
-- Table `banksinatra`.`cryptocurrency`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banksinatra`.`cryptocurrency` (
  `naam` VARCHAR(45) NOT NULL,
  `afkorting` VARCHAR(10) NOT NULL,
  `koerswaarde` VARCHAR(45) NOT NULL,
  `beschrijving` VARCHAR(150) NOT NULL,
  PRIMARY KEY (`naam`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `banksinatra`.`gebruiker`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banksinatra`.`gebruiker` (
  `gebruikersnaam` VARCHAR(25) NOT NULL,
  `wachtwoord` VARCHAR(100) NOT NULL,
  `isBeheerder` TINYINT NOT NULL,
  `salt` VARCHAR(25) NOT NULL,
  `isGeblokkeerd` TINYINT NOT NULL,
  PRIMARY KEY (`gebruikersnaam`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `banksinatra`.`klant`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banksinatra`.`klant` (
  `gebruikersnaamKlant` VARCHAR(25) NOT NULL,
  `voornaam` VARCHAR(45) NOT NULL,
  `tussenvoegsels` VARCHAR(15) NULL DEFAULT NULL,
  `achternaam` VARCHAR(45) NOT NULL,
  `bsn` INT NOT NULL,
  `geboortedatum` DATE NOT NULL,
  `stad` VARCHAR(45) NOT NULL,
  `straat` VARCHAR(45) NOT NULL,
  `huisnummer` INT NOT NULL,
  `huisnummerToevoeging` VARCHAR(15) NULL DEFAULT NULL,
  PRIMARY KEY (`gebruikersnaamKlant`),
  INDEX `Gebruiker_gebruikersNaam2_idx` (`gebruikersnaamKlant` ASC) VISIBLE,
  CONSTRAINT `Gebruiker_gebruikersNaam2`
    FOREIGN KEY (`gebruikersnaamKlant`)
    REFERENCES `banksinatra`.`gebruiker` (`gebruikersnaam`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `banksinatra`.`asset`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banksinatra`.`asset` (
  `cryptoCurrencyNaam` VARCHAR(45) NOT NULL,
  `eenheden` DECIMAL(25,0) NOT NULL,
  `gebruikersnaamKlant` VARCHAR(25) NOT NULL,
  PRIMARY KEY (`cryptoCurrencyNaam`, `gebruikersnaamKlant`),
  INDEX `Asset_naam1_idx` (`cryptoCurrencyNaam` ASC) VISIBLE,
  INDEX `Klant_gebruikersnaamKlant2_idx` (`gebruikersnaamKlant` ASC) VISIBLE,
  CONSTRAINT `Asset_naam1`
    FOREIGN KEY (`cryptoCurrencyNaam`)
    REFERENCES `banksinatra`.`cryptocurrency` (`naam`),
  CONSTRAINT `Klant_gebruikersnaamKlant2`
    FOREIGN KEY (`gebruikersnaamKlant`)
    REFERENCES `banksinatra`.`klant` (`gebruikersnaamKlant`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `banksinatra`.`bankkosten`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banksinatra`.`bankkosten` (
  `percentage` DECIMAL(15,0) NOT NULL,
  PRIMARY KEY (`percentage`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `banksinatra`.`rekening`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banksinatra`.`rekening` (
  `gebruikersnaamKlant` VARCHAR(25) NOT NULL,
  `IBAN` VARCHAR(45) NOT NULL,
  `saldo` DECIMAL(10,0) NOT NULL,
  PRIMARY KEY (`IBAN`),
  INDEX `Klant_gebruikersNaamKlant1_idx` (`gebruikersnaamKlant` ASC) VISIBLE,
  CONSTRAINT `Klant_gebruikersNaamKlant1`
    FOREIGN KEY (`gebruikersnaamKlant`)
    REFERENCES `banksinatra`.`klant` (`gebruikersnaamKlant`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `banksinatra`.`transactie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banksinatra`.`transactie` (
  `IBAN_inkoper` VARCHAR(45) NOT NULL,
  `IBAN_verkoper` VARCHAR(45) NOT NULL,
  `transactieNummer` INT NOT NULL,
  `datum` DATETIME NOT NULL,
  `cryptoCurrencyNaam` VARCHAR(45) NOT NULL,
  `eenheden` DECIMAL(25,0) NOT NULL,
  `koerswaarde` VARCHAR(45) NOT NULL,
  `bankkosten` DECIMAL(15,0) NOT NULL,
  PRIMARY KEY (`transactieNummer`),
  INDEX `Rekening_IBAN11_idx` (`IBAN_verkoper` ASC) VISIBLE,
  INDEX `Rekening_IBAN2_idx` (`IBAN_inkoper` ASC) VISIBLE,
  INDEX `Asset_naam2_idx` (`cryptoCurrencyNaam` ASC) VISIBLE,
  CONSTRAINT `Asset_naam2`
    FOREIGN KEY (`cryptoCurrencyNaam`)
    REFERENCES `banksinatra`.`cryptocurrency` (`naam`),
  CONSTRAINT `Rekening_IBAN11`
    FOREIGN KEY (`IBAN_verkoper`)
    REFERENCES `banksinatra`.`rekening` (`IBAN`),
  CONSTRAINT `Rekening_IBAN2`
    FOREIGN KEY (`IBAN_inkoper`)
    REFERENCES `banksinatra`.`rekening` (`IBAN`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- Gebruiker definiëren en toegang verlenen
CREATE USER 'bs'@'localhost' IDENTIFIED BY 'bs';
GRANT ALL PRIVILEGES ON `banksinatra` . * TO 'bs'@'localhost';

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
