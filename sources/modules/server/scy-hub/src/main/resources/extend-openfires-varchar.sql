USE openfire;
ALTER TABLE ofExtComponentConf MODIFY COLUMN subdomain VARCHAR(255) NOT NULL; 
ALTER TABLE ofExtComponentConf MODIFY COLUMN secret VARCHAR(255) DEFAULT NULL; 
ALTER TABLE ofGroup MODIFY COLUMN groupName VARCHAR(255) NOT NULL; 
ALTER TABLE ofGroupProp MODIFY COLUMN groupName VARCHAR(255) NOT NULL; 
ALTER TABLE ofGroupProp MODIFY COLUMN name VARCHAR(255) NOT NULL; 
ALTER TABLE ofGroupUser MODIFY COLUMN groupName VARCHAR(255) NOT NULL; 
ALTER TABLE ofGroupUser MODIFY COLUMN username VARCHAR(255) NOT NULL; 
ALTER TABLE ofMucMember MODIFY COLUMN firstName VARCHAR(255) DEFAULT NULL; 
ALTER TABLE ofMucMember MODIFY COLUMN lastName VARCHAR(255) DEFAULT NULL; 
ALTER TABLE ofMucMember MODIFY COLUMN url VARCHAR(255) DEFAULT NULL; 
ALTER TABLE ofMucMember MODIFY COLUMN email VARCHAR(255) DEFAULT NULL; 
ALTER TABLE ofMucMember MODIFY COLUMN faqentry VARCHAR(255) DEFAULT NULL; 
ALTER TABLE ofMucRoom MODIFY COLUMN name VARCHAR(255) NOT NULL; 
ALTER TABLE ofMucRoom MODIFY COLUMN roomPassword VARCHAR(255) DEFAULT NULL; 
ALTER TABLE ofMucRoom MODIFY COLUMN subject VARCHAR(255) DEFAULT NULL; 
ALTER TABLE ofMucRoomProp MODIFY COLUMN name VARCHAR(255) NOT NULL; 
ALTER TABLE ofMucServiceProp MODIFY COLUMN name VARCHAR(255) NOT NULL; 
ALTER TABLE ofOffline MODIFY COLUMN username VARCHAR(255) NOT NULL; 
ALTER TABLE ofPresence MODIFY COLUMN username VARCHAR(255) NOT NULL; 
ALTER TABLE ofPrivacyList MODIFY COLUMN username VARCHAR(255) NOT NULL; 
ALTER TABLE ofPrivacyList MODIFY COLUMN name VARCHAR(255) NOT NULL; 
ALTER TABLE ofPrivate MODIFY COLUMN username VARCHAR(255) NOT NULL; 
ALTER TABLE ofPrivate MODIFY COLUMN name VARCHAR(255) NOT NULL; 
ALTER TABLE ofPrivate MODIFY COLUMN namespace VARCHAR(255) NOT NULL; 
ALTER TABLE ofProperty MODIFY COLUMN name VARCHAR(255) NOT NULL; 
ALTER TABLE ofPubsubAffiliation MODIFY COLUMN serviceID VARCHAR(255) NOT NULL; 
ALTER TABLE ofPubsubAffiliation MODIFY COLUMN nodeID VARCHAR(255) NOT NULL; 
ALTER TABLE ofPubsubAffiliation MODIFY COLUMN affiliation VARCHAR(255) NOT NULL; 
ALTER TABLE ofPubsubDefaultConf MODIFY COLUMN serviceID VARCHAR(255) NOT NULL; 
ALTER TABLE ofPubsubDefaultConf MODIFY COLUMN publisherModel VARCHAR(255) NOT NULL; 
ALTER TABLE ofPubsubDefaultConf MODIFY COLUMN accessModel VARCHAR(255) NOT NULL; 
ALTER TABLE ofPubsubDefaultConf MODIFY COLUMN replyPolicy VARCHAR(255) DEFAULT NULL; 
ALTER TABLE ofPubsubDefaultConf MODIFY COLUMN associationPolicy VARCHAR(255) NOT NULL; 
ALTER TABLE ofPubsubItem MODIFY COLUMN serviceID VARCHAR(255) NOT NULL; 
ALTER TABLE ofPubsubItem MODIFY COLUMN nodeID VARCHAR(255) NOT NULL; 
ALTER TABLE ofPubsubItem MODIFY COLUMN id VARCHAR(255) NOT NULL; 
ALTER TABLE ofPubsubNode MODIFY COLUMN serviceID VARCHAR(255) NOT NULL; 
ALTER TABLE ofPubsubNode MODIFY COLUMN nodeID VARCHAR(255) NOT NULL; 
ALTER TABLE ofPubsubNode MODIFY COLUMN parent VARCHAR(255) DEFAULT NULL; 
ALTER TABLE ofPubsubNode MODIFY COLUMN publisherModel VARCHAR(255) NOT NULL; 
ALTER TABLE ofPubsubNode MODIFY COLUMN accessModel VARCHAR(255) NOT NULL; 
ALTER TABLE ofPubsubNode MODIFY COLUMN payloadType VARCHAR(255) DEFAULT NULL; 
ALTER TABLE ofPubsubNode MODIFY COLUMN bodyXSLT VARCHAR(255) DEFAULT NULL; 
ALTER TABLE ofPubsubNode MODIFY COLUMN dataformXSLT VARCHAR(255) DEFAULT NULL; 
ALTER TABLE ofPubsubNode MODIFY COLUMN name VARCHAR(255) DEFAULT NULL; 
ALTER TABLE ofPubsubNode MODIFY COLUMN replyPolicy VARCHAR(255) DEFAULT NULL; 
ALTER TABLE ofPubsubNode MODIFY COLUMN associationPolicy VARCHAR(255) DEFAULT NULL; 
ALTER TABLE ofPubsubNodeGroups MODIFY COLUMN serviceID VARCHAR(255) NOT NULL; 
ALTER TABLE ofPubsubNodeGroups MODIFY COLUMN nodeID VARCHAR(255) NOT NULL; 
ALTER TABLE ofPubsubNodeGroups MODIFY COLUMN rosterGroup VARCHAR(255) NOT NULL; 
ALTER TABLE ofPubsubNodeJIDs MODIFY COLUMN serviceID VARCHAR(255) NOT NULL; 
ALTER TABLE ofPubsubNodeJIDs MODIFY COLUMN nodeID VARCHAR(255) NOT NULL; 
ALTER TABLE ofPubsubNodeJIDs MODIFY COLUMN associationType VARCHAR(255) NOT NULL; 
ALTER TABLE ofPubsubSubscription MODIFY COLUMN serviceID VARCHAR(255) NOT NULL; 
ALTER TABLE ofPubsubSubscription MODIFY COLUMN nodeID VARCHAR(255) NOT NULL; 
ALTER TABLE ofPubsubSubscription MODIFY COLUMN id VARCHAR(255) NOT NULL; 
ALTER TABLE ofPubsubSubscription MODIFY COLUMN state VARCHAR(255) NOT NULL; 
ALTER TABLE ofPubsubSubscription MODIFY COLUMN showValues VARCHAR(255) DEFAULT NULL; 
ALTER TABLE ofPubsubSubscription MODIFY COLUMN subscriptionType VARCHAR(255) NOT NULL; 
ALTER TABLE ofPubsubSubscription MODIFY COLUMN keyword VARCHAR(255) DEFAULT NULL; 
ALTER TABLE ofRemoteServerConf MODIFY COLUMN permission VARCHAR(255) NOT NULL; 
ALTER TABLE ofRoster MODIFY COLUMN username VARCHAR(255) NOT NULL; 
ALTER TABLE ofSASLAuthorized MODIFY COLUMN username VARCHAR(255) NOT NULL; 
ALTER TABLE ofSecurityAuditLog MODIFY COLUMN username VARCHAR(255) NOT NULL; 
ALTER TABLE ofUser MODIFY COLUMN username VARCHAR(255) NOT NULL; 
ALTER TABLE ofUser MODIFY COLUMN plainPassword VARCHAR(255) DEFAULT NULL; 
ALTER TABLE ofUser MODIFY COLUMN name VARCHAR(255) DEFAULT NULL; 
ALTER TABLE ofUser MODIFY COLUMN email VARCHAR(255) DEFAULT NULL; 
ALTER TABLE ofUserFlag MODIFY COLUMN username VARCHAR(255) NOT NULL; 
ALTER TABLE ofUserFlag MODIFY COLUMN name VARCHAR(255) NOT NULL; 
ALTER TABLE ofUserProp MODIFY COLUMN username VARCHAR(255) NOT NULL; 
ALTER TABLE ofUserProp MODIFY COLUMN name VARCHAR(255) NOT NULL; 
ALTER TABLE ofVCard MODIFY COLUMN username VARCHAR(255) NOT NULL; 
ALTER TABLE ofVersion MODIFY COLUMN name VARCHAR(255) NOT NULL; 

