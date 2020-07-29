

/*输入框弹窗*/
var aprompt = new Vue({
  el: '#prompt',
  data: {
    noticeTitle: null,
    promptTip: "",
  }
});


<!-- 实例化编辑器 -->
var ue = UE.getEditor('UEditor', {
  enterTag: '',
  allowDivTransToP: false,
  initialFrameHeight: 350,
  toolbars: [['bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript',
    '|', 'fontfamily', 'fontsize', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist'], [
    'fullscreen', 'undo', 'redo', '|', 'simpleupload', 'insertimage', '|', 'preview', '|',
    'imgonecols', 'imgtwocols', 'imgthreecols', '|', 'img100', 'img50', 'img33', '|', 'newlink', 'insertvideo',]]
});


ue.ready(function () {
  /*隐藏右上角本地保存提示框*/
  $(".edui-editor-messageholder.edui-default").css({"visibility": "hidden"});

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

/*清空内编辑器内容*/
function clearContent() {
  ue.setContent("");
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
    url: baseURL + "sowingMap/goodList",
    datatype: "json",
    colNames: ['序号', '货号', '描述', '图片'],
    colModel: [
      {name: 'seq', width: 50, align: "center", key: true},
      {name: 'goodID', width: 120, align: "center"},
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


function jqPeriodGridTable() {
  $("#jqPeriodGrid").jqGrid({
    url: baseURL + "sowingMap/periodList",
    datatype: "json",
    colNames: ['序号', '名称', '年份', '销售日期'],
    colModel: [
      {name: 'seq', width: 50, align: "center", key: true},
      {name: 'name', width: 120, align: "center"},
      {name: 'year', width: 200, align: "center"},
      {name: 'saleDate', width: 200, align: "center"}
    ],
    height: 'auto',
    rowNum: 2,
    rownumWidth: 25,
    autowidth: true,
    multiselect: true,
    pager: "#jqPeriodGridPager",
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
      $("#jqPeriodGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
    },
    loadComplete: function () {
      // 设置选择项
      if (vm && vm.sowingMap.linkSeq && vm.sowingMap.linkSeq.length > 0) {
        var rows = vm.sowingMap.linkSeq.split(',');
        for (var i = 0; i < rows.length; i++) {
          $("#jqPeriodGrid").jqGrid('setSelection', rows[i]);
        }
      }
    }
  });
}

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
        categorys.push({seq: 0, parentSeq: -1, name: "所有分类"});
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
    // 加载波次列表
    loadPeriodList: function () {
      $("#jqPeriodGrid").trigger("reloadGrid");
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
          area: ['800px', '500px'],
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
      } else if (type == 3) {
        jqPeriodGridTable();
        vm.loadPeriodList();
        layer.open({
          type: 1,
          offset: '50px',
          skin: 'layui-layer-molv',
          title: "选择菜单",
          area: ['600px', '450px'],
          shade: 0,
          shadeClose: false,
          content: jQuery("#periodLayer"),
          btn: ['确定', '取消'],
          btn1: function (index) {
            var ids = $("#jqPeriodGrid").jqGrid("getGridParam", "selarrrow");
            vm.$set(vm.sowingMap, "linkName", "已选择" + ids.length + "个波次");
            vm.sowingMap.linkSeq = ids.join(',');
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
        var num = aprompt.noticeTitle;
        num = Number.parseInt(num);
        if (num > 0 && num <= 100) {

          UE.dom.domUtils.setAttributes(focusNode, {width: num + '%'});
        } else {
          alert("您输入的值不合法，请重新输入(1~100)")
        }

        aprompt.noticeTitle = null;
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

  //文件为空判断
  if (!file) return;

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


var editorAreaVM = new Vue({
  el: "#app",
  data: {
    showList: true,
    //操作类型： 0新建 1更新
    operateType: 0,
    panel: {
      title: "创建公告"
    },
    notice: {
      seq: 0,
      imageSrc: "",
      file: null,
      name: '',
      isUsed: 1,
    }

  },
  methods: {
    fileChange: function (e) {
      var _this = e.target;
      var file = _this.files[0];

      //未选择文件
      if (!file) {
        return;
      }

      //检验是否为图像文件
      if (!/image\/\w+/.test(file.type)) {
        alert("不是图片！");
        return;
      }

      var reader = new FileReader();
      //将文件以Data URL形式读入页面
      reader.readAsDataURL(file);
      reader.onload = function (e) {
        editorAreaVM.notice.imageSrc = this.result;
        editorAreaVM.notice.file = file;
      }
    },
    /**
     * 新建公告最后的保存操作
     */
    saveNotice: function () {
      var _file = this.notice.file;

      //判断操作：新建或更新
      if (this.operateType == 0) {
        //新建, 判断是否存在公告图
        if (!_file) {
          layer.alert("请先选择公告图！", {icon: 2})
          return;
        }

        var name = this.notice.name;
        if (!name || name.trim() == "") {
          layer.alert("公告名称不能为空", {icon: 2});
          return;
        }

        //获取详情页内容
        var detailContent = ue.getContent();

        if (detailContent) {
          detailContent = detailContent.trim();
          detailContent = encodeURIComponent(detailContent);
        }

        var formData = new FormData();
        formData.append("file", _file);
        formData.append("name", name);
        formData.append("isUsed", this.notice.isUsed);
        formData.append("detailedPage", detailContent);

        $.ajax({
          type: "POST",
          url: baseURL + "system/notice/save",
          data: formData,
          dataType: 'json',
          cache: false,//上传文件无需缓存
          contentType: false,//必须
          processData: false,//用于对data参数进行序列化处理 这里必须false
          success: function (data) {
            if (data.code == 0) {
              layer.msg('保存成功', {icon: 6});
              reloadGrid();
            } else {
              layer.msg('保存失败，请重试', {icon: 5});
            }

          }
        })//ajax end


      } else {
        //更新, 判断公告图是否改变
        var _seq = this.notice.seq;

        //获取详情页内容
        var detailContent = ue.getContent();

        if (detailContent) {
          detailContent = detailContent.trim();
          detailContent = encodeURIComponent(detailContent);
        }

        var formData = new FormData();
        formData.append("seq", _seq);
        formData.append("name", this.notice.name);
        formData.append("isUsed", this.notice.isUsed);
        formData.append("detailedPage", detailContent);
        if (_file) {
          formData.append("file", this.notice.file);
        }

        $.ajax({
          type: "POST",
          url: baseURL + "system/notice/update",
          data: formData,
          dataType: 'json',
          cache: false,//上传文件无需缓存
          contentType: false,//必须
          processData: false,//用于对data参数进行序列化处理 这里必须false
          success: function (data) {
            if (data.code == 0) {
              layer.msg('保存成功', {icon: 6});
              reloadGrid();
            } else {
              layer.msg('保存失败，请重试', {icon: 5});
            }

          }
        })//ajax end
      }
    },
    /*返回键操作*/
    goHome: function () {
      editorAreaVM.showList = true;
    }
  }
})


/**
 * 新建公告
 */
function createNotice() {
  //重置标题
  editorAreaVM.operateType = 0;

  //清除公告图
  editorAreaVM.notice = {
    imageSrc: '',
    file: null,
    name: '',
    isUsed: 1,
  }

  //清除详情页
  ue.setContent("");

  editorAreaVM.showList = false;
}


$(function () {
  loadGrid();
})


function loadGrid() {
  //初始化表格
  $("#jqGrid").jqGrid({
    url: baseURL + "system/notice/list2",
    datatype: "json",
    mtype: "POST",
    postData: {},
    colModel: [
      {label: '序号', name: 'seq', width: 100, hidden: true, align: "center"},
      {label: '名称', name: 'name', width: 150, align: "center"},
      {
        label: '公告图', name: 'imgSrc', width: 150, align: "center", formatter: function (cellvalue, options, rowObject) {

          return '<img  src="' + cellvalue + '" style="width: 80px;height: 80px;"/>';
        }
      },
      {
        label: '是否展示', name: 'isUsed', width: 60, align: "center", formatter: function (cellvalue, options, rowObject) {
          return cellvalue == 1 ? '是' : '否';
        }
      },
      {label: '创建时间', name: 'inputTime', width: 100, align: "center"},
      {label: '详情页', name: 'detailedPage', width: 100, hidden: true, align: "center"},
      {
        label: '操作', width: 50, align: "center",
        formatter: function (cellvalue, options, rowObject) {
          return '<button class="operation-btn-security" onclick="lineEdit(' + options.rowId + ')">编辑</button>'
            + '<button class="operation-btn-dangery" onclick="lineDel(' + rowObject.seq + ',\'' + rowObject.name + '\')">删除</button>';
        }
      }
    ],
    height: 'auto',
    rowNum: 10,
    rowList: [10, 30, 50],
    rownumbers: true,
    rownumWidth: 30,
    autowidth: true,
    multiselect: false,
    viewrecords: true,
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
}

/**
 * 重载表格，刷新表格数据
 */
function reloadGrid() {
  $("#jqGrid").setGridParam({
    datatype: 'json',
    postData: {},
    page: 1,
  }).trigger('reloadGrid');

  editorAreaVM.showList = true;
}

/**
 * 编辑表格行
 */
function lineEdit(rowId) {
  editorAreaVM.operateType = 1;

  var rowObj = $("#jqGrid").getRowData(rowId);

  if (!rowObj.seq) {
    layer.msg("更新条件异常：公告序号不存在")
    return;
  }
  rowObj.imgSrc = $(rowObj.imgSrc).attr("src");

  editorAreaVM.notice = {
    seq: rowObj.seq,
    name: rowObj.name,
    imageSrc: rowObj.imgSrc,
    isUsed: rowObj.isUsed == '是' ? 1 : 0,
    file: null,
  };

  //设置详情页
  setContent2(rowObj.detailedPage);

  editorAreaVM.showList = false;
}

/**
 *  删除表格行
 */
function lineDel(seq, name) {

  confirm("确定删除 " + name + " 吗?", function () {
    $.get(baseURL + 'system/notice/del/' + seq, function (data, status) {
      if (data.code == 0) {
        layer.msg("删除成功", {icon: 1});
        reloadGrid();
      } else {
        layer.alert("删除失败!" + data.msg, {icon: 2});
      }
    });
  });
}