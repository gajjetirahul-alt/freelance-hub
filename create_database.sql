-- ============================================================
-- FreelanceHub Database Setup Script
-- Run this in MySQL Workbench
-- ============================================================

-- Step 1: Create the database
CREATE DATABASE IF NOT EXISTS freelancehub
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- Step 2: Select the database
USE freelancehub;

-- Step 3: Verify it was created
SELECT 'Database freelancehub created successfully!' AS Status;

-- ✅ All tables will be auto-created by Spring Boot (JPA/Hibernate)
--    when you run the backend with ddl-auto=update
--
-- After running the backend, the following tables will be created:
--   - users          (id, name, email, password, role, created_at, bio, skills, location)
--   - projects       (id, title, description, budget, deadline, status, client_id, created_at)
--   - proposals      (id, message, budget, status, freelancer_id, project_id, created_at)
--   - messages       (id, content, sender_id, receiver_id, created_at)
--   - notifications  (id, message, is_read, user_id, created_at)
