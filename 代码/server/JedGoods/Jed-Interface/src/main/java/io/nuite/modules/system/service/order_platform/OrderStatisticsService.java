package io.nuite.modules.system.service.order_platform;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderStatisticsService {

	List<Object> getUserSeqByAttachTypeAndAttachSeq(Integer attachType, Integer attachSeq);

	List<Map<String, Object>> orderStatisticsList(Integer companySeq, String userSeqs, Date startTime, Date endTime,
			Integer orderStatus, String keywords);

}
