$(function () {
  $('#effectiveOrderDatePicker').datepicker({
    autoclose: true,
    language: 'zh-CN'
  }).on('changeDate', orderDatePicker);
  $('#effectiveOnlineDatePicker').datepicker({
    autoclose: true,
    language: 'zh-CN'
  }).on('changeDate', onlineDatePicker);
  $("#jqGrid").jqGrid({
    url: baseURL + 'system/factory/list',
    datatype: "json",
    colModel: [
      {label: 'seq', name: 'seq', align: "center", hidden: true, hidedlg: true, key: true},
      {label: '公司名称', name: 'companyName', width: 70, align: "center"},
      {label: '品牌名称', name: 'brandName', width: 70, align: "center"},
      {label: '账户昵称', name: 'userName', width: 70, align: "center"},
      {label: '账户名', name: 'accountName', width: 70, align: "center"},
      {
        label: '操作', name: '', hidden: true, hidedlg: true, align: "center",
        formatter: function (cellvalue, options, rowObject) {
          var detail = '<button onclick="lineEdit('
            + rowObject.seq
            + ')" class="operation-btn-security" ">编辑</button>'
            + '<button onclick="lineDelete('
            + rowObject.seq
            + ')" class="operation-btn-dangery" ">删除</button>';
          return detail;
        }
      }],
    viewrecords: true,
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
  check: {
    enable: true,
    nocheckInherit: true
  }
};
var ztree;

function getGridParam(param) {
  return $("#jqGrid").jqGrid('getGridParam', param);
}

var vm = new Vue({
  el: '#rrapp',
  data: {
    showList: true,
    title: '',
    factoryDetail: {
      createdNumbers: 0,
      userNumbers: 0
    },
    hasOrderPlatform: false,
    hasOnlineSale: false
  },
  methods: {
    add: function () {
      vm.showList = false;
      vm.title = "新增";
      vm.getMenuTree(null);
    },
    update: function () {
      var factoryId = getSelectedRow();
      if (factoryId == null) {
        return;
      }
      vm.getMenuTree(factoryId);
    },
    del: function () {
      var factoryId = getSelectedRow();
      if (factoryId == null) {
        return;
      }
      lineDelete(factoryId);
    },
    reload: function (event) {
      vm.showList = true;
      vm.hasOrderPlatform = false;
      vm.hasOnlineSale = false;
      vm.factoryDetail = {
        createdNumbers: 0,
        userNumbers: 0
      };
      $('#effectiveOnlineDatePicker').datepicker('update', '');
      $('#effectiveOrderDatePicker').datepicker('update', '');
      var page = getGridParam('page');
      $("#jqGrid").jqGrid('setGridParam', {
        page: page
      }).trigger("reloadGrid");
    },
    saveOrUpdate: function () {
      if (isBlank(vm.factoryDetail.companyName)) {
        alert("输入公司名称");
        return;
      }
      if (isBlank(vm.factoryDetail.brandName)) {
        alert("输入品牌名称");
        return;
      }
      if (isBlank(vm.factoryDetail.accountName)) {
        alert("输入登录账号");
        return;
      }
      // 获取选择的菜单
      var nodes = ztree.getCheckedNodes(true);
      console.log(nodes);
      var menuIdList = [];
      for (var i = 0; i < nodes.length; i++) {
        menuIdList.push(nodes[i].seq);
      }
      vm.factoryDetail.menuIdList = menuIdList;

      vm.factoryDetail.hasOnlineSale = vm.hasOnlineSale;
      vm.factoryDetail.hasOrderPlatform = vm.hasOrderPlatform;
      $.ajax({
        type: "POST",
        url: baseURL + 'system/factory/save',
        contentType: "application/json",
        data: JSON.stringify(vm.factoryDetail),
        success: function (r) {
          if (r.code === 0) {
            alert('操作成功', function () {
              vm.reload();
            });
          } else {
            alert(r.msg);
          }
        }
      });
    },
    getMenuTree: function (factoryId) {
      // 加载菜单树
      $.get(baseURL + "system/menu/mylist", function (r) {
        ztree = $.fn.zTree.init($("#menuTree"), setting, r);
        // 展开所有节点
        ztree.expandAll(true);

        if (factoryId != null) {
          lineEdit(factoryId);
        }
      });
    }
  }
});

function lineEdit(seq) {
  $.get(baseURL + "system/factory/info?seq=" + seq, function (r) {
    vm.showList = false;
    vm.title = "修改";
    if (!r.factory.userNumbers) {
      r.factory.userNumbers = 0;
    }
    if (!r.factory.createdNumbers) {
      r.factory.createdNumbers = 0;
    }
    vm.factoryDetail = r.factory;
    if (r.factory.olsSeq) {
      vm.hasOnlineSale = true;
      $('#effectiveOnlineDatePicker').datepicker('update',
        new Date(r.factory.olsDate));
    }
    if (r.factory.opSeq) {
      vm.hasOrderPlatform = true;
      $('#effectiveOrderDatePicker').datepicker('update',
        new Date(r.factory.opDate));
    }

    // 勾选角色所拥有的菜单
    var menuIds = r.factory.menuIdList;
    for (var i = 0; i < menuIds.length; i++) {
      var node = ztree.getNodeByParam("seq", menuIds[i]);
      ztree.checkNode(node, true, false);
    }

  });
}

function lineDelete(seq) {

}

function orderDatePicker(ev) {
  vm.factoryDetail.opDate = ev.date.format('yyyy/MM/dd HH:mm:ss');
}

function onlineDatePicker(ev) {
  vm.factoryDetail.olsDate = ev.date.format('yyyy/MM/dd HH:mm:ss');
}