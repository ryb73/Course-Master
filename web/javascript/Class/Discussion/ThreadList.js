Ext.define("CM.Discussion.ThreadList", {
    extend: 'Ext.grid.Panel',

    initComponent: function() {

        var topics = Ext.create('Ext.data.Store', {
            fields: [ 'id', 'name', 'postedBy', 'postedOn', 'replies' ],
            /*data: [
                { id: 1, name: '<a href="#topic=1">Topic 1</a>', postedBy: 'Ryan', postedOn: "2 days ago", replies: 0 },
                { id: 2, name: '<a href="#topic=2">Topic 2</a>', postedBy: 'Rohit', postedOn: "5 days ago", replies: 2 }
            ]*/
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
                    dataIndex: 'name'
                }, {
                    text: 'Posted By',
                    dataIndex: 'postedBy'
                }, {
                    text: 'On',
                    dataIndex: 'postedOn'
                }, {
                    text: 'Replies',
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
        
        this.callParent(arguments);
    },
    
    onSelect: function(rowModel, record) {
        console.log("Select fired: " + record.get("id"));
    },

    onItemDblClick: function(view, record) {
        console.log("Double clicked: " + record.get("id"));
    }
});