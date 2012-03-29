Ext.define("CM.Sidebar.Class", {
    extend: "Ext.panel.Panel",

    initComponent: function() {

        Ext.apply(this, {
            title: this.class,
            cls: 'sidebar',
            border: false,
            margin: 15,
            itemId: this.class + '-link',
            html: "root<br/>discussion<br/>forum"
        });

        this.callParent(arguments);
    },

    clickFunction: function(button, evt) {

        if (!PageGlobals.contentPanel.getChildByElement(button.text + "-class")) {
            PageGlobals.contentPanel.add(new CM.Class.Home({ class: button.text }));
        }

        PageGlobals.contentPanel.getLayout().setActiveItem(button.text + '-class');
    }
});
