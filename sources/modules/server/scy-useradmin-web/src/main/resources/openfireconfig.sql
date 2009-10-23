delete from ofproperty where name like 'jdbcAuthProvider.passwordSQL';
delete from ofproperty where name like 'jdbcUserProvider.loadUserSQL';
delete from ofproperty where name like 'jdbcUserProvider.emailField';
delete from ofproperty where name like 'jdbcUserProvider.nameField';
delete from ofproperty where name like 'jdbcUserProvider.searchSQL';
delete from ofproperty where name like 'jdbcUserProvider.userCountSQL';
delete from ofproperty where name like 'jdbcUserProvider.usernameField';
delete from ofproperty where name like 'jdbcUserProvider.allUsersSQL';
delete from ofproperty where name like 'provider.admin.className';
delete from ofproperty where name like 'provider.auth.className';
delete from ofproperty where name like 'provider.group.className';
delete from ofproperty where name like 'provider.lockout.className';
delete from ofproperty where name like 'provider.securityAudit.className';
delete from ofproperty where name like 'provider.user.className';
delete from ofproperty where name like 'provider.vcard.className';


insert into ofproperty (name, propValue) values ('jdbcAuthProvider.passwordSQL', 'SELECT username FROM user_details where username like ?');
insert into ofproperty (name, propValue) values ('jdbcUserProvider.loadUserSQL', 'SELECT username,email_address FROM user_details WHERE username=?');
insert into ofproperty (name, propValue) values ('jdbcUserProvider.emailField', 'email_address');
insert into ofproperty (name, propValue) values ('jdbcUserProvider.nameField', 'username');
insert into ofproperty (name, propValue) values ('jdbcUserProvider.searchSQL', 'SELECT username FROM user_details WHERE ');
insert into ofproperty (name, propValue) values ('jdbcUserProvider.userCountSQL', 'SELECT COUNT(*) FROM user_details');
insert into ofproperty (name, propValue) values ('jdbcUserProvider.usernameField', 'username');
insert into ofproperty (name, propValue) values ('jdbcUserProvider.allUsersSQL', 'SELECT username FROM user_details ');
insert into ofproperty (name, propValue) values ('provider.admin.className', 'org.jivesoftware.openfire.admin.DefaultAdminProvider');
insert into ofproperty (name, propValue) values ('provider.auth.className', 'org.jivesoftware.openfire.auth.JDBCAuthProvider');
insert into ofproperty (name, propValue) values ('provider.group.className', 'org.jivesoftware.openfire.group.DefaultGroupProvider');
insert into ofproperty (name, propValue) values ('provider.lockout.className', 'org.jivesoftware.openfire.lockout.DefaultLockOutProvider');
insert into ofproperty (name, propValue) values ('provider.securityAudit.className', 'org.jivesoftware.openfire.security.DefaultSecurityAuditProvider');
insert into ofproperty (name, propValue) values ('provider.user.className', 'org.jivesoftware.openfire.user.JDBCUserProvider');
insert into ofproperty (name, propValue) values ('provider.vcard.className', 'org.jivesoftware.openfire.vcard.DefaultVCardProvider');

delete from ofextcomponentconf where subdomain like 'scyhub';
insert into ofextcomponentconf (subdomain, wildcard, secret, permission) values ('scyhub', 0, 'java', 'allowed');