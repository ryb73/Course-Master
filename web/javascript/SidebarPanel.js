Ext.define("CM.Sidebar.Class", {
    extend: "Ext.button.Button",

    initComponent: function() {

        Ext.apply(this, {
            height: 25,
            margin: '5 0 0',
            cls: 'sidebar-class',
            itemId: this.initialConfig.class + '-link',
            text: this.initialConfig.class,
            handler: this.clickFunction
        });

        PageGlobals.contentPanel.add(new CM.Class.Home({ class: this.initialConfig.class }));

        this.callParent(arguments);
    },

    clickFunction: function(button, evt) {
        
        if (PageGlobals.contentPanel.getChildByElement(button.text + "-class")) {
            PageGlobals.contentPanel.getLayout().setActiveItem(button.text + '-class');
        }
        else {
            console.log("not found");
        }
    }
});
