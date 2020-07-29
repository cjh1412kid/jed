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

$(function () {
  $("#jqGrid").jqGrid({
    url: baseURL + 'sowingMap/list',
    datatype: "json",
    colNames: ['序号', '图片', '关联类型', '关联项目', '创建时间', '操作'],
    colModel: [
      {name: 'seq', align: "center", hidden: true, hidedlg: true, key: true},
      {
        name: 'image', width: 150, formatter: function (cellvalue) {
          var detail = '<image src="' + cellvalue + '" style="width: 250px;height: 70px;"/>';
          return detail;
        }
      },
      {
        name: 'type', width: 200, align: "center", formatter: function (cellvalue) {
          if (cellvalue == 1) {
            return "关联商品";
          } else if (cellvalue == 2) {
            return "关联分类";
          } else if (cellvalue == 3) {
            return "关联波次";
          } else {
            return "-";
          }
        }
      },
      {
        name: 'linkSeq', width: 150, align: "center", formatter: function (cellvalue, options, rowObject) {
          var type = rowObject.type,
            length = cellvalue.split(",").length;
          if (type == 1) {
            return length + "个商品";
          } else if (type == 2) {
            return length + "个分类";
          } else if (type == 3) {
            return length + "个波次";
          } else {
            return "-";
          }
        }
      },
      {name: 'inputTime', width: 80, align: "center"},
      {
        name: 'method', width: 70, align: "center", hidedlg: true,
        formatter: function (cellvalue, options, rowObject) {
          var detail = '<button onclick="lineEdit(' + rowObject.seq + ')" class="operation-btn-security" ">编辑</button>'
            + '<button onclick="lineDelete(' + rowObject.seq + ')" class="operation-btn-dangery" ">删除</button>';
          return detail;
        }
      }
    ],
    height: 'auto',
    rowNum: 10,
    rowList: [10, 30, 50],
    rownumbers: true,
    rownumWidth: 25,
    autowidth: true,
    multiselect: false,
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
      $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
    }
  });

  $("#jqGoodsGrid").jqGrid({
    url: baseURL + "order/goods/getGoodsList",
    datatype: "json",
    mtype: 'post',
    colModel: [
      {label: '序号', name: 'seq', width: 100, align: "center", key: true, hidden: true, sortable: false},
      {label: '货号', name: 'goodID', width: 200, align: "center"},
      {
        label: '图片', name: 'img1', width: 300, align: "center", formatter: function (cellvalue) {
          var detail = '<image src="' + cellvalue + '" style="width: 100px;height: 70px;"/>';
          return detail;
        }
      },
      {label: '分类', name: 'categoryName', width: 120, align: "center"},
    ],
    height: 'auto',
    rowNum: 10,
    rowList: [10, 30, 50],
    rownumWidth: 25,
    autowidth: true,
    multiselect: true,
    viewrecords: true,
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


});

function getGridParam(param) {
  return $("#jqGrid").jqGrid('getGridParam', param);
}

vm = new Vue({
  el: '#rrapp',
  data: {
    showList: true,
    title: '',
    sowingMap: {type: 0},
    // 表格加载条件
    categorySeq: '',
    selectCategoryName: '',
    keywords: '',
    yearList: [],
    seasonList: [],
    categoryList: [],
    year: -1,
    seasonSeq: -1,
    selectSeasonName: '',
  },
  methods: {
    // 切换关联类型之后，删除原选择内容
    typeChange: function (event) {
      vm.$set(vm.sowingMap, "linkName", "");
      vm.sowingMap.linkSeq = "";
    },
    // 加载商品列表
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
    loadYearList: function () {
      $.get(baseURL + "order/goods/yearList", function (data) {
        if (data.result) {
          vm.yearList = data.result;
        }
      })
    },
    loadSeasonList: function () {
      $.get(baseURL + "order/goods/seasonList", function (data) {
        if (data.result) {
          vm.seasonList = data.result;
        }
      })
    },
    loadCategory2: function () {
      // 加载分类树
      $.get(baseURL + "system/goodsCategory/list", function (r) {
        vm.categoryList = r.list;
      });
    },
    categorySelect: function (item) {
      vm.categorySeq = item.seq;
      vm.selectCategoryName = item.name;
    },
    add: function () {
      var records = getGridParam('records');
      if (records >= 5) {
        alert("最多创建5个轮播图");
        return;
      }
      vm.sowingMap = {type: 0};
      vm.showList = false;
    },
    reload: function (event) {
      vm.showList = true;
      var page = getGridParam('page');
      $("#jqGrid").jqGrid('setGridParam', {
        page: page
      }).trigger("reloadGrid");
    },
    handleFileChange: function (value) {
      var inputDOM = value.target;
      // 通过DOM取文件数据
      vm.sowingMap.imageFile = inputDOM.files[0];
      var size = Math.floor(vm.sowingMap.imageFile.size / 1024 / 1024);
      if (size > 10) {
        alert("文件超过10M啦！");
        return;
      }
      vm.imgPreview(vm.sowingMap.imageFile);
      inputDOM.type = 'text';
      inputDOM.type = 'file';
    },
    imgPreview: function (file) {
      if (!file || !window.FileReader) return;
      if (/^image/.test(file.type)) {
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onloadend = function () {
          vm.$set(vm.sowingMap, "image", this.result);
        }
      }
    },
    saveOrUpdate: function () {
      if (vm.validator()) {
        return;
      }
      var url = vm.sowingMap.seq ? "sowingMap/update" : "sowingMap/save";
      var formData = new FormData();
      if (vm.sowingMap.seq) {
        formData.append("seq", vm.sowingMap.seq);
      }
      if (vm.sowingMap.imageFile) {
        formData.append("imageFile", vm.sowingMap.imageFile);
      }
      formData.append("type", vm.sowingMap.type);
      formData.append("linkSeq", vm.sowingMap.linkSeq);
      $.ajax({
        type: "POST",
        url: baseURL + url,
        contentType: false,
        processData: false,
        data: formData,
        enctype: 'multipart/form-data',
        cache: false,
        success: function (r) {
          if (r.code === 0) {
            layer.msg('操作成功');
            vm.reload();
          } else {
            alert(r.msg);
          }
        }
      });
    },
    validator: function () {
      if (isBlank(vm.sowingMap.image)) {
        if (!vm.sowingMap.imageFile) {
          alert("轮播图片不能为空");
          return true;
        }
      }
//      if (isBlank(vm.sowingMap.type)) {
//        alert("请选择关联类型");
//        return true;
//      }
//      if (isBlank(vm.sowingMap.linkSeq)) {
//        alert("请选择关联项");
//        return true;
//      }
    },
    contentSelect: function () {
      var type = vm.sowingMap.type;
      if (type == 1) {
        vm.loadGoodList();
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
            console.log(ids);
            vm.$set(vm.sowingMap, "linkName", "已选择" + ids.length + "件商品");
            vm.sowingMap.linkSeq = ids.join(',');
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
            for (var i = 0; i < nodes.length; i++) {
              if (!nodes[i].children) {
                childSelect.push(nodes[i].seq);
              }
            }
            vm.$set(vm.sowingMap, "linkName", "已选择" + childSelect.length + "个分类");
            vm.sowingMap.linkSeq = childSelect.join(',');
            layer.close(index);
          }
        });
      } else {
        alert("请先选择关联类型");
      }
    },
    search: function () {
      $("#jqGoodsGrid").jqGrid('setGridParam', {
        datatype: 'json',
        postData: {
          'categorySeq': vm.categorySeq,
          'year': vm.year,
          'seasonSeq': vm.seasonSeq,
          'keywords': vm.keywords
        },
        page: 1,
      }).trigger('reloadGrid');
    },

  },
  created: function () {
    this.loadCategory2();
    this.loadYearList();
    this.loadSeasonList();
  },
});

function lineEdit(seq) {
  $.get(baseURL + "sowingMap/edit?seq=" + seq, function (r) {
    vm.showList = false;
    vm.title = "修改";
    var sowingMap = r.sowingMap;
    if (sowingMap.linkSeq && sowingMap.linkSeq.length > 0) {
      var selectLength = sowingMap.linkSeq.split(",").length;
      if (sowingMap.type == 1) {
        sowingMap.linkName = "已选择" + selectLength + "件商品";
      } else if (sowingMap.type == 2) {
        sowingMap.linkName = "已选择" + selectLength + "个分类";
      } else if (sowingMap.type == 3) {
        sowingMap.linkName = "已选择" + selectLength + "个波次";
      }
    }
    vm.sowingMap = sowingMap;
  });
}

function lineDelete(seq) {
  confirm('确定要删除选中的记录？', function () {
    $.ajax({
      type: "POST",
      url: baseURL + "sowingMap/del?seq=" + seq,
      success: function (r) {
        if (r.code === 0) {
          layer.msg('操作成功');
          vm.reload();
        } else {
          alert(r.msg);
        }
      }
    });
  });
}