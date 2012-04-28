Ext.define('CM.Dropbox.DRoot', {
    extend: 'Ext.grid.Panel',

    initComponent: function() {

        var dropboxes = Ext.create('Ext.data.Store', {
            fields: [ 'id', 'name', 'ext', 'start', 'end', 'disp' ],
            /*data: [
                { id: '1', name: 'Board 1', postCount: 12 },
                { id: '2', name: 'Board 2', postCount: 3 }
            ]*/
            proxy: {
                type: 'ajax',
                url: '/service/get-dropbox',
                extraParams: {
                    userId: SessionGlobals.id,
                    courseId: this.courseId
                },
                reader: {
                    type: 'json',
                    root: 'data'
                }
            },
            autoLoad: true
        });

        Ext.apply(this, {
            border: false,
            store: dropboxes,
            id: this.class + '-dropbox-root',
            title: this.class + ' Dropbox',
            listeners: {
                select: this.onSelect,
                itemdblclick: this.onItemDblClick
            },
            columns: {
                items: [{
 					text: 'ID',
					width: 25,
//					align: 'left',
					flex:1,
					dataIndex: 'id' 
				}, {
					text: 'Name',
					width: 800,
//					align: 'center',
                    flex: 2,
                    dataIndex: 'name'
				}, {
					text: 'Description',
					width: 800,
					flex: 1,
//					align: 'right',
					dataIndex: 'ext' 
                }],
                defaults: {
                    draggable: false,
                    resizable: false,
                    hideable: false,
                    sortable: true
                }
            }
        });

        if(SessionGlobals.role == 2) {
            Ext.apply(this, {
                dockedItems: [{
                    xtype: 'toolbar',
                    dock: 'bottom',
                    ui: 'footer',
                    items: [
                        {
                            xtype: 'button',
                            text: 'Add Dropbox',
                            instance: this,
                            listeners: { click: this.addBoard }
                        }
                    ]
                }]
            });
        }

        this.callParent(arguments);
    },

    onSelect: function(rowModel, record) {
        console.log("Select fired: " + record.get("id"));
    },

    onItemDblClick: function(view, record) {
        console.log("Double clicked: " + record.get("id"));
    },

    addBoard: function() {
        var statusOptions = Ext.create('Ext.data.Store', {
            fields: ['id', 'name','ext'],
//            data: [
//                { 'id': '1', 'name': 'Open' }
//            ]
        });

        Ext.create('widget.window', {
            title: 'Add Dropbox',
            closable: true,
            width: 290,
            height: 270,
            layout: 'fit',
            items: {
                xtype: 'form',
                url: '/service/create-dropbox',  //TODO need name,course,descr

                layout: {
                    type: 'vbox',
					align : 'center',
					pack  : 'center',
//                    align: 'center',
//                    pack: 'center'
                },
				
                items: [
                    {
                        xtype: 'textfield',
                        name: 'name',
                        fieldLabel: 'Dropbox Name',
                        allowBlank: false
                    },{
						xtype: 'textarea',
                        name: 'ext',
                        fieldLabel: 'Description',
						height: 160,
                        allowBlank: false
/*                     },{
                        xtype: 'combobox',
                        name: 'status',
                        fieldLabel: 'Status',
                        editable: false,
                        allowBlank: false,
                        store: statusOptions,
                        queryMode: 'local',
                        displayField: 'name',
                        valueField: 'id' */

					},{
						xtype: 'hidden',
						name: 'course',
						value: this.instance.courseId
                    }
                ],

                buttons: [{
                    text: 'Create Dropbox',
                    formBind: true,
                    disabled: true,
                    handler: function() {
                        var form = this.up('form').getForm();
                        if(form.isValid()) {
                            form.submit({
                                success: function() { Ext.Msg.alert("Success","Dropbox has been added."); },
                                failure: function() { Ext.Msg.alert("Error","Unable to add dropbox."); }
                            });
                        }
                    }
                }]
            }
        }).show();
    }
});