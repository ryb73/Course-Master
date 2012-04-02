Ext.define('CM.Discussion.Root', {
    extend: 'Ext.grid.Panel',

    initComponent: function() {

        var discussionBoards = Ext.create('Ext.data.Store', {
            fields: [ 'name', 'postCount', 'id' ],
            data: [
                { id: '1', name: 'Board 1', postCount: 12 },
                { id: '2', name: 'Board 2', postCount: 3 }
            ]
        });

        Ext.apply(this, {
            border: false,
            store: discussionBoards,
            id: this.class + '-board-root',
            title: this.class + ' Discussion Board',
            listeners: {
                select: this.onSelect
            },
            columns: {
                items: [{
                    text: 'Board Name',
                    flex: 1,
                    dataIndex: 'name'
                }, {
                    text: 'Posts',
                    width: 40,
                    align: 'right',
                    dataIndex: 'postCount'
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
    }
});