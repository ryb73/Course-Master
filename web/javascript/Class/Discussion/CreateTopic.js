Ext.define("CM.Discussion.CreateTopic", {
    extend: 'Ext.form.Panel',

	initComponent: function() {

        Ext.apply(this, {
            border: false,
            id: this.class + '-create-topic',
            title: 'Create Topic',
            layout: 'fit',
            items: {
                xtype: 'form',
                url: '/service/discussion/post-topic',
                border: false,
                bodyPadding: 5,
                fieldDefaults: {
                    //labelAlign: 'left',
                    //labelWidth: 90
                },
                items: [{
                    xtype: 'textfield',
                    name: 'topic-name',
                    fieldLabel: 'Topic Name',
                    anchor: '100%'
                },{
                    xtype: 'textarea',
                    name: 'message',
                    fieldLabel: 'Message',
                    anchor: '100% 90%',
                    maxLength: 1024,
                    enforceMaxLength: true
                },{
                    xtype: 'hidden',
                    name: 'board',
                    value: this.boardId
                }],
                tbar: [{
                    text: 'Post',
                    listeners: { click: this.postTopic }
                }, {
                    text: 'Cancel',
                    listeners: { click: this.cancel }
                }]
            }
        });

        this.callParent(arguments);
    },

    postTopic: function() {
        var form = this.up('form').getForm();
        if(form.isValid()) {
            form.submit({
                success: function() { Ext.Msg.alert("success","success"); },
                failure: function() { Ext.Msg.alert("Error","Unable to create topic. Try refreshing the page."); }
            });
        }
    },

    cancel: function() {
        console.log("Cancel");
    }
});