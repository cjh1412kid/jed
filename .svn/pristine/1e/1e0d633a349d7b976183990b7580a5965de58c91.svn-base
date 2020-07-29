// 基于准备好的dom，初始化echarts实例

let myChart1;

$(function () {
  myChart1 = echarts.init(document.getElementById("echart1"));
  initOrderChart(myChart1, "chart/getOrderData", {dateType: 2, changeVal: 0});
})

let vm = new Vue({
  el: "#chart1",
  data: {
    /*1 年，2 月，3 周，0 自定义*/
    dateType: 2,
    /*时间单位的增减*/
    changeVal: 0,
    weekday: ["周一", "周二", "周三", "周四", "周五", "周六", "周日"],

    startTime: "",
    endTime: "",
  },
  methods: {
    changeDateType: function () {
      this.changeVal = 0;
      /*切换年月周初始化操作*/
      if (this.dateType > 0) {
        initOrderChart(myChart1, "chart/getOrderData", {dateType: this.dateType, changeVal: 0});
      }else{
        console.log("执行了没？")
        setTimeout(timer,500);
        // timer();
      }
    },
    handleChart: function (val, event) {
      var obj = event.target;
      /*按钮没有.active 则改变按钮组样式*/
      if (obj.className.search("active") == -1) {
        $(obj).addClass("active").siblings().removeClass("active");
      }

      //对时间单位进行加减
      if (val == 1) {
        this.changeVal++;
      } else if (val == -1) {
        this.changeVal--;
      } else if (val == 0) {
        this.changeVal = 0;
      }

      var postdata = {
        dateType: this.dateType,
        changeVal: this.changeVal,
      }
      initOrderChart(myChart1, "chart/getOrderData", postdata);

    },
    selectTime: function (event) {
      let _target = $(event.target);


    },
    selectData: function () {

      if (isBlank(this.startTime) || isBlank(this.endTime)) return;

      var postdata = {
        dateType: this.dateType,
        startTime: this.startTime,
        endTime: this.endTime
      }
      initOrderChart(myChart1, "chart/getOrderData", postdata);

    },
  },
})

/*初始化订单图表*/
function initOrderChart(echart, uri, postdata) {

  $.post(baseURL + uri, postdata, function (r) {
    // console.log(r)
    if (r.code == 0) {
      let list = r.result.list;

      let xAxisData = [];
      let seriesData = [];

      if (vm.dateType == 3) {
        for (let i in list) {
          xAxisData.push(list[i].day + " (" + vm.weekday[i] + ")");
          seriesData.push(list[i].orderCount);
        }
      } else if (vm.dateType == 2 || vm.dateType == 0) {
        for (let i in list) {
          xAxisData.push(list[i].day);
          seriesData.push(list[i].orderCount);
        }
      } else if (vm.dateType == 1) {
        for (let i in list) {
          xAxisData.push(list[i].datestr);
          seriesData.push(list[i].orderCount);
        }
      }


      let titleText = r.result.title;
      let titleSubtext = r.result.subTitle;
      let legendData = r.result.legendData;
      let seriesName = r.result.seriesName;

      // 指定图表的配置项和数据
      let option = {
        title: {
          text: titleText,
          subtext: titleSubtext
        },
        tooltip: {
          trigger: 'axis'
        },
        toolbox: {
          show: true,
          feature: {
            dataView: {show: false, readOnly: false},
            magicType: {show: true, type: ['bar', 'line']},
            restore: {show: true},
            saveAsImage: {show: true}
          }
        },
        legend: {
          data: legendData
        },
        xAxis: {
          data: xAxisData
        },
        yAxis: {},
        series: [{
          name: seriesName,
          type: 'bar',
          data: seriesData,
          // color: ['#dd6b66', '#759aa0', '#e69d87', '#8dc1a9', '#ea7e53', '#eedd78', '#73a373', '#73b9bc', '#7289ab', '#91ca8c', '#f49f42'],
        }],
        // 全局调色盘。
        // color: ['#c23531', '#2f4554', '#61a0a8', '#d48265', '#91c7ae', '#749f83', '#ca8622', '#bda29a', '#6e7074', '#546570', '#c4ccd3'],
      };

      // 使用刚指定的配置项和数据显示图表。
      echart.setOption(option);

      //图标内容大小自适应
      window.onresize = echart.resize;
    }
  })

}


/*var myChart2 = echarts.init(document.getElementById('echart2'));
$.get(baseURL + "chart/getSalesDataWithWeek?shoeSeq=" + 63, function (r) {

  // console.log(r)
  if (r.code == 0) {

    var list = r.result;

    var xAxisData = [];
    var seriesData1 = [];
    var seriesData2 = [];

    for (var i in list) {
      xAxisData.push(list[i].datestr);
      seriesData1.push(list[i].orderCount);
      seriesData2.push(list[i].cartCount);
    }
    // 指定图表的配置项和数据
    var option2 = {
      title: {
        text: '某款鞋子最近7天的订单数量图',
        subtext: '子标题'
      },
      tooltip: {
        trigger: 'axis'
      },
      toolbox: {
        show: true,
        feature: {
          dataView: {show: true, readOnly: false},
          magicType: {show: true, type: ['line', 'bar']},
          restore: {show: true},
          saveAsImage: {show: true}
        }
      },
      calculable: true,
      legend: {
        data: ['订单量', '购物车量']
      },
      xAxis: {
        type: "category",
        data: xAxisData
      },
      yAxis: [
        {
          type: 'value',
        }
      ],
      series: [{
        name: '订单量',
        type: 'bar',
        data: seriesData1
      }, {
        name: '购物车量',
        type: 'bar',
        data: seriesData2
      }]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart2.setOption(option2);
  }
})*/


function timer() {
  console.log("执行了",$(".startTime"))
  $(".startTime").daterangepicker(
    {
      singleDatePicker: true,
      showDropdowns: true,
      autoUpdateInput: false,
      timePicker24Hour: true,
      timePicker: false,
      locale: {
        format: "YYYY-MM-DD",
        separator: " - ",
        applyLabel: "应用",
        cancelLabel: "取消",
        resetLabel: "重置",
        daysOfWeek: ["日", "一", "二", "三", "四", "五", "六"],
        monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"]
      }

    }
  ).on('cancel.daterangepicker', function (ev, picker) {
    vm.startTime=""
  }).on('apply.daterangepicker', function (ev, picker) {
    vm.startTime=picker.startDate.format('YYYY-MM-DD');
    vm.selectData();
  });

  $(".endTime").daterangepicker(
    {
      singleDatePicker: true,
      showDropdowns: true,
      autoUpdateInput: false,
      timePicker24Hour: true,
      timePicker: false,
      locale: {
        format: "YYYY-MM-DD",
        separator: " - ",
        applyLabel: "应用",
        cancelLabel: "取消",
        resetLabel: "重置",
        daysOfWeek: ["日", "一", "二", "三", "四", "五", "六"],
        monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"]
      }

    }
  ).on('cancel.daterangepicker', function (ev, picker) {
    vm.selectData();
  }).on('apply.daterangepicker', function (ev, picker) {
    vm.endTime=picker.startDate.format('YYYY-MM-DD');
    vm.selectData();
  });
}
