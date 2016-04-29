import boto3;
import botocore;
import boto;

#if not boto.config.get('s3', 'use-sigv4'):
#    boto.config.add_section('s3')
#    boto.config.set('s3', 'use-sigv4', 'True');

print "|-->printing available buckets<--| \n";

s3 = boto3.resource('s3');

for bucket in s3.buckets.all():
	print('Bucket:'+bucket.name);

print "|-----------------------------------------------|\n";

myprojectbucket = 'myprojectBucket';
print "||--->keys in bucket "+myprojectbucket +"\n";

bucket = s3.Bucket(myprojectbucket);

#for each one object in bucket print its key
for key in bucket.objects.all():
	print(key)
		

print "|------------------------------|\n";


