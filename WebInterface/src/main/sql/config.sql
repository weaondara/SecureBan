
CREATE TABLE IF NOT EXISTS `sbwi_configuration` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `property` VARCHAR(64) NOT NULL UNIQUE,
  `propertyvalue` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1;
CREATE TABLE IF NOT EXISTS `sbwi_admin` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `player_id` INT(11) NOT NULL UNIQUE,
  `admin` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
)
  ENGINE=InnoDB
  DEFAULT CHARSET=latin1;

INSERT IGNORE INTO `sbwi_configuration`
SET `property` = 'adminpass',
  `propertyvalue` = 'password';

