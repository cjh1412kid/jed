//调拨管理
var stockList = [];
var index;
//var ztree,rMenu,rClickNode;
//var setting = {
//	view : {
//
//		dblClickExpand : false,
//	},
//	check : {
//		enable : true
//	},
//	data : {
//		keep : {
//			leaf : true,// 属性配置 leaf: true, 表示叶子节点不能变成根节点。parent: true 表示
//			// 根节点不能变成叶子节点
//			parent : true
//		},
//		simpleData : {
//			enable : true,
//			idKey : "name",
//			pIdKey : "parentName",
//			rootPId : -1
//		},
//		key : {
//			url : "nourl"
//		}
//	},
//	edit : {
//		drag : {
//			isCopy : false,
//			isMove : true
//		},
//		enable : true,// 设置是否处于编辑状态
//		editNameSelectAll : true,
//		showRemoveBtn : false,
//		showRenameBtn : false
//	},
//	callback : {
//		onClick : categoryClick,
//		 onRightClick: categoryRightClick,
//	}
//};
//$(function() {
//	zTreelist();
//	rMenu = $("#rMenu");
//});
//function zTreelist() {
//	$.get(baseURL + "system/allocateAndTransfer/list", function(r) {
//		var categorys = r.list;
//		console.log(categorys)
//		ztree = $.fn.zTree.init($("#categoryTree"), setting, categorys);
//		ztree.expandAll(true);
//	});
//
//}
//
//function nOcategoryLsit() {
//	$.get(baseURL + "system/allocateAndTransfer/nOcategoryLsit", function(r) {
//		vm.nOcategoryLsit = r.list;
//	});
//}
//function categoryRightClick(event, treeId, treeNode){
//	var rank=treeNode.rank;
//	var date='';
//	var shoesSeq='';
//	var allocateTime='';
//	vm.rank=rank
//	vm.goodId='';
//	if(rank==1){
//		date=treeNode.allocateDate
//	}else if(rank==2){
//		 date=treeNode.date
//		 shoesSeq=treeNode.shoesSeq
//	}else if(rank==3){
//		 date=treeNode.date
//		 shoesSeq=treeNode.shoesSeq
//		 allocateTime=treeNode.allocateTime
//	}
//	vm.date=date
//	vm.shoesSeq=shoesSeq
//	vm.allocateTime=allocateTime
//	  rClickNode = treeNode;
//	  if (!treeNode && event.target.tagName.toLowerCase() != "button" && $(event.target).parents("a").length == 0) {
//	    ztree.cancelSelectedNode();
//	    showRMenu("root", event.clientX, event.clientY);
//	  } else if (treeNode && !treeNode.noR) {
//	    ztree.selectNode(treeNode);
//	    showRMenu("node", event.clientX, event.clientY);
//	  }
//}
//function showRMenu(type, x, y) {
//
//	  if (!ztree.getSelectedNodes()[0]) {
//	    return;
//	  }
//	  y += $(document).scrollTop();
//	  x += $(document).scrollLeft();
//	  rMenu.css({
//	    "top": y + "px",
//	    "left": x + "px",
//	    "visibility": "visible"
//	  });
//	  $("body").bind("mousedown", onBodyMouseDown);
//	}
//function onBodyMouseDown(event) {
//	  if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length > 0)) {
//	    rMenu.css({
//	      "visibility": "hidden"
//	    });
//	  }
//	}
//function addTreeNode() {
//	  hideRMenu();
//	  	confirm("确认推送消息？", function () {
//        	 	var date=vm.date;
//            	var shoesSeq=vm.shoesSeq;
//            	var allocateTime=vm.allocateTime
//            	$.post(baseURL + "system/allocateAndTransfer/pushRecord", {
//            		date:date,
//            		shoesSeq:shoesSeq,
//            		allocateTime:allocateTime
//    					}, function(r) {
//    						alert("推送成功")
//    		})
//    		})
//}
//function hideRMenu() {
//	  if (rMenu)
//	    rMenu.css({
//	      "visibility": "hidden"
//	    });
//	  $("body").unbind("mousedown", onBodyMouseDown);
//	}
//
//function categoryClick(event, treeId, treeNode) {
//	
//	var rank=treeNode.rank;
//	var date='';
//	var shoesSeq='';
//	var allocateTime='';
//	vm.rank=rank
//	vm.goodId='';
//	if(rank==1){
//		date=treeNode.allocateDate
//	}else if(rank==2){
//		 date=treeNode.date
//		 shoesSeq=treeNode.shoesSeq
//	}else if(rank==3){
//		 date=treeNode.date
//		 shoesSeq=treeNode.shoesSeq
//		 allocateTime=treeNode.allocateTime
//	}
//	vm.date=date
//	vm.shoesSeq=shoesSeq
//	vm.allocateTime=allocateTime
//	$("#jqGoodsGrid").jqGrid('setGridParam', {
//		datatype : 'json',
//		url : baseURL + 'system/allocateAndTransfer/recordList',
//		postData : {
//			date:date,
//			shoesSeq:shoesSeq,
//			allocateTime:allocateTime,
//			goodId:vm.goodId,
//		},
//		width:"100%",
//		height : 'auto',
//		page : 1    
//	}).trigger('reloadGrid');                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
//    layer.open({
//        type: 1,
//        offset: '50px',
//        skin: 'layui-layer-molv',
//        area: ['1200px', '700px'],
//        shade: 0,
//        shadeClose: false,
//        content: jQuery("#goodsLayer"),
//        btn: ['下载excel','删除'],
//        btn1: function (index) {
//        	var form = document.getElementById('exportQRCodeForm');
//            form.action = baseURL + "system/allocateAndTransfer/exportRecord";
//            form.token.value = token;
//            form.date.value = vm.date;
//            form.shoesSeq.value =vm.shoesSeq;
//            form.allocateTime.value = vm.allocateTime;
//            form.goodId.value=vm.goodId,
//            form.submit();
//        },
//        btn2:function(index){
//        	/*//获取多选到的id集合  
//        	var ids = $("#jqGoodsGrid").jqGrid("getGridParam", "selarrrow");  
//        	//遍历访问这个集合  
//        	var seqs=[];
//        	$(ids).each(function (index, id){  
//        	     //由id获得对应数据行  
//        	var row = $("#jqGoodsGrid").jqGrid('getRowData', id);
//        	seqs.push(row.seq)
//        	}) 
//        	console.log(seqs)*/
//        	confirm("确认删除？", function () {
//        	 	var date=vm.date;
//            	var shoesSeq=vm.shoesSeq;
//            	var allocateTime=vm.allocateTime
//            	$.post(baseURL + "system/allocateAndTransfer/deleteRecord", {
//            		date:date,
//            		shoesSeq:shoesSeq,
//            		allocateTime:allocateTime
//    					}, function(r) {
//    						alert("删除成功")
//    							var p = $("#jqGrid").jqGrid('getGridParam', 'page');// 获取当前页
//    					var IsReplenishValue = vm.IsReplenish[0] || null;
//				        $("#jqGrid").jqGrid('setGridParam', {
//				          datatype: 'json',
//				          postData: {
//				        	'categorySeq' : vm.categorySeq,
//				  			'year' : vm.year,
//				  			'seasonSeq' : vm.seasonSeq,
//				  			'keywords' : vm.keywords,
//				  			'orderBy' : 1,
//				  			'orderDir' : 0,
//				  			'saleTimeStart' : vm.saleTimeStart,
//				  			'saleTimeEnd' : vm.saleTimeEnd,
//				  			'sizeType' : vm.sizeType,
//				  			'sizeSeqStart' : vm.minSize,
//				  			'sizeSeqEnd' : vm.maxSize,
//				  			'stockMinNum' : vm.minStock,
//				  			'stockMaxNum' : vm.maxStock,
//				  			'shopSeq' : vm.selectShopSeq,
//				  			'isReplenish' : IsReplenishValue
//				          },
//				          page: p,
//				        }).trigger('reloadGrid');
//    						zTreelist(); 
//    						
//    		})
//        	})
//        }
//      });
//}

$(function() {
	var colModel2=[
		{
			label : '货号',
			name : 'goodID',
			width : 60,
			align : "center"
		},
		{
			label : '尺码',
			name : 'size',
			width : 60,
			align : "center"
		},
		{
			label : '数量',
			name : 'num',
			width : 60,
			align : "center"
		},
		{
			label : '调出方',
			name : 'outShopName',
			width : 60,
			align : "center"
		},
		{
			label : '调入方',
			name : 'inShopName',
			width : 60,
			align : "center"
		},
		{
			label : '调拨时间',
			name : 'inputTime',
			width : 60,
			align : "center"
		},
		{
			label : 'seq',
			name : 'seq',
			width : 105,
			align : "center",
			hidden : true
		},
	]
	
	
	
	var colModel = [
			{
				label : '货号',
				name : 'goodId',
				width : 120,
				align : "center"
			},
			{
				label : '图片',
				name : 'img',
				width : 120,
				align : "center",
				formatter : function(cellvalue,options,rowObject) {
					var detail = '<image src="' + cellvalue
							+ '" style="width: 40px;height: 40px;" onclick="showPic('
							+ rowObject.seq + ')"/>';
					return detail;
				}
			},
			{
				label : '平均成交价',
				name : 'AvgSalePrice',
				width : 60,
				align : "center"
			},
			{
				label : '日均销售',
				name : 'salesDayAvg',
				width : 60,
				align : "center"
			},
			{
				label : '总销售',
				name : 'salesNum',
				width : 60,
				align : "center"
			},
			{
				label : '总库存',
				name : 'webtotalNum',
				width : 60,
				align : "center"
			},
			{
				label : '总仓库存',
				name : 'webtotalStockNum',
				width : 60,
				align : "center"
			},
			{
				label : '门店总库存',
				name : 'webtotalShopNum',
				width : 60,
				align : "center"
			},
			{
				label : '售罄率',
				name : 'selloutRate',
				width : 60,
				align : "center"
			},
			{
				label : '总仓入仓时间',
				name : 'inTime',
				width : 120,
				align : "center"
			},
			{
				label : '最后一次调拨时间',
				name : 'time',
				width : 120,
				align : "center"
			},
			{
				label : 'seq',
				name : 'seq',
				width : 105,
				align : "center",
				hidden : true
			},
			{
				label : '操作',
				name : 'createDate',
				width : 150,
				align : "center",

				formatter : function(cellvalue, options, rowObject) {
					var detail = ('<button class="operation-btn-warn" onclick="lineEdit('
							+ rowObject.seq + ')">编辑</button>');
					return detail;
				}

			} ];

	$('#saletime_range').daterangepicker(
			{
				showDropdowns : true,
				autoUpdateInput : false,
				locale : {
					format : 'YYYY-MM-DD',
					applyLabel : "应用",
					cancelLabel : "取消",
					customRangeLabel : '选择时间',
					daysOfWeek : [ "日", "一", "二", "三", "四", "五", "六" ],
					monthNames : [ "一月", "二月", "三月", "四月", "五月", "六月", "七月",
							"八月", "九月", "十月", "十一月", "十二月" ],
					firstDay : 0
				}
			}).on('apply.daterangepicker', function(ev, picker) {
		$("#saleTimeDiv").removeClass("open");
		vm.saleTimeStart = picker.startDate.format(picker.locale.format);
		vm.saleTimeEnd = picker.endDate.format(picker.locale.format);
		vm.saleStart_endTime = vm.saleTimeStart + " ~ " + vm.saleTimeEnd;
	});
	$('#saletime_range2').daterangepicker(
			{
				showDropdowns : true,
				autoUpdateInput : false,
				locale : {
					format : 'YYYY-MM-DD',
					applyLabel : "应用",
					cancelLabel : "取消",
					customRangeLabel : '选择时间',
					daysOfWeek : [ "日", "一", "二", "三", "四", "五", "六" ],
					monthNames : [ "一月", "二月", "三月", "四月", "五月", "六月", "七月",
							"八月", "九月", "十月", "十一月", "十二月" ],
					firstDay : 0
				}
			}).on('apply.daterangepicker', function(ev, picker) {
		$("#saleTimeDiv2").removeClass("open");
		vm.saleTimeStart2 = picker.startDate.format(picker.locale.format);
		vm.saleTimeEnd2 = picker.endDate.format(picker.locale.format);
		vm.saleStart_endTime2 = vm.saleTimeStart2 + " ~ " + vm.saleTimeEnd2;
	});
	$('#inDatePicker').datepicker({
	    autoclose: true,
	    language: 'zh-CN',
	    format: "yyyy-mm-dd"//日期格式
	  }).on('changeDate', saleDatePicker);
	
	

	$('body').bind(
			'click',
			function(e) {
				var o = e.target;
				// console.log($("#saleTimeDiv").closest());
				// console.log($("#saleTimeDiv").closest('#ddd'));
				// console.log($("#saleTimeDiv").parents('#aaa'));
				// console.log($(o).parents('.daterangepicker'));
				if ($(o).parents('.daterangepicker').length == 0
						&& $(o).parents('.table-condensed').length == 0
						&& $(o).parents('#aaa').length == 0) {// 不是点击的时间选择器区域
					$("#saleTimeDiv").removeClass("open");
				}
			});
	$('body').bind(
			'click',
			function(e) {
				var o = e.target;
				// console.log($("#saleTimeDiv").closest());
				// console.log($("#saleTimeDiv").closest('#ddd'));
				// console.log($("#saleTimeDiv").parents('#aaa'));
				// console.log($(o).parents('.daterangepicker'));
				if ($(o).parents('.daterangepicker').length == 0
						&& $(o).parents('.table-condensed').length == 0
						&& $(o).parents('#bbb').length == 0) {// 不是点击的时间选择器区域
					$("#saleTimeDiv2").removeClass("open");
				}
			});

	loadGoodsGrid(colModel);

	loadGoodsGrid2(colModel2);
	
	setTimeout(function() {
		vm.yearSelectEnter();
		vm.seasonSelectEnter();
		vm.shopSelectEnter();
	}, 100)
})
function saleDatePicker(ev) {
		  vm.inDateTime = ev.date ? ev.date.format('yyyy-MM-dd') : "";
		}
function showPic(seq){
	$.get(baseURL + "order/goods/edit?seq=" + seq,
			function(data) {
		var imgs=data.goods.banners
		if(imgs.length==0){
			layer.alert("暂无图片")
		}else{
			vm.src=imgs[0]
			 layer.open({
			        type: 1,
			        offset: '50px',
			        skin: 'layui-layer-molv',
			        area: ['800px', '800px'],
			        shade: 0,
			        shadeClose: false,
			        content: jQuery("#picture"),
			      });
		}
	})
}



function lineEdit(seq) {
	vm.seq = seq
	vm.showList = false;
	
	
	// 根据货品序号查询相关信息
	$.get(baseURL + "system/allocateAndTransfer/getGoodDetail?shoesSeq=" + seq+ "&saleTimeStart="
			+ vm.saleTimeStart2
			+ "&saleEndTime=" + vm.saleTimeEnd2+ "&orderBy=" + vm.orderBy+"&orderDir=" + vm.orderDir,
			function(data) {
				if (data.code == 0) {
					vm.good = data.map
					var sizeList = data.sizeList
					vm.list = data.list
					vm.dataList=data.list
					vm.sizes = data.sizes
					for ( var i in sizeList) {
						sizeList[i].change = sizeList[i].stock
					}
					vm.sizeList = sizeList
					stockList = data.stockList
					vm.allocateList=[];
				}
			})

}

function loadGoodsGrid(colModel) {
	var date=new Date();
	var year=date.getFullYear();
	var month=date.getMonth()+1;
	var day=date.getDate();
	var saleTimeStart2=y=new Date().setDate((new Date().getDate()-15))//1天
		vm.saleTimeStart=new Date(saleTimeStart2).getFullYear()+"-"+(new Date(saleTimeStart2).getMonth()+1)+"-"+new Date(saleTimeStart2).getDate()
		vm.saleTimeEnd=year+"-"+month+"-"+day
		vm.saleTimeStart2=new Date(saleTimeStart2).getFullYear()+"-"+(new Date(saleTimeStart2).getMonth()+1)+"-"+new Date(saleTimeStart2).getDate()
		vm.saleTimeEnd2=year+"-"+month+"-"+day
		vm.saleTimeType = 2;
		vm.saleStart_endTime = "近15天";
		vm.saleTimeType2 = 2;
		vm.saleStart_endTime2 = "近15天";
		var currYear = new Date().getFullYear();
	$("#jqGrid").jqGrid({
		url : baseURL + 'system/allocateAndTransfer/goodsList',
		datatype : "json",
		mtype : "GET",
		postData : {
			'categorySeq' : vm.categorySeq,
			'yearList' : currYear,
			'seasonSeqList' : '',
			'keywords' : vm.keywords,
			'orderBy' : 1,
			'orderDir' : 0,
			'saleTimeStart' : vm.saleTimeStart,
			'saleTimeEnd' : vm.saleTimeEnd,
			'sizeType' : vm.sizeType,
			'sizeSeqStart' : vm.minSize,
			'sizeSeqEnd' : vm.maxSize,
			'stockMinNum' : vm.minStock,
			'stockMaxNum' : vm.maxStock,
			'shopSeqList' : '',
			'isReplenish' : vm.IsReplenish
		},
		colModel : colModel,
		height : 'auto',
		 width: '1600px',
		rowNum : 10,
		rowList : [ 10, 30, 50 ],
		rownumbers : true,
		rownumWidth : 50,
		autowidth : true,
		multiselect : false,
		viewrecords : true,
		pager : "#jqGridPager",
		jsonReader : {
			root : "page.list",
			page : "page.currPage",
			total : "page.totalPage",
			records : "page.totalCount"
		},
		prmNames : {
			page : "page", // 表示请求页码的参数名称
			rows : "limit", // 表示请求行数的参数名称
			sort : "sidx", // 表示用于排序的列名的参数名称
			order : "sord" // 表示采用的排序方式的参数名称
		},
		gridComplete : function() {
			// 隐藏grid底部滚动条
			$("#jqGrid").closest(".ui-jqgrid-bdiv").css({
				"overflow-x" : "hidden"
			});
			$("#jqGrid").setGridWidth($("#col-xs-12").width());
		}
	});
	
}
function getSelecteds(){  

	}  
	  

function loadGoodsGrid2(colModel2) {

	$("#jqGoodsGrid").jqGrid({
		url : baseURL + 'system/allocateAndTransfer/recordList',
		datatype : "json",
		mtype : "GET",
		postData : {
			'date':vm.date,
			'shoesSeq':vm.shoesSeq,
			'allocateTime':vm.allocateTime,
		},
		colModel : colModel2,
		height : 'auto',
		rowNum : 10,
		rowList : [ 10, 30, 50 ],
		rownumbers : true,
		rownumWidth : 25,
		autowidth : true,
		multiselect : true,
		viewrecords : true,
		pager : "#jqGoodsGridPager",
		jsonReader : {
			root : "page.list",
			page : "page.currPage",
			total : "page.totalPage",
			records : "page.totalCount"
		},
		prmNames : {
			page : "page", // 表示请求页码的参数名称
			rows : "limit", // 表示请求行数的参数名称
			sort : "sidx", // 表示用于排序的列名的参数名称
			order : "sord" // 表示采用的排序方式的参数名称
		},
		gridComplete : function() {
			// 隐藏grid底部滚动条
			$("#jqGoodsGrid").closest(".ui-jqgrid-bdiv").css({
				"overflow-x" : "hidden"
			});
			 $("#jqGoodsGrid").setGridWidth($("#goodsLayer").width());
		}
	});
}
function reloadGrid2() {
	$("#jqGoodsGrid").setGridParam({
		datatype : 'json',
		postData : {
			'date':vm.date,
			'shoesSeq':vm.shoesSeq,
			'allocateTime':vm.allocateTime,
		},
		page : 1,
	}).trigger('reloadGrid');
}


function isIntNum(val){
	 return typeof val === 'number' && !isNaN(val);
}

var vm = new Vue(
		{
			el : '#rrapp',
			data : {
				showList : true,
				options : [ {
					name : '项目1'
				}, {
					name : '项目2'
				}, {
					name : '项目3'
				}, {
					name : '项目4'
				}, ],
				good : '',
				sizeList : [],
				list : [],
				sizes : [],
				seq : '',
				years : [],
				allocateList : [],
				year : '',
				showRecord:false,
				date:'',
				shoesSeq:'',
				allocateTime:'',
				twoKeywords :'',
				operate:'取出',
				operateList:[],
				dataList:[],
				sizeOprateList:[],
				shopOprateList:[],
				goodId:'',
				rank:'',
				src:'',
				orderBy:'',
				orderDir:'',
				
				
				seasons : [],
				seasonSeq : '',
				selectSeasonName : '',

				
				
				categoryArray : [],
				categorySeq : '',
				selectCategoryName : '',

				sizeArray : [],
				selectSizeName : '',
				sizeType : '',
				minSize : '',
				maxSize : '',

				selectStockName : '',
				minStock : '',
				maxStock : '',

				saleTimeType : '',
				saleStartTime : '',
				saleEndTime : '',
				saleStart_endTime : '',

				saleTimeType2 : '',
				saleStartTime2 : '',
				saleEndTime2 : '',
				saleStart_endTime2 : '',

				shops : [],
				selectShopSeq : 0,

				keywords : '',

				IsReplenish : [],
				inDateTime:'',
				emptys:[],
				totalInventory:false,
				temporaryInventory:false,
				selectShopName:'所有门店',
				allSizes:[],
				replenishPlan:'',
				replenishCount:0,
				stockCount:0,
				remark:'',
				showReplenish:true,
				replenishDetail:[],
				allReturnSizes:[],
				returnAllTotal:'',
				isReturn:false,
				shopSeq:'',
			},
			methods : {
				yearSelectEnter: function () {
			        this.loadYears();

			        setTimeout(function () {
			          $("#yearSelect2Id").select2();
			          var currYear = new Date().getFullYear();
			          $("#yearSelect2Id").val(currYear).trigger('change');
			        }, 100);
			    },
			    seasonSelectEnter: function () {
			        this.loadSeasons();

			        setTimeout(function () {
			          $("#seasonSelect2Id").select2();
			        }, 100);
			    },
			    shopSelectEnter: function () {
				    this.loadShops();

				    setTimeout(function () {
				    	$("#shopSelect2Id").select2();
				    }, 100);
				},
				dateFormat:function(time) {
					var date=new Date(time);
					var year=date.getFullYear();
					/* 在日期格式中，月份是从0开始的，因此要加0
					 * 使用三元表达式在小于10的前面加0，以达到格式统一  如 09:11:05
					 * */
					var month= date.getMonth()+1<10 ? "0"+(date.getMonth()+1) : date.getMonth()+1;
					var day=date.getDate()<10 ? "0"+date.getDate() : date.getDate();
					var hours=date.getHours()<10 ? "0"+date.getHours() : date.getHours();
					var minutes=date.getMinutes()<10 ? "0"+date.getMinutes() : date.getMinutes();
					var seconds=date.getSeconds()<10 ? "0"+date.getSeconds() : date.getSeconds();
					// 拼接
					return year+"年"+month+"月"+day+"日";
				},
				replenish:function(){
					//查询上次补单详情和库存详情
					  $.get(baseURL + "order/replenish/replenishDetail?shoesSeq=" + vm.seq, function (data) {
						    if (data.code == 0) {
						      vm.replenishPlan = data.shoesReplenishEntity;
						    	//汇总
						    	var sizes=data.sizes;
						    	vm.allSizes=sizes;
						    	var replenishCount=0;
						    	var stockCount=0;
						    	for (var i = 0; i < sizes.length; i++) {
						    		replenishCount+=sizes[i].replenish
						    		stockCount+=sizes[i].stock
								}
						    	vm.replenishCount=replenishCount
						    	vm.stockCount=stockCount
						    	index=  layer.open({
								    type: 1,
								    title: '补单管理',
								    resize: false,
								    area: ['1083px', '640px'], //宽高
								    content: $('#replenishPlanWindow'),
								    success:function(){
//								    	laydate.render({
//								            elem: "#replenishTime",
//								            type:'datetime',
//								            format: 'yyyy-MM-dd HH:mm', //可任意组合
//								            done: function(value, date, endDate){
//								            	 console.log(value); 
//								            	 vm.ReplenishTime=value;
//								            }
//								        });
								    },
								    cancel: function (index, layero) {
								    	console.log(index)
								      //关闭按钮
								      layer.close(index)
								    }
								  });
						      
						    } else {
						      layer.alert(data.msg)
						    }
						  })
				},
				enterReplenish:function(){
					vm.showReplenish=false
					//查询最后一条记录的详情
					  $.get(baseURL + "order/replenish/replenishNear?shoesSeq=" + vm.seq, function (data) {
						  vm.replenishDetail=data.result
					  })
					
				},
				leaveReplenish:function(){
					vm.showReplenish=true
				},
				subtractReplenish:function(size){
					if(size.replenish==''&&parseInt(size.replenish)<=0){
						size.replenish=0
						return;
					}
					size.replenish=parseInt(size.replenish)-1;
					var sizes=vm.allSizes
					var replenishCount=0;
					for (var i = 0; i < sizes.length; i++) {
						var replenish=sizes[i].replenish
						if(!isIntNum(sizes[i].replenish)){
							replenish=0
						}
			    		replenishCount+=replenish
					}
					vm.replenishCount=replenishCount;
				},
				addReplenish:function(size){
					if(size.replenish==''){
						size.replenish=0
					}
					size.replenish=parseInt(size.replenish)+1;
					var sizes=vm.allSizes
					var replenishCount=0;
					for (var i = 0; i < sizes.length; i++) {
						var replenish=sizes[i].replenish
						if(!isIntNum(sizes[i].replenish)){
							replenish=0
						}
			    		replenishCount+=replenish
					}
					vm.replenishCount=replenishCount;
				},
				integerFormat:function(num){
					if(!isIntNum(num)){
						return 0;
					}
					num=parseInt(num);
					return num
				},
				replenishSubmit:function(){
					$.post(baseURL + "order/replenish/submitReplenish", {
						allSizes:JSON.stringify(vm.allSizes),replenishCount:vm.replenishCount,shoesSeq:vm.seq,remark:vm.remark
					}, function(r) {
						console.log(r)
						if(r.code==0){
							  layer.close(index)
						}
					})
				},
				changeReplenish:function(size){
					if(parseInt(size.replenish)<=0){
						size.replenish=0
						return;
					}
					size.replenish=parseInt(size.replenish);
					var sizes=vm.allSizes
					var replenishCount=0;
					for (var i = 0; i < sizes.length; i++) {
						var replenish=sizes[i].replenish
						if(!isIntNum(sizes[i].replenish)){
							replenish=0
						}
			    		replenishCount+=replenish
					}
					vm.replenishCount=replenishCount;
				},
				reload : function(event) {
					vm.showList = true;
					$("#jqGrid").jqGrid('setGridParam', {}).trigger(
							"reloadGrid");
				},
				changeTotalInventory:function(){
					vm.totalInventory=vm.totalInventory
				},
				changeTemporaryInventory:function(){
					vm.temporaryInventory=vm.temporaryInventory
				},
				restore:function(){
					$.post(baseURL + "system/allocateAndTransfer/restoreTempStock", {
					}, function(r) {
						console.log(r)
						if(r.code==0){
							alert("还原成功")
						}
					})
				},
				record:function(){
					 parent.location.href = webBase + 'index.html#modules/system/order_platform/allocate_record.html';
				},
				sort:function(type){
					var orderBy=vm.orderBy;
					var orderDir=vm.orderDir;
					if(orderBy==type){
						if(orderDir==1){
							vm.orderDir=0
						}else{
							vm.orderDir=1
						}
					}else{
						vm.orderBy=type
						vm.orderDir=0	
					}
					
					
					this.research();
				},
				
				recordSearch:function(){
					var rank=vm.rank
					var date=vm.allocateDate;
					var shoesSeq='';
					var allocateTime='';
					$("#jqGoodsGrid").jqGrid('setGridParam', {
						datatype : 'json',
						url : baseURL + 'system/allocateAndTransfer/recordList',
						postData : {
							date:date,
							shoesSeq:shoesSeq,
							allocateTime:allocateTime,
							goodId:vm.goodId,
						},
						width:"100%",
						height : 'auto',
						page : 1    
					}).trigger('reloadGrid');   
				},
				addEmpty:function(detail){
					console.log(vm.list)
					var emptys=vm.emptys;
					if(detail.addOperate){
						emptys.push({'detail':detail,'shoesSeq':vm.seq})
						vm.shopSeq=detail.seq
						console.log(detail)
						vm.allReturnSizes=JSON.parse(JSON.stringify(detail.sizeList))
						vm.returnAllTotal=0
						vm.isReturn=false
						index=  layer.open({
						    type: 1,
						    title: '退仓管理',
						    resize: false,
						    area: ['1083px', '640px'], //宽高
						    content: $('#returnPlanWindow'),
						    success:function(){
//						    	laydate.render({
//						            elem: "#replenishTime",
//						            type:'datetime',
//						            format: 'yyyy-MM-dd HH:mm', //可任意组合
//						            done: function(value, date, endDate){
//						            	 console.log(value); 
//						            	 vm.ReplenishTime=value;
//						            }
//						        });
						    },
						    cancel: function (index, layero) {
						    	console.log(index)
						      //关闭按钮
						      layer.close(index)
						    }
						  });
					}else{
						for (var i = 0; i < emptys.length; i++) {
							if(emptys[i].detail.seq==detail.seq){
								emptys.splice(i,1)
							}
						}
					}
				},
				returnSubmit:function(){
					var list=vm.list;
					for (var i = 0; i < list.length; i++) {
						if(list[i].seq==vm.shopSeq){
							list[i].sizeList=JSON.parse(JSON.stringify(vm.allReturnSizes))
						}
					}
					
					layer.close(index)
				},
				changeReturn:function(detail){
					if(detail.returnNum>=detail.returnTotal){
						detail.returnNum=detail.returnTotal
					}
					var allReturnSizes=vm.allReturnSizes
					var count=0;
					for (var i = 0; i < allReturnSizes.length; i++) {
						var returnNum=allReturnSizes[i].returnNum;
						if(!isIntNum(allReturnSizes[i].returnNum)){
							returnNum=0
						}
						count+=parseInt(returnNum)
					}
					vm.returnAllTotal=count
				},
				subtractReturn:function(detail){
					if(detail.returnNum==''&&parseInt(detail.returnNum)<=0){
						size.returnNum=0
						return;
					}
					detail.returnNum=parseInt(detail.returnNum)-1;
					var allReturnSizes=vm.allReturnSizes
					var count=0;
					for (var i = 0; i < allReturnSizes.length; i++) {
						var returnNum=allReturnSizes[i].returnNum;
						if(!isIntNum(allReturnSizes[i].returnNum)){
							returnNum=0
						}
						count+=parseInt(returnNum)
					}
					vm.returnAllTotal=count
				},
				addReturn:function(detail){
					if(detail.returnNum==''){
						detail.returnNum=0
					}
					detail.returnNum=parseInt(detail.returnNum)+1;
					if(detail.returnNum>=detail.returnTotal){
						detail.returnNum=detail.returnTotal
					}
					var allReturnSizes=vm.allReturnSizes
					var count=0;
					for (var i = 0; i < allReturnSizes.length; i++) {
						var returnNum=allReturnSizes[i].returnNum;
						if(!isIntNum(allReturnSizes[i].returnNum)){
							returnNum=0
						}
						count+=parseInt(returnNum)
					}
					vm.returnAllTotal=count
				},
				changeReturnAll:function(){
					var allReturnSizes=vm.allReturnSizes
					var count=0;
					var isReturn=vm.isReturn;
					if(isReturn){
						for (var i = 0; i < allReturnSizes.length; i++) {
							allReturnSizes[i].returnNum=allReturnSizes[i].returnTotal
							count+=parseInt(allReturnSizes[i].returnNum)
						}
					}else{
						for (var i = 0; i < allReturnSizes.length; i++) {
							allReturnSizes[i].returnNum=0
							count+=parseInt(allReturnSizes[i].returnNum)
						}
					}
					vm.returnAllTotal=count
				},
				choose : function(detail) {
					var shopOprateList=vm.shopOprateList;
					if(!detail.operate){
						var shopOprate={};	
						for (var i = 0; i < shopOprateList.length; i++) {
							if(detail.name==shopOprateList[i].shopName){
								shopOprate=shopOprateList[i]
								shopOprateList.splice(i,1)
							}
						}
						var list=vm.list
						var shopAllList=shopOprate.shopList
						for (var k = 0; k < shopAllList.length; k++) {
							for (var i = 0; i < list.length; i++) {
								if(shopOprate.shopName==list[i].name){
								var sizeList=list[i].sizeList;
								for (var j = 0; j < sizeList.length; j++) {
									if(shopAllList[k].sizeSeq==sizeList[j].sizeSeq){
										sizeList[j].stock=parseInt(shopAllList[k].stock)
										sizeList[j].operate=true
									}
								}
								}
							}
						}
						this.changeStock();
					}else{
						var shopAllList=[];
						var list=vm.list	
						for (var i = 0; i < list.length; i++) {
							var sizeList=list[i].sizeList;
							if(list[i].name==detail.name){
							for (var j = 0; j < sizeList.length; j++) {
									if(!sizeList[j].stock){
										sizeList[j].stock=0
									}
									shopAllList.push({"stock":JSON.stringify(sizeList[j].stock),"sizeSeq":sizeList[j].sizeSeq})
									sizeList[j].stock=0
									sizeList[j].operate=false
							}
							}
						}	
						var shopOprate={'shopName':detail.name,'shopList':shopAllList};
						shopOprateList.push(shopOprate)
						this.changeStock();	
					}
					
				},
				chooseSize:function(size){
					var sizeOprateList=vm.sizeOprateList;
					if(!size.operate){
						var sizeOprate={};
						for (var i = 0; i < sizeOprateList.length; i++) {
							if(size.sizeSeq==sizeOprateList[i].sizeSeq){
								sizeOprate=sizeOprateList[i]
								sizeOprateList.splice(i,1)
							}
						}
						var list=vm.list
						var sizeAllList=sizeOprate.sizeList
						for (var k = 0; k < sizeAllList.length; k++) {
							for (var i = 0; i < list.length; i++) {
								if(sizeAllList[k].shopName==list[i].name){
								var sizeList=list[i].sizeList;
								for (var j = 0; j < sizeList.length; j++) {
									if(sizeOprate.sizeSeq==sizeList[j].sizeSeq){
										sizeList[j].stock=parseInt(sizeAllList[k].stock)
										sizeList[j].operate=true
									}
								}
								}
							}
						}
						this.changeStock();
					}else{
						var sizeAllList=[];
						var list=vm.list
						for (var i = 0; i < list.length; i++) {
							var sizeList=list[i].sizeList;
							for (var j = 0; j < sizeList.length; j++) {
								if(sizeList[j].sizeSeq==size.sizeSeq){
									if(!sizeList[j].stock){
										sizeList[j].stock=0
									}
									sizeAllList.push({"stock":JSON.stringify(sizeList[j].stock),"shopName":list[i].name})
									sizeList[j].stock=0
									sizeList[j].operate=false
								}
							}
						}
						var sizeOprate={'sizeSeq':size.sizeSeq,'sizeList':sizeAllList};
						sizeOprateList.push(sizeOprate)
						this.changeStock();
					}
				},
				chooseAll:function(){
					if(vm.operate=='恢复'){
						vm.operate="取出"
						vm.list = JSON.parse(vm.operateList) 
						this.changeStock();
					}else{
						vm.operate="恢复"
						var list=vm.list;
						vm.operateList=JSON.stringify(list)
						for (var i = 0; i < list.length; i++) {
							var sizeList=list[i].sizeList;
							for (var j = 0; j < sizeList.length; j++) {
								sizeList[j].stock=0
							}
						}
						this.changeStock();
					}
				},
				goBack:function(){
					var selectYearList= $("#yearSelect2Id").val() != null ? $("#yearSelect2Id").val().toString() : '';
					var selectSeasonSeqList = $("#seasonSelect2Id").val() != null ? $("#seasonSelect2Id").val().toString() : '';
					var selectShopSeqList = $("#shopSelect2Id").val() != null ? $("#shopSelect2Id").val().toString() : '';
				
					this.showList = true;
					var p = $("#jqGrid").jqGrid('getGridParam', 'page');// 获取当前页
					var IsReplenishValue = vm.IsReplenish[0] || null;
				      setTimeout(function () {
				        $("#jqGrid").jqGrid('setGridParam', {
				          datatype: 'json',
				          postData: {
				        	'categorySeq' : vm.categorySeq,
				  			'yearList' : selectYearList,
				  			'seasonSeqList' : selectSeasonSeqList,
				  			'keywords' : vm.keywords,
				  			'orderBy' : 1,
				  			'orderDir' : 0,
				  			'saleTimeStart' : vm.saleTimeStart,
				  			'saleTimeEnd' : vm.saleTimeEnd,
				  			'sizeType' : vm.sizeType,
				  			'sizeSeqStart' : vm.minSize,
				  			'sizeSeqEnd' : vm.maxSize,
				  			'stockMinNum' : vm.minStock,
				  			'stockMaxNum' : vm.maxStock,
				  			'shopSeqList' : selectShopSeqList,
				  			'isReplenish' : IsReplenishValue
				          },
				          page: p,
				        }).trigger('reloadGrid');
				    	zTreelist();
				      }, 500);
				},
				subtract:function(detail){
					if(detail.stock==''&&parseInt(detail.stock)!=0){
						detail.stock=0
						return;
					}
					detail.stock=parseInt(detail.stock)-1;
					this.changeStock();
					
				},
				add:function(detail){
					if(detail.stock==''){
						detail.stock=0
					}
					detail.stock=parseInt(detail.stock)+1;
					this.changeStock();
				},
				down:function(detail){
					if(detail.stock==0){
						detail.stock=''
					}
				},
				// 配平操作
				changeStock : function() {
					var list = vm.list;
					var sizeList = vm.sizeList;
					for (var i = 0; i < sizeList.length; i++) {
						var size = sizeList[i].sizeSeq
						var stock = 0;
						for (var j = 0; j < list.length; j++) {
							var oldtempStock= list[j].oldtempStock;
							var stockTemp=list[j].oldStockTemp;
							var sizeList2 = list[j].sizeList;
							var changeTempStock=0;
							for (var k = 0; k < sizeList2.length; k++) {
								changeTempStock+=sizeList2[k].stock-sizeList2[k].total
								var sizeSeq = sizeList2[k].sizeSeq;
								if (size == sizeSeq) {
									var sizeStock = sizeList2[k].stock
									if (sizeStock == '') {
										sizeStock = 0
									}
									stock += parseInt(sizeStock)
								}
							}
							list[j].tempStock=oldtempStock+changeTempStock;
							list[j].stockTemp=stockTemp+changeTempStock;
						}
						sizeList[i].stock = stock
					}
				},
				create : function() {
					var sizeList = vm.sizeList;
					for (var i = 0; i < sizeList.length; i++) {
						if (sizeList[i].change - sizeList[i].stock != 0) {
							layer.alert("未配平完成")
							return;
						}
					}
					var list = vm.list;
					var changeList = []
					for (var i = 0; i < stockList.length; i++) {
						var stockGood = stockList[i]
						for (var j = 0; j < list.length; j++) {
							var listGood = list[j]
							if (stockGood.seq == listGood.seq) {
								for (var k = 0; k < stockGood.sizeList.length; k++) {
									var stockSize = stockGood.sizeList[k]
									for (var m = 0; m < listGood.sizeList.length; m++) {
										var listSize = listGood.sizeList[m]
										if (stockSize.sizeSeq == listSize.sizeSeq) {
											var stock = listSize.stock
											if (stock == '') {
												stock = 0
											}
											if (parseInt(stock)
													- parseInt(stockSize.stock) != 0) {
												var change = {
													"shopSeq" : listGood.seq,
													"change" : parseInt(stock)
															- parseInt(stockSize.stock),
													"sizeSeq" : listSize.sizeSeq,
													"shoesSeq" : vm.seq
												}
												changeList.push(change)
												break;
											}
										}
									}
								}
							}
						}

					}
					var tempStockList=[]
					for (var i = 0; i < list.length; i++) {
						var shopSeq=list[i].seq
						var tempStock=list[i].tempStock;
						tempStockList.push({"shopSeq":shopSeq,"tempStock":tempStock});
					}
					$.post(baseURL + "system/allocateAndTransfer/allocateTransferPreview", {
						allocateList:JSON.stringify(changeList),emptys:JSON.stringify(vm.emptys),tempStockList:JSON.stringify(tempStockList)
					}, function(r) {
						if(r.code==0){
							vm.allocateList=r.result
						}
					})
				},
				save : function() {
					var sizeList = vm.sizeList;
					for (var i = 0; i < sizeList.length; i++) {
						if (sizeList[i].change - sizeList[i].stock != 0) {
							layer.alert("未配平完成")
							return;
						}
					}
					var list = vm.list;
					var changeList = []
					for (var i = 0; i < stockList.length; i++) {
						var stockGood = stockList[i]
						for (var j = 0; j < list.length; j++) {
							var listGood = list[j]
							if (stockGood.seq == listGood.seq) {
								for (var k = 0; k < stockGood.sizeList.length; k++) {
									var stockSize = stockGood.sizeList[k]
									for (var m = 0; m < listGood.sizeList.length; m++) {
										var listSize = listGood.sizeList[m]
										if (stockSize.sizeSeq == listSize.sizeSeq) {
											var stock = listSize.stock
											if (stock == '') {
												stock = 0
											}
											if (parseInt(stock)
													- parseInt(stockSize.stock) != 0) {
												var change = {
													"shopSeq" : listGood.seq,
													"change" : parseInt(stock)
															- parseInt(stockSize.stock),
													"sizeSeq" : listSize.sizeSeq,
													"shoesSeq" : vm.seq
												}
												changeList.push(change)
												break;
											}
										}
									}
								}
							}
						}

					}
					var tempStockList=[]
					for (var i = 0; i < list.length; i++) {
						var shopSeq=list[i].seq
						var tempStock=list[i].tempStock;
						tempStockList.push({"shopSeq":shopSeq,"tempStock":tempStock});
					}
					$.post(baseURL + "system/allocateAndTransfer/setAllocateTransfer", {
						allocateList:JSON.stringify(changeList),emptys:JSON.stringify(vm.emptys),tempStockList:JSON.stringify(tempStockList)
					}, function(r) {
						if(r.code==0){
							alert("保存成功")
						}
					})
					this.showList = true;
					this.sizeList=[];
					this.list=[];
					this.allocateList=[];
					this.emptys=[];
					
					var p = $("#jqGrid").jqGrid('getGridParam', 'page');// 获取当前页
					var IsReplenishValue = vm.IsReplenish[0] || null;
					$("#jqGrid").jqGrid('setGridParam', {
						datatype : 'json',
						postData : {
							'categorySeq' : vm.categorySeq,
							'year' : vm.year,
							'seasonSeq' : vm.seasonSeq,
							'keywords' : vm.keywords,
							'isReplenish' : IsReplenishValue,
							'saleTimeStart' : vm.saleTimeStart,
							'saleTimeEnd' : vm.saleTimeEnd,
							'sizeType' : vm.sizeType,
							'sizeSeqStart' : vm.minSize,
							'sizeSeqEnd' : vm.maxSize,
							'stockMinNum' : vm.minStock,
							'stockMaxNum' : vm.maxStock,
							'shopSeq' : vm.selectShopSeq,
							'depositDate':vm.inDateTime
						},
						page : p,
					}).trigger('reloadGrid');
				},
				// 加载年份
				loadYears : function() {
					$.get(baseURL + "order/goods/yearList", function(data) {
						if (data.result) {
							var currYear = new Date().getFullYear();
							vm.years=data.result
							vm.year=currYear
						}
					})
					
					
					/*var currYear = new Date().getFullYear();
					this.years.push(currYear + 1);

					for (var i = 0; i < 5; i++) {
						this.years.push(currYear - i);
					}
					this.year=currYear*/
				},

				// 加载季节
				loadSeasons : function() {
					$.get(baseURL + "system/season/all", function(data) {
						if (data.result) {
							vm.seasons = data.result;
						}
					})
				},

				// 加载分类树
				loadCategory : function() {
					$.get(baseURL + "system/goodsCategory/list", function(r) {
						vm.categoryArray = r.list;
						vm.categoryArray.unshift({
							seq : 0,
							parentSeq : 0,
							name : "所有分类"
						});
					});
				},

				// 加载门店
				loadShops : function() {
					$.get(baseURL + "system/shopManage/selectList", function(
							data) {
						if (data) {
							vm.shops = data;
						}
					})
				},
				yearSelect : function(item) {
					vm.year = item;
				},

				seasonSelect : function(item) {
					vm.seasonSeq = item.seq;
					vm.selectSeasonName = item.seasonName;
				},

				categorySelect : function(item) {
					vm.categorySeq = item.seq;
					vm.selectCategoryName = item.name;
				},
				selectShop:function(item){
					vm.selectShopSeq = item.seq;
					vm.selectShopName = item.name;
				},
				
				sizeSelect : function(type, name) { // 尺码类型： 0:断码 1:缺码 2:自定义
													// 3:齐码
					vm.sizeType = type;
					vm.selectSizeName = name;
					if (type == 2) {
						vm.selectSizeName = vm.minSize + " 至 " + vm.maxSize;
					}
				},

				stockSelect : function() {
					vm.selectStockName = vm.minStock + " 至 " + vm.maxStock;
				},

				saleTimeSelect : function(type, name) {
					if (type == -1) { // 控制下拉框是否展示，方式选择时间时下拉框关闭
						$("#saleTimeDiv").addClass("open");
						name = vm.saleStart_endTime;
					} else {
						$("#saleTimeDiv").removeClass("open");
						var date=new Date();
						var year=date.getFullYear();
						var month=date.getMonth()+1;
						var day=date.getDate();
						var saleTimeStart2="";
						if(type==1){
							 saleTimeStart2=y=new Date().setDate((new Date().getDate()-1))//1天
							vm.saleTimeStart=new Date(saleTimeStart2).getFullYear()+"-"+(new Date(saleTimeStart2).getMonth()+1)+"-"+new Date(saleTimeStart2).getDate()
							vm.saleTimeEnd=year+"-"+month+"-"+day
							vm.saleTimeStart2=new Date(saleTimeStart2).getFullYear()+"-"+(new Date(saleTimeStart2).getMonth()+1)+"-"+new Date(saleTimeStart2).getDate()
							vm.saleTimeEnd2=year+"-"+month+"-"+day
						}else if(type==2){
							 saleTimeStart2=y=new Date().setDate((new Date().getDate()-15))//15天
								vm.saleTimeStart=new Date(saleTimeStart2).getFullYear()+"-"+(new Date(saleTimeStart2).getMonth()+1)+"-"+new Date(saleTimeStart2).getDate()
								vm.saleTimeEnd=year+"-"+month+"-"+day
								vm.saleTimeStart2=new Date(saleTimeStart2).getFullYear()+"-"+(new Date(saleTimeStart2).getMonth()+1)+"-"+new Date(saleTimeStart2).getDate()
								vm.saleTimeEnd2=year+"-"+month+"-"+day
						}else if(type==3){
							 saleTimeStart2=y=new Date().setDate((new Date().getDate()-30))//30天
								vm.saleTimeStart=new Date(saleTimeStart2).getFullYear()+"-"+(new Date(saleTimeStart2).getMonth()+1)+"-"+new Date(saleTimeStart2).getDate()
								vm.saleTimeEnd=year+"-"+month+"-"+day
								vm.saleTimeStart2=new Date(saleTimeStart2).getFullYear()+"-"+(new Date(saleTimeStart2).getMonth()+1)+"-"+new Date(saleTimeStart2).getDate()
								vm.saleTimeEnd2=year+"-"+month+"-"+day
						}else if(type==4){
							 saleTimeStart2=y=new Date().setDate((new Date().getDate()-7))//7天
								vm.saleTimeStart=new Date(saleTimeStart2).getFullYear()+"-"+(new Date(saleTimeStart2).getMonth()+1)+"-"+new Date(saleTimeStart2).getDate()
								vm.saleTimeEnd=year+"-"+month+"-"+day
								vm.saleTimeStart2=new Date(saleTimeStart2).getFullYear()+"-"+(new Date(saleTimeStart2).getMonth()+1)+"-"+new Date(saleTimeStart2).getDate()
								vm.saleTimeEnd2=year+"-"+month+"-"+day
						}else if(type==0){
							vm.saleTimeStart=""
							vm.saleTimeEnd=""
								vm.saleTimeStart2=""
									vm.saleTimeEnd2=""
						}
					}
					vm.saleTimeType = type;
					vm.saleStart_endTime = name;
					vm.saleTimeType2 = type;
					vm.saleStart_endTime2 = name;
				},
				saleTimeSelect2 : function(type, name) {
					if (type == -1) { // 控制下拉框是否展示，方式选择时间时下拉框关闭
						$("#saleTimeDiv2").addClass("open");
						name = vm.saleStart_endTime;
					} else {
						$("#saleTimeDiv2").removeClass("open");
						var date=new Date();
						var year=date.getFullYear();
						var month=date.getMonth()+1;
						var day=date.getDate();
						var saleTimeStart2="";
						if(type==1){
							 saleTimeStart2=y=new Date().setDate((new Date().getDate()-1))//1天
							vm.saleTimeStart2=new Date(saleTimeStart2).getFullYear()+"-"+(new Date(saleTimeStart2).getMonth()+1)+"-"+new Date(saleTimeStart2).getDate()
							vm.saleTimeEnd2=year+"-"+month+"-"+day
						}else if(type==2){
							 saleTimeStart2=y=new Date().setDate((new Date().getDate()-15))//15天
							 vm.saleTimeStart2=new Date(saleTimeStart2).getFullYear()+"-"+(new Date(saleTimeStart2).getMonth()+1)+"-"+new Date(saleTimeStart2).getDate()
							vm.saleTimeEnd2=year+"-"+month+"-"+day
						}else if(type==3){
							 saleTimeStart2=y=new Date().setDate((new Date().getDate()-30))//30天
							 vm.saleTimeStart2=new Date(saleTimeStart2).getFullYear()+"-"+(new Date(saleTimeStart2).getMonth()+1)+"-"+new Date(saleTimeStart2).getDate()
							vm.saleTimeEnd2=year+"-"+month+"-"+day
						}else if(type==4){
							 saleTimeStart2=y=new Date().setDate((new Date().getDate()-7))//7天
							 vm.saleTimeStart2=new Date(saleTimeStart2).getFullYear()+"-"+(new Date(saleTimeStart2).getMonth()+1)+"-"+new Date(saleTimeStart2).getDate()
							vm.saleTimeEnd2=year+"-"+month+"-"+day
						}else if(type==0){
							vm.saleTimeStart2=""
							vm.saleTimeEnd2=""
						}
					}
					
					vm.saleTimeType2 = type;
					vm.saleStart_endTime2 = name;
				},

				search : function(toFirstPage) {
					var selectYearList= $("#yearSelect2Id").val() != null ? $("#yearSelect2Id").val().toString() : '';
					var selectSeasonSeqList = $("#seasonSelect2Id").val() != null ? $("#seasonSelect2Id").val().toString() : '';
					var selectShopSeqList = $("#shopSelect2Id").val() != null ? $("#shopSelect2Id").val().toString() : '';

					var p = 1;
					if (!toFirstPage) {
						p = $("#jqGrid").jqGrid('getGridParam', 'page');// 获取当前页
					}
					var IsReplenishValue = vm.IsReplenish[0] || null;
					$("#jqGrid").jqGrid('setGridParam', {
						datatype : 'json',
						postData : {
							'categorySeq' : vm.categorySeq,
							'yearList' : selectYearList,
							'seasonSeqList' : selectSeasonSeqList,
							'keywords' : vm.keywords,
							'isReplenish' : IsReplenishValue,
							'saleTimeStart' : vm.saleTimeStart,
							'saleTimeEnd' : vm.saleTimeEnd,
							'sizeType' : vm.sizeType,
							'sizeSeqStart' : vm.minSize,
							'sizeSeqEnd' : vm.maxSize,
							'stockMinNum' : vm.minStock,
							'stockMaxNum' : vm.maxStock,
							'shopSeqList' : selectShopSeqList,
							'depositDate':vm.inDateTime
							
						},
						page : p,
					}).trigger('reloadGrid');
				},
				research : function() {
					// 根据货品序号查询相关信息
					$
							.get(
									baseURL
											+ "system/allocateAndTransfer/getGoodDetail?shoesSeq="
											+ vm.seq + "&saleTimeStart="
											+ vm.saleTimeStart2
											+ "&saleEndTime=" + vm.saleTimeEnd2+ "&orderBy=" + vm.orderBy+"&orderDir=" + vm.orderDir,
									function(data) {

										if (data.code == 0) {
											vm.good = data.map
											var sizeList = data.sizeList
											var  list= data.list
											vm.dataList=data.list
											vm.sizes = data.sizes
											var allSize=vm.sizeList
											for ( var j = 0; j < allSize.length; j++) {
												for ( var i = 0; i < sizeList.length; i++) {
													if(allSize[j].sizeSeq==sizeList[i].sizeSeq){
														sizeList[i].stock=allSize[j].stock
														sizeList[i].change=allSize[j].change
													}
													
												}
											}
											var allList=vm.list;
											for (var i = 0; i < list.length; i++) {
												for (  var j = 0; j < list.length; j++) {
													if(list[i].seq==allList[j].seq){
														var shopSizeList=list[i].sizeList
														var shopAllSize=allList[j].sizeList
														for (  var k = 0; k< shopSizeList.length; k++) {
															for ( var m = 0; m< shopAllSize.length; m++) {
																if(shopSizeList[k].sizeSeq==shopAllSize[m].sizeSeq){
																	shopSizeList[k].stock=shopAllSize[m].stock
																}
															}
														}
													}
												}
											}
											vm.list=list
											vm.sizeList = sizeList
										}
									})
				},
				
			},
			
			created : function() {
				this.loadCategory();
				this.loadYears();
				this.loadSeasons();
				this.loadShops();
				
				setTimeout(function() {
					$('#shopselect_id').comboSelect();
				}, 500)
				
			},
		});
