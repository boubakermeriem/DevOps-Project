package tn.esprit.spring.dto;

public class RegistrationDTO {
    private Long skierId;
    private Long courseId;
    private String otherField;

    // Getters et Setters
    public Long getSkierId() {
        return skierId;
    }

    public void setSkierId(Long skierId) {
        this.skierId = skierId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getOtherField() {
        return otherField;
    }

    public void setOtherField(String otherField) {
        this.otherField = otherField;
    }
}