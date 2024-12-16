package net.fullstack7.mooc.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import net.fullstack7.mooc.domain.Course;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CourseSearchDTO extends PageDTO<CourseResponseDTO> {
    @Builder.Default
    private int isCreditBank = -1;
    @Setter
    @Builder.Default
    private int subjectId = -1;
    private String status;
    @Builder.Default
    private int institutionId = -1;

    public void setStatus(String status) {
        if (status != null && status.length() > 20) {
            this.status = status.substring(0, 20);
        }
        else this.status = status;
    }

    public void setIsCreditBank(int isCreditBank) {
        if(isCreditBank < -1 || isCreditBank > 1)
            this.isCreditBank = -1;
        else this.isCreditBank = isCreditBank;
    }
    public void setIsCreditBank(long isCreditBank) {
        if(isCreditBank >= -1 && isCreditBank <= 1)
            this.isCreditBank = (int)isCreditBank;
        else this.isCreditBank = -1;
    }

    public void setIsCreditBank(String isCreditBank) {
        if(isCreditBank.matches("^\\d+$") && isCreditBank.length() == 1) {
            if(Integer.parseInt(isCreditBank) >= -1 && Integer.parseInt(isCreditBank) <= 1)
                this.isCreditBank = Integer.parseInt(isCreditBank);
            else this.isCreditBank = -1;
        } else this.isCreditBank = -1;
    }

    public void setSubjectId(int subjectId) {

            this.subjectId = subjectId;

    }

    public void setSubjectId(long subjectId) {
        if(subjectId >= -1 && subjectId <= 20000)
            this.subjectId = (int)subjectId;
        else this.subjectId = -1;
    }

    public void setSubjectId(String subjectId) {
        if(subjectId.matches("^\\d+$") && subjectId.length() <= 5 && !subjectId.isBlank()) {
            this.subjectId = Integer.parseInt(subjectId);
        } else this.subjectId = -1;
    }

    public void setInstitutionId(int institutionId) {
        this.institutionId = institutionId;

    }

    public void setInstitutionId(long institutionId) {
        if(institutionId >= -1 && institutionId <= 1)
            this.institutionId = (int)institutionId;
        else this.institutionId = -1;
    }

    public void setInstitutionId(String institutionId) {
        if(institutionId.matches("^\\d+$") && institutionId.length() <= 5) {
            this.institutionId = Integer.parseInt(institutionId);
        } else this.institutionId = -1;
    }

    @Override
    public void initialize() {
        super.initialize();

        StringBuilder sb = new StringBuilder();
        sb.append("searchField="+ getSearchField() +"&searchValue="+getSearchValue()+"&sortField="+getSortField());
        if(isCreditBank >= 0) {
            sb.append("&isCreditBank="+isCreditBank);
        }
        if(subjectId >= 0) {
            sb.append("&subjectId="+subjectId);
        }
        if(status != null) {
            sb.append("&status="+URLEncoder.encode(status, StandardCharsets.UTF_8));
        }
        if(institutionId >= 0) {
            sb.append("&institutionId="+institutionId);
        }

        this.setQueryString(URLEncoder.encode(sb.toString(), StandardCharsets.UTF_8));
    }
}
