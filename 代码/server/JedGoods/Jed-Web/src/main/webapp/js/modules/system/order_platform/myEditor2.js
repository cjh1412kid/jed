var atree;

var rMenuVM = new Vue({
  el: '#rMenu',
  data: {
    tmpNodeSeq: null,
    tmpNode: null,
    rMenuTitle: null,
    isShowMenu: true
  }
});

var aprompt = new Vue({
  el: '#prompt',
  data: {
    templateName: null,
    promptTip: "",
  }
});
<!-- 实例化编辑器 -->
var ue = UE.getEditor('UEditor', {
  enterTag: '',
  allowDivTransToP: false
});

/*加载模板树*/
loadUeditorRecords();

//存储右击目标对象
var clickTarget = null;

ue.ready(function () {
  /*隐藏右上角本地保存提示框*/
  $(".edui-editor-messageholder.edui-default").css({"visibility": "hidden"});
  /*加载设为默认的模版为选中状态，编辑器显示内容*/
  if (token != null) {
    $.get(baseURL + 'editor/template/used/1', function (data, status) {
      if (data.result) {
        if (data.result.length > 1) {
          if (checkedNode(data.seq)) {
            var content = decodeURIComponent(data.result);
            ue.setContent(content);

            setTimeout(function () {
              setChildNodesDraggable();
            }, 500);
          }
        }
      } else {
        //无返回结果清空编辑器内容
        clearContent();
      }

    })
  } else {
    // console.log("token:", token)
  }

  ue.body.onmousedown = function (e) {
    var e = e || window.event;
    //获取右击事件目标对象
    if (e.button == 2) {
      clickTarget = e.target;
      // console.log(clickTarget);
    }
  }

  setEditorBodyOnDrag();
});


/*自定义请求地址*/
UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
UE.Editor.prototype.getActionUrl = function (action) {
  if (action == 'uploadimage' || action == 'uploadscrawl' || action == 'uploadimage') {
    return baseURL + 'editor/imgUpload?token=' + token + '&action=' + action;
  } else if (action == 'uploadvideo') {
    return baseURL + 'editor/videoUpload?token=' + token + '&action=' + action;
  } else {
    return this._bkGetActionUrl.call(this, action);
  }
}


/*获取编辑器内容生成网页*/
function getAllHtml() {
  console.log(ue.getAllHtml())
}

/*获取编辑器内容*/
function getContent2() {
  // var res=ue.body.innerHTML;
  // console.log(res,typeof res)
  console.log(ue.getContent());
}

/*将获取到的内容编译后显示在编辑器*/
function setContent2(content) {
  ue.setContent(decodeURIComponent(content));
  setTimeout(function () {
    setChildNodesDraggable();
  }, 500);
}

/*获取编辑器内容并上传*/
function getContentAndUpload() {

  var treeObj = $.fn.zTree.getZTreeObj("templateTree");
  var nodes = treeObj.getCheckedNodes(true);
  var content = ue.getContent();

  /*没有节点选中，将创建新节点*/
  if (nodes.length == 0) {

    if (!content || content.trim() == "") {
      alert("内容不能为空！")
      return;
    }
    content = encodeURIComponent(content);

    /*设置输入框提示信息*/
    aprompt.promptTip = "请输入新模板名称";

    layer.open({
      type: 1,
      offset: '50px',
      skin: 'layui-layer-molv',
      title: "请输入新模板名称",
      area: ['300px', '150px'],
      shade: 0,
      shadeClose: false,
      content: jQuery("#prompt"),
      btn: ['确定', '取消'],
      btn1: function (index) {
        var name = aprompt.templateName;
        if (name != null && name.trim() != "" && content != null && content.trim() != "") {
          $.post(baseURL + "editor/save",
            {
              content: content,
              name: name,
              uType: 1
            },
            function (data, status) {
              if (data.code == 0) {
                layer.msg("新模版创建成功");
                loadUeditorRecords(checkMaxSeqNode);
              } else {
                alert("模板创建失败")
              }
              aprompt.templateName = null;
            })
        } else {
          alert("模版名称和内容不能为空");
        }


        layer.close(index);
      }
    });


  } else {
    //编辑器内容为空时可以修改
    if (!content || content.trim() == "") {
      content = " ";
    }
    content = encodeURIComponent(content);

    rMenuVM.tmpNodeSeq = nodes[0].seq;
    $.post(baseURL + "editor/update",
      {
        seq: nodes[0].seq,
        content: content,
      },
      function (data, status) {
        if (data.code == 0) {
          alert("修改成功");
          loadUeditorRecords(checkedNode2);
        } else {
          alert("修改失败")
        }
      })
  }

}

/*清空内编辑器内容*/
function clearContent() {
  ue.setContent("");
}

function createNewTemplate() {
  hideRMenu();
  clearContent();
  var nodes = atree.getCheckedNodes(true);
  if (nodes.length > 0) {
    atree.checkNode(nodes[0], false, true);
  }
}

/*  表格功能区   */

var vm;
var categorySetting = {
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

function table() {

  $("#jqGoodsGrid").jqGrid({
    url: baseURL + "order/goods/listGoods",
    datatype: "json",
    colNames: ['序号', '货号', '描述', '图片'],
    colModel: [
      {name: 'seq', width: 80, align: "center", key: true},
      {name: 'goodID', width: 180, align: "center"},
      {name: 'introduce', width: 350, align: "center"},
      {
        name: 'img1', width: 180, align: "center", formatter: function (cellvalue) {
          var detail = '<image src="' + cellvalue + '" style="width: 100px;height: 70px;"/>';
          return detail;
        }
      }
    ],
    height: 'auto',
    rowNum: 10,
    rownumWidth: 25,
    autowidth: true,
    multiselect: true, //多选
    viewrecords: true,
    // multiboxonly:true,   //单选
    // beforeSelectRow: beforeSelectRow, //单选
    pager: "#jqGoodsGridPager",
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
      $("#jqGoodsGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
    },
    loadComplete: function () {
      // 设置选择项
      if (vm && vm.sowingMap.linkSeq && vm.sowingMap.linkSeq.length > 0) {
        var rows = vm.sowingMap.linkSeq.split(',');
        for (var i = 0; i < rows.length; i++) {
          $("#jqGoodsGrid").jqGrid('setSelection', rows[i]);
        }
      }
    }

  });
};


/*实现表格单选*/
function beforeSelectRow() {
  $("#jqGoodsGrid").jqGrid('resetSelection');
  return (true);
}

function getGridParam(param) {
  return $("#jqGrid").jqGrid('getGridParam', param);
}


vm = new Vue({
  el: '#myModal',
  data: {
    showList: true,
    title: '',
    sowingMap: {
      type: 1,
      linkSeq: "",
      linkName: "",
      title: "",
    },
  },
  methods: {
    // 切换关联类型之后，删除原选择内容
    typeChange: function (event) {
      vm.$set(vm.sowingMap, "linkName", "");
      vm.sowingMap.linkSeq = "";
    },
    // 重新加载物品列表
    loadGoodList: function () {
      $("#jqGoodsGrid").trigger("reloadGrid");
    },
    // 加载可选择的分类
    loadCategory: function () {
      // 加载分类树
      $.get(baseURL + "order/goods/category", function (r) {
        var categorys = r.categorys;
        categorys.push({seq: 0, pIdKey: -1, name: "所有分类"});
        ztree = $.fn.zTree.init($("#categoryTree"), categorySetting, categorys);

        //选中已选择的分类
        var categroySeqs = vm.sowingMap.linkSeq;
        if (categroySeqs && categroySeqs.length > 0) {
          var nodeSeqs = categroySeqs.split(',');
          for (var i = 0; i < nodeSeqs.length; i++) {
            var node = ztree.getNodeByParam("seq", nodeSeqs[i]);
            ztree.checkNode(node, true, true);
          }
        }
        ztree.expandAll(true);
      })
    },
    contentSelect: function () {
      var type = vm.sowingMap.type;
      if (type == 1) {
        table(); //加载关联商品的表格
        vm.loadGoodList();//表格重载，去除选中状态
        layer.open({
          type: 1,
          offset: '50px',
          skin: 'layui-layer-molv',
          title: "选择菜单",
          area: ['900px', '600px'],
          shade: 0,
          shadeClose: false,
          content: jQuery("#goodsLayer"),
          btn: ['确定', '取消'],
          btn1: function (index) {
            var ids = $("#jqGoodsGrid").jqGrid("getGridParam", "selarrrow");
            vm.sowingMap.linkSeq = ids.join(',');
            vm.sowingMap.linkName = "已选择" + ids.length + "商品";
            layer.close(index);
          }
        });
      } else if (type == 2) {
        vm.loadCategory();
        layer.open({
          type: 1,
          offset: '50px',
          skin: 'layui-layer-molv',
          title: "选择菜单",
          area: ['300px', '450px'],
          shade: 0,
          shadeClose: false,
          content: jQuery("#categoryLayer"),
          btn: ['确定', '取消'],
          btn1: function (index) {
            var nodes = ztree.getCheckedNodes(true);
            var childSelect = [];
            var childSelectTitle = [];
            for (var i = 0; i < nodes.length; i++) {
              if (!nodes[i].children) {
                childSelect.push(nodes[i].seq);
                childSelectTitle.push(nodes[i].name);
              }
            }
            vm.$set(vm.sowingMap, "linkName", "已选择" + childSelect.length + "分类");
            vm.sowingMap.linkSeq = childSelect.join(',');
            vm.sowingMap.title = childSelectTitle.join(',');
            layer.close(index);
          }
        });
      } else {
        alert("请先选择关联类型");
      }
    }
  }
});


/*ueditor编辑器工具栏--自定义按钮--功能区*/

/*定义图片排列列数，对所有图片，单列*/
ue.commands['imgonecols'] = {

  execCommand: function () {

    var imgs = this.document.getElementsByTagName("img");

    for (var i = 0, img; img = imgs[i++];) {
      // $(img).attr("style", "float:left");
      $(img).attr("width", "100%");
      // $(img).removeAttr("_src");
    }
  }
}
/*定义图片排列列数，对所有图片，双列*/
ue.commands['imgtwocols'] = {

  execCommand: function () {

    var imgs = this.document.getElementsByTagName("img");

    for (var i = 0, img; img = imgs[i++];) {
      // $(img).attr("style", "float:left");
      $(img).attr("width", "50%");
      // $(img).removeAttr("_src");
    }
  }
}
/*定义图片排列列数，对所有图片，三列*/
ue.commands['imgthreecols'] = {

  execCommand: function () {

    var imgs = this.document.getElementsByTagName("img");

    for (var i = 0, img; img = imgs[i++];) {
      // $(img).attr("style", "float:left");
      $(img).attr("width", "33.33%");
      // $(img).removeAttr("_src");
    }
  }
}

/*设置选中的显示宽度，100%*/
ue.commands['img100'] = {

  execCommand: function () {
    var focusNode = ue.selection.getStart();
    UE.dom.domUtils.setAttributes(focusNode, {width: '100%'})
  }
}
/*设置选中的显示宽度，50%*/
ue.commands['img50'] = {

  execCommand: function () {
    var focusNode = ue.selection.getStart();
    UE.dom.domUtils.setAttributes(focusNode, {width: '50%'})
  }
}
/*设置选中的显示宽度，33.33%*/
ue.commands['img33'] = {

  execCommand: function () {
    var focusNode = ue.selection.getStart();
    UE.dom.domUtils.setAttributes(focusNode, {width: '33.3333%'})
  }
}

ue.commands['imgscale'] = {

  execCommand: function (cmdName, width) {
    var focusNode = ue.selection.getStart();
    UE.dom.domUtils.setAttributes(focusNode, {width: width})
  }
}

/*自定义输入图片宽度比例*/
ue.commands['imgscale2'] = {

  execCommand: function () {

    var focusNode = ue.selection.getStart();

    /*设置输入框提示信息*/
    aprompt.promptTip = "请输入(1~100)";

    layer.open({
      type: 1,
      offset: '50px',
      skin: 'layui-layer-molv',
      title: "自定义图片宽度比例(1~100)%",
      area: ['300px', '150px'],
      shade: 0,
      shadeClose: false,
      content: jQuery("#prompt"),
      btn: ['确定', '取消'],
      btn1: function (index) {
        var num = aprompt.templateName;
        num = Number.parseInt(num);
        if (num > 0 && num <= 100) {

          UE.dom.domUtils.setAttributes(focusNode, {width: num + '%'});
        } else {
          alert("您输入的值不合法，请重新输入(1~100)")
        }

        aprompt.templateName = null;
        layer.close(index);
      }
    });
  }
}

/*定义右键删除选中图片菜单*/
ue.commands['rdelete'] = {

  execCommand: function () {
    var focusNode = ue.selection.getStart();
    if (focusNode && focusNode.tagName == "IMG") {
      UE.dom.domUtils.remove(focusNode);
    }
  }
}

/*定义右键菜单  删除所有视频对象*/
ue.commands['rdelvideos'] = {

  execCommand: function () {
    var videos = this.document.getElementsByTagName("video");
    for (var i = videos.length - 1; i >= 0; i--) {
      //移除video标签
      UE.dom.domUtils.remove(videos[i]);
    }
  }
}


/*右键菜单 删除右击对象*/
ue.commands['rdelvideo'] = {

  execCommand: function () {

    if (clickTarget && clickTarget.tagName == "VIDEO") {
      UE.dom.domUtils.remove(clickTarget);
    }
    clickTarget = null;
  }
}


/*定义右键视频缩略图菜单*/
var videos;
ue.commands['videoposter'] = {

  execCommand: function () {
    $('#videoposterModal').modal('toggle');
    videos = this.document.getElementsByTagName("video");
  }
};

/*定义右键菜单 -- 光标跳到文档最后*/
ue.commands['insertendrow'] = {

  execCommand: function () {
    ue.focus(true);
  }
};

var focusNode2;
/*定义链接商品按钮功能*/
ue.commands['newlink'] = {

  execCommand: function () {
    focusNode2 = ue.selection.getStart();
    if (focusNode2.tagName != 'IMG') {
      return;
    }
    var linkType = focusNode2.getAttribute("data-link-type");
    var linkSeq = focusNode2.getAttribute("data-link-seq");
    if (linkType != null && linkSeq != null && linkSeq != "") {
      vm.sowingMap.type = linkType;
      vm.sowingMap.linkSeq = linkSeq;
      if (linkType == 1) {
        vm.sowingMap.linkName = '已选择' + linkSeq.split(",").length + '商品';
      } else if (linkType == 2) {
        vm.sowingMap.linkName = '已选择' + linkSeq.split(",").length + '分类';
      } else if (linkType == 3) {

      }

    } else {
      vm.sowingMap.type = 1;
      vm.sowingMap.linkSeq = "";
      vm.sowingMap.linkName = '请点击关联内容'
    }

    var img = focusNode2.outerHTML;
    $('#imgShow').html(img);
    $('#myModal').modal('toggle');
  }
}

/*模态框提交按钮触发*/
function ok() {
  UE.dom.domUtils.setAttributes(focusNode2, {
    'data-link-type': vm.sowingMap.type,
    'data-link-seq': vm.sowingMap.linkSeq,
    'tapmode': "active",
    'onclick': 'openAllShoesList(this)',
    'title': vm.sowingMap.title
  })
  $('#myModal').modal('hide');
}

/*设置视频缩略图模态框提交按钮触发方法*/
function ok2() {
  var files = $("#inputfile").prop('files');//获取到文件列表

  if (files.length < 1) {
    alert("上传文件数量不能为0")
    return;
  }

  var formFile = new FormData();
  formFile.append("file", files[0]); //加入文件对象

  $.ajax({
    url: baseURL + "editor/uploadVideoPoster",
    data: formFile,
    type: "Post",
    dataType: "json",
    cache: false,//上传文件无需缓存
    processData: false,//用于对data参数进行序列化处理 这里必须false
    contentType: false, //必须
    success: function (data, status) {
      if (data.code == 0) {
        for (var i = 0; i < videos.length; i++) {
          UE.dom.domUtils.setAttributes(videos[i], {poster: data.imgPath});
        }
        alert("视频缩略图设置成功");
      } else {
        alert("视频缩略图设置失败，请重试");
      }


    },
    error: function (data, status) {
      alert("图片上传失败");
    }
  });

  $('#videoposterModal').modal('hide');
}


$("#inputfile").change(function () {
  var file = this.files[0];

  //图片验证
  if (!/image\/\w+/.test(file.type)) {
    alert("文件必须为图片！");
    return;
  }

  if (window.FileReader) {
    var reader = new FileReader();
    reader.readAsDataURL(file);
    //监听文件读取结束后事件
    reader.onloadend = function (e) {
      //e.target.result就是最后的路径地址
      $("#videoPoster").attr("src", e.target.result);
    };
  }
});

/*右键菜单功能   移动右键对象到下一个元素后*/
ue.commands['movedown'] = {

  execCommand: function () {

    if (clickTarget && (clickTarget.tagName == "IMG" || clickTarget.tagName == "VIDEO")) {
      var nextNode = UE.dom.domUtils.getNextDomNode(clickTarget);
      if (nextNode) {
        UE.dom.domUtils.insertAfter(nextNode, clickTarget);
      }
    }
  }
}

/*右键菜单功能   移动右键对象到上一个元素前*/
ue.commands['moveup'] = {

  execCommand: function () {

    if (clickTarget && (clickTarget.tagName == "IMG" || clickTarget.tagName == "VIDEO")) {
      var prevNode = clickTarget.previousSibling;
      if (prevNode) {
        UE.dom.domUtils.insertAfter(clickTarget, prevNode);
      }
    }
  }
}


/*------模版树状图---------------*/
var templateTreeSetting = {
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
    },
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
    showRemoveBtn: false,
    showRenameBtn: false,
    removeTitle: "删除",
    renameTitle: "重命名"
  },
  callback: {
    onCheck: zTreeOnCheck,
    onRightClick: zTreeOnRightClick,
    onClick: zTreeOnClick,
  }

};


/*触发右击事件后，将触发节点存入zTreeOnRightClickNode，用于设置主模板后选中该节点*/
var zTreeOnRightClickNode;

/*模版树节点右击事件 触发函数*/
function zTreeOnRightClick(event, treeId, treeNode) {
  if (treeNode.seq != 0) {
    rMenuVM.isShowMenu = true;
    if (treeNode.used == 1) {
      rMenuVM.rMenuTitle = "取消展示";
    } else {
      rMenuVM.rMenuTitle = "设置展示";
    }
  } else {
    rMenuVM.isShowMenu = false;
  }
  showRMenu(event.clientX, event.clientY);
  zTreeOnRightClickNode = treeNode;
};

/*设置主模板*/
function setMainTemplate() {
  if (zTreeOnRightClickNode) {
    $.post(baseURL + 'editor/setUsed',
      {
        seq: zTreeOnRightClickNode.seq,
        companySeq: zTreeOnRightClickNode.companySeq,
        used: zTreeOnRightClickNode.used,
        uType: 1
      },
      function (data, status) {
        if (data.code == 0) {
          if (zTreeOnRightClickNode.used == 0) {
            rMenuVM.tmpNodeSeq = zTreeOnRightClickNode.seq;
            loadUeditorRecords(checkedNode2);
          } else if (zTreeOnRightClickNode.used == 1) {
            loadUeditorRecords();
            clearContent();
          }


        } else {
          alert("设置展示失败")
        }
      });
  } else {
    alert("未选中模版")
  }
  hideRMenu();
}

var rMenu = $("#rMenu");

/*显示节点右键菜单*/
function showRMenu(x, y) {
  $("#rMenu").show();
  y += $(document).scrollTop() - 10;
  x += $(document).scrollLeft() - 10;
  rMenu.css({
    "top": y + "px",
    "left": x + "px",
    "visibility": "visible"
  });
  $(document).on("mousedown", onBodyMouseDown);
}

function onBodyMouseDown(event) {
  if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length > 0)) {
    rMenu.css({
      "visibility": "hidden"
    });
  }
}

function hideRMenu() {
  if (rMenu)
    rMenu.css({
      "visibility": "hidden"
    });

  $(document).off("mousedown");
}


function zTreeOnClick(event, treeId, treeNode) {
  // alert(treeNode.tId + ", " + treeNode.name);
  if (treeNode.seq != 0 && !treeNode.checked) {
    checkedNode(treeNode.seq);
    setContent2(treeNode.content);
  }
};


/*单选按钮触发方法*/
function zTreeOnCheck(event, treeId, treeNode) {

  if (treeNode.checked) {
    // console.log("被选中了")
    setContent2(treeNode.content);
  } else {
    // console.log("取消选中了")
    clearContent();
  }
}

function addDiyDom(treeId, treeNode) {

  var aObj = $("#" + treeNode.tId + "_a");
  if (treeNode.seq != 0) {
    var editStr = '&nbsp;<label class="label label-info" >&nbsp;' + treeNode.inputTime + '&nbsp;</label>';
    if (treeNode.used == 1) {
      editStr = editStr + '&nbsp;<label class="label label-warning" >&nbsp;正在展示&nbsp;</label>';
    }
    aObj.append(editStr);
  }
}


/*加载模板树数据*/
function loadUeditorRecords(callback) {
  $.get(baseURL + 'editor/content/1', function (r) {
    var records = r.records;

    for (var i = 0; i < records.length; i++) {
      records[i].parentSeq = 0;
    }
    records.push({seq: 0, parentSeq: -1, name: "所有模版"});
    // console.log(records, typeof records);
    atree = $.fn.zTree.init($("#templateTree"), templateTreeSetting, records);

    atree.expandAll(true);

    if (typeof (callback) === "function") {
      callback();
    }
  })

}

/*根据seq选中节点*/
function checkedNode(seq) {
  var node = atree.getNodeByParam("seq", seq, null);
  if (node == null) {
    return false;
  }
  atree.checkNode(node, true, true);
  return true;
}

/*loadUeditorRecords回调函数*/
function checkedNode2() {
  var node = atree.getNodeByParam("seq", rMenuVM.tmpNodeSeq, null);
  atree.checkNode(node, true, true);
  setContent2(node.content);
}

/*选中最新添加的节点*/
function checkMaxSeqNode() {
  var nodes = atree.getNodesByParam("parentSeq", 0, null);
  atree.checkNode(nodes[nodes.length - 1], true, true);
}

/*拖动对象*/
var srcTarget;
/*存储目的对象宽度*/
var tmpWidth;

/**
 * 设置editor的body内元素的拖拽监听事件,编辑器初始化完成后执行
 */
function setEditorBodyOnDrag() {

  ue.body.ondragstart = function (e) {
    var fromTarget = e.target;
    if (fromTarget && (fromTarget.tagName == "IMG" || fromTarget.tagName == "VIDEO")) {
      srcTarget = fromTarget;
      // console.log("from : ", srcTarget);
    }
  }

  ue.body.ondragover = function (e) {
    e.preventDefault();
  }

  ue.body.ondragenter = function (e) {
    e.preventDefault();
    var tmpTarget = e.target;
    if (tmpTarget && !tmpTarget.isSameNode(srcTarget) && (tmpTarget.tagName == "IMG" || tmpTarget.tagName == "VIDEO")) {

      // console.log("enter:tmpTarget: ", tmpTarget)
      var _tmpTarget = $(tmpTarget);
      _tmpTarget.attr("style", "border-right: 2px dashed red");
      tmpWidth = _tmpTarget.attr("width");
      if (tmpWidth.charAt(tmpWidth.length - 1) == "%") {
        // console.log("------图片%---------------")
        var len = tmpWidth.substring(0, tmpWidth.length - 1) - 1;
        tmpTarget.setAttribute("width", len + "%");
      } else {
        var len = tmpWidth - 2;
        tmpTarget.setAttribute("width", len + "px");
      }
    }
  }

  ue.body.ondragleave = function (e) {
    e.preventDefault();
    var tmpTarget = e.target;
    if (tmpTarget && !tmpTarget.isSameNode(srcTarget) && (tmpTarget.tagName == "IMG" || tmpTarget.tagName == "VIDEO")) {
      tmpTarget.removeAttribute("style");
      tmpTarget.setAttribute("width", tmpWidth);
      // console.log("leave: ", tmpTarget)
    }
  }

  ue.body.ondrop = function (e) {
    e.preventDefault();
    // console.log("to : ", e.target)
    var toTarget = e.target;

    if (srcTarget && toTarget && !srcTarget.isSameNode(toTarget)
      && (toTarget.tagName == "IMG" || toTarget.tagName == "VIDEO")) {
      toTarget.removeAttribute("style");
      toTarget.setAttribute("width", tmpWidth);
      UE.dom.domUtils.insertAfter(toTarget, srcTarget);
    }
    /*    else {
          console.log("两个节点相同，不操作")
        }*/

    srcTarget = null;
    tmpWidth = null;
  }


}

/**
 * 给所有编辑器的内容元素添加draggable属性
 */
function setChildNodesDraggable() {

  var nodes = ue.body.childNodes;
  for (var i in nodes) {
    if (nodes[i].tagName == "IMG" || nodes[i].tagName == "VIDEO") {
      // console.log(nodes[i])
      nodes[i].setAttribute("draggable", "true");
    }
  }
}


/*删除模版节点*/
function delTempalte() {
  hideRMenu();
  if (!zTreeOnRightClickNode) {
    return;
  }

  var treeNode = zTreeOnRightClickNode;

  confirm("确定要删除 " + treeNode.name + " 吗?", function () {
    if (treeNode.checked) {
      clearContent();
    }

    $.post(baseURL + 'editor/delete',
      {
        seq: treeNode.seq,
      },
      function (data, status) {
        if (data.code == 0) {
          layer.msg(treeNode.name + " 删除成功！");
          var treeObj = $.fn.zTree.getZTreeObj("templateTree");
          treeObj.removeNode(treeNode);
        } else {
          alert(treeNode.name + " 删除失败！");
        }
      });
  });

}

function editTemplate() {
  hideRMenu();
  if (!zTreeOnRightClickNode) {
    return;
  }

  var treeNode = zTreeOnRightClickNode;
  rMenuVM.tmpNodeSeq = treeNode.seq;

  aprompt.templateName = null;
  aprompt.promptTip = "修改模板名称";

  layer.open({
    type: 1,
    title: '修改模板名称',
    resize: false,
    area: ['300px', '150px'], //宽高
    content: $('#prompt'),
    btn: ['确定', '取消'],
    yes: function (index, layero) {

      if (isBlank(aprompt.templateName)) {
        layer.alert("名称不能为空")
        return;
      }

      var templateName = aprompt.templateName.trim();

      $.post(baseURL + 'editor/update',
        {
          seq: treeNode.seq,
          name: templateName,
        },
        function (data, status) {
          if (data.code == 0) {
            loadUeditorRecords(checkedNode2);
          } else {
            alert("重命名失败，请重试");
          }
        });

      layer.close(index);
    },
    btn2: function (index, layero) {
      //取消按钮
      layer.close(index)
    },
    cancel: function (index, layero) {
      //关闭按钮
      layer.close(index)
    }
  });


}