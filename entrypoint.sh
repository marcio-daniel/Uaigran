
wait_time=15s
password=1q2w3e4r!#%

sleep $wait_time


/opt/mssql-tools/bin/sqlcmd -S 0.0.0.0 -U sa -P $password -i ./setup.sql