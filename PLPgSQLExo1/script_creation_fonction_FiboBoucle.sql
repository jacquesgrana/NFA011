CREATE OR REPLACE FUNCTION FiboBoucle(monEntier integer) RETURNS integer AS
$$
DECLARE 
fib1 integer := 1;
fib2 integer := 1;
maSomme integer := 0;
BEGIN
IF monEntier <= 0 THEN
	RETURN 0;
ELSEIF monEntier = 1 THEN
	RETURN fib1;
ELSEIF monEntier = 2 THEN
	RETURN fib2;
ELSE
	<<maBoucle>>
	FOR compteur IN 3..monEntier LOOP
		maSomme := fib2 + fib1;
		fib1 := fib2;
		fib2 := maSomme;
	END LOOP maBoucle;
	RETURN maSomme;
END IF;
END
$$
LANGUAGE plpgsql;
