$(function () {
  $("#jqGrid")
    .jqGrid(
      {
        url: baseURL + 'system/goodsColor/list',
        datatype: "json",
        colModel: [
          /*{
            label: '颜色编码',
            name: 'code',
            width: 100,
            align: "center"
          },*/
          {
            label: '颜色名称',
            name: 'name',
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
    goodsColor: {}
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
      var url = vm.goodsColor.seq ? "system/goodsColor/update"
        : "system/goodsColor/add";
      if (vm.goodsColor.seq) {
        var data = {
          seq: vm.goodsColor.seq,
          code: vm.goodsColor.code,
          name: vm.goodsColor.name
        }
      } else {
        var data = {
          code: vm.goodsColor.code,
          name: vm.goodsColor.name
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
      if (isBlank(vm.goodsColor.name)) {
        alert("颜色名称不能为空！");
        return true;
      }
    },
    reload: function (event) {
      vm.goodsColor = {};
      vm.showList = true;
      $("#jqGrid").jqGrid('setGridParam', {}).trigger("reloadGrid");
    },
    /* 同步颜色 */
    syncFromErp: function () {
      parent.window.showLoading();
      $.get(baseURL + "system/erp/sync/color", function (r) {
        parent.window.hideLoading();
        alert("同步结束", function () {
          location.reload();
        });
      });
    }
  }
});

function lineEdit(seq) {
  $.get(baseURL + "system/goodsColor/edit?seq=" + seq, function (r) {
    vm.showList = false;
    vm.title = "修改";
    vm.goodsColor = r.goodsColor;
  });
}

function del(seq) {
  $.get(baseURL + "system/goodsColor/delete?seq=" + seq, function (r) {
    if (r.code == 0) {
      vm.reload();
    } else {
      alert("该颜色已被使用，不可删除");
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
    url: baseURL + 'system/goodsColor/uploadExcel',
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
