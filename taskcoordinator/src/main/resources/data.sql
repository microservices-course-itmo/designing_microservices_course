insert into laundry_db.task_coordinator.laundries_state
values (1, 1, 200, 100), (2, 1, 50, 100), (3, 1, 400, 100) ON CONFLICT DO NOTHING;

insert into laundry_db.task_coordinator.orders
values (1, 1, 1, 100, 'APPROVED', 300, 0) ON CONFLICT DO NOTHING;