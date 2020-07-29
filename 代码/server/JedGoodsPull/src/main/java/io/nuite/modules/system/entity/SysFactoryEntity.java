package io.nuite.modules.system.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SysFactoryEntity {
    private Long seq;
    private String accountName;
    private String userName;
    private Date inputTime;

    private Long companySeq;
    private String companyName;

    private Long brandSeq;
    private String brandName;

    private Long roleSeq;
    private String roleName;

    private Long opSeq;
    private Integer userNumbers;
    private Integer createdNumbers;

    private List<Long> menuIdList;
}
