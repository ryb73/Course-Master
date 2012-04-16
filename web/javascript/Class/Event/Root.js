Ext.define("CM.Event.Root", {
    extend: "Ext.grid.Panel",

    initComponent: function() {
        var store = new Ext.data.Store({
            proxy: {
                type: 'ajax',
                url: '/service/events/course',
                reader: {
                    type: 'json',
                    root: 'data'
                },
                extraParams: {
                    courseId: this.courseId
                }
            },
            autoLoad: true,
            fields: [ 'id', 'name', 'start', 'end', 'descr' ]
        });

        Ext.apply(this, {
            title: this.class + ' Syllabus',
            id: this.class + '-event-root',
            store: store,
            columns: [{
                text: 'Title',
                dataIndex: 'name',
                flex: 1
            }, {
                text: 'Description',
                dataIndex: 'descr',
                flex: 2
            }, {
                text: 'Due Date',
                dataIndex: 'end',
                flex: 1
            }]
        });

        this.callParent(arguments);
    }
});