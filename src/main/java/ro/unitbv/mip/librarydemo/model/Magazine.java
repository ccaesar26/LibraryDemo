package ro.unitbv.mip.librarydemo.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("MAGAZINE")
public class Magazine extends Publication {

    private Integer issueNumber;
    private String month;

    protected Magazine() {}

    public Magazine(String title, Integer issueNumber, String month) {
        super(title);
        this.issueNumber = issueNumber;
        this.month = month;
    }

    // Getters
    public Integer getIssueNumber() { return issueNumber; }
    public void setIssueNumber(Integer issueNumber) { this.issueNumber = issueNumber; }
    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }
}
