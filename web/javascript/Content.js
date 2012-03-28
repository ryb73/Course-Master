Ext.define("CM.ContentPanel", {
    extend: "Ext.panel.Panel",

    initComponent: function() {

        Ext.apply(this, {
            layout: "card",
            items: [ new CM.Class.Home({ class: "Dashboard" }) ]
        });

        this.callParent(arguments);
    }
});