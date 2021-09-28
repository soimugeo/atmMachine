create table cash (
                      billValue int primary key,
                      numberOfAvailableBills long,
                      PRIMARY KEY (billValue)
);

insert into CASH(billValue, numberOfAvailableBills)
VALUES (1, 100),
       (5, 100),
       (10, 100),
       (50, 50),
       (100, 50);