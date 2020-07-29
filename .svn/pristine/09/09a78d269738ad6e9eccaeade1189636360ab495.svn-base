$(function () {
  loadGrid();
  loadGrid2();
})

function loadGrid() {
  $("#jqGrid").jqGrid({
    url: baseURL + 'system/sievePlateDistribute/list',
    datatype: "local",
    colModel: [
      {label: '门店', name: 'shopName', width: 100, align: "center"},
      {label: '货品ID', name: 'goodID', width: 100, align: "center"},
      {label: '数量', name: 'distributeNum', width: 100, align: "center"},
      {label: '品类', name: 'categoryName', width: 100, align: "center"},
      {label: '门店初步订单序号', name: 'seq', width: 100, hidden: true, align: "center"},
      {
        label: '操作', name: 'goodsAllotSeq', width: 200, align: "center",
        formatter: function (cellvalue, options, rowObject) {
          //判断是否存在货品配码单，无则不可门店配码
          if (cellvalue) {
            //判断门店是否已配单
            if (rowObject.shopAllotSeq) {
              return '<button class="btn btn-warning btn-xs" onclick="resetShopAllotSize(' + cellvalue + ',' + rowObject.shopAllotSeq + ',' + options.rowId + ')"> 重新配码 </button>' +
                '&nbsp;&nbsp;&nbsp;&nbsp;<button class="btn btn-danger btn-xs" ' +
                'onclick="delShopAllot(' + cellvalue + ',' + rowObject.shopAllotSeq + ',\'' + rowObject.goodID + '\')"> 删除配码 </button>';
            } else {
              return '<button class="btn btn-primary btn-xs" onclick="shopAllotSize(' + cellvalue + ',' + options.rowId + ')"> 配码 </button>';
            }
          } else {
            return '';
          }

        }
      }
    ],
    height: 'auto',
    viewrecords: true,
    rowNum: 50,
    rowList: [10, 30, 50],
    rownumbers: true,
    rownumWidth: 30,
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
}

function loadGrid2() {
  $("#jqGrid2").jqGrid({
    url: baseURL + 'system/sievePlateDistribute/list2',
    datatype: "local",
    colModel: [
      {label: '货品ID', name: 'goodID', width: 200, align: "center"},
      {label: '品类', name: 'categoryName', width: 200, align: "center"},
      {label: '数量', name: 'total', width: 200, align: "center"},
      {label: '品类Seq', name: 'categorySeq', width: 100, hidden: true, align: "center"},
      {
        label: '操作', name: 'allotOrderSeq', width: 200, align: "center",
        formatter: function (cellvalue, options, rowObject) {
          var operate = "";
          if (cellvalue) {
            operate = '<button class="btn btn-warning btn-xs" onclick="updateGoodsAllotSize(' + options.rowId + ')"> 重新配码 </button>' +
              '&nbsp;&nbsp;&nbsp;&nbsp;<button class="btn btn-danger btn-xs" onclick="delGoodsAllot(' + cellvalue + ',\'' + rowObject.goodID + '\')"> 删除配码 </button>';
          } else {
            operate = '<button class="btn btn-primary btn-xs" onclick="editGoodsAllotSize(' + options.rowId + ')"> 配&nbsp;码 </button>';
          }
          return operate;
        }
      }
    ],
    height: 'auto',
    viewrecords: true,
    rowNum: 50,
    rowList: [10, 30, 50],
    rownumbers: true,
    rownumWidth: 30,
    autowidth: true,
    shrinkToFit: true,
    multiselect: false,
    pager: "#jqGridPager2",
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
      $("#jqGrid2").closest(".ui-jqgrid-bdiv").css({
        "overflow-x": "hidden"
      });
    }
  });
}

var vm = new Vue({
  el: "#app",
  data: {
    /*按门店汇总1，按货品汇总0*/
    tableType: 1,

    yearList: [],
    seasonList: [],
    shopList: [],

    /*查询条件*/
    year: '-1',
    seasonSeq: '-1',
    shopSeq: '-1',

    /*门店配码*/
    shopAllot: {
      shopName: '',
      count: '',
      goodID: '',
      details: [],
      /*工厂生成，余数参考*/
      refer: [
        {size: 35, count: 100, yu: 20},
        {size: 36, count: 200, yu: 30},
        {size: 37, count: 300, yu: 40},
        {size: 38, count: 600, yu: 50},
      ]
    },
    /*货号配码*/
    goodsAllot: {
      count: '',
      goodID: '',
      details: []
    },
    /*配码模版*/
    sizeAllotTemplates: [],
    /*选择的配码模版索引*/
    selectTemplateIndex: null,
    /*相对模版数量的倍数*/
    multiple: 0,
    multipleCopy: 0,
    /*模版所有尺码数量之和*/
    templateTotal: 0,
    /*配码模版倍数的总量*/
    multipleCount: 0,
    /*数量比较提示*/
    tip: '',
    /*聚焦文本框-作恢复用*/
    focusSize: null,
    /*自定义尺码范围*/
    minSize: null,
    maxSize: null,
  },
  methods: {
    reloadGrid: function () {

      var id = this.tableType ? '#jqGrid' : '#jqGrid2';

      var params = {
        'year': this.year,
        'seasonSeq': this.seasonSeq,
      }

      this.tableType && (params.shopSeq = this.shopSeq);

      $(id).setGridParam({
        datatype: 'json',
        postData: params,
        page: 1,
      }).trigger('reloadGrid');

      //在不实际改变浏览器窗口大小的情况下触发 window的 resize 事件
      window.dispatchEvent(new Event('resize'));
    },
    loadYears: function () {
      $.get(baseURL + "system/sievePlateDistribute/years", function (data) {
        if (data.result && data.result.length > 0) {
          vm.yearList = data.result;
          vm.year = data.result[0];
        }
        vm.loadSeasons();
      })
    },
    loadSeasons: function () {
      $.get(baseURL + "system/sievePlateDistribute/seasons", function (data) {
        if (data.result) {
          vm.seasonList = data.result;
        }
        vm.loadShops();
      })
    },
    loadShops: function () {
      $.get(baseURL + "system/sievePlateDistribute/shops", function (data) {
        if (data.result) {
          vm.shopList = data.result;
        }
        vm.initSelectParam();
      })
    },
    loadShopSizeList: function () {
      var sizeList = [];
      for (var i = 35; i <= 43; i++) {
        sizeList.push({size: i, per: null})
      }
      this.shopAllot.details = sizeList;
    },
    initSelectParam: function () {
      $.get(baseURL + 'system/sievePlateDistribute/recent/one', function (data) {
        if (data.result) {
          vm.year = data.result.year;
          vm.seasonSeq = data.result.seasonSeq;
          console.log('初始化查询条件：', vm.year, vm.seasonSeq)
          vm.reloadGrid();
        }
      })
    },
    search: function () {
      this.reloadGrid();
    },
    changeBtnActive: function (val) {
      this.tableType = val;
      this.reloadGrid();
    },
    /*查询品类关联的所有模版*/
    loadTemplate: function (categorySeq) {
      vm.sizeAllotTemplates = [];
      $.get(baseURL + 'sys/size-allot-template/query/' + categorySeq, function (data) {
        if (data.code == 0) {
          if (data.result) {
            // console.log("配码模版查询结果：", data.result)
            if (data.result.length > 0) {
              vm.sizeAllotTemplates = data.result;
            } else {
              layer.msg("未查询到配码模版")
            }
          } else {
            layer.msg("未查询到配码模版")
          }
        } else {
          layer.msg("品类配码模版查询异常")
        }
      })
    },
    /*选择模版，选中单选按钮*/
    selectTemplate: function () {
      var templateObj = this.sizeAllotTemplates[this.selectTemplateIndex];
      // console.log(this.selectTemplateIndex, templateObj)
      if (!templateObj) {
        layer.msg("功能异常：不能获取到模版索引")
        return;
      }
      //选中的模版
      this.templateSeq = templateObj.seq;
      //展示选中的模版
      this.$set(this.goodsAllot, 'details', templateObj.details);
      //计算模版总数
      var tmp = 0;
      for (var i in templateObj.details) {
        tmp += templateObj.details[i].per;
      }
      this.templateTotal = tmp;
      //计算在计划数量内最大倍数
      this.multiple = parseInt(this.goodsAllot.count / this.templateTotal);
      this.multipleCopy = this.multiple;

      this.fillTableData();
    },
    /**填充配码表格数据--最终提交的尺码数据*/
    fillTableData: function () {
      //判断是否使用了模版
      if (!this.goodsAllot.details || this.goodsAllot.details.length == 0) {
        layer.msg("请先选择模版")
        return;
      }

      //判断当前是否处于修改配码操作
      if (!this.goodsAllot.details[0].per) {
        layer.alert("不可加减操作：需选择模版后才可操作，将重新配码");
        return;
      }

      //计算总数
      this.multipleCount = this.multiple * this.templateTotal;

      //剩余提示信息
      var yu = this.goodsAllot.count - this.multipleCount;
      if (yu > 0) {
        this.tip = "比计划数量少 " + yu + " 双";
      } else if (yu < 0) {
        this.tip = "比计划数量多 " + -yu + " 双";
      } else {
        this.tip = '';
      }
      //自动填充表格数据
      for (var i in this.goodsAllot.details) {
        this.goodsAllot.details[i].count = this.goodsAllot.details[i].per * this.multiple;
      }

    },
    minusMultiple: function () {
      if (this.multiple > 1) {
        this.multiple--;
      } else {
        layer.msg("倍数值不能小于1")
        return;
      }
      this.multipleCopy = this.multiple;
      this.fillTableData();
    },
    plusMultiple: function () {
      this.multiple++;
      this.multipleCopy = this.multiple;
      this.fillTableData();
    },
    /*货号配码-改变件数*/
    changeMulti: function (event) {
      // console.log("件数值发生改变：", event)
      if (!isInteger(this.multiple, "件数")) {
        this.multiple = this.multipleCopy;
        return;
      }
      this.multipleCopy = this.multiple;
      this.fillTableData();
    },
    /*货号配码-改变尺码数量*/
    changeSizeCount: function (event) {
      console.log(event.target.value)
      if (!isInteger(event.target.value, "尺码数量值")) return;
      //忽略件数，计算剩余
      var total = 0;
      for (var i in this.goodsAllot.details) {
        var sizeCount = this.goodsAllot.details[i].count || 0;
        if (isInteger(sizeCount, "尺码" + this.goodsAllot.details[i].size + "数量值")) {
          total += this.goodsAllot.details[i].count;
        } else {
          return;
        }
      }
      this.multipleCount = total;

      //剩余提示信息
      this.tipHelper(this.goodsAllot.count, total);
    },
    /*门店配码-聚焦尺码数量输入框*/
    focusSizeCount: function (event, size) {
      var currSizeVal = parseInt(event.target.value);
      this.focusSize = {size: size, count: currSizeVal || 0};
      // console.log('暂存编辑前尺码-数量：',this.focusSize.size, this.focusSize.count)
    },
    /*门店配码-改变尺码数量*/
    changeShopSizeCount: function (event, size, index) {
      var currSizeVal = event.target.value;
      //相同跳过
      if (vm.focusSize.size == currSizeVal) return;
      //值改变且为空
      if (!currSizeVal || currSizeVal.trim() == '') {
        currSizeVal = 0;
      }
      if (!isInteger(currSizeVal, "尺码数量值")) {
        event.target.value = vm.focusSize.count;
        vm.$set(vm.shopAllot.details[index], 'count', vm.focusSize.count);
        return;
      }

      currSizeVal = parseInt(currSizeVal);

      //改变剩余量
      for (var i in this.shopAllot.refer) {
        var refer = this.shopAllot.refer[i];
        if (refer.size == size) {
          if ((refer.yu + vm.focusSize.count - currSizeVal) < 0) {
            vm.$set(vm.shopAllot.details[index], 'count', vm.focusSize.count);
            event.target.value = vm.focusSize.count;
            layer.alert("不能超出剩余数量！请重新输入");
            return;
          } else {
            // console.log(refer.yu, vm.focusSize.count, currSizeVal)
            this.$set(this.shopAllot.refer[i], 'yu', refer.yu + vm.focusSize.count - currSizeVal);
          }

          break;
        }
      }

      //计算所有尺码总数
      var total = 0;
      for (var i in this.shopAllot.details) {
        var detail = this.shopAllot.details[i];
        if (detail.count && detail.count != "" && isInteger(detail.count)) {
          total += detail.count;
        }
      }

      //提示信息
      this.tipHelper(this.shopAllot.count, total)
    },
    /*剩余提示信息*/
    tipHelper: function (planCount, total) {
      var yu = planCount - total;
      if (yu > 0) {
        this.tip = "总量：" + total + " 双， 比计划数量少 " + yu + " 双";
      } else if (yu < 0) {
        this.tip = "总量：" + total + " 双， 比计划数量多 " + -yu + " 双";
      } else {
        this.tip = '';
      }

    },
    exportExcel: function () {
      var excelForm = document.getElementById('exportOrderExcelForm');
      excelForm.token.value = token;
      excelForm.year.value = this.year;
      excelForm.seasonSeq.value = this.seasonSeq;
      if (this.tableType) {
        console.log("下载门店配码单")
        excelForm.shopSeq.value = this.shopSeq;
        excelForm.action = baseURL + 'sys/size-allot/shop/excel';
      } else {
        console.log("下载货品配码单")
        excelForm.action = baseURL + 'sys/size-allot/goods/excel';
      }
      excelForm.submit();
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
    /*自定义尺码范围*/
    setSizeBound: function () {
      //尺码范围检查
      if (!this.checkSizeBound()) return;

      var details = [];
      for (var i = this.minSize; i <= this.maxSize; i++) {
        details.push({size: i, count: null})
      }
      this.$set(this.goodsAllot, 'details', details);
    },
    /*检查年份和季节选择，不合法为false*/
    checkSelectYearAndSeason: function () {
      if (!this.year || this.year == -1) {
        layer.msg("请先选择年份", {icon: 0});
        return false;
      }

      if (!this.seasonSeq || this.seasonSeq == -1) {
        layer.msg("请先选择季节", {icon: 0});
        return false;
      }

      return true;
    }
  },
  created: function () {
    this.loadYears();
    this.loadShopSizeList();
  }
})


/*门店配码操作*/
function shopAllotSize(goodsAllotSeq, rowId) {
  var rowObj = $("#jqGrid").getRowData(rowId);
  // console.log("门店配码-货品配码单号：", goodsAllotSeq);

  vm.shopAllot.shopName = rowObj.shopName;
  vm.shopAllot.count = rowObj.distributeNum; //门店计划订货量
  vm.shopAllot.goodID = rowObj.goodID;
  //初步订单seq
  vm.shopAllot.seq = rowObj.seq;

//根据货品配码单号获取货品配码单信息

  $.get(baseURL + 'sys/size-allot/shop/getGoodsAllot/' + goodsAllotSeq, function (data) {
    if (data.code == 0) {
      if (data.result) {
        var goodsAllot = data.result;
        var refer = goodsAllot.details;//(尺码、数量、剩余)
        var details = [];
        for (var i in refer) {
          details.push({size: refer[i].size, count: null})
        }
        vm.$set(vm.shopAllot, 'refer', refer);
        vm.$set(vm.shopAllot, 'details', details);

        openShopAllotWindow(goodsAllotSeq);
      } else {
        layer.msg("功能异常：未查询到货品配码数据")
      }
    } else {
      layer.msg(data.msg)
    }
  })

}

/*门店重新配码*/
function resetShopAllotSize(goodsAllotSeq, shopAllotSeq, rowId) {
  var rowObj = $("#jqGrid").getRowData(rowId);
  // console.log("门店配码-货品配码单号-门店配码单号：", goodsAllotSeq, shopAllotSeq);

  vm.shopAllot.shopName = rowObj.shopName;
  vm.shopAllot.count = rowObj.distributeNum; //门店计划订货量
  vm.shopAllot.goodID = rowObj.goodID;
  //初步订单seq
  vm.shopAllot.seq = rowObj.seq;


//根据货品配码单号获取货品配码单信息
  $.get(baseURL + 'sys/size-allot/shop/getGoodsAllotAndShopAllot/' + goodsAllotSeq + '/' + shopAllotSeq, function (data) {
    if (data.code == 0) {
      if (data.goodAllot) {
        var goodsAllot = data.goodAllot;
        var refer = goodsAllot.details;//(尺码、数量、剩余)

        vm.$set(vm.shopAllot, 'refer', refer);

        if (data.shopAllot) {
          vm.$set(vm.shopAllot, 'details', data.shopAllot.details);
          openShopAllotWindow(goodsAllotSeq);
        } else {
          layer.msg("功能异常：未查询到门店配码数据")
        }

      } else {
        layer.msg("功能异常：未查询到货品配码数据")
      }
    } else {
      layer.msg(data.msg)
    }
  })


}

/**
 * 打开门店配码弹窗
 */
function openShopAllotWindow(goodsAllotSeq) {
  vm.tip = "";

  layer.open({
    type: 1,
    title: '门店配码',
    resize: false,
    area: ['700px', '450px'], //宽高
    content: $('#shop_size_allot'),
    btn: ['确定', '取消'],
    yes: function (index, layero) {
      //待上传的尺码数据检查
      var total = 0;
      for (var i in vm.shopAllot.details) {
        if (!isInteger(vm.shopAllot.details[i].count, "尺码" + vm.shopAllot.details[i].size + "数量值")) {
          return;
        } else {
          total += vm.shopAllot.details[i].count;
        }
      }


      var shopAllotOrder = {
        silevePlateDistributeSeq: vm.shopAllot.seq, //门店初步订单
        count: total,      //最终数量
        details: vm.shopAllot.details  //订单尺码数量详细
      }

      //创建门店配码订单
      $.ajax({
        type: "POST",
        url: baseURL + 'sys/size-allot/shop/save/' + goodsAllotSeq,
        dataType: "json",
        cache: false,
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(shopAllotOrder),
        success: function (data) {
          if (data.code == 0) {
            layer.msg("保存成功")
            vm.reloadGrid();
            layer.close(index);
          } else {
            layer.alert("保存失败")
          }
        }
      })
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

/*货号配码操作*/
function editGoodsAllotSize(rowId) {

  if (!vm.checkSelectYearAndSeason()) return;

  var rowObj = $("#jqGrid2").getRowData(rowId);

  //初始化参数
  vm.goodsAllot.count = rowObj.total;
  vm.goodsAllot.goodID = rowObj.goodID;
  vm.templateSeq = null;
  vm.selectTemplateIndex = null;
  vm.goodsAllot.details = [];
  vm.multipleCount = 0;
  vm.tip = '';
  vm.multiple = 0;


  if (rowObj.categorySeq) {
    //根据货品品类加载配码模版
    vm.loadTemplate(rowObj.categorySeq);
  } else {
    layer.msg("功能异常：未获取到品类")
  }

  layer.open({
    type: 1,
    title: '货品配码',
    resize: false,
    area: ['850px', '600px'], //宽高
    content: $('#goods_size_allot'),
    btn: ['确定', '取消'],
    yes: function (index, layero) {
      //待上传的尺码数据检查
      for (var i in vm.goodsAllot.details) {
        if (!isInteger(vm.goodsAllot.details[i].count, "尺码" + vm.goodsAllot.details[i].size + "数量值")) {
          return;
        }
      }

      var goodsAllotOrder = {
        goodID: vm.goodsAllot.goodID,        //货号
        total: vm.multipleCount,      //最终数量
        planCount: vm.goodsAllot.count,      //计划数量
        templateSeq: vm.templateSeq, //模版
        year: vm.year, //年份
        seasonSeq: vm.seasonSeq, //季节
        categorySeq: rowObj.categorySeq, //品类
        details: vm.goodsAllot.details  //订单尺码数量详细
      }


      //新增
      $.ajax({
        type: "POST",
        url: baseURL + 'sys/size-allot/goods/save',
        dataType: "json",
        cache: false,
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(goodsAllotOrder),
        success: function (data) {
          if (data.code == 0) {
            layer.msg("保存成功")
            vm.reloadGrid();
            layer.close(index);
          } else {
            layer.alert("保存失败")
          }
        }
      })


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

/*货号配码修改*/
function updateGoodsAllotSize(rowId) {

  if (!vm.checkSelectYearAndSeason()) return;
  console.log("货号重配码")

  var rowObj = $("#jqGrid2").getRowData(rowId);

  var postParam = {
    goodID: rowObj.goodID,
    year: vm.year,
    seasonSeq: vm.seasonSeq,
    goodAllotOrderSeq: rowObj.allotOrderSeq
  }

  //查询指定年份和季节的货号是否已经存在门店配码
  $.post(baseURL + 'sys/size-allot/good/queryGoodAllotShopName', postParam, function (data) {
    if (data.code == 0) {
      if (data.result && data.result.length > 0) {
        var shopNameList = data.result;
        layer.confirm("存在" + shopNameList.length + "家门店已配码，确定要删除吗", {icon: 3}, function (index) {

          //删除门店配码操作
          deleteShopAllot(postParam, rowId);

          layer.close(index);
        })
      } else {
        //不存在门店配码，重新货号配码
        queryGoodAllotOrder(postParam, rowId)
      }

    } else {
      layer.alert(data.msg, {icon: 0})
    }
  })

}

/**
 * 删除门店配码订单-货号重新配码确认删除操作
 */
function deleteShopAllot(postParam, rowId) {
  $.post(baseURL + 'sys/size-allot/good/deleteShopAllot', postParam, function (data) {
    if (data.code == 0) {
      queryGoodAllotOrder(postParam, rowId)
    } else {
      layer.alert(data.msg)
    }
  })
}

/**
 * 查询货号配码订单 - 货号重新配码
 * @param postParam
 * @param rowId
 */
function queryGoodAllotOrder(postParam, rowId) {
  $.post(baseURL + 'sys/size-allot/goods/query', postParam, function (data) {
    if (data.code == 0) {
      if (data.result) {
        afterUpdateGoodsAllotSize(data.result)
      } else {
        layer.confirm('功能异常：未查询到数据! 是否重新配码?', function (index) {
          editGoodsAllotSize(rowId);
          layer.close(index);
        });

      }
    } else {
      layer.msg(data.msg)
    }
  })
}

/**
 * 获取到配码订单之后的编辑操作
 * @param allotOrder
 */
function afterUpdateGoodsAllotSize(allotOrder) {
  //初始化参数
  vm.goodsAllot.count = allotOrder.planCount;
  vm.goodsAllot.goodID = allotOrder.goodID;
  vm.multipleCount = allotOrder.total;

  vm.templateSeq = allotOrder.templateSeq;
  vm.$set(vm.goodsAllot, 'details', allotOrder.details);

  //计划数量和实际数量之间的比较提示
  vm.tipHelper(allotOrder.planCount, allotOrder.total);

  //加载模版
  vm.loadTemplate(allotOrder.categorySeq);

  //填充数据
  vm.goodsAllot.details = allotOrder.details;

  layer.open({
    type: 1,
    title: '修改货品配码',
    resize: false,
    area: ['850px', '600px'], //宽高
    content: $('#goods_size_allot'),
    btn: ['确定', '取消'],
    yes: function (index, layero) {
      //待上传的尺码数据检查
      for (var i in vm.goodsAllot.details) {
        if (!isInteger(vm.goodsAllot.details[i].count, "尺码" + vm.goodsAllot.details[i].size + "数量值")) {
          return;
        }
      }
      var goodsAllotOrder = {
        seq: allotOrder.seq,
        goodID: vm.goodsAllot.goodID,        //货号
        total: vm.multipleCount,      //最终数量
        planCount: vm.goodsAllot.count,      //计划数量
        templateSeq: vm.templateSeq, //模版
        details: vm.goodsAllot.details  //订单尺码数量详细
      }

      //更新
      $.ajax({
        type: "POST",
        url: baseURL + 'sys/size-allot/goods/update',
        dataType: "json",
        cache: false,
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(goodsAllotOrder),
        success: function (data) {
          if (data.code == 0) {
            layer.msg(data.msg)
            layer.close(index);
          } else {
            layer.alert("保存失败")
          }
        }
      })


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


/*判断是否是大于等于0的正整数*/
function isInteger(val, valName) {
  valName || (valName = '输入值');
  if (isNaN(val)) {
    layer.alert(valName + "必须为数字")
    return false;
  } else if (val < 0) {
    layer.alert(valName + "不能<0")
    return false;
  } else if (parseInt(val) != val) {
    layer.alert(valName + "必须为整数")
    return false;
  }
  return true;
}


/**
 * 删除货号配码
 * @param goodsAllotOrderSeq
 */
function delGoodsAllot(goodsAllotOrderSeq, goodsID) {
  if (!vm.checkSelectYearAndSeason()) return;
  console.log("删除货号配码")

  var postParam = {
    goodID: goodsID,
    year: vm.year,
    seasonSeq: vm.seasonSeq,
  }

  //查询指定年份和季节的货号是否已经存在门店配码
  $.post(baseURL + 'sys/size-allot/good/queryGoodAllotShopName', postParam, function (data) {
    if (data.code == 0) {
      if (data.result && data.result.length > 0) {
        var shopNameList = data.result;
        layer.confirm("存在" + shopNameList.length + "家门店已配码，确定要删除吗", {icon: 3}, function (index) {

          //删除门店配码操作
          $.post(baseURL + 'sys/size-allot/good/deleteShopAllot', postParam, function (data) {
            if (data.code == 0) {
              deleteGoodAllot(goodsAllotOrderSeq);
            } else {
              layer.alert(data.msg)
            }
          })

          layer.close(index);
        })
      } else {
        //不存在门店配码，重新货号配码

        layer.confirm("确定要删除 " + goodsID + " 的配码单吗？", function (index) {

          deleteGoodAllot(goodsAllotOrderSeq);
          layer.close(index);

        })
      }

    } else {
      layer.alert(data.msg, {icon: 0})
    }
  })


}

/**
 * 删除货号配码订单
 * @param goodsAllotOrderSeq
 */
function deleteGoodAllot(goodsAllotOrderSeq) {
  $.get(baseURL + 'sys/size-allot/goods/del/' + goodsAllotOrderSeq, function (data) {
    if (data.code == 0) {
      layer.msg(data.msg);
      vm.reloadGrid();
    } else {
      layer.alert("删除失败")
    }
  })
}

/**
 * 删除门店配码
 * @param shopAllotSeq
 */
function delShopAllot(goodsAllotSeq, shopAllotOrderSeq, goodsID) {
  layer.confirm("确定要删除 " + goodsID + " 的配码单吗？", function () {
    $.get(baseURL + 'sys/size-allot/shop/del/' + shopAllotOrderSeq + '/' + goodsAllotSeq, function (data) {
      if (data.code == 0) {
        layer.msg(data.msg);
        vm.reloadGrid();
      } else {
        layer.alert("删除失败")
      }
    })
  })

}