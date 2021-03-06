ADD JAR /home/cloudera/hfeuxi/hive/hiveudfs-0.0.1-SNAPSHOT.jar;
ADD JAR /home/cloudera/hfeuxi/hive/uadetector-core-0.9.11.jar;
ADD JAR /home/cloudera/hfeuxi/hive/uadetector-json-0.1.11.jar;
ADD JAR /home/cloudera/hfeuxi/hive/uadetector-resources-2013.11.jar;
ADD JAR /home/cloudera/hfeuxi/hive/quality-check-1.1.jar;

CREATE TEMPORARY FUNCTION parseUserAgent as 'org.intel.im.analytics.hiveudfs.userAgentParser'; 

DROP TABLE circuit_web_log_test;
DROP TABLE circuit_web_log_browser_usages_info;
DROP VIEW vw_circuit_web_log_parse_user_agent_data;

CREATE EXTERNAL TABLE circuit_web_log_test (
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
LOCATION '/user/cloudera/hfe/weblog_test/';

CREATE VIEW IF NOT EXISTS vw_circuit_web_log_parse_user_agent_data
AS
SELECT 
    log_date,
    log_time ,
    c_ip ,
    cs_username , 
    cs_host , 
    cs_method , 
    cs_uri_stem , 
    cs_uri_query , 
    sc_status , 
    sc_bytes , 
    cs_version , 
    parseUserAgent (cs_user_agent) usr_agnt,
    cs_referer
FROM circuit_web_log_test;

CREATE TABLE circuit_web_log_browser_usages_info 
AS
SELECT 
     log_date,
    log_time ,
    c_ip ,
    cs_username , 
    cs_host , 
    cs_method , 
    cs_uri_stem , 
    cs_uri_query , 
    sc_status , 
    sc_bytes , 
    cs_version , 
    cs_referer,
usr_agnt.os_type[0] AS os_type, 
usr_agnt.browser_type[0] AS browser_type, 
usr_agnt.version_info[0] AS version_info,
usr_agnt.producer_info[0] AS producer_info,
usr_agnt.producer_url[0] AS producer_url,
usr_agnt.device_type[0] AS device_type 
FROM vw_circuit_web_log_parse_user_agent_data;

    
