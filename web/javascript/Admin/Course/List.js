Ext.define("CM.Admin.Course.List", {
    extend: "Ext.grid.Panel",

    initComponent: function() {
        this.courseStore = new Ext.data.Store({
            proxy: {
                type: 'ajax',
                reader: {
                    type: 'json',
                    root: 'data'
                },
                api: {
                    create  : '/action/course/create',
                    read    : '/action/course/get',
                    update  : '/action/course/update',
                    destroy : '/action/course/delete'
                },
                reader: {
                    type: 'json',
                    root: 'data'
                },
                writer: new Ext.data.JsonWriter({
                    root: 'data',
                    encode: true
                })
            },
            fields: [ 'id', 'name', 'prof', 'dept', 'num', 'sect', 'cred', 'sem' ]
        });

        Ext.apply(this, {
            title: "Course List",
            id: 'Course-List-panel',
            store: this.courseStore,
            border: false,
            columns: [{
                header: 'Course Title',
                dataIndex: 'name',
                flex: 1
            }, {
                header: 'Professor',
                dataIndex: 'prof',
                flex: 1
            }, {
                header: 'Department',
                dataIndex: 'dept',
                flex: 1
            }, {
                header: 'Number',
                dataIndex: 'num',
                flex: 1
            }],
            fbar: [{
                xtype: 'button',
                text: 'Remove Course',
                handler: this.deleteCourse
            }, {
                xtype: 'button',
                text: 'Add Course',
                handler: this.onAddClick
            }]
        });

        this.callParent(arguments);
    },

    onAddClick: function() {
        if (!PageGlobals.contentPanel.getChildByElement('Course-Form-panel')) {
            PageGlobals.contentPanel.add(new CM.Admin.Course.Form({ url: '/action/course/create' }));
        }

        PageGlobals.contentPanel.getLayout().setActiveItem('Course-Form-panel');
    },

    beforeShow: function() {
        this.courseStore.load();
    },

    deleteCourse: function(button) {
        var grid = button.up('grid'),
            selected = grid.selModel.selected.items[0];

        if (selected.data.id != SessionGlobals.id) {
           grid.courseStore.remove(selected);
           grid.courseStore.sync();
        }
    }
});