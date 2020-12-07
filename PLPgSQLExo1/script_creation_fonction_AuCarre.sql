CREATE FUNCTION AuCarre(monReel double precision) RETURNS double precision AS
$$
BEGIN
RETURN monReel*monReel;
END
$$
LANGUAGE plpgsql;

