db = db.getSiblingDB('ticket_booking');

db.createCollection('users');
db.createCollection('events');
db.createCollection('tickets');