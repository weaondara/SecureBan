CREATE TABLE IF NOT EXISTS `sbwi_screenshot` (
  `id`         INT(11)     NOT NULL AUTO_INCREMENT,
  `ban_id`     INT(11)     NOT NULL,
  `staff_name` VARCHAR(50) NOT NULL,
  `date`       INT(11)     NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1;
