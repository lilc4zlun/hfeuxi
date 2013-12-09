CREATE VIEW vw_tst 
AS
SELECT
parseUserAgent(strt_time, strt_time) usr_agnt
FROM t_dbg;
CREATE VIEW vw_test3 as select usr_agnt.starttime[0] AS starttime, usr_agnt.endtime[0] AS endtime, usr_agnt.viewedtime[0] AS viewedtime from vw_tst;
select viewedtime, count(1)  from vw_test3 group by viewedtime;
