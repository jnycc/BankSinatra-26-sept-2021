-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema banksinatra
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema banksinatra
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `banksinatra` ;
USE `banksinatra` ;

-- -----------------------------------------------------
-- Table `banksinatra`.`Gebruiker`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banksinatra`.`Gebruiker` (
  `gebruikersnaam` VARCHAR(25) NOT NULL,
  `wachtwoord` VARCHAR(100) NOT NULL,
  `isBeheerder` TINYINT NOT NULL,
  `salt` VARCHAR(25) NOT NULL,
  `isGeblokkeerd` TINYINT NOT NULL,
  PRIMARY KEY (`gebruikersnaam`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `banksinatra`.`Klant`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banksinatra`.`Klant` (
  `gebruikersnaamKlant` VARCHAR(25) NOT NULL,
  `voornaam` VARCHAR(45) NOT NULL,
  `tussenvoegsels` VARCHAR(15) NULL,
  `achternaam` VARCHAR(45) NOT NULL,
  `bsn` INT NOT NULL,
  `geboortedatum` DATE NOT NULL,
  `stad` VARCHAR(45) NOT NULL,
  `straat` VARCHAR(45) NOT NULL,
  `huisnummer` INT NOT NULL,
  `huisnummerToevoeging` VARCHAR(15) NULL,
  PRIMARY KEY (`gebruikersnaamKlant`),
  INDEX `Gebruiker_gebruikersNaam2_idx` (`gebruikersnaamKlant` ASC) VISIBLE,
  CONSTRAINT `Gebruiker_gebruikersNaam2`
    FOREIGN KEY (`gebruikersnaamKlant`)
    REFERENCES `banksinatra`.`Gebruiker` (`gebruikersnaam`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `banksinatra`.`CryptoCurrency`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banksinatra`.`CryptoCurrency` (
  `naam` VARCHAR(45) NOT NULL,
  `afkorting` VARCHAR(10) NOT NULL,
  `koerswaarde` VARCHAR(45) NOT NULL,
  `beschrijving` VARCHAR(150) NOT NULL,
  PRIMARY KEY (`naam`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `banksinatra`.`Rekening`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banksinatra`.`Rekening` (
  `gebruikersnaamKlant` VARCHAR(25) NOT NULL,
  `IBAN` VARCHAR(45) NOT NULL,
  `saldo` DECIMAL NOT NULL,
  INDEX `Klant_gebruikersNaamKlant1_idx` (`gebruikersnaamKlant` ASC) VISIBLE,
  PRIMARY KEY (`IBAN`),
  CONSTRAINT `Klant_gebruikersNaamKlant1`
    FOREIGN KEY (`gebruikersnaamKlant`)
    REFERENCES `banksinatra`.`Klant` (`gebruikersnaamKlant`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `banksinatra`.`Bankkosten`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banksinatra`.`Bankkosten` (
  `percentage` DECIMAL(15) NOT NULL,
  PRIMARY KEY (`percentage`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `banksinatra`.`Transactie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banksinatra`.`Transactie` (
  `IBAN_inkoper` VARCHAR(45) NOT NULL,
  `IBAN_verkoper` VARCHAR(45) NOT NULL,
  `transactieNummer` INT NOT NULL,
  `datum` DATETIME NOT NULL,
  `cryptoCurrencyNaam` VARCHAR(45) NOT NULL,
  `eenheden` DECIMAL(25) NOT NULL,
  `koerswaarde` VARCHAR(45) NOT NULL,
  `bankkostenPercentage` DECIMAL(15) NOT NULL,
  INDEX `Rekening_IBAN11_idx` (`IBAN_verkoper` ASC) VISIBLE,
  INDEX `Rekening_IBAN2_idx` (`IBAN_inkoper` ASC) VISIBLE,
  PRIMARY KEY (`transactieNummer`),
  INDEX `Asset_naam2_idx` (`cryptoCurrencyNaam` ASC) VISIBLE,
  INDEX `Bankkosten_percentage1_idx` (`bankkostenPercentage` ASC) VISIBLE,
  CONSTRAINT `Rekening_IBAN2`
    FOREIGN KEY (`IBAN_inkoper`)
    REFERENCES `banksinatra`.`Rekening` (`IBAN`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `Rekening_IBAN11`
    FOREIGN KEY (`IBAN_verkoper`)
    REFERENCES `banksinatra`.`Rekening` (`IBAN`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `Asset_naam2`
    FOREIGN KEY (`cryptoCurrencyNaam`)
    REFERENCES `banksinatra`.`CryptoCurrency` (`naam`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `Bankkosten_percentage1`
    FOREIGN KEY (`bankkostenPercentage`)
    REFERENCES `banksinatra`.`Bankkosten` (`percentage`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `banksinatra`.`Asset`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `banksinatra`.`Asset` (
  `cryptoCurrencyNaam` VARCHAR(45) NOT NULL,
  `eenheden` DECIMAL(25) NOT NULL,
  `gebruikersnaamKlant` VARCHAR(25) NOT NULL,
  PRIMARY KEY (`cryptoCurrencyNaam`, `gebruikersnaamKlant`),
  INDEX `Asset_naam1_idx` (`cryptoCurrencyNaam` ASC) VISIBLE,
  INDEX `Klant_gebruikersnaamKlant2_idx` (`gebruikersnaamKlant` ASC) VISIBLE,
  CONSTRAINT `Asset_naam1`
    FOREIGN KEY (`cryptoCurrencyNaam`)
    REFERENCES `banksinatra`.`CryptoCurrency` (`naam`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `Klant_gebruikersnaamKlant2`
    FOREIGN KEY (`gebruikersnaamKlant`)
    REFERENCES `banksinatra`.`Klant` (`gebruikersnaamKlant`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- Gebruiker definiëren en toegang verlenen
CREATE USER 'bs'@'localhost' IDENTIFIED BY 'bs';
GRANT ALL PRIVILEGES ON `banksinatra` . * TO 'bs'@'localhost';

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
