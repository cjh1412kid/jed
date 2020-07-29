//生成菜单
var menuItem = Vue.extend({
  name: 'menu-item',
  props: {item: {}, index: 0},
  template: [
    '<li :class="{active: (item.type===0 && index === 0)}">',
    '<a v-if="item.type === 0" href="javascript:;">',
    '<i v-if="item.icon != null" :class="item.icon"></i>',
    '<span>{{item.name}}</span>',
    '<i class="fa fa-angle-left pull-right"></i>',
    '</a>',
    '<ul v-if="item.type === 0" class="treeview-menu" style="margin-top: -2px;">',
    '<menu-item :item="item" :index="index" v-for="(item, index) in item.list"></menu-item>',
    '</ul>',
    '<a v-if="item.type === 1" :href="\'#\'+item.url">' +
    '<i v-if="item.icon != null" :class="item.icon"></i>' +
    '<i v-else class="fa fa-circle-o"></i> {{item.name}}' +
    '</a>',
    '</li>'
  ].join('')
});

//iframe自适应
$(window).on('resize', function () {
  var $content = $('.content');
  $content.height($(this).height() - 155);
  $content.find('iframe').each(function () {
    $(this).height($content.height());
  });
}).resize();

//注册菜单组件
Vue.component('menuItem', menuItem);

var router = new Router();

var vm = new Vue({
  el: '#rrapp',
  data: {
    user: {},
    menuList: {},
    main: "main.html",
    form: {
      password: '',
      newPassword: '',
      newPassword2: '',
    },
    navTitle: "欢迎页"
  },
  methods: {
    getMenuList: function () {
      $.getJSON(baseURL + "system/menu/nav", function (r) {
        vm.menuList = r.menuList;
        window.permissions = r.permissions;
        //路由
        routerList(router, vm.menuList);
      });
    },
    getUser: function () {
      $.getJSON(baseURL + "system/user/info", function (r) {
        vm.user = r.user;
        try {
          websocketInit();
          connectRongCloud(r.rongCloudKey, r.user.rongCloudToken);
        } catch (e) {
          console.error && console.error(e);
        }
      });
    },
    updatePassword: function () {
      layer.open({
        type: 1,
        skin: 'layui-layer-molv',
        title: "修改密码",
        area: ['555px', '318px'],
        shadeClose: false,
        content: jQuery("#passwordLayer"),
        btn: ['修改', '取消'],
        btn1: function (index) {
          //判断输入的两次新密码是否一致
          var pwd1 = vm.form.newPassword;
          var pwd2 = vm.form.newPassword2;
          if (pwd1 != pwd2) {
            alert("两次新密码输入不一致！");
            vm.form.newPassword2 = '';
            $(".reNewPassword").focus();
          } else {
            $.ajax({
              type: "POST",
              url: baseURL + "system/user/password",
              dataType: "json",
              contentType: "application/json",
              data: JSON.stringify(vm.form),
              success: function (r) {
                if (r.code == 0) {
                  layer.close(index);
                  layer.alert('修改成功', function () {
                    location.reload();
                  });
                } else {
                  layer.alert(r.msg);
                }
              }
            });
          }
        }
      });
    },
    logout: function () {
      $.ajax({
        type: "POST",
        url: baseURL + "system/logout",
        dataType: "json",
        success: function (r) {
          //删除本地token
          localStorage.removeItem("token");
          //跳转到登录页面
          location.href = 'login.html';
        }
      });
    }
  },
  created: function () {
    this.getMenuList();
    this.getUser();
  },
  updated: function () {
    router.start();
  }
});


function routerList(router, menuList) {
  for (var key in menuList) {
    var menu = menuList[key];
    if (menu.type == 0) {
      routerList(router, menu.list);
    } else if (menu.type == 1) {
      if (menu.url == 'druid/sql.html') {
        menu.url = baseURL + 'druid/sql.html';
      }
      router.add('#' + menu.url, function () {

        var url = window.location.hash;
        console.log(url);

        //替换iframe的url
        vm.main = url.replace('#', '');

        //导航菜单展开
        $(".treeview-menu li").removeClass("active");
        $(".sidebar-menu li").removeClass("active");
        $("a[href='" + url + "']").parents("li").addClass("active");

        vm.navTitle = $("a[href='" + url + "']").text();
      });
    }
  }
}

var intervalId;
var orderSeq;
function websocketInit() {
  var socket = io.connect(webSocketUrl + '?companySeq=' + vm.user.companySeq + '&userSeq=' + vm.user.seq);

  //连接websocket
  socket.on('connect', function () {
    console.log("Client has connected to the server!");
  });

  //收到消息后具体操作
  socket.on('message', function (time, data, extraMap) {
    console.log(time);
    console.log(data);
    console.log(extraMap);
    orderSeq = extraMap.orderSeq;
    //关闭之前可能未关闭的提示窗
    closeNoticeBox();

    //设置title跑马灯展示
    var s = (data + " ").split("");

    function func() {
      s.push(s[0]); //方法可向数组的末尾添加一个或多个元素，并返回新的长度
      s.shift();// 去掉数组的第一个元素
      document.title = s.join("");
    }

    intervalId = setInterval(func, 200);//设置时间间隔运行

    //播放提示音
    playSound(extraMap.orderStatus);
    //弹出提示框
    showNoticeBox(data);
  });
}

function playSound(status) {
  var obj = document.getElementById("audioOrder");
  obj.src = "media/order" + status + ".mp3";
//  obj.load();
  obj.play();
}

function showNoticeBox(data) {
  $("#noticedata1").html(data);
  $("#noticebox1").animate({height: "show"}, 800);
}

function closeNoticeBox() {
  clearInterval(intervalId);
  document.title = "智慧零售后台管理系统";
  $("#noticebox1").animate({height: "hide"}, 800);
}

function toOrder() {
  window.location.href = "#modules/system/order_platform/order.html?orderSeq=" + orderSeq;
  clearInterval(intervalId);
  document.title = "智慧零售后台管理系统";
}

window.showLoading = function () {
  $('#my_loading_page').show();
};

window.hideLoading = function () {
  $('#my_loading_page').hide();
};
