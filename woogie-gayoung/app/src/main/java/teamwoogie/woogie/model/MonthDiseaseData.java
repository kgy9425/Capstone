package teamwoogie.woogie.model;

public class MonthDiseaseData {
    private String month;
    private String disease_code;
    private String disease_precaution;
    private String disease_name;

    public String getDisease_code() {
        return disease_code;
    }

    public String getMonth() {
        return month;
    }

    public String getDisease_precaution() {
        return disease_precaution;
    }

    public String getDisease_name() {
        return disease_name;
    }

    public void setDisease_code(String disease_code) {
        this.disease_code = disease_code;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setDisease_precaution(String disease_precaution) {
        this.disease_precaution = disease_precaution;
    }
    public void setDisease_name(String disease_name) {
        this.disease_name = disease_name;
    }
}