//调拨管理
$(function() {
	$('#recordDatePicker').datepicker({
	    autoclose: true,
	    language: 'zh-CN',
	    format: "yyyy-mm-dd"//日期格式
	  }).on('changeDate', saleDatePicker);
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
	
	var colModel = [
		{
			label : '日期',
			name : 'allocateDate',
			width : 60,
			align : "left",
			formatter : function(cellvalue,options,rowObject) {
				var allocateDate=rowObject.allocateDate;
				var allocateTime=rowObject.allocateTime;
				var shoesSeq=rowObject.shoesSeq;
				var shoeName=rowObject.shoeName;
				var msg=rowObject.message;
				var state=rowObject.state;
				var detail='';
				if(state==0){//未处理
					detail+='<div style="width:30px;height:10px;background-color:#CD3333;display:inline-block;margin-left: 30px;"></div>'
				}else if(state==1){//部分处理
					detail+='<div style="width:30px;height:10px;background-color:#EEB422;display:inline-block;margin-left: 30px;"></div>'
				}else{//全部处理
					detail+='<div style="width:30px;height:10px;background-color:#4F94CD;display:inline-block;margin-left: 30px;"></div>'
				}
				detail+= '<input type="checkbox" style="margin-left: 60px;" id='+options.rowId+' name='+options.rowId+' class="detail" name="detail"  onclick=choose("'
					+ allocateDate + '","'
					+ allocateTime+ '","'
					+ shoesSeq + '")>'+'<span onclick=recordList("'
					+ allocateDate + '","'
					+ allocateTime+ '","'
					+ shoesSeq + '","'
					+ shoeName + '") >'+cellvalue+'</span>';
				if(msg){
					detail+='<div style="background:#C0C0C0;border-radius:2px;color:rgba(255,255,255,1);margin-left: 20px;width:50px;height:20px;font-size:12px;display:inline-block;text-align:center;line-height:20px;" >'+msg+'</div>'
				}
				return detail;
			}
		},
		{
			label : '货号',
			name : 'shoeName',
			width : 120,
			align : "center",
			formatter : function(cellvalue,options,rowObject) {
				var allocateDate=rowObject.allocateDate;
				var allocateTime=rowObject.allocateTime;
				var shoesSeq=rowObject.shoesSeq;
				var shoeName=rowObject.shoeName;
				var detail = '<span onclick=recordList("'
					+ allocateDate + '","'
					+ allocateTime+ '","'
					+ shoesSeq +'","'
					+ shoeName +  '") >'+cellvalue+'</span>';
				return detail
			}
		},
		{
			label : '时间点',
			name : 'allocateTime',
			width : 60,
			align : "center",
			formatter : function(cellvalue,options,rowObject) {
				var allocateDate=rowObject.allocateDate;
				var allocateTime=rowObject.allocateTime;
				var shoesSeq=rowObject.shoesSeq;
				var shoeName=rowObject.shoeName;
				var detail = '<span onclick=recordList("'
					+ allocateDate + '","'
					+ allocateTime+ '","'
					+ shoesSeq +'","'
					+ shoeName + '") >'+cellvalue+'</span>';
				return detail
			}
		},
		{
			label : '操作',
			width : 50,
			align : "center",
			formatter : function(cellvalue, options,
					rowObject) {
				var allocateDate=rowObject.allocateDate;
				var allocateTime=rowObject.allocateTime;
				var shoesSeq=rowObject.shoesSeq;
				var shoeName=rowObject.shoeName;
				var msg=rowObject.message;
				var detail='';
				if(msg){
					detail+='<button class="operation-btn-warn"onclick=pushAgain("'
					+ allocateDate + '","'
					+ allocateTime+ '","'
					+ shoesSeq +'","'
					+ shoeName + '")>再次推送</button>'
				}
				return detail+'<button class="operation-btn-security"onclick=recordList("'
				+ allocateDate + '","'
				+ allocateTime+ '","'
				+ shoesSeq +'","'
				+ shoeName + '")>查看详情</button>' + '<button class="operation-btn-dangery"  onclick=delOne("'
				+ allocateDate + '","'
				+ allocateTime+ '","'
				+ shoesSeq + '")>删除</button>';
			}
		},
		
		
		{
			label : 'seq',
			name : 'shoesSeq',
			width : 60,
			align : "center",
			hidden : true
		},
		{
			label : 'allocateDate',
			name : 'allocateDate',
			width : 60,
			align : "center",
			hidden : true
		},
		{
			label : 'shoeName',
			name : 'shoeName',
			width : 120,
			align : "center",
			hidden : true
		},
		{
			label : 'allocateTime',
			name : 'allocateTime',
			width : 60,
			align : "center",
			hidden : true
		},
		];
	var colModel2=[
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
			label : '调拨单号',
			name : 'erpOrderNum',
			width : 60,
			align : "center",
//			formatter : function(cellvalue,options,rowObject) {
//				return '待开发'
//			}
			
		},
		{
			label : '物流',
			name : 'expressName',
			width : 60,
			align : "center",
//			formatter : function(cellvalue,options,rowObject) {
//				return '待开发'
//			}
		},
		{
			label : '物流单号',
			name : 'expressNum',
			width : 60,
			align : "center",
//			formatter : function(cellvalue,options,rowObject) {
//				return '待开发'
//			}
		},
		{
			label : '备注',
			name : 'remark',
			width : 60,
			align : "center"
		},
		{
			label : '尺码/数量',
			name : 'records',
			width : 60,
			align : "center",
			formatter : function(cellvalue,options,rowObject) {
				var detail ='';
				for (var i = 0; i < cellvalue.length; i++) {
					detail+=cellvalue[i].size+"/"+cellvalue[i].num+"    "
				}
				return detail
			}
		},
		{
			label : '处理进度',
			name : 'state',
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
	loadGoodsGrid(colModel);
	
	loadGoodsGrid2(colModel2);
})
function delOne(date,time,shoesSeq){
	var allChoose=[];
	var details = document.getElementsByName("detail");
	allChoose.push({'date':date,'time':time,'shoesSeq':shoesSeq})
	vm.allChoose=allChoose;
	vm.del();
	  var e = getEvent();
		if (window.event) {
			e.cancelBubble = true;//阻止冒泡
		} else if (e.preventDefault) {
			e.stopPropagation();//阻止冒泡
		}
}
function choose(date,time,shoesSeq){
	var allChoose=vm.allChoose;
	var count=0
 	var details = $(".detail");
 	for (var i = 0; i < details.length; i++) {
 		if(details[i].checked){
 			count+=1
 		} 
 	}
 	if(count!=details.length){
 		vm.total=false
 	}else{
 		vm.total=true
 	}
	for (var i = 0; i < allChoose.length; i++) {
		var choose=allChoose[i]
		if(choose.date==date&&choose.time==time&&choose.shoesSeq==shoesSeq){
			allChoose.splice(i,1)
			return;
		}
	}
	allChoose.push({'date':date,'time':time,'shoesSeq':shoesSeq})
	    var e = getEvent();
		if (window.event) {
			e.cancelBubble = true;//阻止冒泡
		} else if (e.preventDefault) {
			e.stopPropagation();//阻止冒泡
		}
}
function pushAgain(date,time,shoesSeq,shoesName){
	var allChoose=[];
	allChoose.push({'date':date,'time':time,'shoesSeq':shoesSeq})
	vm.allChoose=allChoose
	vm.pushMessage();
	    var e = getEvent();
		if (window.event) {
			e.cancelBubble = true;//阻止冒泡
		} else if (e.preventDefault) {
			e.stopPropagation();//阻止冒泡
		}
}



function recordList(date,time,shoesSeq,shoesName){
	vm.shoesName=shoesName;
	$("#jqGoodsGrid").jqGrid('setGridParam', {
		datatype : 'json',
		url : baseURL + 'system/allocateAndTransfer/recordList',
		postData : {
			date:date,
			shoesSeq:shoesSeq,
			allocateTime:time,
		},
		width:"100%",
		height : 'auto',
		page : 1    
	}).trigger('reloadGrid');  
	  layer.open({
        type: 1,
        offset: '50px',
        skin: 'layui-layer-molv',
        area: ['1200px', '700px'],
        shade: 0,
        shadeClose: false,
        content: jQuery("#goodsLayer"),
	  })
	  var e = getEvent();
		if (window.event) {
			e.cancelBubble = true;//阻止冒泡
		} else if (e.preventDefault) {
			e.stopPropagation();//阻止冒泡
		}
}

function getEvent() {
	if (window.event) {
		return window.event;
	}
	func = getEvent.caller;
	while (func != null) {
		var arg0 = func.arguments[0];
		if (arg0) {
			if ((arg0.constructor == Event
					|| arg0.constructor == MouseEvent || arg0.constructor == KeyboardEvent)
					|| (typeof (arg0) == "object" && arg0.preventDefault && arg0.stopPropagation)) {
				return arg0;
			}
		}
		func = func.caller;
	}
	return null;
}

function loadGoodsGrid(colModel) {
	//查询最近一天
	$.get(baseURL + "system/allocateAndTransfer/getNearOne",function(r) {
		console.log(r)
		if(r.date){
			vm.saleTimeStart=r.date
			vm.saleTimeEnd=r.date
			vm.saleStart_endTime = vm.saleTimeStart + " ~ " + vm.saleTimeEnd;
		}else{
			var date=new Date();
			var year=date.getFullYear();
			var month=date.getMonth()+1;
			var day=date.getDate();
			vm.saleTimeStart=year+"-"+month+"-"+day
			vm.saleTimeEnd=year+"-"+month+"-"+day
			vm.saleStart_endTime = vm.saleTimeStart + " ~ " + vm.saleTimeEnd;	
		}
		$("#jqGrid").jqGrid({
			url : baseURL + 'system/allocateAndTransfer/allList',
			datatype : "json",
			mtype : "GET",
			postData : {
				'startDate':vm.saleTimeStart,
				'endDate':vm.saleTimeEnd,
				'goodId':vm.goodId
			},
			colModel : colModel,
			height : 'auto',
			rowNum : 1000,
			rowList : [ 1000 ],
			rownumbers : true,
			rownumWidth : 25,
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
			},
			onCellSelect:function(rowid,iRow,iCol,e){
				var row = $("#jqGrid").jqGrid('getRowData', rowid);
				
				var checked=$("input[name="+rowid+"]").is(":checked");
				if(checked){
					$("input[name="+rowid+"]").prop("checked",false);
				}else{
					$("input[name="+rowid+"]").prop("checked",true);
				}
				var date=row.allocateDate;
				var time=row.allocateTime;
				var shoesSeq=row.shoesSeq;
				choose(date,time,shoesSeq)
			}
		});
		
	})
	
	
	
	
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

var index;

var vm = new Vue(
		{	
			el : '#rrapp',
			data : {
				date:'',
				total:false,
				saleStart_endTime:'',
				saleTimeType:'',
				saleTimeStart:'',
				saleTimeEnd:'',
				allChoose:[],
				shoesSeq:'',
				allocateTime:'',
				shoesName:'',
				goodId:'',
			},
			methods : {
				allocate:function(){
					 parent.location.href = webBase + 'index.html#modules/system/order_platform/allocate_transfer.html';
				},
				
				exportRecord:function(){
					index = layer.open({
			            type: 1,
			            offset: '50px',
			            skin: 'layui-layer-molv',
			            title: "选择导出方向",
			            area: ['500px', '200px'],
			            shade: 0,
			            shadeClose: false,
			            content: jQuery("#exportRecordLayer")
			          });
				},
				
				exportRecordHorizontal:function(){
					var form = document.getElementById('exportQRCodeForm');
		            form.action = baseURL + "system/allocateAndTransfer/exportRecordHorizontal";
		            form.allChoose.value=JSON.stringify(vm.allChoose),
		            form.token.value = token;
		            form.submit();
		            layer.close(index);
				},
				
				exportRecordVertical:function(){
					var form = document.getElementById('exportQRCodeForm');
		            form.action = baseURL + "system/allocateAndTransfer/exportRecordVertical";
		            form.allChoose.value=JSON.stringify(vm.allChoose),
		            form.token.value = token;
		            form.submit();
		            layer.close(index);
				},
				
				pushMessage:function(){
					console.log(vm.allChoose)
				var allChoose=JSON.stringify(vm.allChoose)
	            	$.post(baseURL + "system/allocateAndTransfer/pushRecord", {
	            		allChoose:allChoose,
	    					}, function(r) {
	    						alert("推送成功")
	    						vm.allChoose=[];
	    						var p = $("#jqGrid").jqGrid('getGridParam', 'page');// 获取当前页
	    						$("#jqGrid").jqGrid('setGridParam', {
	    							datatype : 'json',
	    							postData : {
	    								'startDate':vm.saleTimeStart,
	    								'endDate':vm.saleTimeEnd,
	    								'goodId':vm.goodId
	    							},
	    							page : p,
	    						}).trigger('reloadGrid');
	    		})
				},
				del:function(){
					confirm("确认删除？", function () {
						var allChoose=JSON.stringify(vm.allChoose)
						$.post(baseURL + "system/allocateAndTransfer/deleteRecord", {
							allChoose:allChoose
    					}, function(r) {
    						alert("删除成功")
    						vm.allChoose=[];
    						var p = $("#jqGrid").jqGrid('getGridParam', 'page');// 获取当前页
    						$("#jqGrid").jqGrid('setGridParam', {
    							datatype : 'json',
    							postData : {
    								'startDate':vm.saleTimeStart,
    								'endDate':vm.saleTimeEnd,
    								'goodId':vm.goodId
    							},
    							page : p,
    						}).trigger('reloadGrid');
    				})
					})
				
				},
				chooseAll:function(){
					var total=vm.total
					var allChoose=[]
					if(total){
						var rowSet = $("#jqGrid").jqGrid('getRowData');
						 var allCountID = $("#jqGrid").jqGrid('getDataIDs'); 
						 rowSet.push($("#jqGrid").jqGrid('getRowData', allCountID[allCountID.length-1]));
						 var details =$(".detail");
						      for (var i = 0; i <details.length; i++) {
						    	  details[i].checked = true;
						  }
						   for (var i = 0; i < rowSet.length; i++) {
							   allChoose.push({'date':rowSet[i].allocateDate,'time':rowSet[i].allocateTime,'shoesSeq':rowSet[i].shoesSeq})
						   }
						 
					}else{
						 var details=$(".detail");
					      for (var i = 0; i < details.length; i++) {
					    	  details[i].checked = false;
					     }
					}
					  vm.allChoose=allChoose
				},
			
				saleTimeSelect : function(type, name) {
					console.log(type)
					if (type == -1) { // 控制下拉框是否展示，方式选择时间时下拉框关闭
						$("#saleTimeDiv").addClass("open");
						name = vm.saleStart_endTime;
					} else {
						$("#saleTimeDiv").removeClass("open");
						var date=new Date();
						var year=date.getFullYear();
						var month=date.getMonth()+1;
						var day=date.getDate();
						if(type==0){
							vm.saleTimeStart=""
							vm.saleTimeEnd=""
								vm.saleTimeStart2=""
									vm.saleTimeEnd2=""
						}
					}
					
					vm.saleTimeType = type;
					vm.saleStart_endTime = name;
				},
				search : function(toFirstPage) {
					var p = 1;
					vm.allChoose=[];
					if (!toFirstPage) {
						p = $("#jqGrid").jqGrid('getGridParam', 'page');// 获取当前页
					}
					$("#jqGrid").jqGrid('setGridParam', {
						datatype : 'json',
						postData : {
							'startDate':vm.saleTimeStart,
							'endDate':vm.saleTimeEnd,
							'goodId':vm.goodId
						},
						page : p,
					}).trigger('reloadGrid');
				},
			},	
			created : function() {
				
			},
		});
function saleDatePicker(ev) {
	  vm.date = ev.date ? ev.date.format('yyyy-MM-dd') : "";
	}