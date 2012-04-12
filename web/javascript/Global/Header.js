Ext.define("CM.Header.Panel", {
    extend: "Ext.panel.Panel",

    initComponent: function() {

        Ext.apply(this, {
            height: 50,
            border: false,
            layout: 'hbox',
            items: [{
                bodyCls: 'page-header title',
                height: 50,
                flex: 2,
                html: 'Course Master'
            }, {
                bodyCls: 'page-header',
                style: 'text-align:right',
                height: 50,
                width: 250,
                html: 'Logged in as ' + SessionGlobals.name + ' | <a href="/action/logout">Logout</a>'
            }]
        });

        this.callParent(arguments);
    }
});