package it.niedermann.nextcloud.deck.persistence.sync.adapters.db.migration;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * <a href="https://github.com/stefan-niedermann/nextcloud-deck/issues/923">Foreign keys don't cascade (Cards stay in the database after deleting an Account)</a>
 */
public class Migration_30_31 extends Migration {

    public Migration_30_31() {
        super(30, 31);
    }

    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database) {
        database.execSQL("CREATE TABLE `AccessControl_tmp` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT, `accountId` INTEGER NOT NULL, `id` INTEGER, `status` INTEGER NOT NULL, `lastModified` INTEGER, `lastModifiedLocal` INTEGER, `etag` TEXT, `type` INTEGER, `boardId` INTEGER, `owner` INTEGER NOT NULL, `permissionEdit` INTEGER NOT NULL, `permissionShare` INTEGER NOT NULL, `permissionManage` INTEGER NOT NULL, `userId` INTEGER, FOREIGN KEY(`boardId`) REFERENCES `Board`(`localId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`accountId`) REFERENCES `Account`(`id`) ON DELETE CASCADE )");
        database.execSQL("DELETE FROM `AccessControl` where accountId not in (select id from `Account`)");
        database.execSQL("INSERT INTO `AccessControl_tmp` select * from `AccessControl`");
        database.execSQL("DROP TABLE `AccessControl`");
        database.execSQL("ALTER TABLE `AccessControl_tmp` RENAME TO `AccessControl`");
        database.execSQL("CREATE INDEX `acl_accId` ON `AccessControl` (`accountId`)");
        database.execSQL("CREATE INDEX `index_AccessControl_boardId` ON `AccessControl` (`boardId`)");
        database.execSQL("CREATE INDEX `index_AccessControl_accountId` ON `AccessControl` (`accountId`)");
        database.execSQL("CREATE INDEX `index_AccessControl_id` ON `AccessControl` (`id`)");
        database.execSQL("CREATE INDEX `index_AccessControl_lastModifiedLocal` ON `AccessControl` (`lastModifiedLocal`)");
        database.execSQL("CREATE UNIQUE INDEX `index_AccessControl_accountId_id` ON `AccessControl` (`accountId`, `id`)");
        database.execSQL("DROP TABLE IF EXISTS `Activity_tmp`");
        database.execSQL("CREATE TABLE `Activity_tmp` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT, `accountId` INTEGER NOT NULL, `id` INTEGER, `status` INTEGER NOT NULL, `lastModified` INTEGER, `lastModifiedLocal` INTEGER, `etag` TEXT, `cardId` INTEGER NOT NULL, `subject` TEXT, `type` INTEGER NOT NULL, FOREIGN KEY(`cardId`) REFERENCES `Card`(`localId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`accountId`) REFERENCES `Account`(`id`) ON DELETE CASCADE )");
        database.execSQL("DELETE FROM `Activity` where accountId not in (select id from `Account`)");
        database.execSQL("UPDATE `Activity` SET `type` = 2 WHERE `type` IS NULL");
        database.execSQL("INSERT INTO `Activity_tmp` select * from `Activity`");
        database.execSQL("DROP TABLE `Activity`");
        database.execSQL("ALTER TABLE `Activity_tmp` RENAME TO `Activity`");
        database.execSQL("CREATE INDEX `activity_accID` ON `Activity` (`accountId`)");
        database.execSQL("CREATE INDEX `activity_cardID` ON `Activity` (`cardId`)");
        database.execSQL("CREATE INDEX `index_Activity_accountId` ON `Activity` (`accountId`)");
        database.execSQL("CREATE INDEX `index_Activity_id` ON `Activity` (`id`)");
        database.execSQL("CREATE INDEX `index_Activity_lastModifiedLocal` ON `Activity` (`lastModifiedLocal`)");
        database.execSQL("CREATE UNIQUE INDEX `index_Activity_accountId_id` ON `Activity` (`accountId`, `id`)");
        database.execSQL("CREATE TABLE `Attachment_tmp` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT, `accountId` INTEGER NOT NULL, `id` INTEGER, `status` INTEGER NOT NULL, `lastModified` INTEGER, `lastModifiedLocal` INTEGER, `etag` TEXT, `cardId` INTEGER NOT NULL, `type` TEXT, `data` TEXT, `createdAt` INTEGER, `createdBy` TEXT, `deletedAt` INTEGER, `filesize` INTEGER NOT NULL, `mimetype` TEXT, `dirname` TEXT, `basename` TEXT, `extension` TEXT, `filename` TEXT, `localPath` TEXT, `fileId` INTEGER, FOREIGN KEY(`cardId`) REFERENCES `Card`(`localId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`accountId`) REFERENCES `Account`(`id`) ON DELETE CASCADE )");
        database.execSQL("DELETE FROM `Attachment` where accountId not in (select id from `Account`)");
        database.execSQL("INSERT INTO `Attachment_tmp` select * from `Attachment`");
        database.execSQL("DROP TABLE `Attachment`");
        database.execSQL("ALTER TABLE `Attachment_tmp` RENAME TO `Attachment`");
        database.execSQL("CREATE INDEX `index_Attachment_cardId` ON `Attachment` (`cardId`)");
        database.execSQL("CREATE INDEX `index_Attachment_accountId` ON `Attachment` (`accountId`)");
        database.execSQL("CREATE INDEX `index_Attachment_id` ON `Attachment` (`id`)");
        database.execSQL("CREATE INDEX `index_Attachment_lastModifiedLocal` ON `Attachment` (`lastModifiedLocal`)");
        database.execSQL("CREATE UNIQUE INDEX `index_Attachment_accountId_id` ON `Attachment` (`accountId`, `id`)");
        database.execSQL("CREATE TABLE `Board_tmp` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT, `accountId` INTEGER NOT NULL, `id` INTEGER, `status` INTEGER NOT NULL, `lastModified` INTEGER, `lastModifiedLocal` INTEGER, `etag` TEXT, `title` TEXT, `ownerId` INTEGER NOT NULL, `color` INTEGER, `archived` INTEGER NOT NULL, `shared` INTEGER NOT NULL, `deletedAt` INTEGER, `permissionRead` INTEGER NOT NULL, `permissionEdit` INTEGER NOT NULL, `permissionManage` INTEGER NOT NULL, `permissionShare` INTEGER NOT NULL, FOREIGN KEY(`ownerId`) REFERENCES `User`(`localId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`accountId`) REFERENCES `Account`(`id`) ON DELETE CASCADE )");
        database.execSQL("DELETE FROM `Board` where accountId not in (select id from `Account`)");
        database.execSQL("INSERT INTO `Board_tmp` select * from `Board`");
        database.execSQL("DROP TABLE `Board`");
        database.execSQL("ALTER TABLE `Board_tmp` RENAME TO `Board`");
        database.execSQL("CREATE INDEX `index_Board_ownerId` ON `Board` (`ownerId`)");
        database.execSQL("CREATE INDEX `index_Board_accountId` ON `Board` (`accountId`)");
        database.execSQL("CREATE INDEX `index_Board_id` ON `Board` (`id`)");
        database.execSQL("CREATE INDEX `index_Board_lastModifiedLocal` ON `Board` (`lastModifiedLocal`)");
        database.execSQL("CREATE UNIQUE INDEX `index_Board_accountId_id` ON `Board` (`accountId`, `id`)");
        database.execSQL("CREATE TABLE `Card_tmp` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT, `accountId` INTEGER NOT NULL, `id` INTEGER, `status` INTEGER NOT NULL, `lastModified` INTEGER, `lastModifiedLocal` INTEGER, `etag` TEXT, `title` TEXT, `description` TEXT, `stackId` INTEGER NOT NULL, `type` TEXT, `createdAt` INTEGER, `deletedAt` INTEGER, `attachmentCount` INTEGER NOT NULL, `userId` INTEGER, `order` INTEGER NOT NULL, `archived` INTEGER NOT NULL, `dueDate` INTEGER, `notified` INTEGER NOT NULL, `overdue` INTEGER NOT NULL, `commentsUnread` INTEGER NOT NULL, FOREIGN KEY(`stackId`) REFERENCES `Stack`(`localId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`accountId`) REFERENCES `Account`(`id`) ON DELETE CASCADE )");
        database.execSQL("DELETE FROM `Card` where accountId not in (select id from `Account`)");
        database.execSQL("INSERT INTO `Card_tmp` select * from `Card`");
        database.execSQL("DROP TABLE `Card`");
        database.execSQL("ALTER TABLE `Card_tmp` RENAME TO `Card`");
        database.execSQL("CREATE INDEX `card_accID` ON `Card` (`accountId`)");
        database.execSQL("CREATE INDEX `index_Card_stackId` ON `Card` (`stackId`)");
        database.execSQL("CREATE INDEX `index_Card_accountId` ON `Card` (`accountId`)");
        database.execSQL("CREATE INDEX `index_Card_id` ON `Card` (`id`)");
        database.execSQL("CREATE INDEX `index_Card_lastModifiedLocal` ON `Card` (`lastModifiedLocal`)");
        database.execSQL("CREATE UNIQUE INDEX `index_Card_accountId_id` ON `Card` (`accountId`, `id`)");
        database.execSQL("CREATE TABLE `DeckComment_tmp` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT, `accountId` INTEGER NOT NULL, `id` INTEGER, `status` INTEGER NOT NULL, `lastModified` INTEGER, `lastModifiedLocal` INTEGER, `etag` TEXT, `objectId` INTEGER, `actorType` TEXT, `creationDateTime` INTEGER, `actorId` TEXT, `actorDisplayName` TEXT, `message` TEXT, `parentId` INTEGER, FOREIGN KEY(`objectId`) REFERENCES `Card`(`localId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`parentId`) REFERENCES `DeckComment`(`localId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`accountId`) REFERENCES `Account`(`id`) ON DELETE CASCADE )");
        database.execSQL("DELETE FROM `DeckComment` where accountId not in (select id from `Account`)");
        database.execSQL("INSERT INTO `DeckComment_tmp` select * from `DeckComment`");
        database.execSQL("DROP TABLE `DeckComment`");
        database.execSQL("ALTER TABLE `DeckComment_tmp` RENAME TO `DeckComment`");
        database.execSQL("CREATE INDEX `comment_accID` ON `DeckComment` (`accountId`)");
        database.execSQL("CREATE INDEX `index_DeckComment_objectId` ON `DeckComment` (`objectId`)");
        database.execSQL("CREATE INDEX `idx_comment_parentID` ON `DeckComment` (`parentId`)");
        database.execSQL("CREATE INDEX `index_DeckComment_accountId` ON `DeckComment` (`accountId`)");
        database.execSQL("CREATE INDEX `index_DeckComment_id` ON `DeckComment` (`id`)");
        database.execSQL("CREATE INDEX `index_DeckComment_lastModifiedLocal` ON `DeckComment` (`lastModifiedLocal`)");
        database.execSQL("CREATE UNIQUE INDEX `index_DeckComment_accountId_id` ON `DeckComment` (`accountId`, `id`)");
        database.execSQL("CREATE TABLE `Label_tmp` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT, `accountId` INTEGER NOT NULL, `id` INTEGER, `status` INTEGER NOT NULL, `lastModified` INTEGER, `lastModifiedLocal` INTEGER, `etag` TEXT, `title` TEXT, `color` INTEGER NOT NULL DEFAULT 0, `boardId` INTEGER NOT NULL, FOREIGN KEY(`boardId`) REFERENCES `Board`(`localId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`accountId`) REFERENCES `Account`(`id`) ON DELETE CASCADE )");
        database.execSQL("DELETE FROM `Label` where accountId not in (select id from `Account`)");
        database.execSQL("INSERT INTO `Label_tmp` select * from `Label`");
        database.execSQL("DROP TABLE `Label`");
        database.execSQL("ALTER TABLE `Label_tmp` RENAME TO `Label`");
        database.execSQL("CREATE INDEX `index_Label_boardId` ON `Label` (`boardId`)");
        database.execSQL("CREATE UNIQUE INDEX `idx_label_title_unique` ON `Label` (`boardId`, `title`)");
        database.execSQL("CREATE INDEX `index_Label_accountId` ON `Label` (`accountId`)");
        database.execSQL("CREATE INDEX `index_Label_id` ON `Label` (`id`)");
        database.execSQL("CREATE INDEX `index_Label_lastModifiedLocal` ON `Label` (`lastModifiedLocal`)");
        database.execSQL("CREATE UNIQUE INDEX `index_Label_accountId_id` ON `Label` (`accountId`, `id`)");
        database.execSQL("CREATE TABLE `OcsProject_tmp` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT, `accountId` INTEGER NOT NULL, `id` INTEGER, `status` INTEGER NOT NULL, `lastModified` INTEGER, `lastModifiedLocal` INTEGER, `etag` TEXT, `name` TEXT NOT NULL, FOREIGN KEY(`accountId`) REFERENCES `Account`(`id`) ON DELETE CASCADE )");
        database.execSQL("DELETE FROM `OcsProject` where accountId not in (select id from `Account`)");
        database.execSQL("INSERT INTO `OcsProject_tmp` select * from `OcsProject`");
        database.execSQL("DROP TABLE `OcsProject`");
        database.execSQL("ALTER TABLE `OcsProject_tmp` RENAME TO `OcsProject`");
        database.execSQL("CREATE INDEX `index_project_accID` ON `OcsProject` (`accountId`)");
        database.execSQL("CREATE INDEX `index_OcsProject_accountId` ON `OcsProject` (`accountId`)");
        database.execSQL("CREATE INDEX `index_OcsProject_id` ON `OcsProject` (`id`)");
        database.execSQL("CREATE INDEX `index_OcsProject_lastModifiedLocal` ON `OcsProject` (`lastModifiedLocal`)");
        database.execSQL("CREATE UNIQUE INDEX `index_OcsProject_accountId_id` ON `OcsProject` (`accountId`, `id`)");
        database.execSQL("CREATE TABLE `OcsProjectResource_tmp` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT, `accountId` INTEGER NOT NULL, `id` INTEGER, `status` INTEGER NOT NULL, `lastModified` INTEGER, `lastModifiedLocal` INTEGER, `etag` TEXT, `type` TEXT, `name` TEXT, `link` TEXT, `path` TEXT, `iconUrl` TEXT, `mimetype` TEXT, `previewAvailable` INTEGER, `idString` TEXT, `projectId` INTEGER NOT NULL, FOREIGN KEY(`projectId`) REFERENCES `OcsProject`(`localId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`accountId`) REFERENCES `Account`(`id`) ON DELETE CASCADE )");
        database.execSQL("DELETE FROM `OcsProjectResource` where accountId not in (select id from `Account`)");
        database.execSQL("INSERT INTO `OcsProjectResource_tmp` select * from `OcsProjectResource`");
        database.execSQL("DROP TABLE `OcsProjectResource`");
        database.execSQL("ALTER TABLE `OcsProjectResource_tmp` RENAME TO `OcsProjectResource`");
        database.execSQL("CREATE INDEX `index_OcsProjectResource_id` ON `OcsProjectResource` (`id`)");
        database.execSQL("CREATE INDEX `index_OcsProjectResource_lastModifiedLocal` ON `OcsProjectResource` (`lastModifiedLocal`)");
        database.execSQL("CREATE UNIQUE INDEX `index_OcsProjectResource_accountId_id` ON `OcsProjectResource` (`accountId`, `id`, `idString`, `projectId`)");
        database.execSQL("CREATE INDEX `index_projectResource_accID` ON `OcsProjectResource` (`accountId`)");
        database.execSQL("CREATE INDEX `index_projectResource_projectId` ON `OcsProjectResource` (`projectId`)");
        database.execSQL("CREATE TABLE `Stack_tmp` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT, `accountId` INTEGER NOT NULL, `id` INTEGER, `status` INTEGER NOT NULL, `lastModified` INTEGER, `lastModifiedLocal` INTEGER, `etag` TEXT, `title` TEXT, `boardId` INTEGER NOT NULL, `deletedAt` INTEGER, `order` INTEGER NOT NULL, FOREIGN KEY(`boardId`) REFERENCES `Board`(`localId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`accountId`) REFERENCES `Account`(`id`) ON DELETE CASCADE )");
        database.execSQL("DELETE FROM `Stack` where accountId not in (select id from `Account`)");
        database.execSQL("INSERT INTO `Stack_tmp` select * from `Stack`");
        database.execSQL("DROP TABLE `Stack`");
        database.execSQL("ALTER TABLE `Stack_tmp` RENAME TO `Stack`");
        database.execSQL("CREATE INDEX `index_Stack_boardId` ON `Stack` (`boardId`)");
        database.execSQL("CREATE INDEX `index_Stack_accountId` ON `Stack` (`accountId`)");
        database.execSQL("CREATE INDEX `index_Stack_id` ON `Stack` (`id`)");
        database.execSQL("CREATE INDEX `index_Stack_lastModifiedLocal` ON `Stack` (`lastModifiedLocal`)");
        database.execSQL("CREATE UNIQUE INDEX `index_Stack_accountId_id` ON `Stack` (`accountId`, `id`)");
        database.execSQL("CREATE TABLE `User_tmp` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT, `accountId` INTEGER NOT NULL, `id` INTEGER, `status` INTEGER NOT NULL, `lastModified` INTEGER, `lastModifiedLocal` INTEGER, `etag` TEXT, `primaryKey` TEXT, `uid` TEXT, `displayname` TEXT, FOREIGN KEY(`accountId`) REFERENCES `Account`(`id`) ON DELETE CASCADE )");
        database.execSQL("DELETE FROM `User` where accountId not in (select id from `Account`)");
        database.execSQL("INSERT INTO `User_tmp` select * from `User`");
        database.execSQL("DROP TABLE `User`");
        database.execSQL("ALTER TABLE `User_tmp` RENAME TO `User`");
        database.execSQL("CREATE INDEX `user_uid` ON `User` (`uid`)");
        database.execSQL("CREATE INDEX `index_User_accountId` ON `User` (`accountId`)");
        database.execSQL("CREATE INDEX `index_User_id` ON `User` (`id`)");
        database.execSQL("CREATE INDEX `index_User_lastModifiedLocal` ON `User` (`lastModifiedLocal`)");
        database.execSQL("CREATE UNIQUE INDEX `index_User_accountId_id` ON `User` (`accountId`, `id`)");
    }
}
