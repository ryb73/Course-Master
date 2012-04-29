Ext.define('CM.Discussion.Root', {
    extend: 'Ext.grid.Panel',

    initComponent: function() {

        var discussionBoards = Ext.create('Ext.data.Store', {
            fields: [ 'name', 'topicCount', 'id' ],
            proxy: {
                type: 'ajax',
                url: '/service/discussion/get-boards',
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
            store: discussionBoards,
            id: this.class + '-board-root',
            title: this.class + ' Discussion Board',
            listeners: {
                select: this.onSelect,
                itemdblclick: this.onItemDblClick
            },
            columns: {
                items: [{
                    text: 'Board Name',
                    flex: 1,
                    dataIndex: 'name'
                }, {
                    text: 'Topics',
                    width: 40,
                    align: 'right',
                    dataIndex: 'topicCount'
                }],
                defaults: {
                    draggable: false,
                    resizable: false,
                    hideable: false,
                    sortable: false
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
                            text: 'Add Board',
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
        if (!PageGlobals.contentPanel.getChildByElement(this.class + "-thread-list")) {
            PageGlobals.contentPanel.add(new CM.Discussion.ThreadList({ class: this.class, courseId: this.courseId, boardId: record.get("id"), boardName: record.get("name") }));
        }

        PageGlobals.contentPanel.getLayout().setActiveItem(this.class + '-thread-list');
    },

    addBoard: function() {
        var statusOptions = Ext.create('Ext.data.Store', {
            fields: ['id', 'name'],
            data: [
                { 'id': '1', 'name': 'Open' }
            ]
        });

        Ext.create('widget.window', {
            title: 'Add Discussion Board',
            closable: true,
            width: 300,
            height: 150,
            layout: 'fit',
            items: {
                xtype: 'form',
                url: '/service/discussion/create-board',

                layout: {
                    type: 'vbox',
                    align: 'center',
                    pack: 'center'
                },

                items: [
                    {
                        xtype: 'textfield',
                        name: 'boardName',
                        fieldLabel: 'Board Name',
                        allowBlank: false
                    },{
                        xtype: 'combobox',
                        name: 'status',
                        fieldLabel: 'Status',
                        editable: false,
                        allowBlank: false,
                        store: statusOptions,
                        queryMode: 'local',
                        displayField: 'name',
                        valueField: 'id'
                    },{
                        xtype: 'hidden',
                        name: 'course',
                        value: this.instance.courseId
                    }
                ],

                buttons: [{
                    text: 'Create Board',
                    formBind: true,
                    disabled: true,
                    handler: function() {
                        var form = this.up('form').getForm();
                        if(form.isValid()) {
                            form.submit({
                                success: function() { Ext.Msg.alert("success","success"); },
                                failure: function() { Ext.Msg.alert("Error","Unable to add discussion board."); }
                            });
                        }
                    }
                }]
            }
        }).show();
    }
});