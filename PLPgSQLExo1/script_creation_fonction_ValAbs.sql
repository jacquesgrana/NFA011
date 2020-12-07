CREATE FUNCTION ValAbs(monEntier integer) RETURNS integer AS
$$
BEGIN
IF monEntier > 0
	THEN
	RETURN monEntier;
	ELSE
	RETURN -1*monEntier;
END IF;
END
$$
LANGUAGE plpgsql;
