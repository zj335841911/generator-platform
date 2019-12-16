layui.use(['table', 'admin', 'ax'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;

    /**
     * 医院标签表管理
     */
    var Tag = {
        tableId: "tagTable"
    };

    /**
     * 初始化表格的列
     */
    Tag.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: '主键'},
            {field: 'tagName', sort: true, title: '标签名字'},
            {align: 'center', toolbar: '#tableBar', title: '操作'}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Tag.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        table.reload(Tag.tableId, {where: queryData});
    };

    /**
     * 弹出添加对话框
     */
    Tag.openAddDlg = function () {
        window.location.href = Feng.ctxPath + '/tag/add';
    };

    /**
     * 导出excel按钮
     */
    Tag.exportExcel = function () {
        var checkRows = table.checkStatus(Tag.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    /**
     * 点击编辑
     *
     * @param data 点击按钮时候的行数据
     */
    Tag.openEditDlg = function (data) {
        window.location.href = Feng.ctxPath + '/tag/edit?id=' + data.id;
    };

    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    Tag.onDeleteItem = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/tag/delete", function (data) {
                Feng.success("删除成功!");
                table.reload(Tag.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id", data.id);
            ajax.start();
        };
        Feng.confirm("是否删除?", operation);
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Tag.tableId,
        url: Feng.ctxPath + '/tag/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: Tag.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Tag.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Tag.openAddDlg();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Tag.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Tag.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            Tag.openEditDlg(data);
        } else if (layEvent === 'delete') {
            Tag.onDeleteItem(data);
        }
    });
});
