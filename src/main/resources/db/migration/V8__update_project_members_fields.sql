ALTER TABLE project_members DROP FOREIGN KEY fk_project_member_project;

ALTER TABLE project_members DROP FOREIGN KEY fk_project_member_user;

ALTER TABLE project_members DROP PRIMARY KEY;

ALTER TABLE project_members
    ADD COLUMN id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY;

ALTER TABLE project_members
    ADD CONSTRAINT fk_project_member_project
        FOREIGN KEY (project_id) REFERENCES projects (id);

ALTER TABLE project_members
    ADD CONSTRAINT fk_project_member_user
        FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE project_members
    ADD CONSTRAINT uk_project_user UNIQUE (project_id, user_id);

ALTER TABLE project_members
    ADD COLUMN deleted_at TIMESTAMP;

ALTER TABLE project_members
    ADD COLUMN deleted_by BIGINT;

ALTER TABLE project_members
    ADD COLUMN is_deleted BOOLEAN DEFAULT FALSE;
