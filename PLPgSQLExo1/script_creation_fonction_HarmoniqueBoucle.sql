CREATE OR REPLACE FUNCTION HarmoniqueBoucle(monEntier integer) RETURNS double precision AS
$$
DECLARE 
maSomme double precision := 0;
BEGIN
-- boucle de 1 à monEntier
IF monEntier <= 0 THEN
	RETURN 0;
ELSE
	<<maBoucle>>
	FOR monIndice IN 1..monEntier LOOP
		maSomme := maSomme + power(monIndice, -1);
	END LOOP maBoucle;
	RETURN maSomme;
END IF;	
END
$$
LANGUAGE plpgsql;
