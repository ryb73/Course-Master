Ext.define("CM.DetailPanel", {
    extend: "Ext.panel.Panel",

    initComponent: function() {
        Ext.apply(this, {
            border: false,
            height: '100%',
            layout: 'card',
            items: [
                new CM.Dashboard()
            ]
        });

        this.callParent(arguments);
    }
});