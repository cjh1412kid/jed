$(function () {
  zTreelist();
});

var ztree, rMenu;
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
  edit: {
    drag: {
      isCopy: false,
      isMove: false
    },
    enable: false,// 设置是否处于编辑状态
    editNameSelectAll: true,
    showRemoveBtn: false,
    showRenameBtn: false
  },
  callback: {
    onClick: function (event, treeId, treeNode) {
      if (treeNode.isParent) {
        // console.log("不可编辑", treeNode.seq, treeNode.name);
        vm.isShow = 0;
      } else {
        // console.log("可编辑", treeNode.seq, treeNode.name);
        vm.panelTitle = treeNode.name;
        vm.isShow = 1;

        //清空数据
        vm.sizeAllotTemplates = [];
        //当前配码操作的品类
        vm.currCategorySeq = treeNode.seq;
        vm.currCategoryName = treeNode.name;

        //获取品类相关联的所有模版
        vm.loadTemplate(treeNode.seq);
      }
    }
  }
};


function zTreelist() {
  $.get(baseURL + "system/goodsCategory/list", function (r) {
    var categorys = r.list;
    categorys.push({
      seq: 0,
      parentSeq: -1,
      name: "所有品类"
    });
    ztree = $.fn.zTree.init($("#categoryTree"), setting, categorys);
    ztree.expandAll(true);
  });
  rMenu = $("#rMenu");
}

function addDiyDom(treeId, treeNode) {
  var aObj = $("#" + treeNode.tId + "_a");

  var editStr = "";

  if (treeNode.catetoryCode) {
    editStr = '&nbsp;<label class="label label-info" > &nbsp;' + treeNode.catetoryCode + ' &nbsp;</label>'
  }

  if (treeNode.seq != 0 && !treeNode.isParent) {
    editStr += '&nbsp;<label class="label label-warning" > 可编辑&nbsp;</label>';
  }

  if (editStr != "") {
    aObj.append(editStr);
  }
}


var vm = new Vue({
  el: '#rrapp',
  data: {
    //尺码分配编辑是否可见
    isShow: 0,
    panelTitle: '',

    /*配码模版列表-展示*/
    sizeAllotTemplates: [],

    /*尺码范围选择*/
    minSize: 35,
    maxSize: 41,

    /*模版编辑对话框*/
    //1新增 0修改
    operate: 1,
    //当前配码操作的品类
    currCategorySeq: -1,
    currCategoryName: '',
    edit: {
      name: '',
      details: []
    },
    update: {
      index: -1,
      templateSeq: -1
    }
  },
  methods: {
    /*查询品类关联的所有模版*/
    loadTemplate: function (categorySeq) {
      $.get(baseURL + 'sys/size-allot-template/query/' + categorySeq, function (data) {
        if (data.code == 0) {
          if (data.result) {
            // console.log("配码模版查询结果：", data.result)
            vm.sizeAllotTemplates = data.result;
          } else {
            // layer.msg("查询到配码结果为空")
          }
        } else {
          layer.msg("品类配码模版查询异常")
        }
      })
    },
    /**
     * 修改模版，修改按钮
     * @param templateSeq
     * @param index 模版对象在数组中的索引
     */
    editDetail: function (templateSeq, index) {
      // console.log("编辑：", templateSeq, index)
      var templateObj = this.sizeAllotTemplates[index];

      if (!templateObj || templateObj.seq != templateSeq) {
        layer.msg("修改功能异常，请联系管理员或刷新页面后重试")
        return;
      }
      //初始化数据
      this.operate = 0;
      this.edit.seq = templateObj.seq;
      this.edit.name = templateObj.name;
      this.edit.details = templateObj.details;

      //记录更新位置
      this.update = {
        index: index,
        templateSeq: templateSeq
      }

      $('#myModal').modal('show');
    },
    /**
     * 删除模版，删除按钮
     * @param templateSeq
     * @param index 模版对象在数组中的索引
     */
    delTemplate: function (templateSeq, index) {
      // console.log("删除：", templateSeq, index)

      layer.confirm("确定要删除吗？", function () {
        $.get(baseURL + 'sys/size-allot-template/del/' + templateSeq, function (data) {
          if (data.code == 0) {
            layer.msg(data.msg);
            //更新页面
            vm.$delete(vm.sizeAllotTemplates, index);
          } else {
            layer.alert("删除模版失败！请重试")
          }
        })
      })

    },
    /*创建新的品类-配码模版*/
    createTemplate: function () {
      //尺码范围检查
      if (!this.checkSizeBound()) return;

      this.operate = 1;
      //初始化数据
      if (isBlank(this.currCategoryName)) {
        this.edit.name = '';
      } else {
        this.edit.name = this.currCategoryName + '-配码模版-';
      }

      var detailList = [];
      for (var i = this.minSize; i <= this.maxSize; i++) {
        detailList.push({size: i, per: null})
      }

      this.$set(this.edit, 'details', detailList);
      $('#myModal').modal('show');
    },
    /*检查尺码范围是否合法*/
    checkSizeBound: function () {
      if (!this.minSize || !this.maxSize) {
        layer.msg("请先输入尺码范围")
        return false;
      } else if (isNaN(this.minSize) || isNaN(this.maxSize)) {
        layer.msg("尺码非数字，请检查")
        return false;
      } else if (this.minSize < 1 || this.maxSize < 1) {
        layer.msg("尺码不能小于1，请检查")
        return false;
      } else if (this.maxSize < this.minSize) {
        layer.msg("尺码范围不合法，请检查")
        return false;
      }

      return true;
    },
    /*检查提交数据是否合法*/
    checkSubmitData: function () {
      //模版名称不能为空
      if (isBlank(this.edit.name)) {
        layer.msg("模版名称不能为空")
        return false;
      }
      //尺码数量都是数字且不能是负数
      var detail;
      for (var i in this.edit.details) {
        detail = this.edit.details[i];
        if (detail.per) {
          if (isNaN(detail.per)) {
            layer.msg("尺码" + detail.size + "的值不合法，含有非数字")
            return false;
          } else if (detail.per < 0) {
            layer.msg("尺码" + detail.size + "的值不合法，数字必须>=0")
            return false;
          }
        } else {
          this.edit.details[i].per = 0;
        }
      }

      return true;
    },
    /*创建或修改模版-提交按钮*/
    ok: function () {
      //数据验证，不合法则结束执行
      if (!this.checkSubmitData()) {
        return;
      }

      if (this.operate) {
        //新增
        $.ajax({
          type: "POST",
          url: baseURL + 'sys/size-allot-template/save/' + vm.currCategorySeq,
          dataType: "json",
          cache: false,
          contentType: "application/json;charset=utf-8",
          data: JSON.stringify(vm.edit),
          success: function (data) {
            if (data.code == 0) {
              vm.sizeAllotTemplates.unshift(data.result);
              $('#myModal').modal('hide');
            } else {
              layer.msg("新增配码模版失败！请重试")
            }
          }
        })

      } else {
        //修改
        $.ajax({
          type: "POST",
          url: baseURL + 'sys/size-allot-template/update',
          dataType: "json",
          cache: false,
          contentType: "application/json;charset=utf-8",
          data: JSON.stringify(vm.edit),
          success: function (data) {
            if (data.code == 0) {
              if (vm.edit.seq == vm.update.templateSeq) {
                vm.loadTemplate(vm.currCategorySeq);
                layer.msg(data.msg);
              } else {
                layer.alert("页面更新异常，请刷新页面")
              }

              $('#myModal').modal('hide');
            } else {
              layer.msg("更新配码模版失败！请重试")
            }
          }
        })
      }


    }

  }
});
