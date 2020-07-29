var faceIcon = ["[微笑]", "[嘻嘻]", "[哈哈]", "[可爱]", "[可怜]", "[挖鼻]", "[吃惊]", "[害羞]", "[挤眼]", "[闭嘴]", "[鄙视]", "[爱你]", "[泪]", "[偷笑]", "[亲亲]", "[生病]", "[太开心]", "[白眼]", "[右哼哼]", "[左哼哼]", "[嘘]", "[衰]", "[委屈]", "[吐]", "[哈欠]", "[抱抱]", "[怒]", "[疑问]", "[馋嘴]", "[拜拜]", "[思考]", "[汗]", "[困]", "[睡]", "[钱]", "[失望]", "[酷]", "[色]", "[哼]", "[鼓掌]", "[晕]", "[悲伤]", "[抓狂]", "[黑线]", "[阴险]", "[怒骂]", "[互粉]", "[心]", "[伤心]", "[猪头]", "[熊猫]", "[兔子]", "[ok]", "[耶]", "[good]", "[NO]", "[赞]", "[来]", "[弱]", "[草泥马]", "[神马]", "[囧]", "[浮云]", "[给力]", "[围观]", "[威武]", "[奥特曼]", "[礼物]", "[钟]", "[话筒]", "[蜡烛]", "[蛋糕]"];
var Layim, rongCloudInstance, imUserData, extraMsg;
$(function () {
  $.get(baseURL + 'system/user/chatInit', function (r) {
    imUserData = r.data;
    extraMsg = {seq: imUserData.mine.id, userName: imUserData.mine.username, headImg: imUserData.mine.avatar};
    var myId = imUserData.mine.id;
    var historyUser = [];
    if (localStorage) {
      var layimStorage = JSON.parse(localStorage.getItem("layim"));
      if (layimStorage) {
        var myChatHistory = layimStorage[myId];
        if (myChatHistory) {
          for (var key in myChatHistory.history) {
            historyUser.push({id: myChatHistory.history[key].id, time: myChatHistory.history[key].historyTime});
          }
        }
      }
    }

    $.get(baseURL + 'system/chat/history?users=' + encodeURIComponent(JSON.stringify(historyUser)), function (r) {
      var chatHistory = r.history;
      if (chatHistory && layimStorage && layimStorage[myId] && layimStorage[myId].chatlog) {
        $.each(chatHistory, function (i, item) {
          $.each(item, function (j, jtem) {
            jtem.content = findFace(jtem.content);
          });
        });
        layimStorage[myId].chatlog = chatHistory;
        localStorage.setItem("layim", JSON.stringify(layimStorage));
      }
      layimInit();
    });

    // 初始化客户列表
    var groupList = imUserData.group;
    if (!groupList || groupList.length <= 0) {
      $('#KFLB').append("<div style='color: grey;margin: 20px 10px;'>暂无其他客服</div>");
    } else {
      $.each(groupList, function (index, item) {
        var kefuLine = "<div class='kefu_name' style='padding: 10px;' data-id='" + item.id + "' data-name='" + item.groupname + "' data-avatar='" + item.avatar + "'>"
          + '<img src="' + item.avatar + '" style="width: 40px;height: 40px;border-radius: 100%;margin-right: 10px;">'
          + item.groupname
          + "</div>";

        $('#KFLB').append(kefuLine);
      });
    }

    $('#KFLB .kefu_name').bind('click', function () {
      $('#KFLB .kefu_name').removeClass("selected");
      $(this).addClass("selected");
    });
  });
});

var kefuChangeMsg;

function layimInit() {
  layui.use('layim', function (layim) {
    Layim = layim;
    layim.config({
      brief: false, //是否简约模式（如果true则不显示主面板）
      title: '客服小e',
      min: true, //页面打开的时候是否最小化
      minRight: '150px',
      notice: true,
      isfriend: true,
      isgroup: false,
      copright: true,
      voice: false,
      init: imUserData,
      tool: [{
        alias: 'kefu', //工具别名
        title: '客服转接', //工具名称
        icon: '&#xe641;' //工具图标，参考图标文档
      }]
      //msgbox: layui.cache.dir + 'css/modules/layim/html/msgbox.html' //消息盒子页面地址，若不开启，剔除该项即可
      //find: layui.cache.dir + 'css/modules/layim/html/find.html' //发现页面地址，若不开启，剔除该项即可
      //chatLog: layui.cache.dir + 'css/modules/layim/html/chatlog.html'
    });

    //监听自定义工具栏点击，以添加代码为例
    layim.on('tool(kefu)', function (insert, send, obj) { //事件中的tool为固定字符，而code则为过滤器，对应的是工具别名（alias）
      layer.open({
        type: 1,
        title: '客服转接',
        area: ['400px', '300px'],
        content: $('#KFLB'),
        btn: ['确定', '取消'],
        closeBtn: 0,
        yes: function (index) {
          var kefuSelect = $('#KFLB .kefu_name.selected');
          if (kefuSelect.length <= 0) {
            alert("未选择客服");
          } else {
            var dataId = $(kefuSelect[0]).attr("data-id");
            var dataName = $(kefuSelect[0]).attr("data-name");
            var dataAvatar = $(kefuSelect[0]).attr("data-avatar");
            layer.close(index);
            kefuChangeMsg = {id: dataId, name: dataName, avatar: dataAvatar};
            $('#KFLB .kefu_name').removeClass("selected");
            insert("[客服转接]:" + dataName);
            send();
          }
        },
        btn2: function (index) {
          layer.close(index);
          $('#KFLB .kefu_name').removeClass("selected");
        }
      });
      console.log(this); //获取当前工具的DOM对象
      console.log(obj); //获得当前会话窗口的DOM对象、基础信息*/
    });

    // 发送消息
    layim.on('sendMessage', function (res) {
      var messageFrom = res.mine;
      var messageTo = res.to;
      console.log(res);
      var conversationType = RongIMLib.ConversationType.PRIVATE;
      var targetId = messageTo.id + "";
      if (messageFrom.content.indexOf('[客服转接]:') > -1) {
        var id = parseInt(messageFrom.content.replace('[客服转接]:', ''));
        //var msg = new RongIMClient.RegisterMessage.KefuMessage({id: id});'RC:KefuMessage'
        var msg = new RongIMLib.TextMessage({content: kefuChangeMsg, extra: extraMsg});
        try {
          rongCloudInstance.sendMessage(conversationType, targetId, msg, {
            onSuccess: function (message) {
              console.log(message);
              kefuChangeMsg = undefined;
            }
          });
        } catch (e) {
          console.error(e);
          kefuChangeMsg = undefined;
        }
      } else {
        var msg = new RongIMLib.TextMessage({content: messageFrom.content, extra: extraMsg});
        console.log(msg);
        try {
          rongCloudInstance.sendMessage(conversationType, targetId, msg, {
            onSuccess: function (message) {
              console.log(message);
              // 发送成功后回传应用服务器
              $.post(baseURL + 'system/chat/upload', {
                from: messageFrom.id,
                to: messageTo.id,
                content: replaceFace(messageFrom.content)
              }, function (r) {

              });
            }
          });
        } catch (e) {
          console.error(e);
        }
      }
    });
  });
}

function connectRongCloud(appKey, userToken) {
  var params = {
    appKey: appKey,
    token: userToken
  };
  var userId = "";
  var callbacks = {
    getInstance: function (instance) {
      rongCloudInstance = instance;

      //自定义客服转接消息
      // var messageName = "KefuMessage"; // 消息名称。
      // var objectName = "RC:KefuChange"; // 消息内置名称，请按照此格式命名。
      // var mesasgeTag = new RongIMLib.MessageTag(false, false);// 消息是否保存是否计数，true true 保存且计数，false false 不保存不计数。
      // var propertys = ["id"]; // 消息类中的属性名。
      // RongIMClient.registerMessageType(messageName, objectName, mesasgeTag, propertys);
    },
    getCurrentUser: function (userInfo) {
      console.log(userInfo);
    },
    receiveNewMessage: function (message) {
      var sendUserId = message.senderUserId;
      var messageMine = (sendUserId == extraMsg.seq);
      var userInfo;
      if (messageMine) {
        userInfo = findUserInfo(message.targetId);
      } else {
        userInfo = findUserInfo(sendUserId);
      }
      var messageContent;
      if (message.objectName === "RC:TxtMsg") {
        messageContent = findFace(message.content.content);
      } else if (message.objectName === "RC:ImgMsg") {
        messageContent = ("img[" + message.content.imageUri + "]");
      } else if (message.objectName === "RC:ImgTextMsg") {
        var shoesImage = JSON.parse(message.content.extra).goodsShortData.image;
        messageContent = ("img[" + shoesImage + "]");
      } else {
        messageContent = "暂不支持此类型消息";
      }
      console.log(message.content.extra);
      var localMessage = {
        username: userInfo.username || "",
        avatar: userInfo.avatar || "",
        content: messageContent,
        id: message.senderUserId,//消息的来源ID（如果是私聊，则是用户id，如果是群聊，则是群组id）
        type: "friend",
        cid: message.messageId,//消息id
        mine: messageMine,
        fromid: message.senderUserId,//消息的发送者id（比如群组中的某个消息发送者）
        timestamp: message.sentTime
      };
      console.log(localMessage);
      Layim && Layim.getMessage(localMessage);
    }
  };
  init(params, callbacks);
}

function init(params, callbacks, modules) {
  var appKey = params.appKey;
  var token = params.token;
  var navi = params.navi || "";

  modules = modules || {};
  var RongIMLib = modules.RongIMLib || window.RongIMLib;
  var RongIMClient = RongIMLib.RongIMClient;
  var protobuf = modules.protobuf || null;

  var config = {};

  //私有云切换navi导航，私有云格式 '120.92.10.214:8888'
  if (navi !== "") {
    config.navi = navi;
  }

  //私有云切换api,私有云格式 '172.20.210.38:81:8888'
  var api = params.api || "";
  if (api !== "") {
    config.api = api;
  }

  //support protobuf url + function
  if (protobuf != null) {
    config.protobuf = protobuf;
  }

  var dataProvider = null;
  var imClient = params.imClient;
  if (imClient) {
    dataProvider = new RongIMLib.VCDataProvider(imClient);
  }
  RongIMLib.RongIMClient.init(appKey, dataProvider, config);

  var instance = RongIMClient.getInstance();

  // 连接状态监听器
  RongIMClient.setConnectionStatusListener({
    onChanged: function (status) {
      // console.log(status);
      switch (status) {
        case RongIMLib.ConnectionStatus["CONNECTED"]:
        case 0:
          console.log("连接成功");
          callbacks.getInstance && callbacks.getInstance(instance);
          break;

        case RongIMLib.ConnectionStatus["CONNECTING"]:
        case 1:
          console.log("连接中");
          break;

        case RongIMLib.ConnectionStatus["DISCONNECTED"]:
        case 2:
          console.log("当前用户主动断开链接");
          break;

        case RongIMLib.ConnectionStatus["NETWORK_UNAVAILABLE"]:
        case 3:
          console.log("网络不可用");
          break;

        case RongIMLib.ConnectionStatus["CONNECTION_CLOSED"]:
        case 4:
          console.log("未知原因，连接关闭");
          break;

        case RongIMLib.ConnectionStatus["KICKED_OFFLINE_BY_OTHER_CLIENT"]:
        case 6:
          console.log("用户账户在其他设备登录，本机会被踢掉线");
          break;

        case RongIMLib.ConnectionStatus["DOMAIN_INCORRECT"]:
        case 12:
          console.log("当前运行域名错误，请检查安全域名配置");
          break;
      }
    }
  });

  /*
  文档：http://www.rongcloud.cn/docs/web.html#3、设置消息监听器
  注意事项：
    1：为了看到接收效果，需要另外一个用户向本用户发消息
    2：判断会话唯一性 ：conversationType + targetId
    3：显示消息在页面前，需要判断是否属于当前会话，避免消息错乱。
    4：消息体属性说明可参考：http://rongcloud.cn/docs/api/js/index.html
  */
  RongIMClient.setOnReceiveMessageListener({
    // 接收到的消息
    onReceived: function (message) {
      //play();
      // 判断消息类型
      console.log("新消息: " + message.targetId);
      console.log(message);
      callbacks.receiveNewMessage && callbacks.receiveNewMessage(message);
    }
  });

  //开始链接
  RongIMClient.connect(token, {
    onSuccess: function (userId) {
      callbacks.getCurrentUser && callbacks.getCurrentUser({userId: userId});
      console.log("链接成功，用户id：" + userId);
    },
    onTokenIncorrect: function () {
      console.log('token无效');
    },
    onError: function (errorCode) {
      console.log(errorCode);
    }
  }, '');
}

function findUserInfo(userId) {
  var friends = imUserData.friend;
  for (var userGroupIndex = 0; userGroupIndex < friends.length; userGroupIndex++) {
    var list = friends[userGroupIndex].list;
    for (var userIndex = 0; userIndex < list.length; userIndex++) {
      var user = list[userIndex];
      if (user.id == userId) {
        return user;
      }
    }
  }

  return {};
}

function findFace(message) {
  var newmessage = message.replace(/\[/g, '{').replace(/\]/g, '}');
  $.each(faceIcon, function (i, faceItem) {
    var faceReg = faceItem.replace(/\[/g, '{').replace(/\]/g, '}');
    var reg = new RegExp(faceReg, 'g');
    newmessage = newmessage.replace(reg, 'face' + faceReg);
  });
  newmessage = newmessage.replace(/{/g, '[').replace(/}/g, ']');
  return newmessage;
}

function replaceFace(message) {
  var newmessage = message.replace(/\[/g, '{').replace(/\]/g, '}');
  $.each(faceIcon, function (i, faceItem) {
    var faceReg = faceItem.replace(/\[/g, '{').replace(/\]/g, '}');
    var reg = new RegExp('face' + faceReg, 'g');
    newmessage = newmessage.replace(reg, faceReg);
  });
  newmessage = newmessage.replace(/{/g, '[').replace(/}/g, ']');
  return newmessage;
}
