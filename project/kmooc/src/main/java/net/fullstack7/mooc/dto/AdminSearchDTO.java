package net.fullstack7.mooc.dto;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import net.fullstack7.mooc.domain.Member;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminSearchDTO extends PageDTO<Member> {
    @Builder.Default
    private int memberType = -1;
    @Builder.Default
    private int isApproved = -1;
    private String status;
    @Size(max = 50, message = "50자 이내 검색 가능")
    private String searchId;
    @Builder.Default
    private String typeSelect = "t";

    @Override
    public void initialize() {
        super.initialize();
        if(this.typeSelect.equals("s"))
            this.setQueryString(URLEncoder.encode("typeSelect=" + this.typeSelect + "&status=" + this.status + "&searchId=" + this.searchId + "&memberType=" + memberType, StandardCharsets.UTF_8));
        else
            this.setQueryString(URLEncoder.encode("typeSelect=" + this.typeSelect + "&status=" + this.status + "&searchId=" + this.searchId + "&isApproved=" + memberType, StandardCharsets.UTF_8));
    }

    public void setSearchId(String searchId) {
        if(searchId != null && searchId.length() > 50) {
            this.searchId = searchId.substring(0, 50);
        }
        else {
            this.searchId = searchId;
        }
    }

    public void setMemberType(int memberType) {
        if(memberType < -1 || memberType > 1)
            this.memberType = -1;
        else this.memberType = memberType;
    }

    public void setMemberType(long memberType) {
        if(memberType >= -1 && memberType <= 1)
            this.memberType = (int)memberType;
        else this.memberType = -1;
    }

    public void setMemberType(String memberType) {
        if(memberType.matches("^\\d+$") && memberType.length() <= 1) {
            if(Integer.parseInt(memberType) >= -1 && Integer.parseInt(memberType) <= 1)
                this.memberType = Integer.parseInt(memberType);
            else this.memberType = -1;
        } else this.memberType = -1;
    }

    public void setIsApproved(int isApproved) {
        if(isApproved < -1 || isApproved > 1)
            this.isApproved = -1;
        else this.isApproved = isApproved;
    }
    public void setIsApproved(long isApproved) {
        if(isApproved >= -1 && isApproved <= 1)
            this.isApproved = (int)isApproved;
        else this.isApproved = -1;
    }

    public void setIsApproved(String isApproved) {
        if(isApproved.matches("^\\d+$") && isApproved.length() <= 1) {
            if(Integer.parseInt(isApproved) >= -1 && Integer.parseInt(isApproved) <= 1)
                this.isApproved = Integer.parseInt(isApproved);
            else this.isApproved = -1;
        } else this.isApproved = -1;
    }

    public void setStatus(String status) {
        if(status.length() > 20) {
            this.status = status.substring(0, 20);
        }
        else {
            this.status = status;
        }
    }

    public void setTypeSelect(String typeSelect) {
        if(typeSelect.length() > 20) {
            this.typeSelect = typeSelect.substring(0, 20);
        }
        else {
            this.typeSelect = typeSelect;
        }
    }
}
