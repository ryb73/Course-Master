Ext.define('CM.Discussion.Board.Model', {
    extend: 'Ext.data.Model',
    fields: [ 'name', 'postCount' ]
});

Ext.define('CM.Discussion.Root', {
    extend: 'Ext.grid.Panel',

    initComponent: function() {

        var discussionBoards = Ext.create('Ext.data.Store', {
            model: 'CM.Discussion.Board.Model',
            data: [
                { name: '<a href="#board=1">Board 1</a>', postCount: 12 },
                { name: '<a href="#board=2">Board 2</a>', postCount: 3 }
            ]
        });

        Ext.apply(this, {
            border: false,
            store: discussionBoards,
            id: this.class + '-board-root',
            title: this.class + ' Discussion Board',
            columns: [{
                text: 'Name',
                width: 700,
                dataIndex: 'name'
            }, {
                text: 'Posts',
                flex: 1,
                dataIndex: 'postCount'
            }]
        });

        this.callParent(arguments);
    }
});