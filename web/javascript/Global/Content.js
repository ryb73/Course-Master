Ext.define("CM.Content.Panel", {
    extend: "Ext.panel.Panel",

    initComponent: function() {

        var items = [ new CM.Class.Dashboard() ];

        Ext.apply(this, {
            layout: "card",
            border: false,
            items: items
        });

        this.callParent(arguments);
    }
});