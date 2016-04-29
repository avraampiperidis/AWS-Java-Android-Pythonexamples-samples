package camtogetherclean;

import java.util.List;
import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;


/**
 * 
 * @author abraham
 *
 * this tool should get all emails from mysql and for one of that emails
 * query the dynamodb and perform clean operation in old left records in hashmap
 * 
 * is intent to run from crontab once per 3 or 4 days
 * the operation should be slow at least 4 seconds sleep between dynamodb connections/queries
 */
public class Main {
	
	public static Logger log = Logger.getLogger("Main");
	
	private static AmazonDynamoDBClient dynamoDB;
	
	private static final String tableName = "Users";

	public static void main(String[] args) {
		
		log.info("main started");
		
		log.info("getting all users from mysql...");
		long start_time = System.nanoTime();
		
		List<User> users = UsersUtils.getAllUsers();
		//for Jtest
		//List<User> users = UsersUtils.getAllUsersDummy();
		
		long end_time = System.nanoTime();
		double difference = (end_time - start_time)/1e6;
		log.info("users fetched in: "+difference+" ,ms");
		
		
		try {
		init();
		} catch(Exception ex) {
			
		}
		
		
		DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
		
		for(int i =0; i < users.size(); i++) {
			
			log.info("Checking dynamoUser:"+users.get(i).getEmail()+":"+users.get(i).getUsername());
			
			//try to light up dynamodb load
			
			
			DynamoUserModel user = mapper.load(DynamoUserModel.class,users.get(i).getEmail());
			
			
			//user used the service so dynamodb record created
			if(user != null) {
				
				log.info("user not null..");
				//get users last online interaction date
				int date = user.getDate();
				int currentDate = getAmericaDenverTimeZoneDate();
				
				int dif = currentDate - date;
				
				if(currentDate > 2 && (dif >= 3)) {
					
					log.info("cleanning user...");
					
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					user.clearMultiMap();
					
					user.setDate(currentDate);
					
					mapper.save(user);
					
				} else {
					//do nothing
				}
				
				
			}
			
		}

	}
	
	
	
	
	
	
	
	private static int getAmericaDenverTimeZoneDate() {
		DateTimeZone denverTimeZone = DateTimeZone.forID( "America/Denver" );
        DateTime denverDateTime =  new DateTime( denverTimeZone );
        int dateAsString = denverDateTime.getDayOfYear();
        
        return dateAsString;
	}
	
	
	
	
	private static void init() throws Exception {
        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located at
         * (C:\\Users\\user\\.aws\\credentials).
         */
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (C:\\Users\\user\\.aws\\credentials), and is in valid format.",
                    e);
        }
        dynamoDB = new AmazonDynamoDBClient(credentials);
        Region euCentral = Region.getRegion(Regions.EU_CENTRAL_1);
        dynamoDB.setRegion(euCentral);
        
    }
	
	

}
