$(function() {
	  $('#periodYearPicker').datepicker({
		  format:'yyyy',
          autoclose:true,//自动关闭
          minViewMode:2,
          startView:2,
          language:'zh-CN',
		  }).on('changeDate', yearPicker);
	  $('#periodMonthPicker').datepicker({
		  format:'mm',
          autoclose:true,//自动关闭
          minViewMode:1,
          startView:1,
          language:'zh-CN',
		  }).on('changeDate', monthPicker);

	loadYearAndSeaon();
	loadAllShops();
});
function yearPicker(ev) {
  vm.edit.targetYear = ev.date ? ev.date.format('yyyy') : "";
}
function monthPicker(ev) {
  vm.edit.targetMonth = ev.date ? ev.date.format('MM') : "";
}
function loadYearAndSeaon() {

	var today = new Date();// 获取当前时间
	var year = today.getFullYear();
	vm.year = year;
	loadGoodsGrid();
}
function loadAllShops(){
	 $.get(baseURL + "system/target/allShopList", function (r) {
		  if (r.code == 0) {
	          vm.shops = r.result;
	        }
		  });
}

function loadGoodsGrid() {
	$("#jqGrid")
			.jqGrid(
					{
						url : baseURL + 'system/target/list',
						datatype : "json",
						mtype : "POST",
						postData : {
							'year' : vm.year
						},
						colModel : [
								{
									label : '门店名称',
									name : 'shopName',
									width : 100,
									align : "center"
								},
								{
									label : '年份',
									name : 'targetYear',
									width : 100,
									align : "center"
								},
								{
									label : '月份',
									name : 'targetMonth',
									width : 100,
									align : "center"
								},
								{
									label : '标准指标',
									name : 'standardMoney',
									width : 100,
									align : "center"
								},
								{
									label : '中级指标',
									name : 'middleMoney',
									width : 100,
									align : "center"
								},
								{
									label : '高级指标',
									name : 'advanceMoney',
									width : 100,
									align : "center"
								},
								{
									label : '销售额度',
									name : 'completeMoney',
									width : 100,
									align : "center"
								},
								{
									label : '标准完成率',
									name : 'completePercent',
									width : 100,
									align : "center",
									formatter : function(cellvalue, options,
											rowObject) {
										var completePercent=rowObject.completePercent
										completePercent=completePercent.toFixed(2)
										return completePercent * 100
												+ "%";
									}
								},
								{
									label : '中级完成率',
									name : 'middlePercent',
									width : 100,
									align : "center",
									formatter : function(cellvalue, options,
											rowObject) {
										var middlePercent=rowObject.middlePercent
										middlePercent=middlePercent.toFixed(2)
										return middlePercent * 100
												+ "%";
									}
								},
								{
									label : '高级完成率',
									name : 'advancePercent',
									width : 100,
									align : "center",
									formatter : function(cellvalue, options,
											rowObject) {
										var advancePercent=rowObject.advancePercent
										advancePercent=advancePercent.toFixed(2)
										return advancePercent * 100
												+ "%";
									}
								},
								{
									label : '标准序号',
									name : 'standardSeq',
									hidden : true
								},
								{
									label : '中级序号',
									name : 'middleSeq',
									hidden : true
								},
								{
									label : '高级序号',
									name : 'advanceSeq',
									hidden : true
								},
								{
									label : '门店序号',
									name : 'shopSeq',
									hidden : true
								},
								{
									label : '操作',
									width : 50,
									align : "center",
									formatter : function(cellvalue, options,
											rowObject) {
										return '<button class="operation-btn-security" onclick="lineEdit('
												+ options.rowId
												+ ')">修改</button>' + '<button class="operation-btn-dangery" onclick="del(' + rowObject.standardSeq +","+rowObject.middleSeq +","+rowObject.advanceSeq  +
')">删除</button>';
									}
								} ],
						height : 'auto',
						rowNum : 10,
						rowList : [ 10, 30, 50 ],
						rownumbers : true,
						rownumWidth : 30,
						autowidth : true,
						multiselect : false,
						// shrinkToFit:false,
						pager : "#jqGridPager",
						jsonReader : {
							root : "page.list",
							page : "page.currPage",
							total : "page.totalPage",
							records : "page.totalCount"
						},
						prmNames : {
							page : "page",
							rows : "limit",
							order : "order"
						},
						gridComplete : function() {
							// 隐藏grid底部滚动条
							$("#jqGrid").closest(".ui-jqgrid-bdiv").css({
								"overflow-x" : "hidden"
							});
						}
					});
}

/**
 * 表格删除按钮触发此方法
 * 
 * @param seq
 */
// function del(seq) {
// if (!seq) {
// layer.alert('删除异常，请重试', {icon: 2});
// return;
// }
// layer.confirm("确定要删除吗", function (index) {
// $.get(baseURL + 'sys/orderMeetingManage/del/' + seq, function (data) {
// if (data.code == 0) {
// layer.msg("删除成功")
// vm.reloadGoods();
// } else {
// layer.alert('删除失败，请重试', {icon: 2});
// }
// })
//
// layer.close(index);
// })
//
// }
/* 表格行编辑按钮方法 */
function lineEdit(rowId) {
	let rowObj = $("#jqGrid").getRowData(rowId);
	vm.addGoods();
	vm.edit = rowObj
}
function del(standardSeq,middleSeq,advanceSeq){
	$.get(baseURL + "system/target/del?seq=" + standardSeq, function (r) {
	    if (r.code == 0) {
	    	$.get(baseURL + "system/target/del?seq=" + middleSeq, function (r) {
	    		if (r.code == 0) {
	    			$.get(baseURL + "system/target/del?seq=" + advanceSeq, function (r) {
	    				if(r.code == 0){
	    					 vm.reloadGoods();
	    				}else{
	    					alert(r.msg);
	    				}
	    			})
	    		}else{
	    			alert(r.msg);
	    		}
	    	})
	    } else {
	      alert(r.msg);
	    }
	  });
}

var vm = new Vue(
		{
			el : '#rrapp',
			data : {
				year : '',

				categoryArray : [],
				years : [],
				seasons : [],

				// 表格加载条件
				categorySeq : '',
				selectCategoryName : '',
				keywords : '',
				yearList : [],
				seasonList : [],
				year : -1,
				seasonSeq : -1,
				selectSeasonName : '',

				showList : true,
				shops:[],
				shopName:'',
				/* 编辑区数据 */
				edit : {
				}
			},
			methods : {
				loadYears : function() {
					var currYear = new Date().getFullYear();
					this.years.push(currYear + 1);

					for (var i = 0; i < 5; i++) {
						this.years.push(currYear - i);
					}
				},
				reloadGoods : function() {
					let currPage = 1;
					currPage = $("#jqGrid").jqGrid('getGridParam', 'page');// 获取当前页

					$("#jqGrid").jqGrid('setGridParam', {
						datatype : 'json',
						postData : {
							'year' : vm.year,
						},
						page : currPage,
					}).trigger('reloadGrid');
				},
				addGoods : function() {
					 vm.reloadGoods();
					this.showList = false;
				},
				yearSelect : function(item) {
					this.year = item;
				},
				shopSelect:function(item,name){
					vm.shopName=name;
					vm.edit.shopSeq=item
				},
				search : function() {
					$("#jqGrid").jqGrid('setGridParam', {
						datatype : 'json',
						postData : {
							'year' : vm.year,
						},
						page : 1,
					}).trigger('reloadGrid');
				},

				uploadGoodsExcel : function() {
					parent.window.showLoading();
					var year = vm.year;
					console.log(year)
					if (year == '') {
						alert("未选择年份！");
						parent.window.hideLoading();
						return false;
					}
					$('#goodsExcelForm').ajaxSubmit({
						url : baseURL + 'system/target/uploadGoodsExcel',
						dataType : 'json',
						cache : false,
						success : function(r) {
							if (r.code === 0) {
								layer.alert(r.msg);
								location.reload();
							} else {
								layer.alert(r.msg);
							}
							$("#goodsExcelForm").resetForm();
							parent.window.hideLoading();
						},
						error : function(r) {
							alert("导入指标出错！");
							$("#goodsExcelForm").resetForm();
							parent.window.hideLoading();
						}
					});
				},
				toMain : function() {
					this.showList = true;
				},
				saveOrUpdate : function() {
					
				
				if(vm.edit.completePercent){
					vm.edit.completePercent = vm.edit.completePercent.replace(
							"%", "") / 100
							}
				if(vm.edit.middlePercent){
					vm.edit.middlePercent=vm.edit.middlePercent.replace(
							"%", "") / 100
				}
				if(vm.edit.advancePercent){
					vm.edit.advancePercent=vm.edit.advancePercent.replace(
							"%", "") / 100	
				}
				console.log(vm.edit)
					$.post(baseURL + "system/target/saveOrUpdate", vm.edit,
							function(data) {
								if (data.code == 0) {
									layer.msg("保存成功")
									vm.reloadGoods();
									vm.showList = true;
								} else {
									layer.alert('保存失败，请重试', {
										icon : 0
									});
								}
							})
				},
			},
			created : function() {
				this.loadYears();
			},
		});
