$(function () {
  moveLabs();
  fourBtns();
  /*加载订单模型列表*/
  initOrderModelList()
  /*给表格添加列拖动功能*/
  add_drag_th();
  dblClickToDelTh();
});

var vm = new Vue({
  el: "#app",
  data: {
    lChecked: [], /*左边select多选项*/
    rChecked: [], /*右select多选项*/

    /*左侧选项框列表*/
    leftCols: [],
    /*缓存原数据，用于取消操作*/
    leftColsTmp: [],
    /*右侧选项框列表*/
    rightCols: [],
    /*缓存原数据，用于取消操作*/
    rightColsTmp: [],

    /*供查询使用*/
    ths: [
      /*行字段*/
      {name: "订单号", value: "orderNum", color: "blue", lineFieldFlag: true},
      {name: "订单状态", value: "orderStatus", color: "blue", lineFieldFlag: true},
      {name: "订货方名称", value: "userName", color: "blue", lineFieldFlag: true},
      {name: "货号", value: "goodID", color: "blue", lineFieldFlag: true},
      {name: "尺码", value: "sizeName", color: "blue", lineFieldFlag: true},
      {name: "颜色", value: "colorName", color: "blue", lineFieldFlag: true},
      {name: "波次", value: "periodName", color: "blue", lineFieldFlag: true},

      /*汇总字段*/
      {name: "单价", value: "productPrice", summaryFieldFlag: true, color2: "gray"},
      {name: "总价格", value: "productTotalPrice", summaryFieldFlag: true, color2: "gray"},
      {name: "购买数量", value: "buyCount", summaryFieldFlag: true, color2: "gray"},
      {name: "已发货数量", value: "deliverNum", summaryFieldFlag: true, color2: "gray"},

      /*自定义列*/
      {name: "时间", value: "inputTime"},
      {name: "分类", value: "categoryName"},
      {name: "详细地址", value: "fullAddress"},
      {name: "电话", value: "telephone"},
      {name: "收件人", value: "receiverName"}
    ],

    /*表格标题列表*/
    thCols: [], //展示的表格标题
    lineFields: [],  //行字段
    showLineFields: [],//显示的行字段
    summaryFields: [], //汇总字段
    showSummaryFields: [],//显示的汇总字段

    /*订单模板列表*/
    orderModels: [],
    /*表格原始数据*/
    orderDataList: [],
    /*表格展示数据*/
    dataList: [],

    /**查询条件**/
    /*订单模板列表选中模板*/
    modelSeqChecked: -1,
    timeRange: "",
    attachTypeChecked: -1,
    orderStatusChecked: -1,
    keywords: null,

  },
  methods: {
    /*初始化表格*/
    initTable: function (orderDataList) {

      /*缓存订单数据*/
      vm.orderDataList = orderDataList;

      var newThCols = [{name: "时间", value: "inputTime"}]; //存放th标题对象
      vm.showLineFields = [];//记录表格上可见的行字段
      vm.showSummaryFields = [];
      vm.leftCols = [];

      /*设置的行字段，展示在标签容器，可拖动，初始表格不显示*/
      for (var i in vm.lineFileds) {
        for (var j in vm.ths) {
          if (vm.ths[j].value == vm.lineFileds[i]) {
            vm.showLineFields.push(vm.ths[j]);  //表格上直接显示模版设定的行字段
            break;
          }
        }
      }

      /*所有不在showLineFields的行字段存放在leftCols*/
      for (var i in vm.ths) {
        if (vm.ths[i].hasOwnProperty("lineFieldFlag")) {
          //如果是行字段，判断是否在showLineFields里
          var noExistFlag = true;
          for (var j in vm.showLineFields) {
            if (vm.showLineFields[j].value == vm.ths[i].value) {
              noExistFlag = false;
              break;
            }
          }
          //不在则添加到vm.leftCols
          if (noExistFlag) {
            vm.leftCols.push(vm.ths[i]);
          }
        }
      }


      /*设置的汇总字段，显示在表格列*/
      for (var i in vm.summaryFields) {
        for (var j in vm.ths) {
          if (vm.ths[j].value == vm.summaryFields[i]) {
            vm.showSummaryFields.push(vm.ths[j]);
            break;
          }
        }
      }

      /*新的标题组*/
      newThCols = vm.showLineFields.concat(newThCols).concat(vm.showSummaryFields);

      vm.thCols = newThCols;
    },
    /*data格式： [[{cell},{cell}],[{},{}]]*/
    steam: function (data, fields) {
      var resArr = [];
      for (var j in data) {

        /*合计行的特殊处理*/
        if (data[j][0].name == "sum") {
          resArr[resArr.length - 1].data.push(data[j]);
          continue;
        }

        var flag = true;
        for (var k in resArr) {
          var flag1 = true;
          /*条件都满足则相同*/
          for (var i in fields) {
            if (resArr[k][fields[i]] != data[j][i].value) {
              flag1 = false;
              break;
            }
          }
          if (flag1) {
            flag = false;
            resArr[k].data.push(data[j]);
            break;
          }
        }
        /*结果数组中没有，创建新的*/
        if (flag) {
          var obj = {};
          for (var i in fields) {
            obj[fields[i]] = data[j][i].value;
          }
          obj.data = [];
          obj.data.push(data[j]);
          resArr.push(obj);
        }
      }

      return resArr;
    },
    /*从steam()返回的对象数组中提取数据*/
    fetchData: function (data) {
      var res = [];
      for (var i in data) {
        res = res.concat(data[i].data);
      }
      return res;
    },
    handleLineField: function (data) {
      var resArr = [];
      for (var index in this.showLineFields) {
        var fields = [];
        for (var j = 0; j <= index; j++) {
          fields.push(this.showLineFields[j].value)
        }
        /**根据行字段分组操作*/
        if (index == 0) {
          resArr = this.steam(data, fields);//对第一列行字段分组
        } else {
          resArr = this.steam(resArr, fields);
        }
        resArr = this.fetchData(resArr);
      }

      /**行字段从后往前推算*/
      for (var i = this.showLineFields.length - 1; i >= 0; i--) {  //遍历行字段列

        var fields = [];
        for (var j = 0; j <= i; j++) {
          fields.push(this.showLineFields[j].value)
        }
        resArr = this.steam(resArr, fields);

        for (var k in resArr) {
          var obj = resArr[k]; //每个封装条件的数据对象

          if (obj.data.length == 1) continue;

          //创建合计行
          //合计单元格的所占列数： thCols.length-(index+1)-showSummaryFields.length
          var newRow = [{
            name: "sum",
            value: "合计：",
            visible: 1,
            colNum: vm.thCols.length - (i + 1) - vm.showSummaryFields.length,
            rowNum: 1,
            color: "blue2"
          }];

          //对汇总字段求和
          for (var n in vm.showSummaryFields) {
            var summaryField = vm.showSummaryFields[n].value; //汇总字段名
            var summaryFieldPos = vm.showSummaryFields[n].index;
            var summaryFieldSum = 0;
            var newCell = {name: summaryField, visible: 1, colNum: 1, rowNum: 1, color: "blue2"};

            //循环求汇总字段之和
            for (var m in obj.data) {
              var row = obj.data[m];  //row为cell数组
              //排除包含的合计行
              if (row[0].name != "sum") {
                summaryFieldSum += row[summaryFieldPos].value;
              }
            }
            newCell.value = summaryFieldSum;
            newRow.push(newCell);
          }

          //添加合计行
          obj.data.push(newRow);

          obj.data[0][i].rowNum = obj.data.length;

          for (var t = 1; t < obj.data.length; t++) {
            if (obj.data[t][0].name != "sum") {
              obj.data[t][i].visible = 0;
            }
          }

        }

        //提取合并 行数据
        resArr = this.fetchData(resArr);
      }
      return resArr;
    }
  },
  watch: {
    /*标题列表改变，表格数据跟随刷新*/
    thCols: function (newThArr) {
      // console.log("进入watch：thCols改变");
      var newData = [];
      this.showLineFields = [];
      this.showSummaryFields = [];

      /*将订单状态改为订单状态名称*/
      for (var i in newThArr) {
        if (newThArr[i].value == "orderStatus") {
          newThArr[i].value = "statusName";
          break;
        }
      }

      /*将每个对象转成单元格对象数组*/
      for (var index in this.orderDataList) {
        var obj = this.orderDataList[index];
        var row = [];

        /*单元格顺序按标题数组顺序排列*/
        for (var i in newThArr) {
          for (var key in obj) {
            if (newThArr[i].value == key) {
              var cell = {};
              cell.name = key;
              cell.value = obj[key];
              cell.rowNum = 1;
              cell.colNum = 1;
              cell.visible = 1;
              cell.color = "";
              row.push(cell);
              break;
            }
          }
        }
        newData.push(row);
      }

      for (var j in newThArr) {
        /*记录行字段*/
        if (newThArr[j].hasOwnProperty("lineFieldFlag")) {
          this.showLineFields.push(newThArr[j]);
        }
        /*记录汇总字段*/
        for (var m in vm.summaryFields) {
          if (newThArr[j].value == vm.summaryFields[m]) {
            newThArr[j].index = j;
            this.showSummaryFields.push(newThArr[j]);
            break;
          }
        }
      }

      var resArr = this.handleLineField(newData);
      this.dataList = resArr;
    }
  },
  created: function () {
    loadGoodsSX();
  },
});

function showLayer() {
  vm.rightCols = vm.thCols.slice(0);
  vm.lChecked = [];
  vm.rChecked = [];

  /*原数据备份，用于取消操作*/
  vm.leftColsTmp = vm.leftCols.slice(0);
  vm.rightColsTmp = vm.rightCols.slice(0);

  layer.open({
    type: 1,
    title: '编辑表格显示列顺序',
    closeBtn: 0,
    area: ['700px', '500px'],
    closeBtn: 1,

    // skin: 'layui-layer-lan',
    shadeClose: false,
    content: $('#content'),
    btn: ['确认', '取消'],
    yes: function (index, layero) {
      //按钮【确认】的回调
      vm.thCols = vm.rightCols.slice(0);
      layer.close(index);
    },
    btn2: function (index, layero) {
      //按钮【取消】的回调
      //return false 开启该代码可禁止点击该按钮关闭
      vm.leftCols = vm.leftColsTmp.slice(0);
      vm.rightCols = vm.rightColsTmp.slice(0);
    },
    cancel: function () {
      //右上角关闭回调
      //return false 开启该代码可禁止点击该按钮关闭
      vm.leftCols = vm.leftColsTmp.slice(0);
      vm.rightCols = vm.rightColsTmp.slice(0);
    }
  });
}

/*选项框操作*/
function fourBtns() {
  $("#leftList").click(function () {
    vm.rChecked = [];
  })

  $(".toLeft").click(function () {
    for (var i = vm.rChecked.length - 1; i >= 0; i--) {
      /*若右侧选项为汇总字段，不能移动，跳过*/
      if (vm.rightCols[vm.rChecked[i]] && vm.rightCols[vm.rChecked[i]].hasOwnProperty("summaryFieldFlag")) {
        alert("汇总列不能移除");
        continue;
      }
      /*将右侧的选项移动到左侧*/
      vm.leftCols.push(vm.rightCols[vm.rChecked[i]]);
      /*删除右侧选项数组的选项*/
      vm.rightCols.splice(vm.rChecked[i], 1);
      vm.lChecked = [vm.leftCols.length - 1];
    }

  });
  $(".toRight").click(function () {
    for (var i = vm.lChecked.length - 1; i >= 0; i--) {
      /*将左侧的选项移动到右侧头部*/
      vm.rightCols.unshift(vm.leftCols[vm.lChecked[i]]);
      /*删除左侧选项数组的选项*/
      vm.leftCols.splice(vm.lChecked[i], 1);
      vm.rChecked = [0];
    }

  });
  $(".toUp").click(function () {
    if (vm.rChecked.length == 1) {
      var index = vm.rChecked[0];
      if (index > 0) {
        /*不能将非行字段移到行字段前*/
        if (!vm.rightCols[index].hasOwnProperty("lineFieldFlag")
          && vm.rightCols[index - 1].hasOwnProperty("lineFieldFlag")) {
          alert("非行字段列不能移到行字段列前")
          return;
        }
        if (vm.rightCols[index].hasOwnProperty("summaryFieldFlag")
          && !vm.rightCols[index - 1].hasOwnProperty("summaryFieldFlag")) {
          alert("汇总列不能移到非汇总列前")
          return;
        }
        var tmp = vm.rightCols[index];
        Vue.set(vm.rightCols, index, vm.rightCols[index - 1]);
        Vue.set(vm.rightCols, index - 1, tmp);
        vm.rChecked = [index - 1];
      }
    } else if (vm.rChecked.length == 0) {
      alert("请先点击选中右侧列表某项，可向上移动操作")
    } else {
      alert("上下移动只能操作单项")
    }

  });
  $(".toDown").click(function () {
    if (vm.rChecked.length == 1) {
      var index = vm.rChecked[0];
      if (index < vm.rightCols.length - 1) {
        /*不能将非汇总字段移到汇总字段后*/
        if (!vm.rightCols[index].hasOwnProperty("summaryFieldFlag")
          && vm.rightCols[index + 1].hasOwnProperty("summaryFieldFlag")) {
          alert("非汇总列不能移到汇总列后")
          return;
        }
        if (vm.rightCols[index].hasOwnProperty("lineFieldFlag")
          && !vm.rightCols[index + 1].hasOwnProperty("lineFieldFlag")) {
          alert("行字段列不能移到非行字段列后")
          return;
        }
        var tmp = vm.rightCols[index];
        Vue.set(vm.rightCols, index, vm.rightCols[index + 1]);
        Vue.set(vm.rightCols, index + 1, tmp);
        vm.rChecked = [index + 1];
      }
    } else if (vm.rChecked.length == 0) {
      alert("请先点击选中右侧列表某项，可向下移动操作")
    } else {
      alert("上下移动只能操作单项")
    }
  });
}


/*给表格添加列拖动功能*/
function add_drag_th() {

  //允许放入
  $("#tbl").on("dragover", function (e) {
    e.originalEvent.preventDefault();
  });
  //拿起
  $("#tbl").on("dragstart", function (e) {
    e.originalEvent.dataTransfer.clearData("enable");
    e.originalEvent.dataTransfer.clearData("obj_add");

    var _target = e.target;
    if ($(_target).hasClass("blue")) { //只允许拖动蓝色标签
      e.originalEvent.dataTransfer.setData("obj_add", _target.cellIndex);
      e.originalEvent.dataTransfer.setData("enable", "true");
    }
  });
  //放下
  $("#tbl").on("drop", function (e) {
    e.originalEvent.preventDefault();

    var _target = e.target;
    /*移动标签到表格标题行*/
    /*标签拖动的目标位置 非蓝色标题，添加到开头*/
    var index = parseInt(e.originalEvent.dataTransfer.getData("indexOfSpans"));

    if (!isNaN(index)) {
      if (!$(_target).hasClass("blue")) {
        /*目标位置是白色标签*/
        //移动对象是行字段标签，将移动标签到白色标签前
        for (var j = 0; j < vm.thCols.length; j++) {
          if (!vm.thCols[j].hasOwnProperty("color")) {
            vm.thCols.splice(j, 0, vm.leftCols[index]);
            break;
          }
        }

      } else {
        //目标位置是蓝色
        /*若是行字段标签，*/
        //目标位置也是行字段标签，则加在后面
        vm.thCols.splice(_target.cellIndex + 1, 0, vm.leftCols[index]);
      }
      /*删除移动后的标签*/
      vm.leftCols.splice(index, 1);
      e.originalEvent.dataTransfer.clearData("indexOfSpans");
      return;
    }

    /*标题标签之间的移动*/
    if (!$(_target).hasClass("blue")) return;

    var flag = e.originalEvent.dataTransfer.getData("enable");
    if (flag != "true") return;

    var i = parseInt(e.originalEvent.dataTransfer.getData("obj_add"));//所拿起的th列下标

    if (isNaN(i)) return;
    var d = _target.cellIndex;//被放入的列下标

    var tmp = vm.thCols[i];
    Vue.set(vm.thCols, i, vm.thCols[d]);
    Vue.set(vm.thCols, d, tmp);
  });
}

/*移动标签*/
function moveLabs() {
  $(".ths").on("dragstart", function (e) {
    e.originalEvent.dataTransfer.setData("indexOfSpans", e.target.getAttribute("value"));
  });

  //允许放入
  $(".ths").on("dragover", function (e) {
    e.originalEvent.preventDefault();
  });
  $(".ths").on("drop", function (e) {
    e.originalEvent.preventDefault();

    /*只能放蓝色标签*/
    var flag = e.originalEvent.dataTransfer.getData("enable");
    if (flag != "true") return;

    var i = parseInt(e.originalEvent.dataTransfer.getData("obj_add"));//所拿起的th列下标

    if (isNaN(i)) return;
    /*将表格标题拖拽到标签列*/
    vm.leftCols.unshift(vm.thCols[i]);

    /*删除移动后的标题*/
    vm.thCols.splice(i, 1);

  })
}

/*初始化订单模型列表*/
function initOrderModelList() {

  $.get(baseURL + "system/orderStatisticsModel/allList", function (data) {
    if (data.code == 0) {
      vm.orderModels = data.result;
    }
  });
}

/*查询条件确认按钮*/
function queryOrderData() {

  if (vm.modelSeqChecked > -1) {
    /*查询条件过滤*/
    var queryCondition = "";
    if (!isBlank(vm.timeRange)) {
      var timeArr = vm.timeRange.split(" ~ ");
      queryCondition += "&startTime=" + timeArr[0] + "&endTime=" + timeArr[1];
    }

    if (vm.orderStatusChecked > -1) {
      queryCondition += "&orderStatus=" + vm.orderStatusChecked;
    }

    /*   if (vm.attachTypeChecked > -1) {
         queryCondition += "&attachType=" + vm.attachTypeChecked;
       }*/

    if (vm.keywords && vm.keywords != "") {
      queryCondition += "&keywords=" + vm.keywords;
    }

    // console.log("queryCondition: ",queryCondition)
    $.get(baseURL + "orderStatistics/list?statisticsModelSeq=" + vm.modelSeqChecked + queryCondition, function (r) {
      if (r.code == 0) {
        if (r.lineField) {
          vm.lineFileds = r.lineField.split(",");
        } else {
          alert("警告： 未设置行字段！");
        }

        if (r.summaryField) {
          vm.summaryFields = r.summaryField.split(",");
        } else {
          alert("警告：未设置汇总字段！");
        }

        vm.initTable(r.list);
      }
    })
  } else {
    alert("请选择当前模板！")
  }
}

function loadGoodsSX() {
  $.get(baseURL + "order/goods/getGoodsSXOption", function (r) {
    if (r.code == 0) {
      for (var i in r.result) {

        var sx = r.result[i];
        var newObj = {
          name: sx.sxname,
          value: sx.sxid,
          color: "blue",
          lineFieldFlag: true
        };
        vm.ths.push(newObj);
      }
    }
  });
}

//初始化时间控件
$(function () {

  $("#timeRange").daterangepicker(
    {
      // singleDatePicker: true,
      linkedCalendars: false,
      showDropdowns: true,
      autoUpdateInput: false,
      showISOWeekNumbers: true,
      showWeekNumbers: true,
      timePicker24Hour: true,
      timePicker: true,
      autoApply: true,
      locale: {
        format: "YYYY/MM/DD HH:mm:ss",
        separator: ' ~ ',
        applyLabel: "应用",
        cancelLabel: "取消",
        resetLabel: "重置",
        customRangeLabel: '选择时间',
        daysOfWeek: ["日", "一", "二", "三", "四", "五", "六"],
        monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        firstDay: 0
      }

    }).on('apply.daterangepicker', function (ev, picker) {
    vm.timeRange = picker.startDate.format(picker.locale.format) + picker.locale.separator + picker.endDate.format(picker.locale.format);
  }).on('cancel.daterangepicker', function (ev, picker) {
    vm.timeRange = ""
  });
})

/*绑定双击删除标题事件*/
function dblClickToDelTh() {
  $("#tbl").dblclick(function (e) {
    var _target = e.target;
    if (_target.tagName == "TH" && $(_target).hasClass("blue")) {
      // console.log("双击了蓝色标题", _target.cellIndex);
      var thIndex = _target.cellIndex;
      if (isNaN(thIndex)) {
        alert("当前浏览器不支持双击删除标题操作 T_T")
        return;
      }
      vm.leftCols.unshift(vm.thCols[thIndex]);
      vm.thCols.splice(_target.cellIndex, 1);
    }
  })
}

/** 导出指定字段的excel*/
function exportExcel() {

  /*post查询参数*/
  /*  var queryData = {
          startTime: null,
          endTime: null,
          orderStatus: null,
          keywords: null,
      fieldsTitle: null, /!*标题名称数组*!/
      fields: null, /!*字段名数组*!/
    };*/
  /*查询条件过滤*/
  var queryParam = "";

  var fieldsTitleTmp = [];
  var fieldsTmp = [];
  if (vm.thCols.length > 1) {

    for (var i in vm.thCols) {
      fieldsTitleTmp.push(vm.thCols[i].name);
      fieldsTmp.push(vm.thCols[i].value);
    }
    // queryParam = "?fieldsTitle=" + fieldsTitleTmp.join() + "&fields=" + fieldsTmp.join();
  } else {
    alert("该功能根据您查询的表格标题顺序导出,请先查询");
    return;
  }

  if (!isBlank(vm.timeRange)) {
    queryParam = queryParam == "" ? queryParam + "?" : queryParam + "&";
    var timeArr = vm.timeRange.split(" ~ ");
    queryParam += "startTime=" + timeArr[0] + "&endTime=" + timeArr[1];
  }

  if (vm.orderStatusChecked > -1) {
    queryParam = queryParam == "" ? queryParam + "?" : queryParam + "&";
    queryParam += "orderStatus=" + vm.orderStatusChecked;
  }

  if (vm.keywords && vm.keywords != "") {
    queryParam = queryParam == "" ? queryParam + "?" : queryParam + "&";
    queryParam += "keywords=" + vm.keywords;
  }

  var excelForm = document.getElementById('exportOrderExcelForm');
  excelForm.action = baseURL + "orderStatistics/exportExcel" + queryParam;
  // console.log("excelForm:", excelForm);
  excelForm.token.value = token;
  excelForm.fieldsTitle.value = fieldsTitleTmp;
  excelForm.fields.value = fieldsTmp;
  excelForm.submit();

}