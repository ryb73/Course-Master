Ext.define("CM.Class.Home", {
    extend: "Ext.panel.Panel",

    initComponent: function() {

        Ext.apply(this, {
            border: false,
            id: this.initialConfig.class + "-class",
            html: "Root panel for " + this.initialConfig.class
        });

        this.callParent(arguments);
    }
});