package com.nuite.controller;

import com.nuite.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ErpController {

    @Autowired
    private StockService stockService;

    @GetMapping("/data")
    public Object getData() {
        return "222";
    }

    @GetMapping("/stock")
    public Object getStock(String cnt) {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("result", stockService.getCurrentStock(cnt));
            result.put("code", 0);
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", e.getLocalizedMessage());
        }
        return result;
    }

    @GetMapping("/detail")
    public Object getDetail(Date lastupdate, Date startDate, Date endDate) {
        Map<String, Object> result = new HashMap<>();
        if (lastupdate != null || (startDate != null && endDate != null)) {
            try {
                result.put("result", stockService.getChangeDetail(lastupdate, startDate, endDate));
                result.put("code", 0);
            } catch (Exception e) {
                result.put("code", 1);
                result.put("msg", e.getLocalizedMessage());
            }
        } else {
            result.put("code", 1);
            result.put("msg", "lastupdate和(startDate、endDate)不能同时都为空");
        }
        return result;
    }
}
