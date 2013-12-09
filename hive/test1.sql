ADD JAR /home/cloudera/hfeuxi/hive/hiveudfs-0.0.1-SNAPSHOT.jar;
CREATE TEMPORARY FUNCTION parseUserAgent as 'org.intel.im.analytics.hiveudfs.userAgentParser'; 

DROP TABLE circuit_web_log_test;

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

CREATE VIEW IF NOT EXISTS vw_web_log_test
AS
SELECT 
    cs_user_agent ,
    parseUserAgent (cs_user_agent)
FROM circuit_web_log_test;
    
