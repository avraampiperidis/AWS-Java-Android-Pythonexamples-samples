#!/usr/bin/python

#this script was used in background in production server for project app as a cron dayly routine. 
#An auto mysql/dynamodb maintenance script. 
#its job was to fetch all users from mysql, 
#and for all the users that had to logged in the app more than 3 days(they where at least 3 days inactive),
#a cleanup maintenance was done in mysql and dynamodb for clean old document data.
#
#I had done this script first in java for this job,
#and put the jar as a cron job.
#but then i realized that python was the perfect candidate for this kind of job.
#
from __future__ import print_function # Python 2/3 compatibility
import boto3
import json
import decimal
import datetime
import MySQLdb

from time import sleep
from boto3.dynamodb.conditions import Key, Attr


#Helper class to convert a DynamoDB item to JSON.
class DecimalEncoder(json.JSONEncoder):
    def default(self, o):
        if isinstance(o, decimal.Decimal):
            if o % 1 > 0:
                return float(o)
            else:
                return int(o)
        return super(DecimalEncoder, self).default(o)


#User class
class User(object):

  def __init__(self,username,email,userDay,id):
	self.username = username
	self.email = email
	self.userDay = userDay
 	self.id = id	

#init empty user array
users = []

#mysql connection
db = MySQLdb.connect("localhost","root","password","database")

cursor = db.cursor()

cursor.execute("SELECT username,email,day,id FROM users");

data = cursor.fetchall()

#for each user from mysql query results
#create a user object and append it in users array
for row in data:
  username = row[0]
  email = row[1]
  day = row[2]
  id = row[3]

  user = User(username,email,day,id)
  users.append(user)

db.close()



#just print the users from users array filled previusly
for user in users:
  print (" ")
  print ("|--------------------------------------------|| ")
  print  ("|-->username: %s" % user.username)
  print  ("|-->email: %s" % user.email)
  print  ("|-->day: " , user.userDay)
  print  ("|-->id: %d" % user.id)
  print("||------------------------------------------------||")
  print(" ")
#end of print

#get the data 
#need this for later comparison
daynow = datetime.datetime.now().timetuple().tm_yday

#get table Users from dynamodb 
dynamodb = boto3.resource('dynamodb',endpoint_url="https://dynamodb.eu-central-1.amazonaws.com")
table = dynamodb.Table("Users")


#this method cleans the user document contents in the dynamodb
#and updating mysql the table users the day is set to now
#users dynamo table primary key index is email
def updateDynamoUser(email):
  response = table.update_item(

    Key={
      'email':email
    },
    UpdateExpression="set  hashmap = :m, emails = :e, allKeys = :k, allValues= :v,multimapSize = :p, multimapEmpty = :o",
    ExpressionAttributeValues={
      ':m': {},
      ':e': ' ',
      ':k': [],
      ':v': [[]],
      ':p': 0,
      ':o': 1
    }
  )

  print("mysql update:...")
  #update mysql users day
  db = MySQLdb.connect("localhost","root","password","database")
  cursor = db.cursor()
  cursor.execute("""
    UPDATE users
    SET day=%s
    WHERE email=%s 
  """,(daynow,email))
  db.commit()



#check who's users day is older than 3 days from users array
#and if true pass the users email to updateDynamoUser method
for user in users:
  print("n",user.userDay)
  if user.userDay is not None:
    print("checking user:",user.email)    
    userday = user.userDay
    dif = daynow - userday
   
    if(day > 2 and dif >= 3):
	sleep(1.00)
	print("updating dynamo user...",user.email)
	updateDynamoUser(user.email)







	
	   



