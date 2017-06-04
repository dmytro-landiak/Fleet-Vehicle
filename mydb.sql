USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`city`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`city` (
  `CITY_ID` INT NOT NULL AUTO_INCREMENT,
  `CITY_NAME` VARCHAR(45) NULL,
  PRIMARY KEY (`CITY_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`buspark`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`buspark` (
  `BUSPARK_ID` INT NOT NULL AUTO_INCREMENT,
  `BUSPARK_NAME` VARCHAR(45) NULL,
  `city_CITY_ID` INT NOT NULL,
  PRIMARY KEY (`BUSPARK_ID`, `city_CITY_ID`),
  INDEX `fk_buspark_city1_idx` (`city_CITY_ID` ASC),
  CONSTRAINT `fk_buspark_city1`
    FOREIGN KEY (`city_CITY_ID`)
    REFERENCES `mydb`.`city` (`CITY_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`bus_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`bus_type` (
  `BUSTYPE_ID` INT NOT NULL AUTO_INCREMENT,
  `TYPE` VARCHAR(45) NULL,
  `CAPACITY` INT NULL,
  PRIMARY KEY (`BUSTYPE_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`bus`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`bus` (
  `BUS_ID` INT NOT NULL AUTO_INCREMENT,
  `REGIST_PLATE` VARCHAR(45) NULL,
  `buspark_BUSPARK_ID` INT NOT NULL,
  `bus_type_BUSTYPE_ID` INT NOT NULL,
  PRIMARY KEY (`BUS_ID`, `bus_type_BUSTYPE_ID`),
  INDEX `fk_bus_buspark1_idx` (`buspark_BUSPARK_ID` ASC),
  INDEX `fk_bus_bus_type1_idx` (`bus_type_BUSTYPE_ID` ASC),
  CONSTRAINT `fk_bus_buspark1`
    FOREIGN KEY (`buspark_BUSPARK_ID`)
    REFERENCES `mydb`.`buspark` (`BUSPARK_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_bus_bus_type1`
    FOREIGN KEY (`bus_type_BUSTYPE_ID`)
    REFERENCES `mydb`.`bus_type` (`BUSTYPE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`user` (
  `USER_ID` INT NOT NULL AUTO_INCREMENT,
  `USER_NAME` VARCHAR(45) NULL,
  `E_MAIL` VARCHAR(45) NOT NULL,
  `PASSWORD` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`USER_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`route`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`route` (
  `ROUTE_ID` INT NOT NULL AUTO_INCREMENT,
  `ROUTE_NAME` VARCHAR(45) NULL,
  PRIMARY KEY (`ROUTE_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`driver`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`driver` (
  `DRIVER_ID` INT NOT NULL AUTO_INCREMENT,
  `DRIVER_NAME` VARCHAR(45) NULL,
  `PHONE` VARCHAR(45) NULL,
  `route_ROUTE_ID` INT NOT NULL,
  `bus_BUS_ID` INT NOT NULL,
  `city_CITY_ID` INT NOT NULL,
  PRIMARY KEY (`DRIVER_ID`),
  INDEX `fk_driver_route_idx` (`route_ROUTE_ID` ASC),
  INDEX `fk_driver_bus1_idx` (`bus_BUS_ID` ASC),
  INDEX `fk_driver_city1_idx` (`city_CITY_ID` ASC),
  CONSTRAINT `fk_driver_route`
    FOREIGN KEY (`route_ROUTE_ID`)
    REFERENCES `mydb`.`route` (`ROUTE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_driver_bus1`
    FOREIGN KEY (`bus_BUS_ID`)
    REFERENCES `mydb`.`bus` (`BUS_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_driver_city1`
    FOREIGN KEY (`city_CITY_ID`)
    REFERENCES `mydb`.`city` (`CITY_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`trip`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`trip` (
  `TRIP_ID` INT NOT NULL AUTO_INCREMENT,
  `DAY` VARCHAR(45) NULL,
  `TIME` VARCHAR(45) NULL,
  `PASSENG_COUNT` INT NULL,
  `route_ROUTE_ID` INT NOT NULL,
  PRIMARY KEY (`TRIP_ID`, `route_ROUTE_ID`),
  INDEX `fk_trip_route1_idx` (`route_ROUTE_ID` ASC),
  CONSTRAINT `fk_trip_route1`
    FOREIGN KEY (`route_ROUTE_ID`)
    REFERENCES `mydb`.`route` (`ROUTE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`prognos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`prognos` (
  `PROGNOS_ID` INT NOT NULL AUTO_INCREMENT,
  `DAY` VARCHAR(45) NULL,
  `TIME` VARCHAR(45) NULL,
  `PROG_COUNT` INT NULL,
  `route_ROUTE_ID` INT NOT NULL,
  PRIMARY KEY (`PROGNOS_ID`, `route_ROUTE_ID`),
  INDEX `fk_prognos_route1_idx` (`route_ROUTE_ID` ASC),
  CONSTRAINT `fk_prognos_route1`
    FOREIGN KEY (`route_ROUTE_ID`)
    REFERENCES `mydb`.`route` (`ROUTE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`buspark_bus_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`buspark_bus_type` (
  `buspark_BUSPARK_ID` INT NOT NULL,
  `bus_type_BUSTYPE_ID` INT NOT NULL,
  `COUNT` INT NULL,
  PRIMARY KEY (`buspark_BUSPARK_ID`, `bus_type_BUSTYPE_ID`),
  INDEX `fk_buspark_has_bus_type_bus_type1_idx` (`bus_type_BUSTYPE_ID` ASC),
  INDEX `fk_buspark_has_bus_type_buspark1_idx` (`buspark_BUSPARK_ID` ASC),
  CONSTRAINT `fk_buspark_has_bus_type_buspark1`
    FOREIGN KEY (`buspark_BUSPARK_ID`)
    REFERENCES `mydb`.`buspark` (`BUSPARK_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_buspark_has_bus_type_bus_type1`
    FOREIGN KEY (`bus_type_BUSTYPE_ID`)
    REFERENCES `mydb`.`bus_type` (`BUSTYPE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;