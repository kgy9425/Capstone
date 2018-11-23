package teamwoogie.woogie.model;

public class DiseaseRecordData {
    private String disease_code;
    private String Date;
    private String disease_name;

    public String getDisease_code() {
        return disease_code;
    }


    public String getDate() {
        return Date;
    }

    public String getDisease_name() {
        return disease_name;
    }

    public void setDisease_code(String disease_code) {
        this.disease_code = disease_code;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }
    public void setDisease_name(String disease_name) {
        this.disease_name = disease_name;
    }
}