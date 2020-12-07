CREATE OR REPLACE FUNCTION FiboRecursive(monEntier integer) RETURNS integer AS
$$
DECLARE 
fib1 integer := 1;
fib2 integer := 1;
BEGIN
IF monEntier <= 0 THEN
	RETURN 0;
ELSEIF monEntier = 1 THEN
	RETURN fib1;
ELSEIF monEntier = 2 THEN
	RETURN fib2;
ELSE
	RETURN FiboRecursive(monEntier - 1) + FiboRecursive(monEntier - 2);
END IF;
END
$$
LANGUAGE plpgsql;
