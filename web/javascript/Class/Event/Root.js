Ext.define("CM.Event.Root", {
    extend: "Ext.panel.Panel",

    initComponent: function() {
        Ext.apply(this, {
            title: "Event"
        });

        this.callParent(arguments);
    }
});