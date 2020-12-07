CREATE OR REPLACE FUNCTION HarmoniqueRecursive(monEntier integer) RETURNS double precision AS
$$
DECLARE 
maSomme double precision := 0;
BEGIN
	IF monEntier <= 1 THEN
	RETURN 1;
	ELSE
	RETURN power(monEntier, -1) + HarmoniqueRecursive(monEntier - 1);
	END IF;	
END
$$
LANGUAGE plpgsql;
