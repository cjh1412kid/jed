package io.nuite.modules.system.service;

import org.springframework.web.multipart.MultipartFile;

public interface ExcelGoodsService {
    void importExcel(Integer companySeq, Integer brandSeq, MultipartFile excelFile) throws Exception;
}
