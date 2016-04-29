import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.auth.CognitoCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.File;

//i created this interface witch helps me intergrate aws api easier in my app
public interface AwsClient {

     AmazonDynamoDBClient dynamoDbClient(CognitoCredentialsProvider credentialsProvider);

     DynamoDBMapper mapper(AmazonDynamoDBClient amazonDynamoDBClient);

     <T extends Object> T load(Class<T> clazz,Object hashkey);

     CognitoCachingCredentialsProvider getCredentialsProvider(Context context);

     AmazonS3 amazonS3Client(CognitoCredentialsProvider cognitoCredentialsProvider);

     AmazonS3 getAmazonS3();

     AmazonS3Client getAmazonS3Client();

     AmazonS3Client setAmazonS3Client(CognitoCredentialsProvider credentialsProvider);

     TransferUtility transferUtility(AmazonS3 amazonS3,Context context);

     TransferUtility getTransferUtility();

     TransferObserver getTransferObserverUpload(String pathName,File file,ObjectMetadata objectMetadata);

     TransferObserver getTransferObserverUpload(String pathName,File file);


}