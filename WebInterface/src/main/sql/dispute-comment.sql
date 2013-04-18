CREATE TABLE IF NOT EXISTS `sbwi_dispute_comment` (
  `id`        INT(11)    NOT NULL AUTO_INCREMENT,
  `ban_id`    INT(11)    NOT NULL,
  `player_id` INT(11)    NOT NULL,
  `date`      INT(11)    NOT NULL,
  `comment`   TEXT(1024) NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1;

ALTER TABLE `sbwi_dispute_comment`
CHANGE `comment` `comment` LONGTEXT NOT NULL;