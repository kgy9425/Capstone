package teamwoogie.woogie;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "woogie-mobilehub-1845358583-DiseaseRecordDB")

public class DiseaseRecordDBDO {
    private String _userId;
    private Double _date;
    private String _diseaseCode;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "date")
    public Double getDate() {
        return _date;
    }

    public void setDate(final Double _date) {
        this._date = _date;
    }
    @DynamoDBAttribute(attributeName = "disease_code")
    public String getDiseaseCode() {
        return _diseaseCode;
    }

    public void setDiseaseCode(final String _diseaseCode) {
        this._diseaseCode = _diseaseCode;
    }

}
