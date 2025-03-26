DELETE FROM bookings;
DELETE FROM items;
DELETE FROM item_requests;
DELETE FROM users;

DELETE FROM item_requests WHERE EXISTS (SELECT * FROM information_schema.tables WHERE table_name = 'item_requests');