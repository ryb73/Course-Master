Ext.define("CM.Discussion.ThreadList", {
    extend: 'Ext.grid.Panel',

    initComponent: function() {

        var topics = Ext.create('Ext.data.Store', {
            fields: [ 'id', 'name', 'postedBy', 'postedOn', 'replies' ],
            proxy: {
                type: 'ajax',
                url: '/service/discussion/get-topics',
                extraParams: {
                    userId: SessionGlobals.id,
                    boardId: this.boardId
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
            store: topics,
            id: this.class + '-thread-list',
            title: this.class + ' Discussion Board > ' + this.boardName,
            listeners: {
                select: this.onSelect,
                itemdblclick: this.onItemDblClick
            },
            columns: {
                items: [{
                    text: 'Name',
                    flex: 1,
                    dataIndex: 'name'
                }, {
                    text: 'Posted By',
                    width: 100,
                    dataIndex: 'postedBy'
                }, {
                    text: 'On',
                    width: 200,
                    dataIndex: 'postedOn'
                }, {
                    text: 'Replies',
                    width: 50,
                    dataIndex: 'replies'
                }],
                defaults: {
                    draggable: false,
                    resizable: false,
                    hideable: false,
                    sortable: false
                }
            }
        });
        
        Ext.apply(this, {
            bbar: [{
                xtype: 'button',
                text: 'Create Topic',
                instance: this,
                listeners: { click: this.createTopic }
            }]
        });
        
        this.callParent(arguments);
    },

    createTopic: function(view, record) {
        if (!PageGlobals.contentPanel.getChildByElement(this.class + "-create-topic")) {
            PageGlobals.contentPanel.add(new CM.Discussion.CreateTopic({ class: this.class, courseId: this.courseId, boardId: this.instance.boardId }));
        }

        PageGlobals.contentPanel.getLayout().setActiveItem(this.class + '-create-topic');
    },

    onSelect: function(rowModel, record) {
        console.log("Select fired: " + record.get("id"));
    },

    onItemDblClick: function(view, record) {
        if (!PageGlobals.contentPanel.getChildByElement(this.class + "-topic" + record.get("id"))) {
            PageGlobals.contentPanel.add(new CM.Discussion.Topic({ class: this.class, courseId: this.courseId, boardId: this.boardId, boardName: this.boardName,
                topicId: record.get("id"), topicName: record.get("name") }));
        }

        var blah = this.class + "-topic" + record.get("id");
        PageGlobals.contentPanel.getLayout().setActiveItem(this.class + "-topic" + record.get("id"));
    }
});