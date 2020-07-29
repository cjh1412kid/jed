package io.nuite.modules.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

public class CompanyBrand {
    private String companyName;
    private String companyDescript;
    private String companyAddress;
    private Long companySeq;
    private String brandName;
    private String brandDescript;
    private String brandImage;
    private Long brandSeq;

    @JsonIgnore
    private MultipartFile brandImageFile;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyDescript() {
        return companyDescript;
    }

    public void setCompanyDescript(String companyDescript) {
        this.companyDescript = companyDescript;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public Long getCompanySeq() {
        return companySeq;
    }

    public void setCompanySeq(Long companySeq) {
        this.companySeq = companySeq;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandImage() {
        return brandImage;
    }

    public void setBrandImage(String brandImage) {
        this.brandImage = brandImage;
    }

    public String getBrandDescript() {
        return brandDescript;
    }

    public void setBrandDescript(String brandDescript) {
        this.brandDescript = brandDescript;
    }

    public Long getBrandSeq() {
        return brandSeq;
    }

    public void setBrandSeq(Long brandSeq) {
        this.brandSeq = brandSeq;
    }

    public MultipartFile getBrandImageFile() {
        return brandImageFile;
    }

    public void setBrandImageFile(MultipartFile brandImageFile) {
        this.brandImageFile = brandImageFile;
    }
}
