ADD JAR /home/cloudera/workspace/hfeuxi/hive/hiveudfs-0.0.1-SNAPSHOT.jar;
ADD JAR /home/cloudera/workspace/hfeuxi/hive/uadetector-core-0.9.11.jar;
ADD JAR /home/cloudera/workspace/hfeuxi/hive/uadetector-json-0.1.11.jar;
ADD JAR /home/cloudera/workspace/hfeuxi/hive/uadetector-resources-2013.11.jar;
ADD JAR /home/cloudera/workspace/hfeuxi/hive/quality-check-1.1.jar;

CREATE TEMPORARY FUNCTION parseUserAgent as 'org.intel.im.analytics.hiveudfs.userAgentParser'; 

DROP TABLE circuit_web_log;
DROP TABLE cs_user_agent_dtl; 
DROP VIEW  v_cs_user_agent_info;
DROP VIEW  v_distinct_cs_user_agent;
DROP VIEW  v_distinct_brower_cnt_by_user;
DROP TABLE distinct_browser_cnt_info;

CREATE EXTERNAL TABLE circuit_web_log (
    log_date STRING,
    log_time STRING,
    c_ip STRING,
    cs_username STRING, 
    cs_host STRING, 
    cs_method STRING, 
    cs_uri_stem STRING, 
    cs_uri_query STRING, 
    sc_status STRING, 
    sc_bytes STRING, 
    cs_version STRING, 
    cs_user_agent STRING,
    cs_referer  STRING                                    
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ' ' 
LOCATION '/user/cloudera/weblog/';

CREATE VIEW v_distinct_cs_user_agent 
AS
SELECT cs_user_agent
FROM circuit_web_log
WHERE log_date NOT LIKE '#%'
GROUP BY cs_user_agent;

CREATE VIEW  v_cs_user_agent_info 
AS
SELECT 	cs_user_agent,
	parseUserAgent (cs_user_agent) usr_agnt_dtl
FROM v_distinct_cs_user_agent
;

CREATE TABLE cs_user_agent_dtl
AS
SELECT
	cs_user_agent,
	usr_agnt_dtl.os_type[0] AS os_type, 
	usr_agnt_dtl.browser_type[0] AS browser_type, 
	usr_agnt_dtl.version_info[0] AS version_info,
	usr_agnt_dtl.producer_info[0] AS producer_info,
	usr_agnt_dtl.producer_url[0] AS producer_url,
	usr_agnt_dtl.device_type[0] AS device_type 
FROM    v_cs_user_agent_info;


CREATE VIEW v_distinct_brower_cnt_by_user
AS
SELECT	cs_username,
	os_type,
        browser_type,
	device_type
FROM 	circuit_web_log a INNER JOIN
	cs_user_agent_dtl b ON
        a.cs_user_agent = b.cs_user_agent
GROUP BY
	cs_username,
	os_type,
        browser_type,
	device_type;


CREATE TABLE distinct_browser_cnt_info
AS
SELECT 	os_type,
        browser_type,
	device_type,
        count(1) count_info
FROM v_distinct_brower_cnt_by_user
GROUP BY 
	os_type,
        browser_type,
	device_type;






        
	
