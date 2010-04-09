ALTER TABLE `student_user_details` ADD COLUMN `profilePicture` varchar(55) default NULL;
ALTER TABLE `student_user_details` ADD KEY `profileFileRef` (`profilePicture`);
ALTER TABLE `student_user_details` ADD CONSTRAINT `const_profileFileRef` FOREIGN KEY (`profilePicture`) REFERENCES `fileref` (`primKey`);
