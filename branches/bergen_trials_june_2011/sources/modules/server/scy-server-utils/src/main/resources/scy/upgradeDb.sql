ALTER TABLE `student_user_details` ADD COLUMN `profilePicture` varchar(55) default NULL;
ALTER TABLE `student_user_details` ADD KEY `profileFileRef` (`profilePicture`);
ALTER TABLE `student_user_details` ADD CONSTRAINT `const_profileFileRef` FOREIGN KEY (`profilePicture`) REFERENCES `fileref` (`primKey`);


ALTER TABLE `groups` ADD COLUMN `type` varchar(55) not null default 'SCYGroup';
ALTER TABLE `groups` ADD COLUMN `pedagogicalPlan_fk` varchar(55) default NULL;
ALTER TABLE `groups` ADD KEY `pedagogicalPlan_fk_key` (`pedagogicalPlan_fk`);
ALTER TABLE `groups` ADD CONSTRAINT `pedagogicalPlan_const` FOREIGN KEY (`pedagogicalPlan_fk`) REFERENCES `pedagogicalPlan` (`primKey`);

ALTER TABLE `teacher_user_details` ADD COLUMN `profilePicture` varchar(55) default NULL;
ALTER TABLE `teacher_user_details` ADD KEY `tprofileFileRef` (`profilePicture`);
ALTER TABLE `teacher_user_details` ADD CONSTRAINT `tconst_profileFileRef` FOREIGN KEY (`profilePicture`) REFERENCES `fileref` (`primKey`);
ALTER TABLE `teacher_user_details` ADD CONSTRAINT `t_const_profile_picture_ref` FOREIGN KEY (`profilePicture`) REFERENCES `fileref` (`primKey`);

ALTER TABLE `user_details` ADD COLUMN `locale` varchar(10) default 'en';