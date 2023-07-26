INSERT INTO department (name) VALUES ('IT');
UPDATE department SET parent_id = 1 WHERE id = 1;
INSERT INTO department (name, parent_id) VALUES ('Java', 1);
INSERT INTO department (name, parent_id) VALUES ('Kotlin', 2);

INSERT INTO employee (name, dep_id, email, salary) VALUES ('Oleg Smirnov', 1, 'olegsmirnov@corpmail.com', 1000);
INSERT INTO employee (name, dep_id, email, salary) VALUES ('Mikhail Ivanov', 2, 'mikhailivanov@corpmail.com', 1500);
INSERT INTO employee (name, dep_id, email, salary) VALUES ('Anna Petrova', 3, 'annapetrova@corpmail.com', 1400);
INSERT INTO employee (name, dep_id, email, salary) VALUES ('Elena Volkova', 2, 'elenavolkova@corpmail.com', 1100);
INSERT INTO employee (name, dep_id, email, salary) VALUES ('Petr Afanasiev', 3, 'petrafanasiev@corpmail.com', 1200);

UPDATE department SET head_id = 1 WHERE id = 1;
UPDATE department SET head_id = 2 WHERE id = 2;
UPDATE department SET head_id = 3 WHERE id = 3;