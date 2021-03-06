ADD JAR /home/cloudera/hfeuxi/hive/hiveudfs-0.0.1-SNAPSHOT.jar;
ADD JAR /home/cloudera/hfeuxi/hive/uadetector-core-0.9.11.jar;
ADD JAR /home/cloudera/hfeuxi/hive/uadetector-json-0.1.11.jar;
ADD JAR /home/cloudera/hfeuxi/hive/uadetector-resources-2013.11.jar;
ADD JAR /home/cloudera/hfeuxi/hive/quality-check-1.1.jar;

CREATE TEMPORARY FUNCTION parseUserAgent as 'org.intel.im.analytics.hiveudfs.userAgentParser'; 

DROP TABLE t_dbg;
DROP VIEW vw_tst;
DROP TABLE tst;

CREATE EXTERNAL TABLE t_dbg (
    id STRING,
    strt_time BIGINT,
    end_time BIGINT,
    usr_agnt STRING
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' 
LOCATION '/user/cloudera/hfe/dbg/';

CREATE VIEW vw_tst 
AS
SELECT
parseUserAgent(usr_agnt) usr_agnt
FROM t_dbg;

CREATE TABLE tst 
AS
SELECT 
usr_agnt.os_type[0] AS os_type, 
usr_agnt.browser_type[0] AS browser_type, 
usr_agnt.version_info[0] AS version_info,
usr_agnt.producer_info[0] AS producer_info,
usr_agnt.producer_url[0] AS producer_url,
usr_agnt.device_type[0] AS device_type 
FROM vw_tst;


