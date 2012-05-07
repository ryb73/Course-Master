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
            },
            viewConfig: { emptyText: "There are no topics in this discussion board." }
        });

        if(this.boardStatus == 1 /* Open */ || SessionGlobals.role == 2 /* Professor */) {
            Ext.apply(this, {
                bbar: [{
                    xtype: 'button',
                    text: 'Create Topic',
                    instance: this,
                    listeners: { click: this.createTopic }
                }]
            });
        }

        this.callParent(arguments);
    },

    createTopic: function(view, record) {
        var createTopicPanel = PageGlobals.contentPanel.getChildByElement("create-topic");
        if (createTopicPanel) {
            createTopicPanel.destroy();
        }

        PageGlobals.contentPanel.add(new CM.Discussion.CreateTopic({ class: this.instance.class, courseId: this.instance.courseId, boardId: this.instance.boardId,
            boardName: this.instance.boardName}));

        PageGlobals.contentPanel.getLayout().setActiveItem('create-topic');
    },

    onSelect: function(rowModel, record) {
        console.log("Select fired: " + record.get("id"));
    },

    onItemDblClick: function(view, record) {
        if (!PageGlobals.contentPanel.getChildByElement(this.class + "-topic" + record.get("id"))) {
            PageGlobals.contentPanel.add(new CM.Discussion.Topic({ class: this.class, courseId: this.courseId, boardId: this.boardId, boardName: this.boardName,
                boardStatus: this.boardStatus, topicId: record.get("id"), topicName: record.get("name"), newPost: false }));
        }

        PageGlobals.contentPanel.getLayout().setActiveItem(this.class + "-topic" + record.get("id"));
    }
});