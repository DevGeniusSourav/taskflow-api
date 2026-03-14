-- Users Table
CREATE TABLE users
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(100)        NOT NULL,
    email      VARCHAR(150) UNIQUE NOT NULL,
    password   VARCHAR(255)        NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Projects Table
CREATE TABLE projects
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(150) NOT NULL,
    description TEXT,
    owner_id    BIGINT       NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_project_owner
        FOREIGN KEY (owner_id)
            REFERENCES users (id)
);

-- Project-Members Table
CREATE TABLE project_members
(
    project_id BIGINT,
    user_id    BIGINT,

    PRIMARY KEY (project_id, user_id),

    CONSTRAINT fk_project_member_project
        FOREIGN KEY (project_id)
            REFERENCES projects (id),

    CONSTRAINT fk_project_member_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
);

-- Tasks Table
CREATE TABLE tasks
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    title       VARCHAR(200) NOT NULL,
    description TEXT,
    status      VARCHAR(50) DEFAULT 'TODO',
    project_id  BIGINT,
    assigned_to BIGINT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_task_project
        FOREIGN KEY (project_id)
            REFERENCES projects (id),

    CONSTRAINT fk_task_assignee
        FOREIGN KEY (assigned_to)
            REFERENCES users (id)
);

-- Comments Table
CREATE TABLE comments
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    content    TEXT NOT NULL,
    task_id    BIGINT,
    user_id    BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_comment_task
        FOREIGN KEY (task_id)
            REFERENCES tasks (id),

    CONSTRAINT fk_comment_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
);

-- Indexes
CREATE INDEX idx_tasks_project ON tasks(project_id);
CREATE INDEX idx_tasks_users ON tasks(assigned_to);
CREATE INDEX idx_comments_task ON comments(task_id);
CREATE INDEX idx_project_members_user ON project_members(user_id);