Ext.define("CM.SidebarPanel", {
    extend: "Ext.panel.Panel",

    initComponent: function() {

        Ext.apply(this, {
            border: false,
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            padding: '15',
            bodyCls: 'sidebar',
            cls: 'sidebar',
            width: 200,
            items: [ new CM.Sidebar.Class({
                class: 'Dashboard'
            }), new CM.Sidebar.Class({
                class: 'CS 111'
            }), new CM.Sidebar.Class({
                class: 'CS 201'
            }), new CM.Sidebar.Class({
                class: 'Math 231'
            }), new CM.Sidebar.Class({
                class: 'Ling 100'
            })]
        });

        this.callParent(arguments);
    }
});