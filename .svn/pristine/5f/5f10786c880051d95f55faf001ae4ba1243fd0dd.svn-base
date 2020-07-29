//颜色尺码选择以及填库存
var atree, btree, rMenu;
$(function () {
	
	$('#saleDatePicker').datepicker({
	    autoclose: true,
	    language: 'zh-CN'
	  }).on('changeDate', saleDatePicker);
	aTreelist();
  var colModel = [
  /*  {label: '年份', name: 'year', width: 60, align: "center"},
    {label: '季节', name: 'seasonName', width: 60, align: "center"},
    {label: '分类', name: 'categoryName', width: 100, align: "center"},*/
    /*{label: '货品名称', name: 'goodName', width: 150, align: "center"},*/
    {label: '货号', name: 'goodID', width: 150, align: "center"},
    {label: '补货数量', name: 'replenishNum', width: 100, align: "center"},
  //  {label: '预计到货时间', name: 'replenishTime', width: 150, align: "center"},
    {label: '最后一次入库时间', name: 'arrivedTime', width: 150, align: "center"},
    {label: '入库总量', name: 'arrivedNum', width: 100, align: "center"},
    {label: '总仓数量', name: 'StockS', width: 100, align: "center"},
    {label: '门店总量', name: 'ShopStock', width: 100, align: "center"},
    {label: '补单时间', name: 'inputTime', width: 150, align: "center"}
    , {
   	 label: 'seq',
     name: 'seq',
     width: 105,
     align: "center",
     hidden:true
    },
    {
      label: '操作', name: 'createDate', width: 150, align: "center",


      formatter: function (cellvalue, options, rowObject) {
    	
        var detail = '';
        /*        detail += ('<button class="operation-btn-security" onclick="lineEdit(' + rowObject.seq + ')">修改</button>'
                  + '&nbsp'
                  + '<button class="operation-btn-dangery" onclick="del(' + rowObject.seq + ')">删除</button>');
                return detail;*/
        detail += '<button class="operation-btn-warn" onclick="edit(' + rowObject.seq + ')">详情</button>';
          detail += '<button class="operation-btn-security" onclick="selectReplenishPlan(' + rowObject.seq + ','+rowObject.replenishNum+','+rowObject.arrivedNum+')">到货管理</button>';
          detail += '<button class="operation-btn-dangery" onclick="del(' + rowObject.seq + ')">删除</button>';
        return detail;

      }

    }
  ];
  $('#onSaleTime').daterangepicker({
    singleDatePicker: true,
    showDropdowns: true,
    autoUpdateInput: false,
    timePicker24Hour: true,
    timePicker: true,
    locale: {
      format: 'YYYY/MM/DD HH:mm:ss',
      applyLabel: "应用",
      cancelLabel: "取消",
      customRangeLabel: '选择时间',
      daysOfWeek: ["日", "一", "二", "三", "四", "五", "六"],
      monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
      firstDay: 0
    }
  }).on('apply.daterangepicker', function (ev, picker) {
    vm.$set(vm.goodsDetail.orderPlatform, "onSaleTime", picker.startDate.format(picker.locale.format));
  });

  $('#offSaleTime').daterangepicker({
    singleDatePicker: true,
    showDropdowns: true,
    autoUpdateInput: false,
    timePicker24Hour: true,
    timePicker: true,
    locale: {
      format: 'YYYY/MM/DD HH:mm:ss',
      applyLabel: "应用",
      cancelLabel: "取消",
      customRangeLabel: '选择时间',
      daysOfWeek: ["日", "一", "二", "三", "四", "五", "六"],
      monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
      firstDay: 0
    }
  }).on('apply.daterangepicker', function (ev, picker) {
    vm.$set(vm.goodsDetail.orderPlatform, "offSaleTime", picker.startDate.format(picker.locale.format));
  });
  $('#inDatePicker').datepicker({
	    autoclose: true,
	    language: 'zh-CN',
	    format: "yyyy-mm-dd"//日期格式
	  }).on('changeDate', datePicker);
  



  loadGoodsGrid(colModel)
})

var setting1 = {
  view: {
    dblClickExpand: false,
   
  },
  data: {
    keep: {
      leaf: true,
      parent: true
    },
    simpleData: {
      enable: true,
      idKey: "seq",
      pIdKey: "parentSeq",
      rootPId: 0
    },
    key: {
      url: "nourl"
    }
  },
  check: {
    enable: false,
    nocheckInherit: false,
  },
  edit: {
    drag: {
      isCopy: false,
      isMove: false
    },
    enable: false,// 设置是否处于编辑状态
    editNameSelectAll: true,
  },
  callback: {
    onClick: zTreeOnClick,
  }

};

var setting = {
		  data: {
		    simpleData: {
		      enable: true,
		      idKey: "seq",
		      pIdKey: "parentSeq",
		      rootPId: -1
		    },
		    key: {
		      url: "nourl"
		    }
		  },
		  callback: {
//		    onClick: categoryClick
		  }
		};
var ztree;
function zTreeOnClick(event, treeId, treeNode) {

	  if (treeNode.checked || treeNode.seq == 0) {
	    return;
	  }
	  atree.checkNode(treeNode, true, true);
	  vm.days=treeNode.name;
	   $("#jqGrid").jqGrid('setGridParam', {
	         datatype: 'json',
	         postData: {
	           'inputTime': treeNode.name,
	         },
	         page: 1,
	       }).trigger('reloadGrid');
	};

function aTreelist() {

	  $.get(baseURL + "order/replenish/daysList", function (r) {
	    var sxs = [];
	  
	    if (r.code == 0) {
	    	
	      var list = r.result;
	      
	      if (list && list.length > 0) {
	        for (var i = 0; i < list.length; i++) {
	          sxs.push({
	        	  name: list[i],
	        	  parentSeq: 0,
	          });
	        }
	      } else {
	        layer.msg("未查询到属性")
	      }

	    } else {
	      alert(r.msg);
	    }
	    sxs.push({
	    	 seq: 0,
	         parentSeq: -1,
	         name: "补单时间"
	    })
	    atree = $.fn.zTree.init($("#SXTree"), setting1, sxs);
	    atree.expandAll(true);
	  });


}

function loadGoodsGrid(colModel) {
	
  $("#jqGrid").jqGrid({
    url: baseURL + 'order/replenish/replenishList',
    datatype: "json",
    mtype: "POST",
    postData: {
      'inputTime':vm.inputTime
    },
    colModel: colModel,
    height: 'auto',
    rowNum: 10,
    rowList: [10, 30, 50],
    rownumbers: true,
    rownumWidth: 25,
    autowidth: true,
    multiselect: true,
    viewrecords: true,
//	    shrinkToFit:false,
//	    autoScroll: true,
    pager: "#jqGridPager",
    jsonReader: {
      root: "page.list",
      page: "page.currPage",
      total: "page.totalPage",
      records: "page.totalCount"
    },
    prmNames: {
      page: "page",
      rows: "limit",
      order: "order"
    },
    gridComplete: function () {
      // 隐藏grid底部滚动条
      $("#jqGrid").closest(".ui-jqgrid-bdiv").css({
        "overflow-x": "hidden"
      });
   /*   var rowIds = $("#jqGrid").jqGrid('getDataIDs');
      for(var k=0; k<rowIds.length; k++) {
          var curRowData = $("#jqGrid").jqGrid('getRowData', rowIds[k]);
      	var seq=curRowData.seq
      	if(seq!=''){
      		jQuery("#jqGrid").jqGrid('setSelection',rowIds[k]);
      	}
      }*/
    }
  });
}



var vm = new Vue({
  el: '#rrapp',
/*  components: {
    'stockItem': stockItem
  },*/
  data: {
    orderPlatformShow: false,
    onlineSaleShow: false,
    categoryArray: [],
    colorArray: [],
    sizeArray: [],
    sxArray: [],
    periods: {},
    years: [],
    seasons: [],
    stores: [],
    IsMainpush: [],
    IsNewest: [],
    IsReplenish:[],
    
    // 表格加载条件
    currentPeriodYear: '',
    periodSeq: -1,
    selectPeriodName: '',
    categorySeq: '',
    selectCategoryName: '',
    keywords: '',
    onSale: -1,
    year: '',
    seasonSeq: '',
    selectSeasonName: '',
    push: '',
    //界面显示属性
    title: '',
    colorKeywords:'',
    
    
    //颜色、尺码、库存
    orderPlatformStock: [],
    onlineSaleStock: [],

    // 商品详情内容
    goodsDetail: {
      orderPlatform: {author: [], onSale: false},
      onlineSale: {},
      description: [],
      banner: [],
    },
    //补货计划
    
    days:'',
    surplus:0,
    replenishSeq:'',
    ReplenishTime:'',
    ReplenishNum:'',
    replenishPlans: [],
    showList: true,
    title: '',
    replenish: {},
    replenishDetail:[],
    shoesReplenishEntity:'',
    inDateTime:'',
  },
  methods: {
	  search:function(){
		var inDateTime=vm.inDateTime;
		console.log(inDateTime)
		  $("#jqGrid").jqGrid('setGridParam', {
		         datatype: 'json',
		         postData: {
		           'inputTime': inDateTime,
		         },
		         page: 1,
		       }).trigger('reloadGrid');
	  },
	  exportRecord:function(){
		  var ids = $("#jqGrid").jqGrid("getGridParam", "selarrrow"); 
		  var seqs=[];
		  $(ids).each(function (index, id){  
			     //由id获得对应数据行  
			var row = $("#jqGrid").jqGrid('getRowData', id);  
			seqs.push(row.seq);
			}) 
			var form = document.getElementById('exportQRCodeForm');
          	form.action = baseURL + "order/replenish/exportRecord";
          	form.seqs.value=JSON.stringify(seqs),
          	form.token.value = token;
          	form.submit();
			
	  },
	  add:function(){
		  vm.reload();
	      vm.showList = false;
	      vm.title='新增补货计划';
	  },
	  reload: function (event) {
	      vm.replenish = {};
	      vm.showList = true;
	      $("#jqGrid").jqGrid('setGridParam', {}).trigger("reloadGrid");
	    },
	    saveOrUpdate: function () {
	        if (vm.validator()) {
	          return;
	        }
	        var url = vm.replenish.seq ? "order/replenish/update"
	          : "order/replenish/add";
	        console.log(vm.replenish)
	        $.ajax({
	          type: "POST",
	          url: baseURL + url,
	          data: JSON.stringify(vm.replenish),
	          contentType: "application/json",
	          cache: false,
	          success: function (r) {
	            if (r.code === 0) {
	              alert(r.msg);
	              vm.reload();
	            } else {
	              alert(r.msg);
	            }
	          }
	        });
	      },
	      validator: function () {
	          if (isBlank(vm.replenish.goodID)) {
	            alert("货品名称不能为空！");
	            return true;
	          }
	          if (isBlank(vm.replenish.replenishTime)) {
	            alert("预计到货时间不能为空！");
	            return true;
	          }
	          if (isBlank(vm.replenish.replenishNum)) {
		          alert("补货数量不能为空！");
		          return true;
		      }
	        },
    loadYears: function () {
      var currYear = new Date().getFullYear();
      this.years.push(currYear + 1);

      for (var i = 0; i < 5; i++) {
        this.years.push(currYear - i);
      }
    },

    loadGoodsSX: function () {
      $.get(baseURL + "order/goods/getGoodsSXOption", function (r) {
        if (r.code === 0 || r.code === '0') {
          vm.sxArray = r.result;
        }
      });
    },
    reloadGoods: function (toFirstPage) {
      vm.showList = true;
      /*      if (!vm.periodSeq) {
              return;
            }*/

      var p = 1;
      if (!toFirstPage) {
        p = $("#jqGrid").jqGrid('getGridParam', 'page');//获取当前页
      }
      setTimeout(function () {
        $("#jqGrid").jqGrid('setGridParam', {
          datatype: 'json',
          postData: {
            'categorySeq': vm.categorySeq,
            'year': vm.year,
            'seasonSeq': vm.seasonSeq,
            'keywords': vm.keywords,
            'IsMainpush': vm.IsMainpush,
            'IsNewest': vm.IsNewest,
            'IsReplenish':vm.IsReplenish,
            'colorKeywords':vm.colorKeywords,
          },
          page: p,
        }).trigger('reloadGrid');
      }, 500);
    },
//    handleDetailFileChange: function (event) {
//      var inputDOM = event.target;
//      var files = event.target.files;
//      for (var i = 0; i < files.length; i++) {
//        if (files[i].size >= 10 * 1024 * 1024) {
//          alert("文件超过10M啦！");
//          return;
//        }
//      }
//
//      randerImages(0, inputDOM, vm.goodsDetail.description);
//    },
//    handleBandFileChange: function (value) {
//      var inputDOM = value.target;
//      if (inputDOM.files.length + vm.goodsDetail.banner.length > 5) {
//        alert("轮播图不能超过5张！");
//        return;
//      }
//      for (var fileSize = 0; fileSize < inputDOM.files.length; fileSize++) {
//        var size = Math.floor(inputDOM.files[fileSize].size / 1024 / 1024);
//        if (size > 10) {
//          alert("文件超过10M啦！");
//          return;
//        }
//      }
//
//      randerImages(0, inputDOM, vm.goodsDetail.banner);
//    },
//    handleVideoFileChange: function (value) {
//      var inputDOM = value.target;
//
//      var size = Math.floor(inputDOM.files[0].size / 1024 / 1024);
//
//      if (size > 20) {
//        alert("文件超过20M啦！");
//        return;
//      }
//      vm.goodsDetail.videoFile = inputDOM.files[0];
//    },
//    /*    periodChoose: function (item) {
//          vm.$set(vm.goodsDetail, "periodName", item.name);
//          vm.goodsDetail.periodSeq = item.seq;
//        },*/
//    dragStartImage: function (index, event) {
//      // console.log("moveImage:", index, event)
//      var fromTarget = event.target;
//      var imageType = fromTarget.parentNode.parentNode.id;
//      // console.log("fromTarget:", fromTarget, imageType)
//      event.dataTransfer.setData("fromIndex", index);
//      event.dataTransfer.setData("fromType", imageType);
//    },
//    dragEnterImage: function (index, event) {
//      // console.log("dragEnterImage:",index,event)
//      //目标元素边框改为红色
//      var toTarget = event.target;
//      // console.log("改变相框颜色为红色")
//      toTarget.parentNode.style.border = "1px solid red";
//    },
//    dragOverImage: function (index, event) {
//      // console.log("dragOverImage:",index,event)
//    },
//    dragLeaveImage: function (index, event) {
//      // console.log("dragLeaveImage:",index,event)
//      // console.log("还原相框颜色")
//      var toTarget = event.target;
//      toTarget.parentNode.removeAttribute("style");
//    },
//    dropImage: function (index, event) {
//      // console.log("dropImage:", index, event);
//      var toTarget = event.target;
//      var imageType = toTarget.parentNode.parentNode.id;
//      // console.log("toTarget:", toTarget, imageType)
//
//      var fromIndex = event.dataTransfer.getData("fromIndex");
//      var fromType = event.dataTransfer.getData("fromType");
//
//      //去除红色边框
//      toTarget.parentNode.removeAttribute("style");
//
//      //判断源对象和目标对象是否相同
//      if (fromType == imageType && fromIndex != index) {
//        //相同类型的不同图片进行排序操作
//        // console.log("图片位置进行交换...")
//        if (imageType == "bannerImages") {
//          //轮播图
//          swapArr(vm.goodsDetail.banner, fromIndex, index);
//        } else {
//          //描述图
//          swapArr(vm.goodsDetail.description, fromIndex, index);
//        }
//      } else {
//        //不同类型的图片或同类型索引相同的图片不进行操作
//        // console.log("忽略不操作")
//      }
//
//    },
//    /*查询订货、分销 上架或待上架列表*/
//    upLoad: function (num) {
//      this.onSale = num;
//      setTimeout(function () {
//        $("#jqGrid").jqGrid('setGridParam', {
//          postData: {
//            'onSale': vm.onSale,
//          },
//          page: 1,
//        }).trigger('reloadGrid');
//      }, 100);
//    },
    save:function(){
    	var replenishTime=vm.ReplenishTime;
    	var replenishNum=vm.ReplenishNum;
    	var surplus=vm.surplus;
    	if(!replenishTime){
    		alert("未输入到货时间");
   		  return;
    	}
    	if(!replenishNum){
    		alert("未输入到货数量");
     		  return;
    	}
    	if(surplus<replenishNum){
    		  alert("到货总数大于入库总数，剩余到货数量为"+surplus);
    		  return;
    	}
    $.post(baseURL + "order/replenish/save",
    {replenishSeq: vm.replenishSeq, replenishTime: replenishTime, replenishNum: replenishNum},
    function (data) {
    
      if (data.code === 0) {
    	  location.reload();
      } else {
        alert(data.msg);
      }
    })
    	
    	
    },
    uploadGoodsExcel: function () {
      parent.window.showLoading();
      $('#goodsExcelForm').ajaxSubmit({
        url: baseURL + 'order/goods/uploadGoodsExcel',
        dataType: 'json',
        success: function (r) {
          if (r.code === 0) {
            alert(r.msg);
            location.reload();
          } else {
            alert(r.msg);
          }
          $("#goodsExcelForm").resetForm();
          parent.window.hideLoading();
        },
        error: function (r) {
          alert("导入商品出错！");
          $("#goodsExcelForm").resetForm();
          parent.window.hideLoading();
        }
      });
    },

    /*导入补货计划文件*/
    changeUploadFile: function (event) {
      if (!event.target.files[0]) {
        return;
      }
      this.excelFile = event.target.files[0];

      parent.window.showLoading();

      $("#uploadExcelForm").ajaxSubmit({
        url: baseURL + 'system/shoesreplenish/uploadExcel',
        dataType: 'json',
        data: {},
        cache: false,
        success: function (data) {
          if (data.code == 0) {
            layer.msg(data.msg);
          } else {
            layer.alert(data.msg);
          }
          $("#uploadExcelForm").resetForm();
          parent.window.hideLoading();
        },
        error: function (data) {
          alert("导入补货计划出错！");
          $("#uploadExcelForm").resetForm();
          parent.window.hideLoading();
        }
      });
    }
  },
  created: function () {
    this.loadGoodsSX();
  },
});





function upAndDown(seq, platform, upDown) {
  $.post(baseURL + "order/goods/updown",
    {seq: seq, platform: platform, updown: upDown},
    function (data) {
      if (data.code === 0) {
        vm.reloadGoods(false);
      } else {
        alert(data.msg);
      }
    });
}

/*对尺码大小进行排序    sizeArray*/
function sizeSortNumber(a, b) {
  var _a = parseInt(a.sizeName).toString();
  var _b = parseInt(b.sizeName).toString();
  if (_a == "NaN") {
    return 1;
  }
  if (_b == "NaN") {
    return -1;
  }
  return _a - _b;
}




/**
 * 查询补货计划
 */
function selectReplenishPlan(replenishSeq,replenishNum,arrivedNum) {
	if(!arrivedNum){
		arrivedNum=0;
	}
	vm.replenishSeq=replenishSeq
	vm.surplus=replenishNum-arrivedNum;
  $.get(baseURL + "order/replenish/arrivedList?replenishSeq=" + replenishSeq, function (data) {
	  
    if (data.code == 0) {
      vm.replenishPlans = data.result;
      readReplenishPlan();
    } else {
      layer.alert(data.msg)
    }
  })

}
function del(replenishSeq){
	 $.get(baseURL + "order/replenish/delete?seq=" + replenishSeq, function (r) {
		    if (r.code == 0) {
		      vm.reload();
		      aTreelist() ;
		      
		    } else {
		      alert(r.msg);
		    }

	});
}
//查询详情
function edit(seq){
	  $.get(baseURL + "order/replenish/replenishNearOne?replenishSeq=" + seq, function (data) {
			 console.log(data)
			  vm.replenishDetail=data.result
			  vm.shoesReplenishEntity=data.shoesReplenishEntity
			  layer.open({
				    type: 1,
				    title: '补单详情',
				    resize: false,
				    area: ['700px', '450px'], //宽高
				    content: $('#replenishDetail'),
				    success:function(){
				    	
				    },
				    cancel: function (index, layero) {
				      //关闭按钮
				      layer.close(index)
				    }
				  });
		  })
}


/**
 * 查看补货计划
 */
function readReplenishPlan() {
  layer.open({
    type: 1,
    title: '补货计划',
    resize: false,
    area: ['700px', '450px'], //宽高
    content: $('#replenishPlanWindow'),
    success:function(){
    	laydate.render({
            elem: "#replenishTime",
            type:'datetime',
            format: 'yyyy-MM-dd HH:mm', //可任意组合
            done: function(value, date, endDate){
            	 console.log(value); 
            	 vm.ReplenishTime=value;
            }
        });
    },
    cancel: function (index, layero) {
      //关闭按钮
      layer.close(index)
    }
  });
}
function saleDatePicker(ev) {
	  vm.replenish.replenishTime = ev.date ? ev.date.format('yyyy/MM/dd HH:mm:ss') : "";
	}
function datePicker(ev) {
	  vm.inDateTime = ev.date ? ev.date.format('yyyy-MM-dd') : "";
	}
