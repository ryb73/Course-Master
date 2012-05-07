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
            listeners: {
                itemdblclick: this.onItemDblClick
            },
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
    
    onItemDblClick: function(view, record) {

        var profs = new Ext.data.Store({
            fields: ['value', 'name'],
            data : [
                { value: 5, name: 'John Boyland' },
                { value: 6, name: 'Jayson Rock' },
                { value: 7, name: 'Joseph Bockhorst' },
                { value: 8, name: 'Mukal Goyal' },
                { value: 9, name: 'Hossein Hosseini' }
            ]
        });

        new Ext.window.Window({
            layout: 'fit',
            items: [{
                xtype: 'form',
                url: '/action/course/update',
                border: false,
                items: [{
                    xtype: 'hidden',
                    name: 'id',
                    value: record.data.id
                }, {
                    fieldLabel: 'Course Name',
                    xtype: 'textfield',
                    name: 'name',
                    value: record.data.name
                }, {
                    fieldLabel: 'Department',
                    xtype: 'textfield',
                    name: 'dept',
                    value: record.data.dept
                }, {
                    fieldLabel: 'Professor',
                    xtype: 'combo',
                    name: 'role',
                    store: profs,
                    displayField: 'name',
                    valueField: 'value',
                    editable: false,
                    allowBlank: false,
                    queryMode: 'local',
                    value: record.data.prof == 'John Boyland' ? 5 : record.data.prof == 'Jayson Rock' ? 6 : record.data.prof == 'Joseph Bockhorst' ? 7 : record.data.prof == 'Mukal Goyal' ? 8 : 9
                }, {
                    fieldLabel: 'Course Number',
                    name: 'num',
                    xtype:'textfield',
                    value: record.data.dept
                }, {
                   fieldLabel: 'Section Number',
                    name: 'sect',
                    xtype:'textfield',
                    value: record.data.sect
                }, {
                    fieldLabel: 'Credits',
                    name: 'cred',
                    xtype:'textfield',
                    value: record.data.cred
                }, {
                    fieldLabel: 'Semester',
                    name: 'sem',
                    xtype:'textfield',
                    value: record.data.sem
                }],
                bbar: [{
                    xtype: 'button',
                    text: 'Update',
                    handler: function() {
                        this.up('form').getForm().submit({
                            success: function(form, action) {
                               Ext.Msg.alert('Success', 'Your entry was successfully stored.');
                            },
                            failure: function(form, action) {
                                Ext.Msg.alert('Failed', action.result.errors.message);
                            }
                        });
                    }
                }]
            }]
        }).show();
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