import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.auth.CognitoCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;

import com.protectsoft.mypackage.Constants;

import java.io.File;

//and this is the class implementing the awsInterface
//again this helps me accomplish my goals more easy as i call only methods
//
public class AwsClientAccess implements AwsClient {

    private CognitoCredentialsProvider credentialsProvider;
    private TransferUtility transferUtility;
    private AmazonS3 amazonS3;
    private AmazonS3Client amazonS3Client;
    private AmazonDynamoDBClient amazonDynamoDBClient;
    private DynamoDBMapper mapper;

    public AwsClientAccess() {
    }

    @Override
    public  CognitoCachingCredentialsProvider getCredentialsProvider(Context context) {

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                "us-east-1:my amazon Identity Pool ID", // my amazon Identity Pool ID
                Regions.US_EAST_1 // Region
        );
        this.credentialsProvider = credentialsProvider;
        return credentialsProvider;

    }


    @Override
    public TransferUtility transferUtility(AmazonS3 amazonS3, Context context) {

        TransferUtility transferUtility = new TransferUtility(amazonS3,context);
        this.transferUtility = transferUtility;

        return  transferUtility;
    }


    @Override
    public TransferUtility getTransferUtility() {
        return transferUtility;
    }


    @Override
    public TransferObserver getTransferObserverUpload(String pathName, File file,ObjectMetadata objectMetadata) {

        return transferUtility.upload(Constants.BUCKET_NAME,pathName,file,objectMetadata);
    }


    @Override
    public TransferObserver getTransferObserverUpload(String pathName, File file) {
        return transferUtility.upload(Constants.BUCKET_NAME,pathName,file);
    }


    @Override
    public AmazonS3 amazonS3Client(CognitoCredentialsProvider cognitoCredentialsProvider) {

        AmazonS3 amazonS3 = new AmazonS3Client(cognitoCredentialsProvider);
        amazonS3.setRegion(com.amazonaws.regions.Region.getRegion(Regions.EU_CENTRAL_1));
        this.amazonS3 = amazonS3;

        return amazonS3;
    }


    @Override
    public AmazonS3 getAmazonS3() {
        return amazonS3;
    }


    @Override
    public AmazonS3Client getAmazonS3Client() {
        return amazonS3Client;
    }


    @Override
    public AmazonS3Client setAmazonS3Client(CognitoCredentialsProvider credentialsProvider) {

        AmazonS3Client amazonS3Client = new AmazonS3Client(credentialsProvider);
        amazonS3Client.setRegion(Region.getRegion(Regions.EU_CENTRAL_1));
        this.amazonS3Client = amazonS3Client;

        return amazonS3Client;
    }


    @Override
    public AmazonDynamoDBClient dynamoDbClient(CognitoCredentialsProvider credentialsProvider) {

        AmazonDynamoDBClient amazonDynamoDBClient = new AmazonDynamoDBClient(credentialsProvider);
        amazonDynamoDBClient.setRegion(Region.getRegion(Regions.EU_CENTRAL_1));
        this.amazonDynamoDBClient = amazonDynamoDBClient;

        return amazonDynamoDBClient;
    }


    @Override
    public DynamoDBMapper mapper(AmazonDynamoDBClient amazonDynamoDBClient) {

        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDBClient);
        this.mapper = mapper;

        return mapper;
    }


    @Override
    public <T extends Object> T load(Class<T> clazz,Object hashkey) {

        return mapper.load(clazz,hashkey);
    }



}