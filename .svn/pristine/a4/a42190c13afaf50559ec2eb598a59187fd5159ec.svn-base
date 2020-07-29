$(function () {
  $("#jqGrid").jqGrid({
    url: baseURL + 'order/meetingPlan/shop/list',
    datatype: "local",
    colModel: [
      {label: '门店seq', name: 'shopSeq', width: 100, hidden: true, align: "center",},
      {label: '门店名称', name: 'shopName', width: 150, align: "center"},
      {label: '区域', name: 'areaName', width: 150, align: "center"},
      {label: '所属公司', name: 'companyName', width: 100, align: "center"},
      {label: '门店ID', name: 'shopID', width: 100, hidden: true, align: "center"},
      {
        label: '操作' +
          '<button class="operation-btn-warn" style="margin-left: 30px; width:100px;padding: 1px;" onclick="batchUpload()">批量上传</button>' +
          '<button class="operation-btn-dangery" style="margin-left: 20px; width:100px;padding: 1px;" onclick="batchDel()">批量删除</button>',
        name: 'operate',
        width: 200,
        align: "center",
        formatter: function (cellvalue, options, rowObject) {
          var detailOne = '';
          if (rowObject.isUpload == 0) {
            detailOne += ('<button class="operation-btn-warn" onclick="upload(' + rowObject.shopSeq + ')">上传</button>' + '&nbsp');
          }
          if (rowObject.isUpload == 1) {
            detailOne += ('<button class="operation-btn-security" onclick="detail(' + rowObject.shopSeq + ')">查看</button>' + '&nbsp');
            detailOne += ('<button class="operation-btn-warn" onclick="upload(' + rowObject.shopSeq + ')">重新上传</button>' + '&nbsp');
            detailOne += ('<button class="operation-btn-dangery" onclick="del(' + rowObject.shopSeq + ')">删除</button>' + '&nbsp;');
          }
          return detailOne;
        }
      }],
    height: 'auto',
    rowNum: 10,
    rowList: [10, 30, 50],
    rownumbers: true,
    rownumWidth: 25,
    viewrecords: true,
    autowidth: true,
    multiselect: true,
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

      $("#jqgh_jqGrid_operation").css("height", "25px");
    }
  });


  $("#jqPlanDetailGrid").jqGrid({
    url: baseURL + "order/meetingPlan/detail",
    datatype: "local",
    colModel: [
      {label: '大类', name: 'daLeiName', width: 100, align: "center"},
      {label: '中类', name: 'zhLeiName', width: 100, align: "center"},
      {label: '风格', name: 'categoryName', width: 150, align: "center"},
      {label: '计划订货款数', name: 'planGoodsIDs', width: 100, align: "center"},
      {label: '计划订货量', name: 'planNum', width: 100, align: "center"}
    ],
    height: 'auto',
    rowNum: 10,
    rownumWidth: 25,
    autowidth: true,
    multiselect: false,
    viewrecords: true,
    pager: "#jqPlanDetailGridPager",
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
      $("#jqPlanDetailGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
    }
  });

});


var vm = new Vue({
  el: '#rrapp',
  data: {
    saleType: 2,
    uploadState: 0,
    yearList: [],
    seasonList: [],
    seasonSeq: '',
    year: ''
  },
  methods: {
    selectUploadState: function (state) {
      vm.uploadState = state;
      vm.reload();
    },
    selectByYear: function () {
      vm.reload();
    },
    selectBySeason: function () {
      vm.reload();
    },
    reload: function () {
      setTimeout(function () {
        $("#jqGrid").jqGrid('setGridParam', {
          datatype: 'json',
          postData: {
            'uploadState': vm.uploadState,
            'year': vm.year,
            'seasonSeq': vm.seasonSeq
          },
          page: 1,
        }).trigger('reloadGrid');
      }, 500);
    },
    loadYears: function () {
      var currYear = new Date().getFullYear();
      this.yearList.push(currYear + 1);

      for (var i = 0; i < 20; i++) {
        this.yearList.push(currYear - i);
      }
    },
    loadSeasons: function () {
      $.get(baseURL + "system/season/all", function (data) {
        if (data.result) {
          vm.seasonList = data.result;
        }
      })
    },
  },
  created: function () {
    this.loadYears();
    this.loadSeasons();
  },
  mounted: function () {
    this.reload();
  }
});

//详细
function detail(shopSeq) {

  if (isBlank(vm.year)) {
    layer.alert("请先选择年份", {icon: 0});
    return;
  }
  if (isBlank(vm.seasonSeq)) {
    layer.alert("请先选择季节", {icon: 0});
    return;
  }

  $("#jqPlanDetailGrid").jqGrid('setGridParam', {
    datatype: 'json',
    postData: {
      shopSeq: shopSeq,
      year: vm.year,
      seasonSeq: vm.seasonSeq
    },
    page: 1,
  }).trigger('reloadGrid');

  layer.open({
    type: 1,
    offset: '50px',
    skin: 'layui-layer-molv',
    title: "订货计划详细",
    area: ['630px', '560px'],
    shade: 0,
    shadeClose: false,
    content: jQuery("#planDetailLayer")
  });
}

//上传
function upload(shopSeq) {

  if (isBlank(vm.year)) {
    layer.alert("请先选择年份", {icon: 0});
    return;
  }
  if (isBlank(vm.seasonSeq)) {
    layer.alert("请先选择季节", {icon: 0});
    return;
  }

  var seqArr = new Array();
  seqArr[0] = shopSeq;
  $("#shopSeqArr").val(seqArr);
  $('input[id=excelFile]').click();
}

//批量上传
function batchUpload() {

  if (isBlank(vm.year)) {
    layer.alert("请先选择年份", {icon: 0});
    return;
  }
  if (isBlank(vm.seasonSeq)) {
    layer.alert("请先选择季节", {icon: 0});
    return;
  }

  var ids = $("#jqGrid").jqGrid("getGridParam", "selarrrow");
  if (ids.length == 0) {
    alert("至少勾选一个订货方");
    return false;
  }
  var seqArr = new Array();
  for (var i = 0; i < ids.length; i++) {
    var rowData = $("#jqGrid").jqGrid("getRowData", ids[i]);
    seqArr.push(rowData.shopSeq);
  }
  $("#shopSeqArr").val(seqArr);
  $('input[id=excelFile]').click();
}

//删除
function del(shopSeq) {
  var seqArr = new Array();
  seqArr[0] = shopSeq;
  dell('确定要删除此门店的订货计划？', seqArr);
}

//批量删除
function batchDel() {
  var ids = $("#jqGrid").jqGrid("getGridParam", "selarrrow");
  if (ids.length == 0) {
    alert("至少勾选一个订货方");
    return false;
  }
  var seqArr = new Array();
  for (var i = 0; i < ids.length; i++) {
    var rowData = $("#jqGrid").jqGrid("getRowData", ids[i]);
    seqArr[i] = rowData.shopSeq;
  }

  dell('确定要删除所有选中门店的订货计划？', seqArr);
}

function dell(msg, shopSeqArr) {

  if (isBlank(vm.year)) {
    layer.alert("请先选择年份", {icon: 0});
    return;
  }
  if (isBlank(vm.seasonSeq)) {
    layer.alert("请先选择季节", {icon: 0});
    return;
  }

  confirm(msg, function () {
    $.get(baseURL + "order/meetingPlan/delete?year=" + vm.year
      + "&seasonSeq=" + vm.seasonSeq + "&shopSeqArr=" + shopSeqArr, function (r) {
      if (r.code === 0) {
        alert('操作成功', function () {
          vm.reload();
        });
      } else {
        alert(r.msg);
      }
    });
  });
}


//ajax 方式提交form表单，上传excel文件 
function uploadExcel() {
  var file = $("#excelFile")[0].files[0];
  if (!file) {
    alert("选择需要导入的Excel文件！");
    return false;
  }

  var filename = file.name;
  var suffix = filename.substr(filename.lastIndexOf("."));
  if (".xls" != suffix && ".xlsx" != suffix) {
    alert("选择Excel格式的文件导入！");
    return false;
  }

  $('#form1').ajaxSubmit({
    url: baseURL + "order/meetingPlan/upload?year=" + vm.year + "&seasonSeq=" + vm.seasonSeq,
    dataType: 'json',
    success: function (r) {
      if (r.code === 0) {
        vm.reload();
      }
      alert(r.msg);
      $("#shopSeqArr").val("");
      $("#excelFile").val("");
    },
    error: function (r) {
      alert("导入excel出错！");
      $("#shopSeqArr").val("");
      $("#excelFile").val("");
    },
  });
}
