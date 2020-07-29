$(function () {
  //如果有orderSeq参数，展示订单详情（用于点击通知时跳转）
  var orderSeq = getIframeUrlParam("orderSeq");
  console.log(orderSeq);
  if (!isBlank(orderSeq)) {
    getOrderDetail(orderSeq);
  }
  $("#jqGrid").jqGrid({
    url: baseURL + 'order/orderlist',
    datatype: "json",
    postData: {
      'orderStatus': vm.orderStatus,
      'keywords': vm.keywords,
      'attachType': vm.attachType,
      'attachSeq': vm.attachSeq,
      'startTime': vm.startTime,
      'endTime': vm.endTime,
      'userSeq': vm.userSeq
    },
    colModel: [
      {label: 'seq', name: 'seq', width: 0, hidden: true, align: "center"},
      {label: 'orderStatus', name: 'orderStatus', width: 0, hidden: true, align: "center"},
      {label: '订单号', name: 'orderNum', width: 85, align: "center"},
      {
        label: '总数量', name: 'species', width: 50, align: "center",
        cellattr: function (rowId, value, rowObject, colModel, arrData) {
          return 'id=\'species' + rowId + "\'";
        }
      },
      {
        label: '已付金额', name: 'paid', width: 60, align: "center",
        cellattr: function (rowId, value, rowObject, colModel, arrData) {
          return 'id=\'paid' + rowId + "\'";
        }
      },
      {
        label: '应付金额', name: 'orderPrice', width: 60, align: "center",
        cellattr: function (rowId, value, rowObject, colModel, arrData) {
          return 'id=\'orderPrice' + rowId + "\'";
        }
      },
      {
        label: '订单状态', name: 'statusName', width: 65, align: "center",
        cellattr: function (rowId, value, rowObject, colModel, arrData) {
          if (parseInt(rowObject.orderStatus) == 4) {
            return "id=\'statusName" + rowId + "\' style='color:red'";
          }
          return 'id=\'statusName' + rowId + "\'";
        }
      },
      {label: '订货方名称', name: 'orderingparty', width: 90, align: "center"},
      {
        label: '提交时间', name: 'inputTime', width: 70, align: "center",
        cellattr: function (rowId, value, rowObject, colModel, arrData) {
          return 'id=\'inputTime' + rowId + "\'";
        }
      },
      {
        label: '要求到货时间', name: 'requireTime', width: 0, hidden: true, align: "center",
        cellattr: function (rowId, value, rowObject, colModel, arrData) {
          return 'id=\'requireTime' + rowId + "\'";
        }
      },
      {label: '备注', name: 'remark', width: 60, align: "center",},
      {
        label: '留言', name: 'suggestion', width: 60, align: "center",
        cellattr: function (rowId, value, rowObject, colModel, arrData) {
          return 'id=\'suggestion' + rowId + "\'";
        }
      },
      {
        label: '导入ERP状态', name: 'importErpState', width: 50, align: "center",
        formatter: function (cellvalue, options, rowObject) {
          switch (rowObject.importErpState) {
            case 0:
              return "未导入";
            case 1:
              return "已导入";
            case -1:
              return "无需导入";
            default:
              return "";
          }
        },
        cellattr: function (rowId, value, rowObject, colModel, arrData) {
          if (parseInt(rowObject.importErpState) == 0) {
            return "id=\'importErpState" + rowId + "\' style='color:red'";
          } else if (parseInt(rowObject.importErpState) == -1) {
            return "id=\'importErpState" + rowId + "\' style='color:grey'";
          } else {
            return 'id=\'importErpState' + rowId + "\'";
          }
        }
      },
      {
        label: '操作', name: 'action', width: 150, align: "center",
        formatter: function (cellvalue, options, rowObject) {
          var buttonHtml = '';
          buttonHtml += ('<button class="operation-btn-security" onclick="getOrderDetail(' + rowObject.seq + ')">详情</button>' + '&nbsp;');

          switch (rowObject.orderStatus) {
            case 0:
              if (hasPermission("order:splitOrder") && hasPermission("order:importIntoERP") && rowObject.importErpState === 0) {
                buttonHtml += ('<button class="operation-btn-security" onclick="importIntoErp(' + rowObject.seq + ')">导入ERP</button>' + '&nbsp;');
              }
              if (hasPermission("order:receiveOrder")) {
                buttonHtml += ('<button class="operation-btn-security" onclick="makeSure(' + rowObject.seq + ')">接单</button>' + '&nbsp;');
                if (hasPermission("order:cancelOrder")) {
                  buttonHtml += ('<button class="operation-btn-warn" onclick="cancelOrder(' + rowObject.seq + ')">取消订单</button>' + '&nbsp;');
                }
              }
              return buttonHtml;
            case 1:
//	          if (hasPermission("order:splitOrder")) {
//	        	  buttonHtml += ('<button class="operation-btn-security" onclick="splitOrder(' + rowObject.seq + ')">拆单</button>' + '&nbsp;');
//	          }
              if (hasPermission("order:checkOrder")) {
                buttonHtml += ('<button class="operation-btn-security" onclick="payButton(' + rowObject.seq + ')">审核</button>' + '&nbsp;');
                buttonHtml += ('<button class="operation-btn-security" onclick="changePrice(' + rowObject.seq + ')">修改价格</button>' + '&nbsp;');
                if (hasPermission("order:cancelOrder")) {
                  buttonHtml += ('<button class="operation-btn-warn" onclick="cancelOrder(' + rowObject.seq + ')">取消订单</button>' + '&nbsp;');
                }
              }
              return buttonHtml;
            case 2:
              if (hasPermission("order:checkOrder")) {
                if (rowObject.orderPrice > rowObject.paid) {
                  buttonHtml += ('<button class="operation-btn-security" onclick="payButton(' + rowObject.seq + ')">收款</button>' + '&nbsp;');
                }
                if (hasPermission("order:importIntoERP") && rowObject.importErpState === 0) {
                  buttonHtml += ('<button class="operation-btn-security" onclick="importIntoErp(' + rowObject.seq + ')">导入ERP</button>' + '&nbsp;');
                }
              }
              if (hasPermission("order:storeOrder")) {
                buttonHtml += ('<button class="operation-btn-security" onclick="depotButton(' + rowObject.seq + ')">入库</button>' + '&nbsp;');
                if (hasPermission("order:cancelOrder")) {
                  buttonHtml += ('<button class="operation-btn-warn" onclick="cancelOrder(' + rowObject.seq + ')">取消订单</button>' + '&nbsp;');
                }
              }
              return buttonHtml;
            case 3:
              if (hasPermission("order:checkOrder")) {
                if (rowObject.orderPrice > rowObject.paid) {
                  buttonHtml += ('<button class="operation-btn-security" onclick="payButton(' + rowObject.seq + ')">收款</button>' + '&nbsp;');
                }
              }
              if (hasPermission("order:deliverOrder")) {
                buttonHtml += ('<button class="operation-btn-security" onclick="shipButton(' + rowObject.seq + ')">发货</button>' + '&nbsp;');
                if (hasPermission("order:cancelOrder")) {
                  buttonHtml += ('<button class="operation-btn-warn" onclick="cancelOrder(' + rowObject.seq + ')">取消订单</button>' + '&nbsp;');
                }
              }
              return buttonHtml;
            case 4:
              if (hasPermission("order:checkOrder")) {
                if (rowObject.orderPrice > rowObject.paid) {
                  buttonHtml += ('<button class="operation-btn-security" onclick="payButton(' + rowObject.seq + ')">收款</button>' + '&nbsp;');
                }
              }
              if (hasPermission("order:deliverOrder")) {
                buttonHtml += ('<button class="operation-btn-security" onclick="shipButton(' + rowObject.seq + ')">发货</button>' + '&nbsp;');
              }
              return buttonHtml;
            case 5:
              if (hasPermission("order:checkOrder")) {
                if (rowObject.orderPrice > rowObject.paid) {
                  buttonHtml += ('<button class="operation-btn-security" onclick="payButton(' + rowObject.seq + ')">收款</button>' + '&nbsp;');
                }
              }
              if (hasPermission("order:deliverOrder")) {
                buttonHtml += ('<button class="operation-btn-security" onclick="receiveButton(' + rowObject.seq + ')">延长收货</button>' + '&nbsp;');
              }
              return buttonHtml;
            case 6:
              if (hasPermission("order:checkOrder")) {
                if (rowObject.orderPrice > rowObject.paid) {
                  buttonHtml += ('<button class="operation-btn-security" onclick="payButton(' + rowObject.seq + ')">收款</button>' + '&nbsp;');
                }
              }
              return buttonHtml;
            case 7:
              return buttonHtml;
          }
        },
        cellattr: function (rowId, value, rowObject, colModel, arrData) {
          return 'id=\'action' + rowId + "\'";
        }
      }],
    autowidth: true,
    height: 'auto',
    rowNum: 10,
    rowList: [10, 30, 50],
    rownumbers: true,
    rownumWidth: 25,
    multiselect: false,
    pager: "#jqGridPager",
    jsonReader: {
      root: "page.list",
      page: "page.currPage",// 数据页码(当前页码currPage)
      total: "page.totalPage",// 数据总页码(总页数totalPage)
      records: "page.totalCount"// 数据总记录数(totalCount
      // 总记录数)
    },
    prmNames: {
      page: "page",
      rows: "limit",
      order: "order"
    },
    gridComplete: function () {
      // 隐藏grid底部滚动条
      $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
      if (!hasPermission("order:importIntoERP")) {
        $("#jqGrid").setGridParam().hideCol("importErpState");
        $("#jqGrid").setGridWidth($(window).width());
      }
    },
    subGrid: true,// 开启子表格支持
    // 子表格的id；当子表格展开的时候，在主表格中会创建一个div元素用来容纳子表格，subgrid_id就是这个div的id
    subGridRowExpanded: function (subgrid_id, row_id) {// 子表格容器的id和需要展开子表格的行id
      var before = $("#jqGrid").jqGrid('getRowData', row_id);
      bindSubGrid(subgrid_id, before.seq, before.orderStatus);
    }
  });

  $('#config-demo').daterangepicker({
    showDropdowns: true,
    autoUpdateInput: false,
    timePicker24Hour: true,
    timePicker: true,
    locale: {
      format: 'YYYY/MM/DD HH:mm:ss',
      applyLabel: "应用",
      cancelLabel: "取消",
      customRangeLabel: '选择时间',
      daysOfWeek: ["日", "一", "二", "三", "四", "五", "六"],
      monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
      firstDay: 0
    }
  }).on('apply.daterangepicker', function (ev, picker) {
    vm.startTime = picker.startDate.format(picker.locale.format);
    vm.endTime = picker.endDate.format(picker.locale.format);
    vm.start_endTime = vm.startTime + " ~ " + vm.endTime;
  });

  $('#receiveData').daterangepicker({
    singleDatePicker: true,
    showDropdowns: true,
    autoUpdateInput: false,
    timePicker24Hour: true,
    timePicker: true,
    locale: {
      format: 'YYYY/MM/DD HH:mm:ss',
      applyLabel: "应用",
      cancelLabel: "取消",
      customRangeLabel: '选择时间',
      daysOfWeek: ["日", "一", "二", "三", "四", "五", "六"],
      monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
      firstDay: 0
    }
  }).on('apply.daterangepicker', function (ev, picker) {
    vm.$set(vm.orderDetail, "receiveTime", picker.startDate.format(picker.locale.format));
  });

});

//一级子表格，所有货号列表
function bindSubGrid(subgrid_id, seq, status) {
  var subgrid_table_id = subgrid_id + "_t"; // (3)根据subgrid_id定义对应的子表格的table的id
  var subgrid_pager_id = subgrid_id + "_pgr"; // (4)根据subgrid_id定义对应的子表格的pager的id
  // (5)动态添加子报表的table和pager
  $("#" + subgrid_id).html("<table id=" + subgrid_table_id + " class='scroll'></table>");

  var colModel = [
    {label: '货号', name: 'goodId', width: 220, align: "center"},
    {label: '价格', name: 'productPrice', width: 120, align: "center"},
    {label: '总数量', name: 'totalBuyCount', width: 120, align: "center"}];
  if (parseInt(status) == 4) {
    colModel.push({label: '已发量', name: 'totalDeliverNum', width: 120, align: "center"},
      {
        label: '未发量', name: 'totalUndeliverNum', width: 120, align: "center",
        formatter: function (cellvalue, options, rowObject) {
          return (parseInt(rowObject.totalBuyCount) - parseInt(rowObject.totalDeliverNum));
        }, cellattr: function (rowId, value, rowObject, colModel, arrData) {
          if (parseInt(rowObject.totalBuyCount) - parseInt(rowObject.totalDeliverNum) > 0) {
            return "style='color:red'";
          }
        }
      });
  }
  // (6)创建jqGrid对象
  $("#" + subgrid_table_id).jqGrid({
    url: baseURL + 'order/orderGoodsIds',
    datatype: "json",
    shrinkToFit: false,
    autoScroll: true, // shrinkToFit: false,autoScroll:
    // true,这两个属性产生水平滚动条
    autowidth: true,
    postData: {
      'orderSeq': seq
    },
    colModel: colModel,
    viewrecords: false,
    height: 'auto',
    rownumbers: true,
    rownumWidth: 25,
    multiselect: false,
    jsonReader: {
      root: "page.list",
      page: "page.currPage",// 数据页码(当前页码currPage)
      total: "page.totalPage",// 数据总页码(总页数totalPage)
      records: "page.totalCount"// 数据总记录数(totalCount
      // 总记录数)
    },
    prmNames: {
      page: "page",
      rows: "limit",
      order: "order"
    },
    subGrid: true,// 开启子表格支持
    // 子表格的id；当子表格展开的时候，在主表格中会创建一个div元素用来容纳子表格，subgrid_id就是这个div的id
    subGridRowExpanded: function (subgrid_id, row_id) {// 子表格容器的id和需要展开子表格的行id
      var before = $("#" + subgrid_table_id).jqGrid('getRowData', row_id);
      console.log(before.goodId);
      bindSubGrid2(subgrid_id, seq, before.goodId, status);
    }
  });
}

//二级子表格，货号的颜色尺码列表
function bindSubGrid2(subgrid_id, seq, goodId, status) {
  var subgrid_table_id = subgrid_id + "_t"; // (3)根据subgrid_id定义对应的子表格的table的id
  var subgrid_pager_id = subgrid_id + "_pgr"; // (4)根据subgrid_id定义对应的子表格的pager的id
  // (5)动态添加子报表的table和pager
  $("#" + subgrid_id).html("<table id=" + subgrid_table_id + " class='scroll'></table>");

  var colModel = [{
    label: '图片', name: 'img1', width: 200, align: "center",
    formatter: function (cellvalue, options, rowObject) {
      var detail = '<image src="' + cellvalue + '" style="width: 80px;height: 80px;"/>';
      return detail;
    }
  },
    {label: '颜色', name: 'color', width: 120, align: "center"},
    {label: '尺码', name: 'shoesSize', width: 100, align: "center"},
    {label: '数量', name: 'buyCount', width: 90, align: "center"}];
  if (parseInt(status) == 4) {
    colModel.push({label: '已发', name: 'deliverNum', width: 90, align: "center"},
      {
        label: '未发', name: 'undeliverNum', width: 120, align: "center",
        formatter: function (cellvalue, options, rowObject) {
          return (parseInt(rowObject.buyCount) - parseInt(rowObject.deliverNum));
        }, cellattr: function (rowId, value, rowObject, colModel, arrData) {
          if (parseInt(rowObject.buyCount) - parseInt(rowObject.deliverNum) > 0) {
            return "style='color:red'";
          }
        }
      });
  }
  // (6)创建jqGrid对象
  $("#" + subgrid_table_id).jqGrid({
    url: baseURL + 'order/goodsIdProducts',
    datatype: "json",
    shrinkToFit: false,
    autoScroll: true, // shrinkToFit: false,autoScroll:
    // true,这两个属性产生水平滚动条
    autowidth: false,
    postData: {
      'orderSeq': seq,
      'goodId': goodId
    },
    colModel: colModel,
    viewrecords: false,
    height: 'auto',
    rownumbers: true,
    rownumWidth: 25,
    multiselect: false,
    jsonReader: {
      root: "page.list",
      page: "page.currPage",// 数据页码(当前页码currPage)
      total: "page.totalPage",// 数据总页码(总页数totalPage)
      records: "page.totalCount"// 数据总记录数(totalCount
      // 总记录数)
    },
    prmNames: {
      page: "page",
      rows: "limit",
      order: "order"
    }
  });
}


var vm = new Vue({
  el: '#rrapp',
  data: {
    orderStatus: '',
    keywords: '',
    attachType: '',
    attachSeq: '',
    branchOfficeName: '',
    agentName: '',
    startTime: '',
    endTime: '',
    start_endTime: '',
    userSeq: '',
    userSeqNameList: [],

    showList: true,
    showChangePrice: false,
    showPay: false,
    showShip: false,
    showReceive: false,
    showOrderDetail: false,
    expressCompanyList: [],
    title: '',
    orderTotalPrice: '',
    orderPayPrice: 0,
    orderDetail: {},
    shipGoodsDetail: {expressCompanySeq: -1},
    productsPriceList: [],
    splitBySXId: '',
    sXIdNameList: [],
    splitOrderModelSeq: '',
    defaultSplitModelSeq: '',
    splitOrderModelList: []
  },
  methods: {
    statusName: function (type) {
      vm.keywords = '';
      vm.orderStatus = type;
      vm.reloadOrder();
    },

    reloadOrder: function () {
      if (parent.location.hash.indexOf("orderSeq=") != -1) {
        parent.location.hash = "#modules/system/order_platform/order.html";
        return;
      }
      // 初始化参数
      vm.showList = true;
      vm.showChangePrice = false;
      vm.showPay = false;
      vm.showShip = false;
      vm.showReceive = false;
      vm.showOrderDetail = false,

        vm.orderTotalPrice = '';
      vm.orderPayPrice = 0;
      vm.orderDetail = {};
      vm.shipGoodsDetail = {expressCompanySeq: -1};
      vm.productsPriceList = [];

      setTimeout(function () {
        $("#jqGrid").jqGrid('setGridParam', {
          postData: {
            'keywords': vm.keywords,
            'orderStatus': vm.orderStatus,
            'attachType': vm.attachType,
            'attachSeq': vm.attachSeq,
            'startTime': vm.startTime,
            'endTime': vm.endTime,
            'userSeq': vm.userSeq
          },
          page: 1,
        }).trigger('reloadGrid');
      }, 500);
    },

    //修改价格
    saveOrderPrice: function () {
      var seqPriceList = new Array();
      var flag = false;
      for (var i = 0; i < vm.productsPriceList.length; i++) {
        var product = vm.productsPriceList[i];
        var productSeq = product.seq;
        var changePrice = parseFloat(product.changePrice);
        if (isNaN(changePrice)) {
          changePrice = null;
        } else if (changePrice <= 0) {
          alert('请输入正确的金额');
          return;
        } else {
          flag = true;
        }
        var params = {
          productSeq: productSeq,
          changePrice: changePrice
        };
        seqPriceList[i] = JSON.stringify(params);
      }

      if (!flag && !vm.orderTotalPrice) {
        alert('请输入要修改的总价或某些商品单价');
        return;
      }
      if (flag && vm.orderTotalPrice) {
        alert('不能同时修改总价与商品单价');
        return;
      }

      var orderTotalPrice = parseFloat(vm.orderTotalPrice);
      if (isNaN(orderTotalPrice)) {
        orderTotalPrice = null;
      } else if (orderTotalPrice <= 0) {
        alert('请输入正确的金额');
        return;
      }

      confirm("确认修改金额？", function () {
        $.ajax({
          type: "POST",
          url: baseURL + 'order/changePrice',
          data: {
            orderSeq: vm.orderDetail.seq,
            orderTotalPrice: orderTotalPrice,
            seqPriceList: JSON.stringify(seqPriceList)
          },
          success: function (r) {
            if (r.code === 0) {
              alert('操作成功', function () {
                vm.reloadOrder();
              });
            } else {
              alert(r.msg);
            }
          },
          error: function () {
            alert('服务器开小差啦');
          }
        });
      });
    },

    saveOrderPay: function () {
      var price = parseFloat(vm.orderPayPrice);

      if (price >= 0) {
        confirm("确认支付金额为:" + price + "？", function () {
          $.post(baseURL + "order/payPrice", {
            seq: vm.orderDetail.seq,
            price: price
          }, function (r) {
            if (r.code === 0) {
              alert('操作成功', function () {
            	 vm.statusName(2);
              });
            } else {
              alert(r.msg);
            }
          });
        });
      } else {
        alert("请输入正确的金额");
      }
    },
    saveOrderShip: function () {
      var formData = new FormData();
      var companyInputed = !vm.shipGoodsDetail.expressCompanySeq || parseInt(vm.shipGoodsDetail.expressCompanySeq) === -1;
      var noInputed = isBlank(vm.shipGoodsDetail.expressNo);
      var fileInputed = vm.shipGoodsDetail.expressFile == null;
      if (companyInputed && fileInputed) {
        alert("请填写物流信息");
        return;
      }
      if (!companyInputed && noInputed) {
        alert("请填写物流单号");
        return;
      }

      for (var lIndex = 0; lIndex < vm.shipGoodsDetail.list.length; lIndex++) {
        var indexProduct = vm.shipGoodsDetail.list[lIndex];
        var deliverNum = parseInt(indexProduct.deliverNum) || 0;
        var shipNum = parseInt(indexProduct.shipNum) || 0;
        if (indexProduct.buyCount < (shipNum + deliverNum)) {
          alert("发货量不能大于购买量");
          return;
        }
      }
      formData.append("orderSeq", vm.orderDetail.seq);
      formData.append("expressCompanySeq", vm.shipGoodsDetail.expressCompanySeq === -1 ? '' : vm.shipGoodsDetail.expressCompanySeq);
      formData.append("expressNo", vm.shipGoodsDetail.expressNo || '');
      if (!fileInputed) {
        formData.append("expressFile", vm.shipGoodsDetail.expressFile);
      }

      for (lIndex = 0; lIndex < vm.shipGoodsDetail.list.length; lIndex++) {
        var subProduct = vm.shipGoodsDetail.list[lIndex];
        formData.append("list[" + lIndex + "]", JSON.stringify(subProduct));
      }

      $.ajax({
        type: "POST",
        url: baseURL + 'order/partShipped',
        contentType: false,
        processData: false,
        data: formData,
        enctype: 'multipart/form-data',
        cache: false,
        success: function (r) {
          if (r.code === 0) {
            alert('操作成功', function () {
              vm.reloadOrder();
            });
          } else {
            alert(r.msg);
          }
        },
        error: function () {
          alert('服务器出错啦');
        }
      });
    },
    handleFileChange: function (value) {
      var inputDOM = value.target;
      vm.shipGoodsDetail.expressFile = inputDOM.files[0];
      var size = Math.floor(vm.shipGoodsDetail.expressFile.size / 1024 / 1024);

      if (size > 10) {
        alert("文件超过10M啦！");
        return;
      }
      vm.imgPreview(vm.shipGoodsDetail.expressFile);
      inputDOM.type = 'text';
      inputDOM.type = 'file';
    },
    imgPreview: function (file) {
      if (!file || !window.FileReader) return;
      if (/^image/.test(file.type)) {
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onloadend = function () {
          vm.$set(vm.shipGoodsDetail, "expressImage", this.result);
        }
      }
    },

    saveOrderReceive: function () {
      var receiveTime = vm.orderDetail.receiveTime;
      if (receiveTime && receiveTime > vm.orderDetail.deliverTime) {
        confirm("确认修改自动收货时间为:" + receiveTime + "？", function () {
          $.post(baseURL + "order/changeReceiveTime", {
            seq: vm.orderDetail.seq,
            receiveTime: receiveTime
          }, function (r) {
            if (r.code === 0) {
              alert('操作成功', function () {
                vm.reloadOrder();
              });
            } else {
              alert(r.msg);
            }
          });
        });
      } else {
        alert("请选择正确的时间");
      }
    },

    search: function () {
      $("#jqGrid").jqGrid('setGridParam', {
        postData: {
          'keywords': vm.keywords,
          'orderStatus': vm.orderStatus,
          'attachType': vm.attachType,
          'attachSeq': vm.attachSeq,
          'startTime': vm.startTime,
          'endTime': vm.endTime,
          'userSeq': vm.userSeq
        },
        page: 1,
      }).trigger('reloadGrid');
    },
    //导出
    exportOrder: function () {
      var excelForm = document.getElementById('exportOrderExcelForm');
      excelForm.action = baseURL + "order/exportExcel?keywords=" + vm.keywords
        + "&orderStatus=" + vm.orderStatus
        + "&attachType=" + vm.attachType
        + "&attachSeq=" + vm.attachSeq
        + "&startTime=" + vm.startTime
        + "&endTime=" + vm.endTime
        + "&userSeq=" + vm.userSeq
        + "&page=1&limit=5000";
      console.log(excelForm);
      excelForm.token.value = token;
      excelForm.submit();
    },

    attachTypeChoose: function () {
      vm.attachSeq = '';
      vm.userSeq = '';
      this.userList();
    },
    branchOfficeChoose: function (item) {
      vm.attachSeq = item.seq;
      vm.branchOfficeName = item.name;
      vm.userSeq = '';
      this.userList();
    },
    agentChoose: function (item) {
      vm.attachSeq = item.seq;
      vm.agentName = item.agentName;
      vm.userSeq = '';
      this.userList();
    },

    /* 获得分公司的列表 */
    branchOfficeList: function () {
      $.get(baseURL + "order/orderParty/branchOfficeList", function (r) {
        vm.branchOfficeList = r.branchOfficeList;
      });
    },

    /* 获得代理的列表 */
    agentList: function () {
      $.get(baseURL + "order/orderParty/agentList", function (r) {
        vm.agentList = r.agentList;
      });
    },

    /* 获得用户的列表 */
    userList: function () {
      $.get(baseURL + "order/userList?attachType=" + vm.attachType + "&attachSeq=" + vm.attachSeq, function (r) {
        vm.userSeqNameList = r.result;
      });
    },

    /* 获得拆单方式的列表 */
    sXList: function () {
      $.get(baseURL + "order/sXIdNameList", function (r) {
        vm.sXIdNameList = r.result;
      });
    },

    /* 获得拆单模板的列表 */
    splitModelList: function () {
      $.get(baseURL + "system/splitOrderModel/allList", function (r) {
        vm.splitOrderModelList = r.result;
      });
      /* 默认模板seq */
      $.get(baseURL + "system/splitOrderModel/defaultModel", function (r) {
        vm.defaultSplitModelSeq = r.result.seq;
      });
    },

    /*付款自动适配金额，点击收款框自动填入*/
    changePayPrice: function () {
      vm.orderPayPrice = sub(vm.orderDetail.orderPrice, vm.orderDetail.paid);
      setTimeout(
        function () {
          $(".orderPayPrice").focus().select();
        }, 100);
    },
    /* 同步订单 */
    syncFromErp: function () {
      parent.window.showLoading();
      $.get(baseURL + "system/erp/sync/order", function (r) {
        parent.window.hideLoading();
        alert("同步结束", function () {
          location.reload();
        });
      });
    },
    /* 根据订单样品转商品 */
    goodsToErp: function () {
      parent.window.showLoading();
      $.get(baseURL + "system/erp/sync/goodsImport", function (r) {
        parent.window.hideLoading();
        alert("导入成功", function () {
          location.reload();
        });
      });
    }
  },

  created: function () {
    getExpressCompany();
    this.agentList();
    this.branchOfficeList();
    this.sXList();
    this.splitModelList();
  }
});

function getExpressCompany() {
  $.get(baseURL + "order/expressCompany", function (r) {
    vm.expressCompanyList = r.list;
  });
}

// 取消订单
function cancelOrder(seq) {
  layer.open({
    type: 1,
    offset: '50px',
    skin: 'layui-layer-molv',
    title: "取消订单",
    area: ['450px', '225px'],
    shade: 0,
    shadeClose: false,
    content: jQuery("#cancelOrderLayer"),
    btn: ['确定', '取消'],
    btn1: function (index) {
      var remark = $("#cancelRemark").val();
      if (!isBlank(remark)) {
        confirm("确定要取消订单？", function () {
          $.post(baseURL + "order/cancelOrder", {
            orderSeq: seq,
            remark: remark
          }, function (r) {
            if (r.code == 0) {
              alert(r.msg, function () {
                vm.statusName(7);
              });
            } else {
              alert(r.msg);
            }
          });
          layer.close(index);
        });
      } else {
        alert("备注不能为空");
      }
    }
  });
}

// 接单
function makeSure(seq) {
  layer.open({
    type: 1,
    offset: '50px',
    skin: 'layui-layer-molv',
    title: "接单",
    area: ['450px', '225px'],
    shade: 0,
    shadeClose: false,
    content: jQuery("#confirmOrderLayer"),
    zIndex: 1000, //防止daterangepicker控件被覆盖
    success: function (layero, index) {
      $(layero).find('#orderRequireTime').daterangepicker({
        minDate: moment(),
        singleDatePicker: true,
        showDropdowns: true,
        autoUpdateInput: false,
        locale: {
          format: 'YYYY/MM/DD',
          applyLabel: "应用",
          cancelLabel: "取消",
          customRangeLabel: '选择时间',
          daysOfWeek: ["日", "一", "二", "三", "四", "五", "六"],
          monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
          firstDay: 0
        }
      }).on('apply.daterangepicker', function (ev, picker) {
        $("#orderRequireTime").val(picker.startDate.format(picker.locale.format));
      });
    },
    btn: ['确定', '取消'],
    btn1: function (index) {
      var requireTime = $("#orderRequireTime").val();
      if (!isBlank(requireTime)) {
        $.post(baseURL + "order/confirmOrder", {
          orderSeq: seq,
          requireTime: requireTime
        }, function (r) {
          if (r.code == 0) {
            alert(r.msg, function () {
              vm.statusName(1);
            });
          } else {
            alert(r.msg);
          }
        });
        layer.close(index);
      } else {
        alert("请选择到货时间");
      }
    },
    end: function () {
      $("#orderRequireTime").val('');
    }
  });
}


//拆单
function splitOrder(orderSeq) {
  var seqArr = new Array();
  seqArr[0] = orderSeq;

  layer.open({
    type: 1,
    offset: '50px',
    skin: 'layui-layer-molv',
    title: "选择拆单方式",
    area: ['450px', '225px'],
    shade: 0,
    shadeClose: false,
    content: jQuery("#splitOrderLayer"),
    btn: ['确定', '取消'],
    btn1: function (index) {
      var splitSXId = vm.splitBySXId;
      if (!isBlank(splitSXId)) {
        confirm("确认要拆单？", function () {
          $.ajax({
            type: "POST",
            url: baseURL + "order/splitOrder",
            traditional: true,
            data: {
              orderSeqArr: seqArr,
              splitSXId: splitSXId
            },
            success: function (r) {
              if (r.code === 0) {
                alert(r.msg, function () {
                  vm.reloadOrder();
                });
              } else {
                alert(r.msg);
              }
            },
            error: function () {
              alert('服务器开小差啦');
            }
          });
          vm.splitBySXId = '';
          layer.close(index);
        });

      } else {
        alert("请选择拆单方式");
      }
    }
  });
}


//归类拆单同步
function classifySplitOrder() {
  vm.splitOrderModelSeq = vm.defaultSplitModelSeq;
  layer.open({
    type: 1,
    offset: '50px',
    skin: 'layui-layer-molv',
    title: "接单",
    area: ['550px', '225px'],
    shade: 0,
    shadeClose: false,
    content: jQuery("#classifySplitOrderLayer"),
    zIndex: 1000, //防止daterangepicker控件被覆盖
    success: function (layero, index) {
      $(layero).find('#classifySplitTime').daterangepicker({
        showDropdowns: true,
        autoUpdateInput: false,
        timePicker: true,
        timePicker24Hour: true,
        timePickerSeconds: true, //时间显示到秒
        locale: {
          format: 'YYYY/MM/DD HH:mm:ss',
          applyLabel: "应用",
          cancelLabel: "取消",
          customRangeLabel: '选择时间',
          daysOfWeek: ["日", "一", "二", "三", "四", "五", "六"],
          monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
          firstDay: 0
        }
      }).on('apply.daterangepicker', function (ev, picker) {
        $("#classifySplitTime").val(picker.startDate.format(picker.locale.format) + "~" + picker.endDate.format(picker.locale.format));
      });
    },
    btn: ['确定', '取消'],
    btn1: function (index) {
      var splitModelSeq = vm.splitOrderModelSeq;
      var classifySplitTime = $("#classifySplitTime").val();
      if (isBlank(splitModelSeq)) {
        alert("请选择拆单模板");
        return;
      }
      if (isBlank(classifySplitTime)) {
        alert("请选择时间段");
        return;
      }
      confirm("确认要拆单？", function () {
        $.post(baseURL + "order/splitOrderByModel", {   //classifySplitOrder
          modelSeq: splitModelSeq,
          classifySplitTime: classifySplitTime
        }, function (r) {
          if (r.code === 0) {
            alert(r.msg, function () {
              vm.reloadOrder();
            });
          } else {
            alert(r.msg);
          }
        });
        layer.close(index);
      });
    },
    end: function () {
      $("#classifySplitTime").val('');
    }
  });
}


// 修改总价
function changePrice(seq) {
  $.get(baseURL + 'order/orderGoods?seq=' + seq, function (r) {
    vm.showList = false;
    vm.showChangePrice = true;
    vm.showPay = false;
    vm.showShip = false;
    vm.showReceive = false;
    vm.showOrderDetail = false,
      vm.orderDetail = r.order;
    vm.productsPriceList = r.list;
  });
}

// 支付
function payButton(seq) {
  $.get(baseURL + "order/getOrder?seq=" + seq, function (r) {
    vm.showList = false;
    vm.showChangePrice = false;
    vm.showPay = true;
    vm.showShip = false;
    vm.showReceive = false;
    vm.showOrderDetail = false,
      vm.orderDetail = r.order;
  });
}

//入库
function depotButton(seq) {
  confirm("确认商品已经入库？", function () {
    $.post(baseURL + "order/updateOrderStatus", {
      seq: seq,
      orderStatus: 3
    }, function (r) {
      if (r.code === 0) {
        alert(r.msg, function () {
          vm.statusName(3);
        });
      } else {
        alert(r.msg);
      }
    });
  });
}

// 发货
function shipButton(seq) {
  $.get(baseURL + 'order/orderGoods?seq=' + seq, function (r) {
    vm.showList = false;
    vm.showChangePrice = false;
    vm.showPay = false;
    vm.showShip = true;
    vm.showReceive = false;
    vm.showOrderDetail = false,
      vm.title = '订单发货详情';
    vm.orderDetail = r.order;
    vm.shipGoodsDetail.list = r.list;
  });
}

//延长收货
function receiveButton(seq) {
  $.get(baseURL + "order/getOrder?seq=" + seq, function (r) {
    vm.showList = false;
    vm.showChangePrice = false;
    vm.showPay = false;
    vm.showShip = false;
    vm.showReceive = true;
    vm.showOrderDetail = false,
      vm.title = '延长收货';
    vm.orderDetail = r.order;
  });
}

//详情
function getOrderDetail(seq) {
  $.get(baseURL + 'order/orderGoods?seq=' + seq, function (r) {
    vm.showList = false;
    vm.showChangePrice = false;
    vm.showPay = false;
    vm.showShip = false;
    vm.showReceive = false;
    vm.showOrderDetail = true;
    vm.title = '订单详情';
    vm.orderDetail = r.order;
    vm.productsPriceList = r.list;
  });
}

//删除订单
function del(id) {
  $.get(baseURL + "order/deleteOrder?seq=" + id, function (r) {
    if (r.code === 0) {
      alert('操作成功', function () {
        vm.reloadOrder();
      });
    } else {
      alert(r.msg);
    }
  });
}


//将订单导入ERP
function importIntoErp(seq) {
  $.post(baseURL + "order/importIntoErp", {
    orderSeq: seq
  }, function (r) {
    if (r.code === 0) {
      alert(r.msg, function () {
        vm.reloadOrder();
      });
    } else {
      alert(r.msg);
    }
  });
}


//获取iframe的url中名为name的参数
function getIframeUrlParam(name) {
  var reg = new RegExp("[^\?&]?" + encodeURI(name) + "=[^&]+");
  var arr = window.parent.document.getElementById("mainIframe").contentWindow.location.search.match(reg);
  if (arr != null) {
    return decodeURI(arr[0].substring(arr[0].search("=") + 1));
  }
  return "";
}
