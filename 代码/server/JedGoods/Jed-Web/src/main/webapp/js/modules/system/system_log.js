$(function () {
  $("#jqGrid").jqGrid({
    url: baseURL + "log/list",
    datatype: "json",
    colModel: [
      {label: '序号', name: 'seq', width: 50, align: "center", key: true, sortable: false},
      {label: '描述', name: 'content', width: 400, align: "center", sortable: false},
      {label: '创建日期', name: 'inputTime', width: 100, align: "center", sortable: false},
      {
        label: '操作', name: '', width: 100, align: "center", sortable: false,
        formatter: function (cellvalue, options, rowObject) {
          var detail = ('<button class="operation-btn-security" onclick="lineEdit(' + rowObject.seq + ')">修改</button>'
            + '&nbsp' + '<button class="operation-btn-dangery" onclick="delRow(' + rowObject.seq + ')">删除</button>');
          return detail;
        }
      }
    ],
    height: 'auto',
    rowNum: 10,
    rowList: [10, 20, 30, 40, 50],
    rownumbers: true,
    rownumWidth: 50,
    autowidth: true,
    multiselect: true,
    // sortname: 'inputTime',
    // sortorder: "desc",
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
      // order: "order"
    },
    pgbuttons: true, // 	是否显示翻页按钮
    pginput: true, //是否显示跳转页面的输入框
    viewrecords: true, //显示总记录数

    gridComplete: function () {
      // 隐藏grid底部滚动条
      $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
    },
  });

  /*文本域添加回车事件，自动添加序号*/
  $("#content").keydown(function (event) {

    if (event.which == 13) {
      //如果光标所在位置小于文本长度，则表示不在文本末尾
      if(getCursorPos(this)<vm.logContent.length) return;

      var ds = vm.logContent.match(/\d+、/g);
      var ds2 = [0];
      for (let i in ds) {
        ds2.push(ds[i].match(/\d+/)[0]);
      }
      //延时，可避免先生成序号，再回车换行的情况
      setTimeout(function () {
        vm.logContent = vm.logContent + "\n" + ++ds2[ds2.length - 1] + "、";
      }, 100);
    }
  });

})

/*获取光标在textarea文本中的位置*/
function getCursorPos(textAreaObj) {
  var cursurPosition = -1;
  if (textAreaObj.selectionStart) {//非IE浏览器
    cursurPosition = textAreaObj.selectionStart;
  } else {//IE
    if (!document.selection) return 0;
    var range = document.selection.createRange();
    range.moveStart("character", -textAreaObj.value.length);
    cursurPosition = range.text.length;
  }
  return cursurPosition;
}

let vm = new Vue({
  el: "#rrapp",
  data: {
    showList: true,
    panelTitle: "日志编辑面板标题",
    logAction: null,
    logContent: "",
    logSeq: null,
    logInputTime: null,
    wordCount: 2,
  },
  methods: {
    add: function () {
      this.showList = false;
      this.panelTitle = "新增日志";
      this.logAction = "add";
      this.enterSeq = 2;
      this.logInputTime = new Date().format("yyyy/MM/dd HH:mm:ss")
      this.logContent = "1、";
    },
    /*日志的保存或更新*/
    saveOrUpdate: function () {
      if (!vm.logContent || vm.logContent == "") {
        alert("内容不能为空");
        return;
      }
      vm.logContent = vm.logContent.replace(/\n/g, "&lt;br&gt;")

      if (this.wordCount > 600) {
        alert("字数不得超过600！！")
        return;
      }

      $.post(baseURL + "log/" + vm.logAction, {
          seq: vm.logSeq,
          content: vm.logContent,
          inputTime: vm.logInputTime,
        }, function (result, status) {
          if (result.code === 0) {
            vm.reload();
            alert(result.msg);
          } else {
            alert(r.msg);
          }
        }
      )
    },
    reload: function (event) {
      vm.showList = true;
      $("#jqGrid").jqGrid('setGridParam', {}).trigger("reloadGrid");

    },
    delRows: function () {
      /*获取所有选中行id*/
      var ids = $("#jqGrid").jqGrid("getGridParam", "selarrrow");
      if(ids.length == 0) return;
      $.post(baseURL + "log/deleteS", {seqs: JSON.stringify(ids)}, function (r) {
        vm.reload();
        alert(r.msg);
      })
    },
  },

  watch: {
    logContent: function (val, oldVal) {
      this.wordCount = val.length;
      if (this.wordCount > 600) {
        alert("字数不得超过600！！")
      }
    },
  }
})

/*vue end*/


function lineEdit(seq) {
  vm.logSeq = seq;
  $.get(baseURL + "log/one?seq=" + seq, function (r) {
    if (r.code == 0) {
      vm.showList = false;
      vm.panelTitle = "修改日志"
      vm.logAction = "update";
      vm.logContent = r.result.content.replace(/&lt;br&gt;/g, "\n");
      vm.logInputTime = r.result.inputTime
    } else {
      alert(r.msg)
    }
  });

}

function delRow(seq) {
  $.get(baseURL + "log/delete?seq=" + seq, function (r) {
    vm.reload();
    alert(r.msg);
  })


}

//初始化时间控件
$(function () {

  $("#logInputTime").daterangepicker(
    {
      singleDatePicker: true,
      showDropdowns: true,
      autoUpdateInput: false,
      showISOWeekNumbers: true,
      showWeekNumbers: true,
      timePicker24Hour: true,
      timePicker: true,
      autoApply: true,  //timePicker为false时有用
      locale: {
        format: "YYYY/MM/DD HH:mm:ss",
        applyLabel: "应用",
        cancelLabel: "取消",
        resetLabel: "重置",
        daysOfWeek: ["日", "一", "二", "三", "四", "五", "六"],
        monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        firstDay: 1
      }

    }).on('apply.daterangepicker', function (ev, picker) {
    vm.logInputTime = picker.startDate.format(picker.locale.format);
  })
})
