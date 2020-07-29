$(function () {
  $('#periodYearPicker').datepicker({
    autoclose: true,
    startView: 2,
    maxViewMode: 2,
    minViewMode: 2,
    format: 'yyyy'
  }).on('changeDate', yearPicker);
  $('#saleDatePicker').datepicker({
    autoclose: true,
    language: 'zh-CN'
  }).on('changeDate', saleDatePicker);
  $('#meetingStartTimePicker').datepicker({
    autoclose: true,
    language: 'zh-CN'
  }).on('changeDate', meetingStartTimePicker);
  $('#meetingEndTimePicker').datepicker({
    autoclose: true,
    language: 'zh-CN'
  }).on('changeDate', meetingEndTimePicker);
  $("#jqGrid")
    .jqGrid(
      {
        url: baseURL + 'system/period/list',
        datatype: "json",
        colModel: [
          {
            label: '波次所属年份',
            name: 'year',
            width: 100,
            align: "center"
          },
          {
            label: '波次名称',
            name: 'name',
            width: 250,
            align: "center"
          },
          {
            label: '销售时间',
            name: 'saleDate',
            width: 200,
            align: "center"
          },
          {
            label: '创建时间',
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
    goodsPeriod: {}
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
      var url = vm.goodsPeriod.seq ? "system/period/update"
        : "system/period/add";

      $.ajax({
        type: "POST",
        url: baseURL + url,
        data: JSON.stringify(vm.goodsPeriod),
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
      if (isBlank(vm.goodsPeriod.name)) {
        alert("波次名称不能为空！");
        return true;
      }
      if (isBlank(vm.goodsPeriod.year)) {
        alert("所属年分不能为空！");
        return true;
      }
    },
    reload: function (event) {
      vm.goodsPeriod = {};
      vm.showList = true;
      $("#jqGrid").jqGrid('setGridParam', {}).trigger("reloadGrid");
    },
  }
});

function lineEdit(seq) {
  $.get(baseURL + "system/period/edit?seq=" + seq, function (r) {
    vm.showList = false;
    vm.title = "修改";
    vm.goodsPeriod = r.goodsPeriod;
  });
}

function del(seq) {
  $.get(baseURL + "system/period/delete?seq=" + seq, function (r) {
    if (r.code == 0) {
      vm.reload();
    } else {
      alert(r.msg);
    }

  });
}

function yearPicker(ev) {
  vm.goodsPeriod.year = ev.date ? ev.date.getFullYear() : "";
}

function saleDatePicker(ev) {
  vm.goodsPeriod.saleDate = ev.date ? ev.date.format('yyyy/MM/dd HH:mm:ss') : "";
}

function meetingStartTimePicker(ev) {
  vm.goodsPeriod.meetingStartTime = ev.date ? ev.date.format('yyyy/MM/dd HH:mm:ss') : "";
}

function meetingEndTimePicker(ev) {
  vm.goodsPeriod.meetingEndTime = ev.date ? ev.date.format('yyyy/MM/dd HH:mm:ss') : "";
}
