delete from ofProperty where name like 'admin.authorizedJIDs';
delete from ofProperty where name like 'jdbcAuthProvider.passwordSQL';
delete from ofProperty where name like 'jdbcUserProvider.loadUserSQL';
delete from ofProperty where name like 'jdbcUserProvider.emailField';
delete from ofProperty where name like 'jdbcUserProvider.nameField';
delete from ofProperty where name like 'jdbcUserProvider.searchSQL';
delete from ofProperty where name like 'jdbcUserProvider.userCountSQL';
delete from ofProperty where name like 'jdbcUserProvider.usernameField';
delete from ofProperty where name like 'jdbcUserProvider.allUsersSQL';
delete from ofProperty where name like 'provider.admin.className';
delete from ofProperty where name like 'provider.auth.className';
delete from ofProperty where name like 'provider.group.className';
delete from ofProperty where name like 'provider.lockout.className';
delete from ofProperty where name like 'provider.securityAudit.className';
delete from ofProperty where name like 'provider.user.className';
delete from ofProperty where name like 'provider.vcard.className';

delete from ofProperty where name like 'xmpp.component.defaultSecret';
delete from ofProperty where name like 'xmpp.component.socket.active';
delete from ofProperty where name like 'xmpp.session.conflict-limit';


insert into ofProperty (name, propValue) values ('admin.authorizedJIDs', 'scy@collide.info,admin@collide.info');
insert into ofProperty (name, propValue) values ('jdbcAuthProvider.passwordSQL', 'SELECT password FROM user_details where username like ?');
insert into ofProperty (name, propValue) values ('jdbcUserProvider.loadUserSQL', 'SELECT username,email_address FROM user_details WHERE username=?');
insert into ofProperty (name, propValue) values ('jdbcUserProvider.emailField', 'email_address');
insert into ofProperty (name, propValue) values ('jdbcUserProvider.nameField', 'username');
insert into ofProperty (name, propValue) values ('jdbcUserProvider.searchSQL', 'SELECT username FROM user_details WHERE ');
insert into ofProperty (name, propValue) values ('jdbcUserProvider.userCountSQL', 'SELECT COUNT(*) FROM user_details');
insert into ofProperty (name, propValue) values ('jdbcUserProvider.usernameField', 'username');
insert into ofProperty (name, propValue) values ('jdbcUserProvider.allUsersSQL', 'SELECT username FROM user_details ');
insert into ofProperty (name, propValue) values ('provider.admin.className', 'org.jivesoftware.openfire.admin.DefaultAdminProvider');
insert into ofProperty (name, propValue) values ('provider.auth.className', 'org.jivesoftware.openfire.auth.JDBCAuthProvider');
insert into ofProperty (name, propValue) values ('provider.group.className', 'org.jivesoftware.openfire.group.DefaultGroupProvider');
insert into ofProperty (name, propValue) values ('provider.lockout.className', 'org.jivesoftware.openfire.lockout.DefaultLockOutProvider');
insert into ofProperty (name, propValue) values ('provider.securityAudit.className', 'org.jivesoftware.openfire.security.DefaultSecurityAuditProvider');
insert into ofProperty (name, propValue) values ('provider.user.className', 'org.jivesoftware.openfire.user.JDBCUserProvider');
insert into ofProperty (name, propValue) values ('provider.vcard.className', 'org.jivesoftware.openfire.vcard.DefaultVCardProvider');

insert into ofProperty (name, propValue) values ('xmpp.component.defaultSecret', 'java');
insert into ofProperty (name, propValue) values ('xmpp.component.socket.active', 'true');
insert into ofProperty (name, propValue) values ('xmpp.session.conflict-limit', 0);

delete from ofExtComponentConf where subdomain like 'scyhub';
insert into ofExtComponentConf (subdomain, wildcard, secret, permission) values ('scyhub', 0, 'java', 'allowed');
delete from ofExtComponentConf where subdomain like 'sqlspaces';
insert into ofExtComponentConf (subdomain, wildcard, secret, permission) values ('sqlspaces', 0, 'sqlspaces', 'allowed');