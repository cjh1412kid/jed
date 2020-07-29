$(function() {
	$("#jqGrid")
			.jqGrid(
					{
						url : baseURL + 'system/agent/list',
						datatype : "json",
						colModel : [
								{
									label : '代理商名称',
									name : 'agentName',
									width : 100,
									align : "center"
								},
								{
									label : '备注',
									name : 'remark',
									width : 250,
									align : "center"
								},
								{
									label : '建立时间',
									name : 'inputTime',
									width : 200,
									align : "center"
								},
								{
									label : '操作',
									name : 'createDate',
									width : 70,
									align : "center",
									formatter : function(cellvalue, options,
											rowObject) {
										var detail = '<button onclick="lineEdit('
												+ rowObject.seq
												+ ')" class="operation-btn-security" ">编辑</button>'
												+ '<button class="operation-btn-dangery" onclick="del('
												+ rowObject.seq
												+ ')">删除</button>';
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
		agent : {}
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
			var url = vm.agent.seq ? "system/agent/update"
					: "system/agent/save";
			var formData = new FormData();
			if (vm.agent.seq) {
				formData.append("seq", vm.agent.seq);
			}
			formData.append("agentName", vm.agent.agentName);
			formData.append("remark", vm.agent.remark);
			$.ajax({
				type : "POST",
				url : baseURL + url,
				contentType : false,
				processData : false,
				data : formData,
				enctype : 'multipart/form-data',
				cache : false,
				success : function(r) {
					if (r.code === 0 && r.result == 1) {
						alert('操作成功');
						vm.reload();
					} else {
						alert(r.msg);
					}
				}
			});
		},
		validator : function() {
			if (isBlank(vm.agent.agentName)) {
				alert("代理名称不能为空");
				return true;
			}
			if (isBlank(vm.agent.remark)) {
				alert("描述不能为空");
				return true;
			}
		},
		reload : function(event) {
			vm.agent = {};
			vm.showList = true;
			$("#jqGrid").jqGrid('setGridParam', {}).trigger("reloadGrid");
		},
	}
});

function lineEdit(seq) {
	$.get(baseURL + "system/agent/edit?seq=" + seq, function(r) {
		vm.showList = false;
		vm.title = "修改";
		vm.agent = r.agent;
	});
}
function del(seq) {
	$.get(baseURL + "system/agent/delete?seq=" + seq, function(r) {
		if (r.result == 1) {
			vm.reload();
		}
	});
}
