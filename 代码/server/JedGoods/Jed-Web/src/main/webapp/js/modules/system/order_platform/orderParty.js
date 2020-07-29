$(function () {
  $("#jqGrid").jqGrid(
    {
      url: baseURL + 'order/orderParty/list',
      datatype: "json",
      mtype: "POST",
      postData: {
        'companyType': vm.companyType
      },
      colModel: [
        {
          label: '所属类型',
          name: 'attachTypeName',
          width: 120,
          align: "center"
        },
        {
          label: '所属公司/代理名称',
          name: 'attachCompanyName',
          width: 280,
          align: "center"
        },
        {
          label: '用户名',
          name: 'accountName',
          width: 85,
          align: "center"
        },
        {
          label: '昵称',
          name: 'userName',
          width: 85,
          align: "center"
        },
        {
          label: '账号创建时间',
          name: 'inputTime',
          width: 105,
          align: "center"
        },
        {
          label: '账号截止时间',
          name: 'effectiveDate',
          width: 105,
          align: "center"
        },

        {
          label: '操作',
          name: 'createDate',
          width: 130,
          align: "center",
          formatter: function (cellvalue, options, rowObject) {
            var detailOne = '';

            if (hasPermission("orderParty:update")) {
              detailOne += ('<button class="operation-btn-security" onclick="lineEdit(' + rowObject.seq + ')">修改</button>' + '&nbsp');
            }

            if (hasPermission("orderParty:disable")) {
              if (rowObject.isDisable == 0) {
                detailOne += ('<button class="operation-btn-warn" onclick="disable(' + rowObject.seq + ',' + rowObject.isDisable + ')">禁用</button>' + '&nbsp');
              }
              if (rowObject.isDisable == 1) {
                detailOne += ('<button class="operation-btn-warn" onclick="disable(' + rowObject.seq + ',' + rowObject.isDisable + ')">解禁</button>' + '&nbsp');
              }
            }

            if (hasPermission("orderParty:delete")) {
              detailOne += ('<button class="operation-btn-dangery" onclick="del(' + rowObject.seq + ')">删除</button>' + '&nbsp;');
            }
            return detailOne;
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
    branchOfficeName: "",
    agentName: "",
    confirmPassword: "",
    orderPartyDetail: {},
    showList: true,
    showorderParty: false,
    title: '',
    companyType: 2,
    restOfQuota: '',
    attachTypeArray: [{
      attachType: 1,
      attachTypeName: "直属工厂"
    }, {
      attachType: 2,
      attachTypeName: "分公司"
    }, {
      attachType: 3,
      attachTypeName: "代理商"
    }],
    saleTypeArray: [{
      saleType: 2,
      saleTypeName: "总代理"
    }, {
      saleType: 3,
      saleTypeName: "批发商"
    }, {
      saleType: 4,
      saleTypeName: "直营店"
    }],
    shopList: ''
  },
  methods: {
    attachTypeChoose: function (item) {
      vm.$set(vm.orderPartyDetail, "attachType", item.attachType);
      vm.$set(vm.orderPartyDetail, "attachTypeName", item.attachTypeName);

      //清除可能已经选择的数据
      vm.$set(vm.orderPartyDetail, "attachSeq", '');
      vm.branchOfficeName = '';
      vm.agentName = '';
      vm.$set(vm.orderPartyDetail, "saleType", '');
      vm.$set(vm.orderPartyDetail, "saleTypeName", '');

    },
    branchOfficeChoose: function (item) {
      vm.$set(vm.orderPartyDetail, "attachSeq", item.seq);
      vm.branchOfficeName = item.name;

      //清除可能已经选择的数据
      vm.$set(vm.orderPartyDetail, "saleType", '');
      vm.$set(vm.orderPartyDetail, "saleTypeName", '');
    },
    agentChoose: function (item) {
      vm.$set(vm.orderPartyDetail, "attachSeq", item.seq);
      vm.agentName = item.agentName;
    },
    saleTypeChoose: function (item) {
      vm.$set(vm.orderPartyDetail, "saleType", item.saleType);
      vm.$set(vm.orderPartyDetail, "saleTypeName", item.saleTypeName);
    },
    shopSelectEnter: function () {
      if (vm.orderPartyDetail.attachType == 2 && vm.orderPartyDetail.attachSeq) {
        this.getShopList(vm.orderPartyDetail.attachSeq);
      } else {
        this.getShopList('');
      }

      setTimeout(function () {
        $("#shopSeqs").select2();
        //编辑时初始化数据
        if (vm.orderPartyDetail.shopSeq) {
          $("#shopSeqs").val(vm.orderPartyDetail.shopSeq.split(',')).trigger('change');
        }
      }, 100);
    },

    loadRestOfQuota: function () {
      $.get(baseURL + "order/orderParty/restOfQuota", function (r) {
        vm.restOfQuota = r.restOfQuota;
        vm.reloadOrderParty();
      });
    },

    addOrderParty: function () {
      vm.reloadOrderParty();
      vm.showList = false;
      vm.showorderParty = true;
      vm.title = '添加账户';
    },

    getCompanyType: function (type) {
      vm.companyType = type;
      vm.reloadOrderParty();
    },

    reloadOrderParty: function () {
      // 初始化参数
      vm.branchOfficeName = "", vm.agentName = "", vm.showList = true;
      vm.showorderParty = false;
      vm.orderPartyDetail = {};
      setTimeout(function () {
        $("#jqGrid").jqGrid('setGridParam', {
          postData: {
            'companyType': vm.companyType
          }
        }).trigger('reloadGrid');
      }, 500);
    },

    saveOrUpdateOrderParty: function () {
      var formData = new FormData();
      if (isBlank(vm.orderPartyDetail.saleType)) {
        alert("请选择账号类型");
        return;
      }
      formData.append("saleType", vm.orderPartyDetail.saleType);
      if (isBlank(vm.orderPartyDetail.attachType)) {
        alert("请选择账号所属类型");
        return;
      }
      formData.append("attachType", vm.orderPartyDetail.attachType);
      if (isBlank(vm.orderPartyDetail.accountName)) {
        alert("用户名不能为空");
        return;
      }
      formData.append("accountName", vm.orderPartyDetail.accountName);
      if (isBlank(vm.orderPartyDetail.userName)) {
        alert("昵称不能为空");
        return;
      }
      formData.append("userName", vm.orderPartyDetail.userName);

      //新增，密码必填
      if (vm.orderPartyDetail.seq == null) {
        if (isBlank(vm.orderPartyDetail.password)) {
          alert("密码不能为空");
          return;
        }
        if (isBlank(vm.orderPartyDetail.confirmPassword)) {
          alert("确认密码不能为空");
          return;
        }
      } else {
        formData.append("seq", vm.orderPartyDetail.seq);
      }

      if (!isBlank(vm.orderPartyDetail.password) && vm.orderPartyDetail.password != vm.orderPartyDetail.confirmPassword) {
        alert("两次密码不一致");
        return;
      }
      formData.append("password", vm.orderPartyDetail.password);

      if (vm.orderPartyDetail.attachType == 2) {
        if (!vm.orderPartyDetail.attachSeq) {
          alert("请选择分公司");
          return;
        }
      }

      if (vm.orderPartyDetail.attachType == 3) {
        if (!vm.orderPartyDetail.attachSeq) {
          alert("请选择代理");
          return;
        }
      }

      if (vm.orderPartyDetail.saleType == 4 && vm.orderPartyDetail.attachType != 3) {
        var shopSeqs = $("#shopSeqs").val();
        console.log(shopSeqs);
        if (!shopSeqs) {
          alert("请选择直营店");
          return;
        }
        if (vm.orderPartyDetail.attachType == 2 && shopSeqs.length > 1) {
          alert("分公司只能选择一个直营店");
          return;
        }
        formData.append("shopSeq", shopSeqs.toString());
      }

      /* 当添加的时候是直属工厂的时候 关联的Seq给定值 */
      if (vm.orderPartyDetail.attachType == 1) {
        formData.append("attachSeq", 0);
      } else {
        formData.append("attachSeq", vm.orderPartyDetail.attachSeq);
      }

      var url = vm.orderPartyDetail.seq ? "order/orderParty/updateOrderParty" : "order/orderParty/addOrderParty";

      parent.window.showLoading();
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
            parent.window.hideLoading();
            alert('操作成功', function () {
              vm.reloadOrderParty();
              vm.loadRestOfQuota();
            });
          } else {
            parent.window.hideLoading();
            alert(r.msg);
            vm.reloadOrderParty();
            vm.loadRestOfQuota();
          }
        }
      });
      vm.reloadOrderParty();
    },

    /* 获得直营店的列表 */
    getShopList: function (attachSeq) {
      $.ajax({
        type: "get",
        url: baseURL + "order/orderParty/shopList?attachSeq=" + attachSeq,
        async: false,
        success: function (r) {
          vm.shopList = r.list;
        }
      });


    },
    /* 获得代理的列表 */
    agentList: function () {
      $.get(baseURL + "order/orderParty/agentList", function (r) {
        vm.agentList = r.agentList;
      });
    },
    /* 获得分公司的列表 */
    branchOfficeList: function () {
      $.get(baseURL + "order/orderParty/branchOfficeList", function (r) {
        vm.branchOfficeList = r.branchOfficeList;
      });
    },
    /* 同步客户 */
    syncFromErp: function () {
      parent.window.showLoading();
      $.get(baseURL + "system/erp/sync/kehu", function (r) {
        parent.window.hideLoading();
        alert("同步结束", function () {
          location.reload();
        });
      });
    }
  },
  created: function () {
    this.loadRestOfQuota();
    this.agentList();
    this.branchOfficeList();
  }
});

function del(id) {
  confirm('确定要删除选中的账号？', function () {
    $.get(baseURL + "order/orderParty/deleteOrderParty?seq=" + id, function (r) {
      if (r.code === 0) {
        alert('操作成功', function () {
          vm.reloadOrderParty();
        });
      } else {
        alert(r.msg);
      }
    });
  });
}

function lineEdit(seq) {
  $.get(baseURL + "order/orderParty/edit?seq=" + seq, function (r) {
    var orderPartyDetail = r.user;
    vm.orderPartyDetail = orderPartyDetail;

    if (vm.orderPartyDetail.attachType == 1) {
      vm.orderPartyDetail.attachTypeName = "直属工厂";
    }
    if (vm.orderPartyDetail.attachType == 2) {
      vm.orderPartyDetail.attachTypeName = "分公司";
      vm.branchOfficeName = vm.orderPartyDetail.attachCompanyName
    }
    if (vm.orderPartyDetail.attachType == 3) {
      vm.orderPartyDetail.attachTypeName = "代理商";
      vm.agentName = vm.orderPartyDetail.attachCompanyName
    }

    if (vm.orderPartyDetail.saleType == 2) {
      vm.orderPartyDetail.saleTypeName = "总代理";
    } else if (vm.orderPartyDetail.saleType == 3) {
      vm.orderPartyDetail.saleTypeName = "批发商";
    } else if (vm.orderPartyDetail.saleType == 4) {
      vm.orderPartyDetail.saleTypeName = "直营店";
    }

    vm.showList = false;
    vm.showorderParty = true;
    vm.title = "修改账户";

  });
}

function disable(seq, disable) {
  $.get(baseURL + "order/orderParty/disable?seq=" + seq + '&disable=' + disable, function (r) {
    if (r.code === 0) {
      alert('操作成功', function () {
        vm.reloadOrderParty();
      });
    } else {
      alert(r.msg);
    }
  });
}
