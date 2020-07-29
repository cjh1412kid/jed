package com.nuite.service.Impl;

import com.nuite.dao.StockMapper;
import com.nuite.service.StockService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StockServiceImpl implements StockService {
    @Autowired
    StockMapper stockMapper;


    @Override
    public List<Map<String, Object>> getChangeDetail(Date lastupdate, Date startDate, Date endDate) {
        Map<String,Object> map = new HashMap<>(10);
        map.put("lastupdate",lastupdate);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        return stockMapper.queryChangeDetail(map);
    }

    @Override
    public List<Map<String, Object>> getCurrentStock(String cnt) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String endDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + day;
        if (day < 10) {
            calendar.add(Calendar.MONTH, -2);
        } else {
            calendar.add(Calendar.MONTH, -1);
        }

        int startYear = calendar.get(Calendar.YEAR);
        int startMonth = calendar.get(Calendar.MONTH) + 1;

        String startDate = startYear + "-" + startMonth + "-01";
        String mycnt = StringUtils.isBlank(cnt) ? null : cnt;
        Map<String,Object> map = new HashMap<>(10);
        map.put("cnt",mycnt);
        map.put("startYear",startYear);
        map.put("startMonth",startMonth);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        return stockMapper.queryStock(map);
    }
}
