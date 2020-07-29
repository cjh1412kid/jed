package io.nuite.modules.erp.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.nuite.modules.erp.service.ErpSeasonSyncService;
import io.nuite.modules.erp.utils.BoJunErpPortalUtil;
import io.nuite.modules.sr_base.dao.GoodsSeasonDao;
import io.nuite.modules.sr_base.entity.GoodsSeasonEntity;
import io.nuite.modules.sr_base.service.BaseBrandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class ErpSeasonSyncServiceImpl implements ErpSeasonSyncService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Value("${jrd.companySeq}")
    private Integer companySeq;

    @Autowired
    private GoodsSeasonDao goodsSeasonDao;

    @Autowired
    private BaseBrandService baseBrandService;


	
	
	/**
	 * 同步季节
	 * @return
	 */
	@Override
	public boolean seasonSync(){
        try {
			logger.info("季节拉取开始");
			Long startTime = System.currentTimeMillis();
			Map<String, Object> sqlWhereMap = new HashMap<String, Object>();
			sqlWhereMap.put("column", "M_DIMDEF_ID");
			sqlWhereMap.put("condition", "= 60");
			JSONObject jsonObject = BoJunErpPortalUtil.querySql("M_DIM", "ATTRIBNAME", sqlWhereMap, null, null, null);
			if(jsonObject == null) {
				logger.error("BojunErp同步季节，接口请求失败");
				return false;
			}
//			{
//				"message": "完成:0.0050 seconds",
//				"id": "112",
//				"code": 0,
//				"rows": [["春季"],["春夏"],["冬季"],["秋冬"],["秋季"],["夏季"]]
//			}
			JSONArray rows = jsonObject.getJSONArray("rows");
			for(int i = 0; i < rows.size(); i++) {
				JSONArray seasonArr = rows.getJSONArray(i);
				String seasonName = seasonArr.getString(0);
				
				//判断季节是否已存在，不存在则插入
		        GoodsSeasonEntity goodsSeasonEntity = new GoodsSeasonEntity();
		        goodsSeasonEntity.setSeasonName(seasonName);
		        goodsSeasonEntity.setBrandSeq(baseBrandService.getBrandSeqByCompanySeq(companySeq));
		        GoodsSeasonEntity existEntity = goodsSeasonDao.selectOne(goodsSeasonEntity);
		        if(existEntity == null) {
//		        	Seq			int	0	0	0	-1	0	0		0	0	0	0	序号(主键)		-1			
//		        	Brand_Seq	int	0	0	-1	0	0	0		0	0	0	0	品牌序号(外键:YHSR_Base_Brand表)		0			
//		        	SeasonName	varchar	255	0	-1	0	0	0		0	0	0	0	季节名字	Chinese_PRC_CI_AS	0			
//		        	InputTime	datetime	0	0	-1	0	0	0	(getdate())	0	0	0	0	入库时间		0			
//		        	Del			int	0	0	0	0	0	0	((0))	0	0	0	0	删除标识(0:未删除,1:已删除)		0			

		        	goodsSeasonEntity.setBrandSeq(baseBrandService.getBrandSeqByCompanySeq(companySeq));
		        	goodsSeasonEntity.setInputTime(new Date());
		        	goodsSeasonEntity.setDel(0);
		        	goodsSeasonDao.insert(goodsSeasonEntity);
		        }
			}
			logger.error("季节拉取结束,耗时:" + (System.currentTimeMillis() - startTime) + "毫秒");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
            logger.error("BojunErp同步季节，出错：" + e.getMessage(), e);
    		return false;
		}
	}
    
}
