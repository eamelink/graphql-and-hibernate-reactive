INSERT into Person (id, name) VALUES (1, 'Erik');
INSERT into Person (id, name) VALUES (2, 'Willem Jan');

INSERT into Foo (id, personId, foo) VALUES(1, 1, 'Eriks Foo');
INSERT into Foo (id, personId, foo) VALUES(2, 1, 'Eriks 2nd Foo');
INSERT into Foo (id, personId, foo) VALUES(3, 1, 'Eriks 3rd Foo');

INSERT into Foo (id, personId, foo) VALUES(4, 2, 'Willems Foo');
INSERT into Foo (id, personId, foo) VALUES(5, 2, 'Willems 2nd Foo');