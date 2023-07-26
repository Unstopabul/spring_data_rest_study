CREATE TABLE department
(
    id        identity primary key,
    name      varchar(255) unique not null,
    parent_id int,
    FOREIGN KEY (parent_id) REFERENCES department (id),
    head_id   int
);

CREATE TABLE employee
(
    id     identity primary key,
    name   varchar(255)        not null,
    dep_id int,
    FOREIGN KEY (dep_id) REFERENCES department (id),
    email  varchar(255) unique not null,
    salary decimal(13, 2)      not null
);

ALTER TABLE department
    ADD FOREIGN KEY (head_id) REFERENCES employee (id);