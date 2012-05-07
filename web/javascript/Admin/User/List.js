Ext.define("CM.Admin.User.List", {
    extend: "Ext.grid.Panel",

    initComponent: function() {
        this.userStore = new Ext.data.Store({
            proxy: {
                type: 'ajax',
                reader: {
                    type: 'json',
                    root: 'data'
                },
                api: {
                    create  : '/action/user/create',
                    read    : '/action/user/get',
                    update  : '/action/user/update',
                    destroy : '/action/user/delete'
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
            fields: [ 'id', 'fullname', 'email', {
                name: 'role',
                convert: function(value, record) {
                    return value == 1 ? 'Student' : value == 2 ? 'Professor' : 'Administrator';
                }
            }]
        });

        Ext.apply(this, {
            title: "User List",
            id: 'User-List-panel',
            store: this.userStore,
            border: false,
            listeners: {
                itemdblclick: this.onItemDblClick
            },
            columns: [{
                header: 'Full Name',
                dataIndex: 'fullname',
                flex: 1
            }, {
                header: 'Email Address',
                dataIndex: 'email',
                flex: 1
            }, {
                header: 'Role',
                dataIndex: 'role',
                flex: 1
            }],
            fbar: [{
                xtype: 'button',
                text: 'Remove User',
                handler: this.deleteUser
            }, {
                xtype: 'button',
                text: 'Add User',
                handler: this.onAddClick
            }]
        });

        this.callParent(arguments);
    },

    onItemDblClick: function(view, record) {
        var roleStore = new Ext.data.Store({
            fields: ['value', 'name'],
            data : [
                { value: 1, name: 'Student'       },
                { value: 2, name: 'Professor'     },
                { value: 3, name: 'Administrator' }
            ]
        });

        new Ext.window.Window({
            layout: 'fit',
            height: 150,
            width: 300,
            items: [{
                xtype: 'form',
                url: '/action/user/update',
                border: false,
                items: [{
                    xtype: 'hidden',
                    name: 'id',
                    value: record.data.id
                }, {
                    fieldLabel: 'Full Name',
                    xtype: 'textfield',
                    name: 'fullname',
                    value: record.data.fullname
                },{
                    fieldLabel: 'Role',
                    xtype: 'combo',
                    name: 'role',
                    store: roleStore,
                    displayField: 'name',
                    valueField: 'value',
                    editable: false,
                    allowBlank: false,
                    queryMode: 'local',
                    value: record.data.role == 'Administrator' ? 3 : record.data.role == 'Professor' ? 2 : 1
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
        if (!PageGlobals.contentPanel.getChildByElement('User-Form-panel')) {
            PageGlobals.contentPanel.add(new CM.Admin.User.Form({ url: '/action/user/create' }));
        }

        PageGlobals.contentPanel.getLayout().setActiveItem('User-Form-panel');
    },

    beforeShow: function() {
        this.userStore.load();
    },

    deleteUser: function(button) {
        var grid = button.up('grid'),
            selected = grid.selModel.selected.items[0];

        if (selected.data.id != SessionGlobals.id) {
           grid.userStore.remove(selected);
           grid.userStore.sync();
        }
    }
});