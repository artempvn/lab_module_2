CREATE TABLE IF NOT EXISTS `gift_certificates` (
  `id` BIGINT(5) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `description` VARCHAR(1000) NULL,
  `price` DECIMAL(8,2) NULL,
  `duration` INT NULL,
  `create_date` DATETIME NULL,
  `last_update_date` DATETIME NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) );

CREATE TABLE IF NOT EXISTS `tag` (
  `id` BIGINT(5) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
UNIQUE INDEX `id_UNIQUE1` (`id` ASC) );

CREATE TABLE IF NOT EXISTS `certificates_tags` (
  `tag_id` BIGINT(5) NOT NULL,
  `certificate_id` BIGINT(5) NOT NULL,
  CONSTRAINT `certificate_fk`
    FOREIGN KEY (`certificate_id`)
    REFERENCES `gift_certificates` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `tag_fk`
    FOREIGN KEY (`tag_id`)
    REFERENCES `tag` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

INSERT INTO tag(name) VALUES ('first tag');
INSERT INTO tag(name) VALUES ('second tag');
INSERT INTO tag(name) VALUES ('third tag');
INSERT INTO tag(name) VALUES ('fourth tag');

INSERT INTO gift_certificates(name,description,price,duration, create_date,last_update_date) VALUES ('first certificate','first description',1.33,5,'2020-12-25T15:0:0','2020-12-30T16:30:0');
INSERT INTO gift_certificates(name,description,price,duration, create_date,last_update_date) VALUES ('second certificate','second description',2.33,10,'2020-12-25T15:0:0','2021-1-5T14:0:0');

INSERT INTO certificates_tags(tag_id,certificate_id) VALUES (1,1);
INSERT INTO certificates_tags(tag_id,certificate_id) VALUES (2,1);
INSERT INTO certificates_tags(tag_id,certificate_id) VALUES (2,2);