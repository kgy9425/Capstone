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

@DynamoDBTable(tableName = "woogie-mobilehub-1845358583-DiseaseDB")

public class DiseaseDBDO {
    private String _diseaseCode;
    private String _diseaseName;
    private String _diseasePrecaution;

    @DynamoDBHashKey(attributeName = "disease_code")
    @DynamoDBAttribute(attributeName = "disease_code")
    public String getDiseaseCode() {
        return _diseaseCode;
    }

    public void setDiseaseCode(final String _diseaseCode) {
        this._diseaseCode = _diseaseCode;
    }
    @DynamoDBAttribute(attributeName = "disease_name")
    public String getDiseaseName() {
        return _diseaseName;
    }

    public void setDiseaseName(final String _diseaseName) {
        this._diseaseName = _diseaseName;
    }
    @DynamoDBAttribute(attributeName = "disease_precaution")
    public String getDiseasePrecaution() {
        return _diseasePrecaution;
    }

    public void setDiseasePrecaution(final String _diseasePrecaution) {
        this._diseasePrecaution = _diseasePrecaution;
    }

}
