$(function () {

  initJGrid();

  $('#config-demo').daterangepicker({
    singleDatePicker: true,
    showDropdowns: true,
    autoUpdateInput: true,
    timePicker24Hour: true,
    timePickerSeconds: true,
    timePicker: true,
    autoApply: true,
    locale: {
      format: 'YYYY/MM/DD HH:mm:ss',
      applyLabel: "应用",
      cancelLabel: "取消",
      customRangeLabel: '选择时间',
      daysOfWeek: ["日", "一", "二", "三", "四", "五", "六"],
      monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
      firstDay: 0
    },
  }).on('apply.daterangepicker', function (ev, picker) {
    vm.$set(vm.shop, "installDate", picker.startDate.format(picker.locale.format));
  });
});

function initJGrid() {
  $("#jqGrid").jqGrid({
    url: baseURL + 'system/shopManage/list',
    datatype: "json",
    colModel: [
      {label: 'seq', name: 'seq', align: "center", hidden: true, hidedlg: true, key: true},
      {label: '门店编号', name: 'id', width: 80, align: "center"},
      {label: '所属分公司', name: 'areaName', width: 120, align: "center"},
      {label: '所属区域', name: 'regionName', width: 120, align: "center"},
      {
        label: '门店类型', name: 'shopTypeFlag', width: 120, align: "center",
        formatter: function (cellvalue, options, rowObject) {
          if (cellvalue == 0) {
            return '直营';
          } else if (cellvalue == 1) {
            return '联营';
          } else if (cellvalue == 2) {
            return '加盟';
          }
        }
      },
      {label: '门店名称', name: 'name', width: 120, align: "center"},
      {label: '门店地址', name: 'address', width: 300, align: "center"},
      {label: '纬度', name: 'lat', width: 80, align: "center"},
      {label: '经度', name: 'lng', width: 80, align: "center"},
      {label: '备注', name: 'remark', width: 80, align: "center"},
      // {label: '安装时间', name: 'installDate', width: 105, align: "center"},
      // {label: '创建时间', name: 'createDate', hidden: true, width: 80, align: "center"},
      {
        label: '操作', name: 'seq', width: 80, align: "center",
        formatter: function (cellvalue, options, rowObject) {

          var detail = '<button onclick="lineEdit(' + rowObject.seq + ')" class="operation-btn-security" ">编辑</button>'
            + '<button class="operation-btn-dangery" onclick="lineDelete(' + rowObject.seq + ')">删除</button>';
          return detail;
        }
      }],
    viewrecords: true,
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
}

function getGridParam(param) {
  return $("#jqGrid").jqGrid('getGridParam', param);
}

var vm = new Vue({
  el: '#rrapp',
  data: {
    showList: true,
    title: '',
    shop: {
      shopImage: '',
      typeOfStore: "直营",
      shopTypeFlag: 0
    },
    typeOfStoreList: [{
      typeOfStore: "直营",
      shopTypeFlag: 0,
    }/*, {
      typeOfStore: "联营",
      shopTypeFlag: 1
    }, {
      typeOfStore: "加盟",
      shopTypeFlag: 2
    }*/],
    areaList: [],
    regionList:[],
  },
  methods: {
    initShopData: function () {
      vm.shop = {
        shopImage: '',
        typeOfStore: "直营",
        shopTypeFlag: 0
      }
    },
    getAreaList: function (item) {
      $.get(baseURL + "system/shopManage/areaList", function (r) {
    		console.log(r)
        vm.areaList = r.areaList;
      });
    },
    getRegionList: function (item) {
        $.get(baseURL + "system/shopManage/regionList", function (r) {
        	console.log(r)
          vm.regionList = r.regionList;
        });
      },
    categoryChoose: function (item) {
      vm.$set(vm.shop, "areaName", item.name);
      vm.$set(vm.shop, "areaSeq", item.seq);
    },
    regionChoose: function (item) {
        vm.$set(vm.shop, "regionName", item.name);
        vm.$set(vm.shop, "regionSeq", item.seq);
      },
    categoryChoosetOpic: function (item) {
      vm.$set(vm.shop, 'typeOfStore', item.typeOfStore);
      vm.$set(vm.shop, "shopTypeFlag", item.shopTypeFlag);
    },
    reload: function (event) {
      vm.showList = true;
      // var page = getGridParam('page');
      $("#jqGrid").jqGrid('setGridParam', {
        datatype: 'json',
        page: 1
      }).trigger("reloadGrid");
    },

    add: function () {
      vm.reload();
      vm.showList = false;
      vm.initShopData();
      vm.title = '新增门店';
      vm.shop.installDate = new Date().format('yyyy/MM/dd HH:mm:ss');
    },

    initMap: function () {
      if (isBlank(vm.shop.address)) {
        alert("门店地址不能为空");
        return;
      }

      //打开地图窗口
      layer.open({
        type: 1,
        offset: '50px',
        skin: 'layui-layer-molv',
        title: "地图选点",
        area: ['818px', '460px'],
        shade: 0,
        shadeClose: false,
        content: jQuery("#BMapLayer"),
        btn: ['确定', '取消'],
        btn1: function (index) {
          vm.$set(vm.shop, "lng", marker.getPosition().lng);
          vm.$set(vm.shop, "lat", marker.getPosition().lat);
          layer.close(index);
        }
      });

      // 新建百度地图
      var map = new BMap.Map("allmap");
      var point = new BMap.Point(120.706362, 28.001972); //默认地址"温州市"
      map.centerAndZoom(point, 13);
      map.enableScrollWheelZoom(true);

      //添加覆盖物
      var marker = new BMap.Marker(point);
      marker.enableDragging();
      //覆盖物标签
      var label = new BMap.Label(marker.getPosition().lng + "," + marker.getPosition().lat, {offset: new BMap.Size(-8, -18)});
      label.setStyle({
        maxWidth: "none"
      });
      marker.setLabel(label);

      map.addOverlay(marker);

      //单击设置覆盖物位置
      map.addEventListener("click", function (e) {
        marker.setPosition(e.point);
        label.setContent(e.point.lng + "," + e.point.lat);
      });

      //拖拽覆盖物过程事件
      marker.addEventListener("dragging", function (e) {
        label.setContent(e.point.lng + "," + e.point.lat)
      })

      //定位到输入的门店地址
      var myGeo = new BMap.Geocoder();
      myGeo.getPoint(vm.shop.address, function (point) {
        if (point) {
          map.centerAndZoom(point, 16);
          marker.setPosition(point);
          label.setContent(point.lng + "," + point.lat);
        } else {
          alert("没有解析到结果，请手动选择！");
        }
      }, "");

    },
    update: function () {
      var shopSeq = getSelectedRow();
      if (shopSeq == null) {
        return;
      }
      lineEdit(shopSeq);
    },
    del: function () {
      var shopSeq = getSelectedRow();
      if (shopSeq == null) {
        return;
      }
      lineDelete(shopSeq);
    },
    handleShopFileChange: function (event) {
      // 通过DOM取文件数据
      var file = event.target.files[0];

      if (!file) {
        return;
      }
      vm.shop.shopImageFile = file;
      var size = Math.floor(vm.shop.shopImageFile.size / 1024 / 1024);

      if (size > 10) {
        alert("文件超过10M啦！");
        return;
      }
      vm.shopImgPreview(vm.shop.shopImageFile);
    },
    shopImgPreview: function (file) {
      if (!file || !window.FileReader) return;
      if (/^image/.test(file.type)) {
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onloadend = function () {
          vm.$set(vm.shop, "shopImage", this.result);
        }
      }
    },
    saveOrUpdate: function () {
      if (vm.validator()) {
        return;
      }

      var formData = new FormData();
      var url = "system/shopManage/save";
      if (vm.shop.seq) {
        formData.append("seq", vm.shop.seq);
        url = "system/shopManage/update";
      }
      formData.append("id", vm.shop.id);
      formData.append("name", vm.shop.name);
      formData.append("address", vm.shop.address);
      formData.append("lat", vm.shop.lat);
      formData.append("lng", vm.shop.lng);
      // formData.append("installDate", vm.shop.installDate);
      if (!isBlank(vm.shop.remark)) {
        formData.append("remark", vm.shop.remark);
      }
      formData.append("shopTypeFlag", vm.shop.shopTypeFlag);
      formData.append("areaSeq", vm.shop.areaSeq);
      formData.append("regionSeq", vm.shop.regionSeq);
      if (typeof vm.shop.shopImageFile == 'object') {
        //门店图片文件发生更新
        formData.append("shopImageFile", vm.shop.shopImageFile);
      }

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
        },
        error: function () {
          parent.window.hideLoading();
          alert('服务器出错啦');
        }
      });
    },
    validator: function () {
      if (isBlank(vm.shop.id)) {
        alert("门店编号不能为空");
        return true;
      }
      if (isBlank(vm.shop.name)) {
        alert("门店名称不能为空");
        return true;
      }
      if (isBlank(vm.shop.address)) {
        alert("门店地址不能为空");
        return true;
      }
      if (isBlank(vm.shop.lng) || isBlank(vm.shop.lat)) {
        alert("经纬度不能为空");
        return true;
      }
      if (!(/^\d+(\.\d+)?$/.test(vm.shop.lng)) || !(/^\d+(\.\d+)?$/.test(vm.shop.lat))) {
        alert("经纬度格式错误");
        return true;
      }
/*      if (isBlank(vm.shop.installDate)) {
        alert("安装时间不能为空");
        return true;
      }*/
      /*if (isBlank(vm.shop.shopImage)) {
        alert("门店图片不能为空");
        return true;
      }*/
      if (!vm.shop.shopTypeFlag && vm.shop.shopTypeFlag != 0) {
        alert("请选择门店类型");
        return true;
      }
      if (isBlank(vm.shop.areaSeq)) {
        alert("请选择所在区域");
        return true;
      }

    }
  }
});

function lineEdit(seq) {
  $.get(baseURL + "system/shopManage/edit?seq=" + seq, function (r) {
    vm.showList = false;
    vm.initShopData();
    vm.title = "修改";
    vm.shop = r.shop;
    if (r.shop.shopTypeFlag == 0) {
      vm.shop.typeOfStore = '直营';
    } else if (r.shop.shopTypeFlag == 1) {
      vm.shop.typeOfStore = '联营';
    } else if (r.shop.shopTypeFlag == 2) {
      vm.shop.typeOfStore = '加盟';
    }
  });
}

function lineDelete(seq) {
  confirm('确定要删除选中的门店？', function () {
    $.ajax({
      type: "GET",
      url: baseURL + "system/shopManage/del?seq=" + seq,
      success: function (r) {
        if (r.code === 0) {
          layer.msg('删除成功');
          vm.reload();
        } else {
          alert(r.msg);
        }
      }
    });
  });
}
