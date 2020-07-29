var atree, btree, rMenu;

$(function () {
  aTreelist();
  rMenu = $("#rMenu");
  /*右键菜单打开后，在页面其他处点击隐藏*/
  $(document).click(function () {
    hideRMenu();
  });
})


var vm = new Vue({

  el: "#rrapp",
  data: {
    showPanel: false,

    sx: null,
    sxName: "",
    optName: "",
    optCode: "",

    opts: [],
    /*新增属性提示消息*/
    optCodeMsg: null,
    optNameMsg: null,

    /*属性右键菜单标题*/
    SXVisibleUpdateTitle: "",
    /*右键对象*/
    rMenuObj: null,

    /*编辑属性名称*/
    SX1: null,
    SX2: null,
    SX3: null,
    SX4: null,
    SX5: null,
    SX6: null,
    SX7: null,
    SX8: null,
    SX9: null,
    SX10: null,
    SX11: null,
    SX12: null,
    SX13: null,
    SX14: null,
    SX15: null,
    SX16: null,
    SX17: null,
    SX18: null,
    SX19: null,
    SX20: null,
  },
  methods: {
    /* 同步属性 */
    syncFromErp: function () {
      parent.window.showLoading();
      $.get(baseURL + "system/erp/sync/sx", function (r) {
        parent.window.hideLoading();
        alert("同步结束", function () {
          location.reload();
        });
      });
    }
  }
});


var setting1 = {
  view: {
    dblClickExpand: false,
    addDiyDom: addDiyDom,
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
      rootPId: -1
    },
    key: {
      url: "nourl"
    }
  },
  check: {
    enable: true,
    nocheckInherit: false,
    chkStyle: "radio"
  },
  edit: {
    drag: {
      isCopy: false,
      isMove: false
    },
    enable: true,// 设置是否处于编辑状态
    editNameSelectAll: true,
    showRemoveBtn: true,
    showRenameBtn: true,
    removeTitle: "删除",
    renameTitle: "重命名"
  },
  callback: {
    onCheck: zTreeOnCheck,
    beforeRemove: zTreeBeforeRemove,
    beforeRename: zTreeBeforeRename,
    onRename: zTreeOnRename,
    onClick: zTreeOnClick,
    onRightClick: sxRightClick,
  }

};

function sxRightClick(event, treeId, treeNode) {
  if (treeNode && treeNode.seq != 0) {
    // console.log("属性右键菜单")
    vm.SXVisibleUpdateTitle = treeNode.visible == 0 ? "不可见" : "可见";
    vm.rMenuObj = treeNode;
    atree.selectNode(treeNode);
    showRMenu(event);
  }
}

function showRMenu(event) {
  var tl = event.target.offsetLeft + 10;
  var tt = event.target.offsetTop + 8;
  rMenu.css({
    "top": tt + "px",
    "left": tl + "px",
    "visibility": "visible"
  });

  $("body").bind("mousedown", onBodyMouseDown);
}

function hideRMenu() {
  if (rMenu)
    rMenu.css({
      "visibility": "hidden"
    });
  $("body").unbind("mousedown", onBodyMouseDown);
}

function onBodyMouseDown(event) {
  if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length > 0)) {
    rMenu.css({
      "visibility": "hidden"
    });
  }
}

function zTreeBeforeRemove(treeId, treeNode) {
  if (treeNode.seq == 0) {
    return false;
  }

  confirm("是否确定删除 " + treeNode.name + " 吗?", function () {
    $.get(baseURL + 'sx/delete?seq=' + treeNode.seq + "&sxid=" + treeNode.sxid,
      function (data, status) {
        if (data.code == 0) {
          alert(treeNode.name + " 删除成功！包括所有关联选项");
          atree.removeNode(treeNode);
          bTreeDelAllBySXseq(treeNode.seq);
        } else {
          alert(treeNode.name + " 删除失败！\n" + data.msg);
        }
      });
  });

  return false;
}

function zTreeOnCheck(event, treeId, treeNode) {
  if (treeNode.seq == 0) {
    return;
  }

  atree.checkNode(treeNode, true, true);
  bTreelist(treeNode.seq);
};

function zTreeOnRename(event, treeId, treeNode, isCancel) {
  if (treeNode.seq == 0) return;

  var newName = treeNode.name.trim();
  if (originalName == newName) {
    return;
  }

  $.post(baseURL + "sx/update",
    {
      seq: treeNode.seq,
      SXName: treeNode.name
    }, function (data) {
      if (data.code == 0) {
        layer.msg("属性更新成功")
      } else {
        alert(data.msg)
      }

    })

}


function zTreeOnClick(event, treeId, treeNode) {

  if (treeNode.checked || treeNode.seq == 0) {
    return;
  }

  atree.checkNode(treeNode, true, true);
  bTreelist(treeNode.seq);
};


function aTreelist() {

  $.get(baseURL + "sx/list", function (r) {
    var sxs = [{
      seq: 0,
      parentSeq: -1,
      name: "所有属性"
    }];

    if (r.code == 0) {
      var list = r.list;

      if (list && list.length > 0) {
        for (var i = 0; i < list.length; i++) {
          sxs.push({
            seq: list[i].seq,
            parentSeq: 0,
            name: list[i].sxname,
            sx: list[i].sxid.substring(2),
            sxid: list[i].sxid,
            visible: list[i].visible
          });
        }
      } else {
        layer.msg("未查询到属性")
      }

    } else {
      alert(r.msg);
    }

    atree = $.fn.zTree.init($("#SXTree"), setting1, sxs);
    atree.expandAll(true);
  });


}


function addDiyDom(treeId, treeNode) {

  var aObj = $("#" + treeNode.tId);

  if (treeNode.seq != 0) {
    var editStr = '&nbsp;<span class="label label-info">&nbsp;属性' + treeNode.sx + '&nbsp;</span>';
    if (treeNode.visible == 0) {
      editStr += '&nbsp;<span class="label label-warning">&nbsp;可见&nbsp;</span>';
    }
    aObj.append(editStr);
  }
};

function categoryRightClick() {

}

/*
************************************************************************************
*/

var setting2 = {
  view: {
    dblClickExpand: false,
    addDiyDom: addDiyDom2,
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
      rootPId: -1
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
    enable: true,// 设置是否处于编辑状态
    editNameSelectAll: true,
    showRemoveBtn: true,
    showRenameBtn: true,
    removeTitle: "删除",
    renameTitle: "重命名"
  },
  callback: {
    beforeRemove: bTreeBeforeRemove,
    beforeRename: zTreeBeforeRename,
    onRename: bTreeOnRename,
    onClick: bTreeOnClick,
  }

};

/*修改节点名称前，先保存原来的节点名，修改前后名称相同则不更新*/
var originalName;

function zTreeBeforeRename(treeId, treeNode, newName, isCancel) {

  if (newName && newName.trim() != "") {
    originalName = treeNode.name;
    return true;
  }
  return false;
}

function addDiyDom2(treeId, treeNode) {

  var aObj = $("#" + treeNode.tId);

  if (treeNode.seq != 0) {
    var editStr = '&nbsp;<span class="label label-info">&nbsp;' + treeNode.code + '&nbsp;</span>';
    aObj.append(editStr);
  }
};

function bTreeBeforeRemove(treeId, treeNode) {
  if (treeNode.seq == 0) {
    return false;
  }

  var nodes = atree.getCheckedNodes(true);
  if (!nodes || nodes.length == 0) {
    return;
  }

  confirm("是否确定删除 " + treeNode.name + " 吗?", function () {
    $.get(baseURL + 'sxOption/delete?seq=' + treeNode.seq + "&sxid=" + nodes[0].sxid + "&code=" + treeNode.code,
      function (data, status) {
        if (data.code == 0) {
          alert(treeNode.name + " 删除成功！");
          btree.removeNode(treeNode);
        } else {
          alert(treeNode.name + " 删除失败！\n" + data.msg);
        }
      });
  });

  return false;
}


function bTreeOnRename(event, treeId, treeNode, isCancel) {
  if (treeNode.seq == 0) return;

  var newName = treeNode.name.trim();
  if (originalName == newName) {
    return;
  }

  $.post(baseURL + "sxOption/update",
    {
      seq: treeNode.seq,
      value: newName
    }, function (data) {
      if (data.code == 0) {
        layer.msg("选项更新成功！")
      } else {
        alert(data.msg)
      }
    })

}

function bTreeOnClick(event, treeId, treeNode) {
  btree.checkNode(treeNode, !treeNode.checked, true)
};

/*点击属性时，获取属性对应选项列表*/
function bTreelist(sxSeq) {

  $.get(baseURL + "sxOption/list?seq=" + sxSeq, function (data) {
    vm.opts = [];
    vm.opts.push({
      seq: 0,
      parentSeq: -1,
      name: "属性选项"
    });

    if (data.code == 0) {
      var list = data.list;

      if (list && list.length > 0) {
        for (var i = 0; i < list.length; i++) {
          vm.opts.push({
            seq: list[i].seq,
            parentSeq: 0,
            name: list[i].value,
            code: list[i].code
          });
        }
      } else {
        layer.msg("未查询到属性选项", {time: 1000})
      }

    } else {
      alert(data.msg);
    }
    btree = $.fn.zTree.init($("#optTree"), setting2, vm.opts);
    btree.expandAll(true);

  });

}


function editPage() {
  var nodes = atree.getNodes()[0].children;
  for (var i in nodes) {
    var node = nodes[i];
    vm["SX" + node.sx] = node.name;
  }
  vm.showPanel = true;
}

function quitEdit() {
  vm.showPanel = false;

  for (var i = 1; i < 21; i++) {
    var sx = 'SX' + i;
    vm[sx] = null;
  }
}

function saveOrUpdateSX() {
  var sxs = {};
  for (var i = 1; i < 21; i++) {
    var sx = 'SX' + i;
    if (vm[sx] && vm[sx].trim() != "") {
      sxs[sx] = vm[sx].trim();
    }
  }

  $.ajax({
    type: "POST",
    contentType: "application/json;charset=UTF-8",
    url: baseURL + "sx/save",
    data: JSON.stringify(sxs),
    dataType: 'json',
    success: function (result) {

      if (result.code == 0) {
        aTreelist();
        $.fn.zTree.destroy("optTree");
        quitEdit();
        layer.msg("操作成功")
      } else {
        alert(result.msg)
      }
    }
  });
}


function addOpt() {
  var nodes = atree.getCheckedNodes(true);
  if (!nodes || nodes.length == 0 || nodes[0].seq == 0) {
    layer.msg("先选中属性后，才可编辑选项")
    return;
  }
  vm.sx = nodes[0];
  vm.sxName = nodes[0].name;
  vm.optName = "";
  vm.optCode = "";
  $('#myModal').modal('show');
}

function saveOpt() {

  if (!vm.optName || !vm.optCode || vm.optName == "" || vm.optCode == "" || vm.optNameMsg || vm.optCodeMsg) {
    return;
  }

  $.post(baseURL + "sxOption/save",
    {
      SXSeq: vm.sx.seq,
      value: vm.optName.trim(),
      code: vm.optCode.trim()

    }, function (result) {

      if (result.code == 0) {
        $('#myModal').modal('hide');
        bTreelist(vm.sx.seq);
        layer.msg("属性选项保存成功")
      } else {
        alert(result.msg);
      }

    })
}

function bTreeDelAllBySXseq(SXseq) {

  $.get(baseURL + 'sxOption/delAllBySX?sxSeq=' + SXseq,
    function (data, status) {
      $.fn.zTree.destroy("optTree");
    });
}

/*失去焦点触发*/
function isExistOptCode() {
  if (!vm.optCode) {
    vm.optCodeMsg = "选项编号不能为空！";
    return;
  }

  for (var i in vm.opts) {
    if (vm.opts[i].code == vm.optCode) {
      vm.optCodeMsg = "此选项编号已存在！";
      return;
    }
  }
  vm.optCodeMsg = null;
}

function isExistOptName() {
  if (!vm.optName) {
    vm.optNameMsg = "选项名称不能为空！";
    return;
  }

  for (var i in vm.opts) {
    if (vm.opts[i].name == vm.optName) {
      vm.optNameMsg = "此选项名称已存在！";
      return;
    }
  }
  vm.optNameMsg = null;
}

function clearMsg(val) {
  if (val == 1) {
    vm.optCodeMsg = null;
  } else if (val == 2) {
    vm.optNameMsg = null;
  }
}

/*更新属性的可见状态*/
function updateSXVisible() {
  if (!vm.rMenuObj) return;
  $.post(baseURL + "sx/visible",
    {
      visible: vm.rMenuObj.visible == 0 ? 1 : 0,
      seq: vm.rMenuObj.seq,
    }, function (data) {

      if (data.code == 0) {
        layer.msg("可见状态更新成功")
        aTreelist();
        $.fn.zTree.destroy("optTree");
      } else {
        alert(r.msg)
      }


    })
}

/*var menuObj = [
  {
    text: '编辑属性', action: function (e) {

    }
  },
  {
    text: '删除属性', action: function (e) {

    }
  },
]
rMenuContext.init({
  preventDoubleContext: true,
  compress: true
});

var $Menu1 = rMenuContext.attach('#SXTree', menuObj),
  $Menu2 = rMenuContext.attach('#optTree', menuObj);*/
