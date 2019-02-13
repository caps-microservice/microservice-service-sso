$(document).ready(function () {
    getUserInfo();
    initTble();
});

(function($){
    $.fn.serializeJson=function(){
        var serializeObj={};
        var array=this.serializeArray();
        var str=this.serialize();
        $(array).each(function(){
            if(serializeObj[this.name]){
                if($.isArray(serializeObj[this.name])){
                    serializeObj[this.name].push(this.value);
                }else{
                    serializeObj[this.name]=[serializeObj[this.name],this.value];
                }
            }else{
                serializeObj[this.name]=this.value;
            }
        });
        return serializeObj;
    };
})(jQuery);

function getUserInfo() {
    $.ajax({
        url:"/getUserInfo",
        contentType:'application/json',
        success:function(result){
            console.log(result);
            if(result.status===200){
                $('#loginUser').html(result.data.phone);
            }else{
                alert(result.message)
            }
        }
    });
}

function initTble() {
    $('#myTable').bootstrapTable({
        method: 'get',
        url: '/pageData',
        striped: true,
        cache: false,
        pagination: true,
        pageSize: 10,
        pageList: [10, 25, 50, 100],
        sidePagination: "client",
        search: true,
        uniqueId: "id",
        clickToSelect: true,
        columns: [
            {
                field: 'title',
                title: '标题',
                align: 'center'
            },
            {
                field: 'updated',
                title: '时间',
                align: 'center'
            },

            {
                field: 'edit',
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    var s = '<button class="btn btn-success" onclick="create(' + row.id + ')">增加</button>';
                    var a = '<button class="btn btn-info" onclick="edit(' + row.id + ')">修改</button>';
                    var d = '<button class="btn btn-danger" onclick="del(' + row.id + ')">删除</button>';
                    return s+' '+a+' '+d;
                }
            }],
    });
}

function edit(id) {
    var row=$('#myTable').bootstrapTable('getRowByUniqueId', id);
    $('#myAjaxType').val("put");
    $('#myModalLabel').html("修改");
    $('#id').val(row.id);
    $('#title').val(row.title);
    $('#content').val(row.content);
    $('#created').val(row.created);
    $('#updated').val(row.updated);
    $('#editModal').modal('show');
}

function create() {
    $('#myAjaxType').val("post");
    $('#myModalLabel').html("添加");
    $('#id').val('');
    $('#title').val('');
    $('#content').val('');
    $('#created').val('');
    $('#updated').val('');
    $('#editModal').modal('show');
}

function save() {
    var json=$('#myForm').serializeJson();
    console.log(toJSONString(json));
    $.ajax({
        type:"post",
        url:"/save",
        contentType:'application/json',
        data:JSON.stringify(json),
        success:function(result){
            console.log(result);
            $('#editModal').modal('hide');
            $('#myTable').bootstrapTable('refresh');
            if(result.data==="update"){
                alert("修改成功")
            }else if (result.data==="insert"){
                alert("增加成功")
            }
        }
    });
}


function del(id) {
    console.log(id);
    $.ajax({
        url:"/selective?id="+id,
        contentType:'application/json',
        success:function(result){
            console.log(result);
            if(result.status===200){
                alert("删除成功")
                $('#myTable').bootstrapTable('refresh');
            }else{
                alert("删除失败")
            }
        }
    });
}

function toJSONString(jsonObj) {
    var jStr = "{";
    for(var item in jsonObj){
        jStr += "'"+item+"':'"+jsonObj[item]+"',";
    }
    return jStr.substring(0,jStr.length-1)+'}';
}

$("#logout").bind("click",function(){
    $.ajax({
        url:"/logout",
        contentType:'application/json',
        success:function(result){
            console.log(result);
            if(result.status===200){
                alert("注销成功")
                window.location.href = "/loginPage/"
            }else{
                alert("获取用户数据失败")
            }
        }
    });
});
