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
LOCATION '/user/hfe/weblog/';