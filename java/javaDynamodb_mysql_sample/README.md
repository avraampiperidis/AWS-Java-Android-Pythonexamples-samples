this lib was used in background in production server for project app as a cron dayly routine. An auto mysql/dynamodb maintenance lib. its job was to fetch all users from mysql, and for all the users that had to logged in the app more than 3 days(they where at least 3 days inactive), a cleanup maintenance was done in mysql and dynamodb for clean old document data.
 This is old! i changed this lib and created python script for the job.
 see-> AWS examples/samples / python 