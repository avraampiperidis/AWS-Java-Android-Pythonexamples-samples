
//this is a test activity class
//covers basic functionality for aws api in android.
//
//lets create a activity sample class
//
//!REMEMBER NOT TO EXECUTE AWS API METHODS ON THE UI THREAD OR YOU WILL GET NetworkOnMainThreadException or something simmilar.
//
//!NOTE imports are missing here eg.for context,R,Bundle
//i included only aws imports
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferType;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;

public class MyActivity extends Activity {
	
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.main_layout);
		
		
		//test in other thread!
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				//aws s3 bucket upload test
				awsBucketUploadS3Test();
				
				//aws s3 bucket download test
				//im calling this method from awsBucketUploadS3Test() so i can pass the key 
				//awsBucketS3DownloadTest(key)
				
				//aws dynamoDb test
				awsDynamoDbTest();
				
			}
		}).start();
		
	}
	
	
	
	private void awsBucketUploadS3Test() {
		
		AwsClient awsClient = new AwsClientAccess();
		
		// Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = awsClient.getCredentialsProvider(getApplicationContext());
		
		AmazonS3 s3Client = awsClient.amazonS3Client(credentialsProvider);
		
		String identityId = credentialsProvider.getIdentityId();
		
		TransferUtility transferUtilityUpload = awsClient.transferUtility(s3Client, getApplicationContext());
		
		//lets put some metadata in out object
		ObjectMetadata objectMetadata = new ObjectMetadata();
		
		Map<String,String> userMetaData = new HashMap<>();
		userMetaData.put('date','11-11-2015');
        objectMetadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
        objectMetadata.setUserMetadata(userMetaData);
		
		//now lets say we have a file we want to upload
		File f = new File("/path/myPicture.jpg");
		
		//lets give it a unique link key in our object
		String linkKey = identityId+"/"+f.getName();
		
		//lets start out upload
		TransferObserver observer = s3Client.getTransferObserverUpload(linkKey,f,objectMetadata);
		
		//set out transfer listeners
		observer.setTransferListener(new TransferListener() {
			
			@Override
			public void onStateChanged(int id, TransferState state) {
				
			}
			
			@Override
			public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
				int percantage = (int) (bytesCurrent / bytesTotal * 100);
				if(percantage == 100) {
					//upload completed
					
					//now lets download the object
					//we gona need the key
					//witch is linkKey
					//remember to execute on other thread
					//onProgressChanged is joining with main UI
					new Thread(new Runnable() {
						@Override
						public void run() {
							awsBucketS3DownloadTest(linkKey);
						}
					}).start();
					
				}
			}
			
			@Override
			public void onError(int id, Exception ex) {
				
			}
			
		});
		
		
	}
	
	
	
	
	private void awsBucketS3DownloadTest(String key) {
		
		AwsClient awsClient = new AwsClientAccess();
		
		CognitoCachingCredentialsProvider credentialsProvider = awsClient.getCredentialsProvider(getApplicationContext());
		
		AmazonS3Client amazonS3Client = awsClient.setAmazonS3Client(credentialsProvider);
		
		TransferUtility transferUtilityDownload = awsClient.transferUtility(amazonS3Client, getApplicationContext());
		
		File fileToSaveDownload = new File("/mypath/mydownloadedPicture.jpg");
		
		TransferObserver observer = transferUtilityDownload.download(Constants.BUCKET_NAME, key, fileToSaveDownload);
		
		observer.setTransferListener(new TransferListener() {
			
			@Override
			public void onStateChanged(int id, TransferState state) {
				
			}
			
			@Override
			public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
				int percantage = (int) (bytesCurrent / bytesTotal * 100);
				if(percantage == 100) {
					//download completed
				}
			}
			
			@Override
			public void onError(int id, Exception ex) {
				
			}
			
		}
		
	}
	
	
	
	
	private void awsDynamoDbTest() {
		
		AwsClient awsClient = new AwsClientAccess();
		
		CognitoCachingCredentialsProvider credentialsProvider = awsClient.getCredentialsProvider(getApplicationContext());
		
		AmazonDynamoDBClient amazonDynamoDBClient = awsClient.dynamoDbClient(credentialsProvider);
		
		DynamoDBMapper mapper = awsClient.mapper(amazonDynamoDBClient);
		
		
		DynamoUserModel dynamoUserModel = awsClient.load(DynamoUserModel.class,"dynamousersEmailstoredinDynamoDb@gmail.com");
		
		if(dynamoUserModel != null) {
			
			//dynamoUserModel object is filled with attributes defined in DynamoUserModel class
			//as long as there was a previus save record with this user
			//treat dynamoUserModel as normal object
			
			//so lets change a attribute and save it back to dynamoDb
			dynamoUserModel.deleteValueFromMultimap("","");
			dynamoUserModel.changeName('ex new name');
			
			//and save it to dynamodb
			mapper.save(dynamoUserModel);
			
		} else {
			//if its null lets create a new DynamoUserModel object and save it
			dynamoUserModel.setEmail("email@gmail.com");
			//more setters...
			
			//and
			mapper.save(dynamoUserModel);
			//new dynamoDb record added
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}