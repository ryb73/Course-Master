Ext.define('CM.Discussion.Root', {
    extend: 'Ext.grid.Panel',
    plugins: [
        Ext.create('Ext.grid.plugin.CellEditing', {
            clicksToEdit: 1,
            listeners: {
                edit: function(editor, e) {
                    var tmpForm = new Ext.form.Panel({
                        url: '/service/discussion/edit-board-status',
                        items: [{
                            xtype: 'hidden',
                            name: 'boardId',
                            value: e.record.get("id")
                        },{
                            xtype: 'hidden',
                            name: 'status',
                            value: e.record.get("status")
                        }]
                    });

                    tmpForm.submit({
                        success: function() { },
                        failure: function() { Ext.Msg.alert("Error", "Unable to change board status. Try refreshing the page."); }
                    });
                }
            }
        })
    ],

    initComponent: function() {

        var discussionBoards = Ext.create('Ext.data.Store', {
            fields: [ 'name', 'topicCount', 'id', 'status' ],
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

        var statusColumn;
        if(SessionGlobals.role == 2 /* Professor */) {
            statusColumn = {
                text: 'Status',
                width: 100,
                dataIndex: 'status',
                editor: {
                    xtype: 'combo',
                    store: PageGlobals.statusOptions,
                    displayField: 'name',
                    valueField: 'id',
                    editable: false,
                    allowBlank: false,
                    queryMode: 'local'
                },
                renderer: function(val){
                    var index = PageGlobals.statusOptions.findExact('id',val); 
                    if (index != -1){
                        return PageGlobals.statusOptions.getAt(index).get("name");
                    }
                }
            };
        } else {
            statusColumn = {
                text: 'Status',
                width: 100,
                dataIndex: 'status',
                renderer: function(val){
                    var index = PageGlobals.statusOptions.findExact('id',val); 
                    if (index != -1){
                        return PageGlobals.statusOptions.getAt(index).get("name");
                    }
                }
            };
        }

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
                }, statusColumn ],
                defaults: {
                    draggable: false,
                    resizable: false,
                    hideable: false,
                    sortable: false
                }
            },
            viewConfig: { emptyText: "There are no discussion boards for this class." }
        });

        if(SessionGlobals.role == 2) {
            Ext.apply(this, {
                bbar: [{
                    xtype: 'button',
                    text: 'Add Board',
                    instance: this,
                    listeners: { click: this.addBoard }
                }]
            });
        }

        this.callParent(arguments);
    },

    onSelect: function(rowModel, record) {
        console.log("Select fired: " + record.get("id"));
    },

    onItemDblClick: function(view, record) {
        var threadListPanel = PageGlobals.contentPanel.getChildByElement(this.class + "-thread-list");
        if (threadListPanel) {
            threadListPanel.destroy();
        }
        
        PageGlobals.contentPanel.add(new CM.Discussion.ThreadList({ class: this.class, courseId: this.courseId, boardId: record.get("id"),
            boardName: record.get("name"), boardStatus: record.get("status") }));

        PageGlobals.contentPanel.getLayout().setActiveItem(this.class + '-thread-list');
    },

    addBoard: function() {
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
                        store: PageGlobals.statusOptions,
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