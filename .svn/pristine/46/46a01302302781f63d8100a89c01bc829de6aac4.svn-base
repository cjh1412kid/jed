Date.prototype.format = function (fmt) {
  var o = {
    "M+": this.getMonth() + 1,                 // 月份
    "d+": this.getDate(),                    // 日
    "H+": this.getHours(),                   // 小时
    "m+": this.getMinutes(),                 // 分
    "s+": this.getSeconds(),                 // 秒
    "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
    "S": this.getMilliseconds()             // 毫秒
  };
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
  }
  for (var k in o) {
    if (new RegExp("(" + k + ")").test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    }
  }
  return fmt;
};

if ($.jgrid) {
// jqGrid的配置信息
  $.jgrid.defaults.width = 1000;
  $.jgrid.defaults.responsive = true;
  $.jgrid.defaults.styleUI = 'Bootstrap';
}

// 工具集合Tools
window.T = {};

// 获取请求参数
// 使用示例
// location.href = http://localhost/index.html?id=123
// T.p('id') --> 123;
var url = function (name) {
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
  var r = window.location.search.substr(1).match(reg);
  if (r != null) return unescape(r[2]);
  return null;
};
T.p = url;

var environment = "${profiles.active}";
var hostURL, webSocketUrl;
if (environment === 'dev') {
  hostURL = "http://localhost:8080/";
  webSocketUrl = "http://localhost:9093";
} else if (environment === 'localtest') {
  hostURL = "http://192.168.2.146:8181/";
  webSocketUrl = "http://192.168.2.250:9093";
} else if (environment === 'test') {
  hostURL = "http://119.3.150.141:8181/";
  webSocketUrl = "http://119.3.150.141:9093";
} else if (environment === 'prod') {
  hostURL = "http://117.48.157.6:8181/";
  webSocketUrl = "http://117.48.157.6:9093";
} else {
  hostURL = "http://localhost:8080/";
  webSocketUrl = "http://localhost:9093";
}

var baseURL = hostURL + "interface/";
var webBase = hostURL + "web/";

// 登录token
var token = localStorage.getItem("token");
if (token == 'null') {
  parent.location.href = webBase + 'login.html';
}

// jquery全局配置
$.ajaxSetup({
  dataType: "json",
  cache: false,
  headers: {
    "token": token
  },
  xhrFields: {
    withCredentials: true
  },
  complete: function (xhr) {
    // token过期，则跳转到登录页面
    if (xhr.responseJSON == null || xhr.responseJSON.code == 401) {
      parent.location.href = webBase + 'login.html';
    }
  }
});

// jqgrid全局配置
if ($.jgrid) {
  $.extend($.jgrid.defaults, {
    ajaxGridOptions: {
      headers: {
        "token": token
      }
    }
  });
}

// 权限判断
function hasPermission(permission) {
  if (window.parent.permissions.indexOf(permission) > -1) {
    return true;
  } else {
    return false;
  }
}

// 重写alert
window.alert = function (msg, callback) {
  parent.layer.alert(msg, function (index) {
    parent.layer.close(index);
    if (typeof(callback) === "function") {
      callback("ok");
    }
  });
}

// 重写confirm式样框
window.confirm = function (msg, callback) {
  parent.layer.confirm(msg, {btn: ['确定', '取消']},
    function () {// 确定事件
      parent.layer.closeAll('dialog');
      if (typeof(callback) === "function") {
        callback("ok");
      }
    });
}

// 选择一条记录
function getSelectedRow() {
  var grid = $("#jqGrid");
  var rowKey = grid.getGridParam("selrow");
  if (!rowKey) {
    alert("请选择一条记录");
    return;
  }

  return rowKey;
}

// 选择多条记录
function getSelectedRows() {
  var grid = $("#jqGrid");
  var rowKey = grid.getGridParam("selrow");
  if (!rowKey) {
    alert("请选择一条记录");
    return;
  }

  return grid.getGridParam("selarrrow");
}

// 判断是否为空
function isBlank(value) {
  return !value || !/\S/.test(value)
}

function getUUID() {
  var s = [];
  var hexDigits = "0123456789abcdef";
  for (var i = 0; i < 36; i++) {
    s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
  }
  s[14] = "4";
  s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);

  s[8] = s[13] = s[18] = s[23] = "-";

  var uuid = s.join("");
  return uuid;
}

// 阻止所有表单的enter键提交功能
window.onload = function () {
  var forms = document.getElementsByTagName("form");
  for (var index = 0; index < forms.length; index++) {
    forms[index].onkeypress = function (event) {
      var ev = window.event || event;
      if (ev.keyCode == 13 || ev.which == 13) {
        return false;
      }
    }
  }
}

//准确计算浮点数 +
function add(a, b) {
  var c, d, e;
  try {
    c = a.toString().split(".")[1].length;
  } catch (f) {
    c = 0;
  }
  try {
    d = b.toString().split(".")[1].length;
  } catch (f) {
    d = 0;
  }
  return e = Math.pow(10, Math.max(c, d)), (mul(a, e) + mul(b, e)) / e;
}

//准确计算浮点数 -
function sub(a, b) {
  var c, d, e;
  try {
    c = a.toString().split(".")[1].length;
  } catch (f) {
    c = 0;
  }
  try {
    d = b.toString().split(".")[1].length;
  } catch (f) {
    d = 0;
  }
  return e = Math.pow(10, Math.max(c, d)), (mul(a, e) - mul(b, e)) / e;
}

//准确计算浮点数 *
function mul(a, b) {
  var c = 0,
    d = a.toString(),
    e = b.toString();
  try {
    c += d.split(".")[1].length;
  } catch (f) {
  }
  try {
    c += e.split(".")[1].length;
  } catch (f) {
  }
  return Number(d.replace(".", "")) * Number(e.replace(".", "")) / Math.pow(10, c);
}

//准确计算浮点数 /
function div(a, b) {
  var c, d, e = 0,
    f = 0;
  try {
    e = a.toString().split(".")[1].length;
  } catch (g) {
  }
  try {
    f = b.toString().split(".")[1].length;
  } catch (g) {
  }
  return c = Number(a.toString().replace(".", "")), d = Number(b.toString().replace(".", "")), mul(c / d, Math.pow(10, f - e));
};


/**
 * 交换数组元素
 * @param arr 源数组
 * @param index1 添加项目的位置
 * @param index2 删除位置
 */
function swapArr(arr, index1, index2) {
  arr[index1] = arr.splice(index2, 1, arr[index1])[0];
  return arr;
}