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

@DynamoDBTable(tableName = "woogie-mobilehub-1845358583-MonthlyDiseaseDB")

public class MonthlyDiseaseDBDO {
    private Double _month;
    private String _diseaseCode;

    @DynamoDBHashKey(attributeName = "month")
    @DynamoDBAttribute(attributeName = "month")
    public Double getMonth() {
        return _month;
    }

    public void setMonth(final Double _month) {
        this._month = _month;
    }
    @DynamoDBAttribute(attributeName = "disease_code")
    public String getDiseaseCode() {
        return _diseaseCode;
    }

    public void setDiseaseCode(final String _diseaseCode) {
        this._diseaseCode = _diseaseCode;
    }

}
