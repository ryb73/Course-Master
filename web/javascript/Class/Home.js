Ext.define("CM.Class.Home", {
    extend: "Ext.panel.Panel",

    initComponent: function() {

        Ext.apply(this, {
            border: false,
            id: this.class + "-class",
            title: this.class,
            items: [{
                border: false,
                html: "Root panel for " + this.class
            }, {
                xtype: 'button',
                text: 'Discussion Board',
                class: this.class,
                handler: this.loadBoard,
                instance: this
            }]
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

    loadBoard: function(btn, evt) {

        if (!PageGlobals.contentPanel.getChildByElement(btn.class + "-board-root")) {
            PageGlobals.contentPanel.add(new CM.Discussion.Root({ class: btn.class, courseId: btn.instance.courseId }));
        }

        PageGlobals.contentPanel.getLayout().setActiveItem(btn.class + '-board-root');
    },

    courseId: -1
});