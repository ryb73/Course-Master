Ext.define("CM.Dashboard", {
    extend: "Ext.panel.Panel",

    initComponent: function() {
        Ext.apply(this, {
            border: false,
            height: '100%',
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
            items: [{
                border: false,
                html: 'class schedule panel',
                flex: 1
            }, {
                border: false,
                html: 'calendar panel',
                flex: 3
            }]
        });

        this.callParent(arguments);
    }
});