var ztree, rMenu, rClickNode;
var setting = {
  view: {
    addDiyDom: addDiyDom,
    dblClickExpand: false,
  },
  check: {
    enable: false
  },
  data: {
    keep: {
      leaf: true,// 属性配置 leaf: true, 表示叶子节点不能变成根节点。
      // parent: true 表示 根节点不能变成叶子节点
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
  edit: {
    drag: {
      isCopy: false,
      isMove: false
    },
    enable: true,// 设置是否处于编辑状态
    editNameSelectAll: true,
    showRemoveBtn: false,
    showRenameBtn: false
  },
  callback: {
    onRightClick: categoryRightClick,
  }
};
var vm = new Vue({
  el: '#rrapp',
  data: {
    classification: false,
    node: false,
    category: {
      name: "",
      code: "",
      visible: 0
    }
  },
  methods: {}
});


$(function () {
  zTreelist();
  rMenu = $("#rMenu");
});

function reload() {
  zTreelist();
}

function zTreelist() {
  $.get(baseURL + "system/goodsCategory/list", function (r) {
    var categorys = r.list;
    categorys.push({
      seq: 0,
      parentSeq: -1,
      name: "所有分类"
    });
    ztree = $.fn.zTree.init($("#categoryTree"), setting, categorys);
    ztree.expandAll(true);
  });
}


function categoryRightClick(event, treeId, treeNode) {
  rClickNode = treeNode;
  if (!treeNode && event.target.tagName.toLowerCase() != "button" && $(event.target).parents("a").length == 0) {
    ztree.cancelSelectedNode();
    showRMenu("root", event.clientX, event.clientY);
  } else if (treeNode && !treeNode.noR) {
    ztree.selectNode(treeNode);
    showRMenu("node", event.clientX, event.clientY);
  }
}

function showRMenu(type, x, y) {

  if (!ztree.getSelectedNodes()[0]) {
    return;
  }
  y += $(document).scrollTop();
  x += $(document).scrollLeft();
  rMenu.css({
    "top": y + "px",
    "left": x + "px",
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

function addTreeNode() {
  hideRMenu();
  vm.category = {
    name: "",
    code: "",
    visible: 0
  };

  layer.open({
    type: 1,
    offset: '50px',
    skin: 'layui-layer-molv',
    title: "新增",
    area: ['300px', '260px'],
    shade: 0,
    shadeClose: false,
    content: $("#menuLayer"),
    btn: ['确定', '取消'],
    btn1: function (index) {
      if (isBlank(vm.category.name)) {
        alert("分类名不能为空！");
        return;
      }

      $.post(baseURL + 'system/goodsCategory/add?', {
        parentSeq: rClickNode.seq,
        name: vm.category.name,
        catetoryCode: vm.category.code,
        visible: vm.category.visible,
      }, function (r) {
        if (r.code == 0) {
          layer.msg(r.msg);
          vm.category = {};
          zTreelist();
        } else {
          layer.alert(r.msg);
        }

      }, 'json');
      layer.close(index);
    }
  });

}

function removeTreeNode() {
  hideRMenu();
  var nodes = ztree.getSelectedNodes();
  if (nodes && nodes.length > 0) {
    if (nodes[0].children && nodes[0].children.length > 0) {
      confirm('要删除的节点是父节点，如果删除将连同子节点一起删掉。\n\n请确认！？', function () {
        $.get(baseURL + 'system/goodsCategory/delete?seq=' + ztree.getSelectedNodes()[0].seq, function (r) {
          if (r.code == 0) {
            layer.msg(r.msg);
            reload();
          } else {
            alert(r.msg);
          }
        }, 'json');

      });
    } else {
      confirm('确定要删除节点！？', function () {
        $.get(baseURL + 'system/goodsCategory/delete?seq=' + ztree.getSelectedNodes()[0].seq, function (r) {
          if (r.code == 0) {
            layer.msg(r.msg);
            reload();
          } else {
            alert(r.msg);
          }
        }, 'json');

      });
    }
  }

}

function beforeEditName() {
  vm.$set(vm.category, "name", rClickNode.name);
  vm.$set(vm.category, "code", rClickNode.catetoryCode);
  vm.$set(vm.category, "visible", rClickNode.visible);
  hideRMenu();
  layer.open({
    type: 1,
    offset: '50px',
    skin: 'layui-layer-molv',
    title: "修改",
    area: ['300px', '260px'],
    shade: 0,
    shadeClose: false,
    content: jQuery("#menuLayer"),
    btn: ['确定', '取消'],
    btn1: function (index) {
      if (isBlank(vm.category.name)) {
        alert("分类名不能为空！");
        return;
      }

      $.post(baseURL + 'system/goodsCategory/update', {
        seq: rClickNode.seq,
        parentSeq: rClickNode.parentSeq,
        name: vm.category.name,
        catetoryCode: vm.category.code,
        visible: vm.category.visible,
      }, function (data) {
        if (data.code == 0) {
          layer.msg(data.msg);
          zTreelist();
        } else {
          layer.alert(data.msg);
        }

      }, 'json');
      layer.close(index);
    }
  });
}

function addDiyDom(treeId, treeNode) {
  var aObj = $("#" + treeNode.tId + "_a");
  var editStr = "";

/*  if (treeNode.parentSeq != 0 && !treeNode.isParent) {
    if (treeNode.visible == 0) {
      editStr += '<label class="label label-warning" >&nbsp; 可见 &nbsp;</label>'
    }
    if (treeNode.visible == 1) {
      editStr += '<label class="label label-danger" > &nbsp;不可见&nbsp; </label>'
    }
  }*/
  if (treeNode.catetoryCode) {
    editStr += '&nbsp;<label class="label label-info" > &nbsp;' + treeNode.catetoryCode + ' &nbsp;</label>'
  }

  aObj.append(editStr);
}