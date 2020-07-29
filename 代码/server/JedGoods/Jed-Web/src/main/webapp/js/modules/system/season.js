$(function() {
	$("#jqGrid")
			.jqGrid(
					{
						url : baseURL + 'system/season/list',
						datatype : "json",
						colModel : [
								{
									label : '季节名称',
									name : 'seasonName',
									width : 250,
									align : "center"
								},
								{
									label : '创建时间',
									name : 'inputTime',
									width : 200,
									align : "center"
								},
								{
									label : '操作',
									name : '',
									width : 70,
									align : "center",
									formatter : function(cellvalue, options,
											rowObject) {
										var detail = '<button onclick="lineEdit(' + rowObject.seq + ')" class="operation-btn-security" ">编辑</button>'
												+ '<button class="operation-btn-dangery" onclick="del(' + rowObject.seq + ')">删除</button>';
										return detail;
									}
								} ],
						height : 'auto',
						rowNum : 10,
						rowList : [ 10, 30, 50 ],
						rownumbers : true,
						rownumWidth : 25,
						autowidth : true,
						multiselect : false,
						pager : "#jqGridPager",
						jsonReader : {
							root : "page.list",
							page : "page.currPage",
							total : "page.totalPage",
							records : "page.totalCount"
						},
						prmNames : {
							page : "page",
							rows : "limit",
							order : "order"
						},
						gridComplete : function() {
							// 隐藏grid底部滚动条
							$("#jqGrid").closest(".ui-jqgrid-bdiv").css({
								"overflow-x" : "hidden"
							});
						}
					});
});

var vm = new Vue({
	el : '#rrapp',
	data : {
		showList : true,
		title : '',
		goodsSeason : {}
	},
	methods : {
		add : function() {
			vm.reload();
			vm.showList = false;
		},
		saveOrUpdate : function() {
			if (vm.validator()) {
				return;
			}
			var url = vm.goodsSeason.seq ? "system/season/update" : "system/season/add";

			$.ajax({
				type : "POST",
				url : baseURL + url,
				data : JSON.stringify(vm.goodsSeason),
				contentType : "application/json",
				cache : false,
				success : function(r) {
					if (r.code === 0) {
						alert(r.msg);
						vm.reload();
					} else {
						alert(r.msg);
					}
				}
			});
		},
		validator : function() {
			if (isBlank(vm.goodsSeason.seasonName)) {
				alert("季节名称不能为空！");
				return true;
			}
		},
		reload : function(event) {
			vm.goodsSeason = {};
			vm.showList = true;
			$("#jqGrid").jqGrid('setGridParam', {}).trigger("reloadGrid");
		},
	}
});

function lineEdit(seq) {
	$.get(baseURL + "system/season/edit?seq=" + seq, function(r) {
		vm.showList = false;
		vm.title = "修改";
		vm.goodsSeason = r.goodsSeason;
	});
}
function del(seq) {
	confirm('确定要删除？', function () {
		$.get(baseURL + "system/season/delete?seq=" + seq, function(r) {
			if (r.code == 0) {
				vm.reload();
			} else {
				alert(r.msg);
			}
	
		});
	});
}