Ext.define("CM.Sidebar.Panel", {
    extend: "Ext.panel.Panel",

    initComponent: function() {
        var classStore = new Ext.data.Store({
            proxy: {
                type: 'ajax',
                url : '/service/courses',
                extraParams: {
                    userId: SessionGlobals.id
                },
                reader: {
                    type: 'json',
                    root: 'data'
                }
            },
            autoLoad: true,
            fields: [ 'id', 'name', 'prof', 'dept', 'num', 'sec', 'cred', 'sem' ]
        });

        classStore.on('load', this.onLoad, this);

        Ext.apply(this, {
            border: false,
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            padding: 15,
            bodyCls: 'sidebar',
            cls: 'sidebar',
            width: 200,
            height: 200,
            items: [
                new CM.Sidebar.Button({ class: 'Dashboard' })
            ]
        });

        this.callParent(arguments);
    },

    onLoad: function(store, records, success, operation, opts) {
        Ext.each(records, function(rec) {
            this.add(new CM.Sidebar.Button({ class: rec.data.dept + '-' + rec.data.num }));
        }, this);
    }
});