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
                handler: this.loadBoard
            }, {
                xtype: 'button',
                text: 'Course Syllabus',
                class: this.class,
                handler: this.loadEvents
            }]
        });

        this.callParent(arguments);
    },

    loadBoard: function(btn, evt) {
        if (!PageGlobals.contentPanel.getChildByElement(btn.class + "-board-root")) {
            PageGlobals.contentPanel.add(new CM.Discussion.Root({ class: btn.class, courseId: this.ownerCt.courseId }));
        }

        PageGlobals.contentPanel.getLayout().setActiveItem(btn.class + '-board-root');
    },

    loadEvents: function(btn, evt) {
        if (!PageGlobals.contentPanel.getChildByElement(btn.class + "-event-root")) {
            PageGlobals.contentPanel.add(new CM.Event.Root({ class: btn.class, courseId: this.ownerCt.courseId }));
        }

        PageGlobals.contentPanel.getLayout().setActiveItem(btn.class + '-event-root');
    }
});