Ext.define("CM.Header.Panel", {
    extend: "Ext.panel.Panel",

    initComponent: function() {

        Ext.apply(this, {
            height: 50,
            border: false,
            layout: 'hbox',
            items: [{
                bodyCls: 'page-header title',
                flex: 1,
                html: 'Course Master'
            }, {
                bodyCls: 'page-header',
                flex: 1,
                style: 'text-align:right;',
                html: '<div>Logged in as: ' + SessionGlobals.name + '</div>'
            }]
        });

        this.callParent(arguments);
    }
});