package com.example.demo.modules.user.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class QualityReport {
    private Long id;
    private String reportNo;
    private String productName;
    private String batchNo;
    private Integer sampleCount;
    private String sampleStatus;
    private Date testDate;
    private Date reportDate;
    private String testStandard;
    private String judgmentRule;
    private List<TestItem> testItems;
    private String finalJudgment;
    private String inspector;
    private String reviewer;
    private String approver;


}