$(function () {
  $("#jqGrid").jqGrid({
    url: baseURL + 'system/brand/list',
    datatype: "json",
    colModel: [
      {label: '公司名称', name: 'companyName', width: 100 ,align: "center"},
      {label: '公司描述', name: 'companyDescript', width: 200,align: "center"},
      {label: '公司地址', name: 'companyAddress', width: 150,align: "center"},
      {label: '品牌名称', name: 'brandName', width: 80 ,align: "center"},
      {
        label: '品牌图片', name: 'brandImage', width: 150, formatter: function (cellvalue) {
          var detail = '<image src="' + cellvalue + '" style="width: 200px;height: 70px;"/>';
          return detail;
        }
      },
      {
        label: '操作', name: '', width: 70,align: "center",
        formatter: function (cellvalue, options, rowObject) {
          var detail = '<button onclick="lineEdit()" class="operation-btn-security" ">编辑</button>';
          return detail;
        }
      }
    ],
    height : 'auto',
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
      $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
    }
  });
});

function getGridParam(param) {
  return $("#jqGrid").jqGrid('getGridParam', param);
}

var vm = new Vue({
  el: '#rrapp',
  data: {
    showList: true,
    title: '',
    companyBrand: {}
  },
  methods: {
    add: function () {
      var records = getGridParam('records');
      if (records > 0) {
        alert("只能创建一个品牌");
      } else {
        $.get(baseURL + "system/brand/company", function (r) {
          if (r.code === 0) {
            vm.showList = false;
            vm.title = "新增";
            vm.companyBrand = r.companyBrand;
          } else {
            alert("加载公司信息出错！");
          }
        });
      }
      // vm.reload();
    },
    reload: function (event) {
      vm.showList = true;
      var page = getGridParam('page');
      $("#jqGrid").jqGrid('setGridParam', {
        page: page
      }).trigger("reloadGrid");
    },
    handleFileChange: function (value) {
    	/* console.log(value); */
      var inputDOM = value.target;
      // 通过DOM取文件数据
      vm.companyBrand.brandImageFile = inputDOM.files[0];
      /* console.log(inputDOM.files[0]); */

      var size = Math.floor(vm.companyBrand.brandImageFile.size / 1024 / 1024);

      if (size > 10) {
        alert("文件超过10M啦！");
        return;
      }
      vm.imgPreview(vm.companyBrand.brandImageFile);
    },
    imgPreview: function (file) {
      if (!file || !window.FileReader) return;
      if (/^image/.test(file.type)) {
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onloadend = function () {
          vm.companyBrand.brandImage = this.result;
        }
      }
    },
    saveOrUpdate: function () {
      if (vm.validator()) {
        return;
      }

      var url = vm.companyBrand.brandSeq ? "system/brand/update" : "system/brand/save";
      var formData = new FormData();
      if (vm.companyBrand.brandImageFile) {
        formData.append("brandImageFile", vm.companyBrand.brandImageFile);
      }
      formData.append("companyName", vm.companyBrand.companyName);
      formData.append("companySeq", vm.companyBrand.companySeq);
      formData.append("companyDescript", vm.companyBrand.companyDescript);
      formData.append("companyAddress", vm.companyBrand.companyAddress);
      formData.append("brandName", vm.companyBrand.brandName);
      formData.append("brandDescript", vm.companyBrand.brandDescript);
      formData.append("brandSeq", vm.companyBrand.brandSeq);
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
            alert('操作成功', function () {
              vm.reload();
            });
          } else {
            alert(r.msg);
          }
        }
      });
    },
    validator: function () {
      if (isBlank(vm.companyBrand.companyName)) {
        alert("公司名称不能为空");
        return true;
      }
      if (isBlank(vm.companyBrand.companyDescript)) {
        alert("公司描述不能为空");
        return true;
      }
      if (isBlank(vm.companyBrand.companyAddress)) {
        alert("公司地址不能为空");
        return true;
      }
      if (isBlank(vm.companyBrand.brandName)) {
        alert("品牌名称不能为空");
        return true;
      }
      if (isBlank(vm.companyBrand.brandImage)) {
        if (!vm.companyBrand.brandImageFile) {
          alert("品牌图片不能为空");
          return true;
        }
      }
    }
  }
});

function lineEdit() {
  $.get(baseURL + "system/brand/edit", function (r) {
    vm.showList = false;
    vm.title = "修改";
    vm.companyBrand = r.companyBrand;
  });
}