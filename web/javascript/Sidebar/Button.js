Ext.define("CM.Sidebar.Button", {
    extend: "Ext.button.Button",

    initComponent: function() {
        Ext.apply(this, {
            text: this.class,
            cls: 'sidebar-class',
            border: false,
            height: 25,
            margin: '0 0 5',
            id: this.class + '-button',
            handler: this.loadClass
        });

        this.callParent(arguments);
    },
    
    constructor: function(opt) {
        this.callParent(arguments);

        if(opt.courseId) {
            this.courseId = opt.courseId;
        }

        return this;
    },

    loadClass: function(btn, evt) {

        if (!PageGlobals.contentPanel.getChildByElement(btn.text + "-class")) {
            PageGlobals.contentPanel.add(new CM.Class.Home({ class: btn.text, courseId: btn.courseId }));
        }

        PageGlobals.contentPanel.getLayout().setActiveItem(btn.text + '-class');
    },

    courseId: -1
});
