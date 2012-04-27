Ext.define("CM.Content.Panel", {
    extend: "Ext.panel.Panel",

    initComponent: function() {

        var items = [ new CM.Class.Dashboard() ];

        if (SessionGlobals.role == '3') {
            items.push([
                new CM.Admin.User.Form({ url: '/action/user/create' }),
                new CM.Admin.Course.Form({ url: '/action/course/create' })
            ]);
        }

        Ext.apply(this, {
            layout: "card",
            border: false,
            items: items
        });

        this.callParent(arguments);
    }
});