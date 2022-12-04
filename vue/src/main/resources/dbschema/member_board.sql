-- DROP SCHEMA IF EXISTS `side` ;
CREATE SCHEMA IF NOT EXISTS `side` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `side` ;

-- -----------------------------------------------------
-- Table `side`.`member`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `side`.`member` ;

CREATE TABLE `member` (
  `user_id` VARCHAR(16) NOT NULL,
  `user_name` VARCHAR(20) NOT NULL,
  `user_pwd` VARCHAR(100) NOT NULL,
  `email` VARCHAR(60) NULL DEFAULT NULL,
  `join_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `role` VARCHAR(20) DEFAULT 'ROLE_USER',
  PRIMARY KEY (`user_id`))

ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

commit;

-- -----------------------------------------------------
-- Table `side`.`board`
-- -----------------------------------------------------
CREATE TABLE `board` (
  `article_no` INT NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(16) NULL DEFAULT NULL,
  `subject` VARCHAR(100) NULL DEFAULT NULL,
  `content` VARCHAR(2000) NULL DEFAULT NULL,
  `hit` INT NULL DEFAULT 0,
  `reg_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`article_no`),
  INDEX `board_to_member_fk` (`user_id` ASC) VISIBLE,
  CONSTRAINT `board_to_member_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `member` (`user_id`) on delete cascade)

ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;



-- -----------------------------------------------------
-- Table `side`.`memo`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `side`.`memo` ;

CREATE TABLE IF NOT EXISTS `side`.`memo` (
  `memo_no` INT NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(20) NULL DEFAULT NULL,
  `content` VARCHAR(2000) NULL DEFAULT NULL,
  `article_no` INT NULL NULL,
  `register_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`memo_no`),
  INDEX `memo_to_board_fk` (`article_no` ASC) VISIBLE,
  INDEX `memo_to_member_fk_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `memo_to_board_fk`
    FOREIGN KEY (`article_no`)
    REFERENCES `side`.`board` (`article_no`) on delete cascade,
  CONSTRAINT `memo_to_member_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `side`.`member` (`user_id`)
    ON DELETE cascade
    )
ENGINE = InnoDB
AUTO_INCREMENT = 23
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

commit;