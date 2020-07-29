$(function () {
  loadGoodsGrid();
});


function loadGoodsGrid() {
  $("#jqGrid").jqGrid({
    url: baseURL + 'sys/orderMeetingManage/list',
    datatype: "json",
    mtype: "POST",
    postData: {
      'categorySeq': vm.categorySeq,
      'year': vm.year,
      'seasonSeq': vm.seasonSeq,
      'keywords': vm.keywords,
    },
    colModel: [
      {label: '货号', name: 'goodID', width: 100, align: "center"},
      {label: '颜色', name: 'colorName', width: 60, align: "center"},
      {label: '大类', name: 'daLeiName', width: 60, align: "center"},
      {label: '中类', name: 'zhLeiName', width: 60, align: "center"},
      {label: '风格', name: 'categoryName', width: 60, align: "center"},
      {label: '年份', name: 'year', width: 60, align: "center"},
      {label: '季节', name: 'seasonName', width: 60, align: "center"},
      {label: '创建时间', name: 'inputTime', width: 100, align: "center"},
      {label: '季节序号', name: 'seasonSeq', hidden: true},
      {label: '分类序号', name: 'categorySeq', hidden: true},
      {label: '序号', name: 'seq', hidden: true},
      {
        label: '操作', width: 50, align: "center",
        formatter: function (cellvalue, options, rowObject) {
          return '<button class="operation-btn-security" onclick="lineEdit(' + options.rowId + ')">修改</button>'
            + '<button class="operation-btn-dangery" onclick="del(' + rowObject.seq + ')">删除</button>';
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
//	    shrinkToFit:false,
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
 * 表格删除按钮触发此方法
 * @param seq
 */
function del(seq) {
  if (!seq) {
    layer.alert('删除异常，请重试', {icon: 2});
    return;
  }
  layer.confirm("确定要删除吗", function (index) {
    $.get(baseURL + 'sys/orderMeetingManage/del/' + seq, function (data) {
      if (data.code == 0) {
        layer.msg("删除成功")
        vm.reloadGoods();
      } else {
        layer.alert('删除失败，请重试', {icon: 2});
      }
    })

    layer.close(index);
  })

}

/*表格行编辑按钮方法*/
function lineEdit(rowId) {
  let rowObj = $("#jqGrid").getRowData(rowId);
  vm.addGoods();
  vm.edit = rowObj
}

var vm = new Vue({
  el: '#rrapp',
  data: {
    categoryArray: [],
    years: [],
    seasons: [],

    // 表格加载条件
    categorySeq: '',
    selectCategoryName: '',
    keywords: '',
    yearList: [],
    seasonList: [],
    year: -1,
    seasonSeq: -1,
    selectSeasonName: '',

    showList: true,

    /*编辑区数据*/
    edit: {
      goodID: '',
    }
  },
  methods: {
    loadYears: function () {
      var currYear = new Date().getFullYear();
      this.years.push(currYear + 1);

      for (var i = 0; i < 10; i++) {
        this.years.push(currYear - i);
      }
    },
    loadSeasons: function () {
      $.get(baseURL + "system/season/all", function (data) {
        if (data.result) {
          vm.seasons = data.result;
        }
      })
    },

    loadYearList: function () {
      $.get(baseURL + "sys/orderMeetingManage/years", function (data) {
        if (data.result) {
          vm.yearList = data.result;
        }
      })

    },
    loadSeasonList: function () {
      $.get(baseURL + "sys/orderMeetingManage/seasons", function (data) {
        if (data.result) {
          vm.seasonList = data.result;
        }
      })
    },
    loadCategory: function () {
      // 加载分类树
      $.get(baseURL + "system/goodsCategory/list", function (r) {
        vm.categoryArray = r.list;
        // vm.categoryArray.unshift({seq: -1, parentSeq: 0, name: "所有分类"});
      });
    },
    reloadGoods: function () {
      let currPage = 1;
      currPage = $("#jqGrid").jqGrid('getGridParam', 'page');//获取当前页

      $("#jqGrid").jqGrid('setGridParam', {
        datatype: 'json',
        postData: {
          'categorySeq': vm.categorySeq,
          'year': vm.year,
          'seasonSeq': vm.seasonSeq,
          'keywords': vm.keywords
        },
        page: currPage,
      }).trigger('reloadGrid');
    },
    addGoods: function () {
      this.showList = false;
      this.edit = {
        goodID: '',
        year: '',
        seasonName: '',
        seasonSeq: '',
        categoryName: '',
        categorySeq: ''
      }
    },
    yearSelect: function (item) {
      this.year = item;
    },
    seasonSelect: function (index) {
      if (index == -1) {
        this.selectSeasonName = "所有季节";
        this.seasonSeq = -1;
        return;
      }
      this.selectSeasonName = this.seasons[index].seasonName;
      this.seasonSeq = this.seasons[index].seq;
    },
    categorySelect: function (item) {
      vm.categorySeq = item.seq;
      vm.selectCategoryName = item.name;
    },
    categoryChoose: function (item) {
      vm.$set(vm.goodsDetail, "categoryName", item.name);
      vm.goodsDetail.categorySeq = item.seq;
    },
    yearChoose: function (item) {
      this.$set(vm.goodsDetail, "year", item);
    },
    seasonChoose: function (index) {
      this.$set(this.goodsDetail, "seasonName", this.seasons[index].seasonName);
      this.goodsDetail.seasonSeq = this.seasons[index].seq;
    },
    chooseCategory: function (item) {
      this.$set(vm.edit, "categoryName", item.name);
      this.edit.categorySeq = item.seq;
    },
    chooseYear: function (item) {
      this.$set(vm.edit, "year", item);
    },
    chooseSeason: function (index) {
      this.$set(vm.edit, "seasonName", this.seasons[index].seasonName);
      this.edit.seasonSeq = this.seasons[index].seq;
    },
    search: function () {
      $("#jqGrid").jqGrid('setGridParam', {
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

    uploadGoodsExcel: function () {
      parent.window.showLoading();

      $('#goodsExcelForm').ajaxSubmit({
        url: baseURL + 'sys/orderMeetingManage/uploadGoodsExcel',
        dataType: 'json',
        cache: false,
        success: function (r) {
          if (r.code === 0) {
            layer.alert(r.msg);
            location.reload();
          } else {
            layer.alert(r.msg);
          }
          $("#goodsExcelForm").resetForm();
          parent.window.hideLoading();
        },
        error: function (r) {
          alert("导入商品出错！");
          $("#goodsExcelForm").resetForm();
          parent.window.hideLoading();
        }
      });
    },
    toMain: function () {
      this.showList = true;
    },
    saveOrUpdate: function () {

      if (!this.validateParams()) {
        //参数不合法
        console.log("参数不合法")
        return;
      }

      $.post(baseURL + "sys/orderMeetingManage/saveOrUpdate", vm.edit, function (data) {
        if (data.code == 0) {
          layer.msg("保存成功")
          vm.reloadGoods();
          vm.showList = true;
        } else {
          layer.alert('保存失败，请重试', {icon: 0});
        }
      })
    },
    validateParams: function () {
      if (isBlank(this.edit.goodID)) {
        layer.alert("货号不能为空", {icon: 0});
        return false;
      }

      if (isBlank(this.edit.year)) {
        layer.alert("请选择年份", {icon: 0});
        return false;
      }

      if (!this.edit.seasonSeq) {
        layer.alert("请选择季节", {icon: 0});
        return false;
      }

      if (!this.edit.categorySeq) {
        layer.alert("请选择分类", {icon: 0});
        return false;
      }

      return true;
    },
    exportTemplate: function () {
      var excelForm = document.getElementById('exportExcelForm');
      excelForm.action = baseURL + "sys/orderMeetingManage/exportExcel";
      excelForm.token.value = token;
      excelForm.submit();
    },
    exportQRCode: function () {

      var msg = '确定下载 '
      if (this.year == -1) {
        msg += '所有年份'
      } else {
        msg += this.year + '年';
      }

      if (this.seasonSeq == -1) {
        msg += '所有季节'
      } else {
        for (var i in this.seasonList) {
          var season = this.seasonList[i];
          if (season.seq == this.seasonSeq) {
            msg += season.seasonName;
            break;
          }
        }
      }

      msg += ' 的货品二维码吗？';

      layer.confirm(msg, {icon: 3}, function (index) {
        var form = document.getElementById('exportExcelForm');
        form.action = baseURL + "sys/orderMeetingManage/exportQRCode";
        form.token.value = token;
        form.year.value = vm.year;
        form.seasonSeq.value = vm.seasonSeq;
        form.submit();
        layer.close(index);
      })
    },
  },
  created: function () {
    this.loadCategory();
    this.loadYears();
    this.loadSeasons();
    this.loadYearList();
    this.loadSeasonList();
  },
});


