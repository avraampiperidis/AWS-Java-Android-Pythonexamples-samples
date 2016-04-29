package camtogetherclean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Users")
public class DynamoUserModel {
	
	private String email;

    private Map<String,List<String>> multimap;
    private String deviceid;
    
    private int date;


    @DynamoDBHashKey(attributeName = "email")
    public String getEmail() {
        return email;
    }


    public void setEmail(String em) {
        email = em;
    }

    
    @DynamoDBIgnore
    public void clearMultiMap() {
        if(multimap != null && !multimap.isEmpty()) {
            multimap.clear();
        }
    }


    @DynamoDBAttribute(attributeName = "hashmap")
    public Map<String,List<String>> getMultimap() {

        return multimap;

    }


    @DynamoDBIgnore
    public Map<String,List<String>> getFixedMultimap() {

        //code removed

        return null;
    }


    public void setMultimap(Map<String,List<String>> mltmap) {
        multimap = mltmap;
    }


    @DynamoDBAttribute(attributeName = "deviceid")
    public String getDeviceId() {
        return deviceid;
    }


    public void setDeviceId(String id) {
        deviceid = id;
    }




    @DynamoDBAttribute(attributeName = "emails")
    public String getAllEmails() {

        //code removed
        return null;
    }


    @DynamoDBIgnore
    public boolean isEmailExist(String email,String username) {
        //code removed
        return false;
    }

    @DynamoDBIgnore
    public List<String> getAllKeysForEmail(String email,String username) {
        //code removed
        return null;
    }

    public void setAllEmails(String emails) {

    }

    public void setMultimapEmpty(boolean b) {

    }

    public boolean isMultimapEmpty() {
        if(multimap == null) {
            return true;
        }

        return multimap.isEmpty();
    }


    public int getMultimapSize() {
        if(multimap == null) {
            return 0;
        }
        return multimap.size();
    }

    public void setMultimapSize(int s) {

    }


    public boolean isKeyExists(String key) {
        if(multimap != null && !multimap.isEmpty()) {

            Iterator<Entry<String, List<String>>> iterator = multimap.entrySet().iterator();

            while(iterator.hasNext()) {
                Map.Entry pair = (Map.Entry)iterator.next();
                if(key.equals(pair.getKey())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setKeyExists(boolean b) {

    }

    public List<String> getAllKeys() {
        if(multimap != null && !multimap.isEmpty()) {
            List<String> keys = new ArrayList<String>();
            Iterator<Entry<String, List<String>>> iterator = multimap.entrySet().iterator();

            while(iterator.hasNext()) {
                Map.Entry pair = (Map.Entry)iterator.next();
                keys.add(pair.getKey().toString());
            }
            return keys;
        }
        return null;
    }

    public void setAllKeys(List<String> emails) {

    }

    public List<List<String>> getAllValues() {

        if(multimap != null && !multimap.isEmpty()) {

            List<List<String>> values = new ArrayList<List<String>>();

            Iterator iterator = multimap.entrySet().iterator();

            while(iterator.hasNext()) {

                Map.Entry pair = (Map.Entry)iterator.next();
                List<String> node = new ArrayList<String>();
                node = (List<String>)pair.getValue();
                values.add(node);

            }

            return values;
        }
        return null;
    }



    public void setAllValues(List<List<String>> values) {

    }



    public List<String> getKeyValues(String key) {

        if(multimap != null && !multimap.isEmpty()) {

            List<String> values = new ArrayList<String>();

            Iterator<Entry<String, List<String>>> iterator = multimap.entrySet().iterator();

            while(iterator.hasNext()) {

                Map.Entry pair = (Map.Entry)iterator.next();
                if(key.equals(pair.getKey().toString())) {
                    values = (List<String>)pair.getValue();
                    break;
                }
            }

            return values;
        }
        return null;
    }


    public void setKeyValues(List<String> val) {

    }


    public void insertValueToMultimap(String key,String value) {
        //code removed
    }




    public void deleteValueFromMultimap(String key,String value) {

        //code removed

    }


    @Override
    public String toString() {
        return getClass().getName() + "[email="+ email + ",multimap=" + multimap.toString() + "]";
    }


    @DynamoDBAttribute(attributeName = "date")
	public int getDate() {
		return date;
	}


	public void setDate(int date) {
		this.date = date;
	}
	

}
