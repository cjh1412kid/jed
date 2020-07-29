$(function () {
  $("#jqGrid").jqGrid(
    {
      url: baseURL + 'system/splitOrderModel/list',
      datatype: "json",
      colModel: [
        {label: '模板名称', name: 'modelName', width: 250, align: "center"},
        {label: '建立时间', name: 'inputTime', width: 200, align: "center"},
        {
          label: '是否默认', name: 'isDefault', width: 200, align: "center",
          formatter: function (cellvalue, options, rowObject) {
            if (rowObject.isDefault === 1) {
              return "默认模板";
            } else {
              return "";
            }
          },
        },
        {
          label: '操作', name: '', width: 150, align: "center",
          formatter: function (cellvalue, options, rowObject) {
            var buttonHtml = '';
            buttonHtml += ('<button class="operation-btn-security" onclick="lineEdit(' + rowObject.seq + ')">修改</button>' + '&nbsp;');
            buttonHtml += ('<button class="operation-btn-security" onclick="modelDetailEdit(' + rowObject.seq + ')">步骤编辑</button>' + '&nbsp;');
            buttonHtml += ('<button class="operation-btn-dangery" onclick="del(' + rowObject.seq + ')">删除</button>');
            return buttonHtml;
          }
        }],
      height: 'auto',
      rowNum: 10,
      rowList: [10, 30, 50],
      rownumbers: true,
      rownumWidth: 25,
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
});

var vm = new Vue({
  el: '#rrapp',
  data: {
    showList: true,
    showStepPage: false,
    title: '',
    sxArray: [],
    categoryArray: [],
    periods: [],
    shoesBrands: [],
    splitOrderModel: {
      isDefault: 0
    },
    steps: [],
    editStep: {}
  },
  methods: {
    add: function () {
      vm.title = '新增模板';
      vm.showList = false;
    },
    saveOrUpdate: function () {
      if (isBlank(vm.splitOrderModel.modelName)) {
        alert("模板名称不能为空！");
        return;
      }
      var url = vm.splitOrderModel.seq ? "system/splitOrderModel/update" : "system/splitOrderModel/add";
      if (vm.splitOrderModel.seq) {
        var data = {
          seq: vm.splitOrderModel.seq,
          modelName: vm.splitOrderModel.modelName,
          isDefault: vm.splitOrderModel.isDefault
        }
      } else {
        var data = {
          modelName: vm.splitOrderModel.modelName,
          isDefault: vm.splitOrderModel.isDefault
        }
      }
      $.ajax({
        type: "POST",
        url: baseURL + url,
        data: data,
        enctype: 'multipart/form-data',
        cache: false,
        success: function (r) {
          if (r.code === 0) {
            alert('操作成功');
            vm.reload();
          } else {
            alert(r.msg);
          }
        }
      });
    },
    reload: function (event) {
      vm.splitOrderModel = {};
      vm.showList = true;
      vm.showStepPage = false;
      vm.editStep = {};
      $("#jqGrid").jqGrid('setGridParam', {}).trigger("reloadGrid");
    },
    addSplitStep: function () {
      layer.open({
        type: 1,
        offset: '50px',
        skin: 'layui-layer-molv',
        title: "选择属性",
        area: ['600px', '450px'],
        shade: 0,
        shadeClose: false,
        content: jQuery("#sxSelectDialog"),
        btn: ['确定', '取消'],
        btn1: function (index) {
          var stepData = {};
          var periodValue = $("#Period_Seq").val();
          if (periodValue) {
            stepData.periodSeq = periodValue.join(',');
          }

          var categoryValue = $("#Category_Seq").val();
          if (categoryValue) {
            stepData.categorySeq = categoryValue.join(',');
          }

          var shoesBrandValue = $("#ShoesBrand").val();
          if (shoesBrandValue) {
            stepData.shoesBrand = shoesBrandValue.join(',');
          }

          for (var sxIndex = 1; sxIndex <= 20; sxIndex++) {
            var sxValue = $("#SX" + sxIndex).val();
            if (sxValue) {
              stepData["SX" + sxIndex] = sxValue.join(',');
            }
          }

          stepData.seq = vm.editStep.seq;
          stepData.modelSeq = vm.editStep.modelSeq;


          $.ajax({
            type: "POST",
            url: baseURL + "system/splitOrderModel/addOrUpdateModelDetail",
            data: stepData,
            enctype: 'multipart/form-data',
            cache: false,
            success: function (r) {
              if (r.code === 0) {
                $("#Period_Seq").val([]).trigger('change');
                $("#Category_Seq").val([]).trigger('change');
                $("#ShoesBrand").val([]).trigger('change');
                $(".sx_select").val([]).trigger('change');
                layer.close(index);
                vm.editStep.seq = undefined;
                modelDetailEdit(vm.editStep.modelSeq);
              } else {
                alert(r.msg);
              }
            }
          });


        },
        btn2: function () {
          $("#Period_Seq").val([]).trigger('change');
          $("#Category_Seq").val([]).trigger('change');
          $("#ShoesBrand").val([]).trigger('change');
          $(".sx_select").val([]).trigger('change');
        }
      });
    },
    editSplitStep: function (index) {
      var sxList = vm.steps[index].sxList;
      console.log(sxList);
      for (var sxIndex = 0; sxIndex < sxList.length; sxIndex++) {
        var id = sxList[sxIndex].id;
        var values = sxList[sxIndex].code.split(',');
        $('#' + id).val(values).trigger('change');
      }

      vm.editStep.seq = vm.steps[index].seq;
      vm.addSplitStep();
    },
    removeSplitStep: function (modelSeq, seq) {
      confirm('确认要删除此步骤吗？删除后不可恢复！', function () {
        $.get(baseURL + "system/splitOrderModel/deleteModelDetail?seq=" + seq, function (r) {
          if (r.code === 0) {
            modelDetailEdit(modelSeq);
          }
        });
      });
    },
    loadGoodsSX: function () {
      $.get(baseURL + "order/goods/getGoodsSXOption", function (r) {
        if (r.code === 0 || r.code === '0') {
          vm.sxArray = r.result;
        }
        setTimeout(function () {
          $('.sx_select').select2();
        }, 100);

      });
    },
    loadGoodsBrand: function () {
      $.get(baseURL + "order/goods/shoesBrand", function (r) {
        if (r.code === 0 || r.code === '0') {
          vm.shoesBrands = r.brands;
        }
        setTimeout(function () {
          $('#ShoesBrand').select2();
        }, 100);

      });
    },
    loadPeriod: function () {
      $.get(baseURL + "order/goods/periods", function (r) {
        var objPeriods = r.periods;
        var arrays = [];
        for (var year in objPeriods) {
          arrays = arrays.concat(objPeriods[year]);
        }
        vm.periods = arrays;
        setTimeout(function () {
          $('#Period_Seq').select2();
        }, 100);
      });
    },
    loadCategory: function () {
      // 加载分类树
      $.get(baseURL + "system/goodsCategory/list", function (r) {
        vm.categoryArray = r.list;
        setTimeout(function () {
          $('#Category_Seq').select2();
        }, 100);
      });
    },
  },
  created: function () {
    this.loadPeriod();
    this.loadCategory();
    this.loadGoodsSX();
    this.loadGoodsBrand();
  }
});

function lineEdit(seq) {
  $.get(baseURL + "system/splitOrderModel/edit?seq=" + seq, function (r) {
    vm.showList = false;
    vm.title = "修改模板";
    vm.splitOrderModel = r.splitOrderModel;
  });
}

function del(seq) {
  confirm('确定要删除选中的记录？', function () {
    $.get(baseURL + "system/splitOrderModel/delete?seq=" + seq, function (r) {
      if (r.code === 0) {
        alert('删除成功');
        vm.reload();
      } else {
        alert("删除失败");
      }
    });
  });
}

function modelDetailEdit(seq) {
  $.get(baseURL + "system/splitOrderModel/editModelDetail?seq=" + seq, function (r) {
    if (r.code === 0) {
      vm.showStepPage = true;
      vm.showList = false;
      vm.steps = r.modelDetail;
      vm.editStep.modelSeq = seq;
    }
  });
}

