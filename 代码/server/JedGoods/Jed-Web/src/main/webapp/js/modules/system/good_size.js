$(function () {
  $("#jqGrid")
    .jqGrid(
      {
        url: baseURL + 'system/goodsSize/list',
        datatype: "json",
        colModel: [
          /*{
            label: '尺码编码',
            name: 'sizeCode',
            width: 100,
            align: "center"
          },*/
          {
            label: '尺码名称',
            name: 'sizeName',
            width: 250,
            align: "center"
          },
          {
            label: '建立时间',
            name: 'inputTime',
            width: 200,
            align: "center"
          },
          {
            label: '操作',
            name: '',
            width: 70,
            align: "center",
            formatter: function (cellvalue, options,
                                 rowObject) {
              var detail = '<button onclick="lineEdit('
                + rowObject.seq
                + ')" class="operation-btn-security" ">编辑</button>'
                + '<button class="operation-btn-dangery" onclick="del('
                + rowObject.seq
                + ')">删除</button>';
              return detail;
            }
          }],
        height: 'auto',
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: false,
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
        }
      });
});

var vm = new Vue({
  el: '#rrapp',
  data: {
    showList: true,
    title: '',
    goodsSize: {}
  },
  methods: {
    add: function () {
      vm.reload();
      vm.showList = false;
    },
    saveOrUpdate: function () {
      if (vm.validator()) {
        return;
      }
      var url = vm.goodsSize.seq ? "system/goodsSize/update"
        : "system/goodsSize/add";
      if (vm.goodsSize.seq) {
        var data = {
          seq: vm.goodsSize.seq,
          sizeCode: vm.goodsSize.sizeCode,
          sizeName: vm.goodsSize.sizeName
        }
      } else {
        var data = {
          sizeCode: vm.goodsSize.sizeCode,
          sizeName: vm.goodsSize.sizeName
        }
      }
      $.ajax({
        type: "POST",
        url: baseURL + url,
        data: data,
        enctype: 'multipart/form-data',
        cache: false,
        success: function (r) {
          if (r.code === 0) {
            alert('操作成功');
            vm.reload();
          } else {
            alert(r.msg);
          }
        }
      });
    },
    validator: function () {
      if (isBlank(vm.goodsSize.sizeName)) {
        alert("尺码名称不能为空！");
        return true;
      }
    },
    reload: function (event) {
      vm.goodsSize = {};
      vm.showList = true;
      $("#jqGrid").jqGrid('setGridParam', {}).trigger("reloadGrid");
    },
    /* 同步尺码 */
    syncFromErp: function () {
      parent.window.showLoading();
      $.get(baseURL + "system/erp/sync/size", function (r) {
        parent.window.hideLoading();
        alert("同步结束", function () {
          location.reload();
        });
      });
    }
  }
});

function lineEdit(seq) {
  $.get(baseURL + "system/goodsSize/edit?seq=" + seq, function (r) {
    vm.showList = false;
    vm.title = "修改";
    vm.goodsSize = r.goodsSize;
  });
}

function del(seq) {
  $.get(baseURL + "system/goodsSize/delete?seq=" + seq, function (r) {
    if (r.code == 0) {
      vm.reload();
    } else {
      alert("该尺码已被使用，不可删除");
    }

  });
}


//ajax 方式提交form表单，上传excel文件 
function upload() {
  var fileDir = $("#excelFile").val();
  var suffix = fileDir.substr(fileDir.lastIndexOf("."));
  if ("" == fileDir) {
    alert("选择需要导入的Excel文件！");
    return false;
  }
  if (".xls" != suffix && ".xlsx" != suffix) {
    alert("选择Excel格式的文件导入！");
    return false;
  }

  $('#form1').ajaxSubmit({
    url: baseURL + 'system/goodsSize/uploadExcel',
    dataType: 'json',
    success: function (r) {
      if (r.code === 0) {
        alert(r.msg);
        $("#excelFile").val("");
        vm.reload();
      } else {
        alert(r.msg);
        $("#excelFile").val("");
      }
    },
    error: function (r) {
      alert("导入excel出错！");
      $("#excelFile").val("");
    },
  });
}
