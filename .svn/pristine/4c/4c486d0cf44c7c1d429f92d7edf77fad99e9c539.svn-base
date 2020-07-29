package io.nuite.modules.job.task;

import io.nuite.modules.erp.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @description: 伯俊ERP拉取定时任务
 * @author: jxj
 * @create: 2019-05-24 08:58
 */
@Component("boJunErpTask")
public class BoJunErpTask  implements CommandLineRunner {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private ErpShopService erpShopService;
	@Autowired
	private ErpColorService erpColorService;
	@Autowired
	private ErpSeasonSyncService erpSeasonSyncService;
	@Autowired
	private ErpGoodsSyncService erpGoodsSyncService;
	@Autowired
	private ErpSellService erpSellService;
	@Autowired
	private ErpSaleService erpSaleService;
	@Autowired
	private ErpShoesDataSyncService erpShoesDataSyncService;
	@Autowired
	private ErpSalesService erpSalesService;
	@Autowired
	private ErpTargetService erpTargetService;
    @Autowired
	private  ErpSizeService erpSizeService;
	
	
	/*
	 * 按顺序同步伯俊ERP数据
	 */
	public void getErpData(String goodsType) {
		try {
			// 步骤1：同步门店
			Integer goodTypeInt=Integer.valueOf(goodsType);
			boolean shopFlag = erpShopService.getErpShop();
			if (!shopFlag) {
				logger.error("--BojunErp--失败，同步门店失败");
				return;
			}

			// 步骤2：同步店员数据
			boolean erpSalesFlag = erpSalesService.getErpSales();
			if (!erpSalesFlag) {
				logger.error("--BojunErp--失败，同步店员数据失败");
				return;
			}

			// 步骤3：同步季节
			boolean seasonFlag = erpSeasonSyncService.seasonSync();
			if (!seasonFlag) {
				logger.error("--BojunErp--失败，同步季节失败");
				return;
			}

			// 步骤4：同步颜色
			boolean colorFlag = erpColorService.getErpColor();
			if (!colorFlag) {
				logger.error("--BojunErp--失败，同步颜色失败");
				return;
			}

			// 步骤5：同步货品，同时同步出层级结构的分类 + 鞋子价格信息
			boolean goodsFlag = erpGoodsSyncService.goodsSync(goodTypeInt);
			if (!goodsFlag) {
				logger.error("--BojunErp--失败，同步货品失败");
				return;
			}

			// 步骤6：同步库存
			boolean shoesDataFlag = erpShoesDataSyncService.shoesDataSync(goodTypeInt);
			if (!shoesDataFlag) {
				logger.error("--BojunErp--失败，同步库存失败");
				return;
			}

			// 步骤7：同步销售数据（即进出库记录YHSR_OP_ShoesData_Daily_Detail表）
			boolean erpSellFlag = erpSellService.getErpSell(goodTypeInt);
			if (!erpSellFlag) {
				logger.error("--BojunErp--失败，同步进出库数据");
				return;
			}

			// 步骤8：同步零售数据（即销售记录YHSR_OP_SaleShoesDetail表）
			boolean erpSaleFlag = erpSaleService.getErpSale(goodTypeInt);
			if (!erpSaleFlag) {
				logger.error("--BojunErp--失败，同步零售数据失败");
				return;
			}

			// 步骤9：同步门店指标数据
			boolean erpShopTargetFlag = erpTargetService.getErpShopTarget();
			if (!erpShopTargetFlag) {
				logger.error("--BojunErp--失败，同步门店指标数据失败");
				return;
			}

			// 步骤10：同步店员指标数据
			boolean erpSalesTargetFlag = erpTargetService.getErpSalesTarget();
			if (!erpSalesTargetFlag) {
				logger.error("--BojunErp--失败，同步店员指标数据失败");
				return;
			}
			logger.info("--BojunErp--成功，同步伯俊ERP数据成功！");
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			logger.error("--BojunErp--失败，同步伯俊ERP数据失败！");
		}
	}
	
	@Override
	public void run(String... strings) throws Exception {
		erpSizeService.getErpSize();
	
	}
}
