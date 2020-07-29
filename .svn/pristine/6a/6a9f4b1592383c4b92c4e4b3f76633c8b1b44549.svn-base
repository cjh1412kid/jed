"use strict";

var zTree1, $rMenu;


$(function () {
  $rMenu = $("#rMenu");

  $.get(baseURL + "sys/adjust-price/tree", function (data) {
    var zNodes1 = [{seq: 0, parentSeq: -1, name: '所有调价'}]

    if (data.code == 0) {
      if (data.result && data.result.length > 0) {

        var list = data.result;
        console.log("调价事件列表：", list)
        for (var i in list) {
          zNodes1.push({seq: list[i].seq, parentSeq: 0, name: list[i].flag, uploadFlag: true})
        }
      }
    }
    //初始化树对象
    zTree1 = $.fn.zTree.init($("#tree1"), setting, zNodes1);
    zTree1.expandAll(true);
  })


  initGrid();
})


var vm = new Vue({
  el: "#app",
  data: {
    //导入的Excel文件
    excelFile: null,
    //右击树节点对象
    rClickNode: null,
    //右键菜单是否显示
    isShowMenu: true,
    //表格数据
    tableData: [],
  },
  methods: {
    uploadExcel: function (event) {
      if (!event.target.files[0]) {
        return;
      }
      this.excelFile = event.target.files[0];

      /*      var excelForm = document.getElementById("uploadExcelForm");
            excelForm.treeNodeName.value = this.rClickNode.name;
            console.log("节点名称：", excelForm.treeNodeName.value)*/

      parent.window.showLoading();

      $("#uploadExcelForm").ajaxSubmit({
        url: baseURL + 'sys/adjust-price/upload/excel',
        dataType: 'json',
        data: {
          treeNodeName: vm.rClickNode.name
        },
        cache: false,
        success: function (r) {
          if (r.code == 0) {
            layer.alert(r.msg, function () {
              location.reload();
            });
          } else {
            layer.alert(r.msg, function () {
              location.reload();
            });
          }

          $("#uploadExcelForm").resetForm();
          parent.window.hideLoading();
        },
        error: function (r) {
          alert("导入商品出错！");
          $("#uploadExcelForm").resetForm();
          parent.window.hideLoading();
        }
      });
    },
    importExcel: function () {
      if (this.rClickNode.uploadFlag) {
        layer.alert("不能重复导入数据")
      } else {
        $('#fileSelect').click();
        hideRMenu();
      }
    }
  }
})

var setting = {
  view: {
    // addDiyDom: addDiyDom,
    selectedMulti: false,
    dblClickExpand: function (treeId, treeNode) {
      return treeNode.level > 0;
    }
  },
  edit: {
    enable: true,
    showRemoveBtn: false,
    showRenameBtn: false,
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
    }
  },
  callback: {
    onRightClick: function (event, treeId, treeNode) {
      var treeObj = $.fn.zTree.getZTreeObj(treeId);
      treeObj.selectNode(treeNode);
      vm.isShowMenu = treeNode.seq == 0 ? false : true;
      vm.rClickNode = treeNode;
      showRMenu(event.clientX, event.clientY);
    },
    onClick: function (event, treeId, treeNode) {
      // console.log(treeNode.seq, treeNode.parentSeq)
      if (treeNode.seq == 0) {

      } else {
        reloadGrid(treeNode.name);
      }
    }
  }
}


/*显示节点右键菜单*/
function showRMenu(x, y) {
  $rMenu.show();
  y += $(document).scrollTop() - 10;
  x += $(document).scrollLeft() - 10;
  $rMenu.css({
    "top": y + "px",
    "left": x + "px",
    "visibility": "visible"
  });
  $(document).on("mousedown", onBodyMouseDown);
}

function onBodyMouseDown(event) {
  if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length > 0)) {
    $rMenu.css({
      "visibility": "hidden"
    });
  }
}

function hideRMenu() {
  if ($rMenu)
    $rMenu.css({
      "visibility": "hidden"
    });

  $(document).off("mousedown");
}


/*新建调价*/
function createNewTemplate() {

  var dateStr = new Date().format('yyyy-MM-dd HH:mm:ss')

  var treeObj = $.fn.zTree.getZTreeObj("tree1");
  console.log(vm.rClickNode)
  var newNodes = treeObj.addNodes(null, 0, {name: dateStr});
  console.log(newNodes)

  hideRMenu();
}


/**
 * 点击节点获取调价数据
 */
function getExcelData() {
  $.get(baseURL + 'sys/adjust-price/excel/' + vm.rClickNode.name(), function (data) {
    if (data.code == 0) {
      vm.tableData = data.result;
    } else {
      layer.alert(data.msg);
    }
  })

}

function initGrid() {
  $("#jqGrid").jqGrid({
    url: baseURL + 'sys/adjust-price/excel',
    datatype: "local",
    mtype: "get",
    colModel: [
      {label: '门店', name: 'shopName', width: 100, align: "center"},
      {label: '货号', name: 'goodID', width: 150, align: "center"},
      {label: '现价', name: 'currentPrice', width: 60, align: "center"},
      {label: '原价', name: 'previousPrice', width: 60, align: "center"},
    ],
    height: 'auto',
    rowNum: 10,
    // rowList: [10, 30, 50],
    rownumbers: true,
    rownumWidth: 30,
    autowidth: true,
    multiselect: false,
    viewrecords: true,
//     shrinkToFit:false,
//     autoScroll: true,
    pager: "#jqGridPager",
    jsonReader: {
      root: "list",
      // page: "page.currPage",
      // total: "page.totalPage",
      // records: "page.totalCount"
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
}

function reloadGrid(flag) {
  //重载表格数据
  $("#jqGrid").jqGrid('setGridParam', {
    datatype: 'json',
    postData: {
      flag: flag
    },
    page: 1,
  }).trigger('reloadGrid');

}