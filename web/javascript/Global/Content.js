Ext.define("CM.Content.Panel", {
    extend: "Ext.panel.Panel",

    initComponent: function() {

        Ext.apply(this, {
            layout: "card",
            border: false,
            items: [ new CM.Class.Dashboard() ]
        });

        this.callParent(arguments);
    }
});