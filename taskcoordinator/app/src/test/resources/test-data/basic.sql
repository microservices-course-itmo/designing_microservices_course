insert into task_coordinator_test.laundries_state
values (1, 1, 200, 100),
       (2, 1, 250, 150);

insert into task_coordinator_test.orders
values (3, 1, 1, 200, 'SUBMITTED', 200, 0),
       (4, 2, 1, 250, 'SUBMITTED', 250, 0),
       (1, 1, 1, 100, 'RESERVED', 300, 0),
       (2, 2, 1, 150, 'RESERVED', 400, 0);

insert into task_coordinator_test.details
values (1, 10, 100, 3),
       (2, 10, 100, 3),
       (3, 10, 200, 4),
       (4, 10, 50, 4),
       (5, 10, 100, 1),
       (6, 10, 100, 2),
       (7, 10, 50, 2)

