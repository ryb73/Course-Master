Ext.define("CM.Discussion.Topic", {
    extend: 'Ext.panel.Panel',

    initComponent: function() {

        var posts = Ext.create('Ext.data.Store', {
            fields: [ 'id', 'owner', 'ownerName', 'dte', 'content' ],
            proxy: {
                type: 'ajax',
                url: '/service/discussion/get-topic',
                extraParams: {
                    topicId: this.topicId
                },
                reader: {
                    type: 'json',
                    root: 'data'
                }
            }
        });

        Ext.apply(this, {
            border: false,
            id: this.class + '-topic' + this.topicId,
            title: this.class + ' Discussion Board > ' + this.boardName + ' > ' + this.topicName,
            autoScroll: false,
            layout: {
                type: 'vbox',
                align: 'stretch'
            }
        });

        posts.load({
            scope: this,
            callback: function(records, operation, success) {
                if(!success) {
                    Ext.Msg.alert("Error", "Unable to get topic content. Try refreshing the page.");
                    PageGlobals.contentPanel.getLayout().setActiveItem(this.class + '-thread-list');
                } else {
                    var postCount = posts.getCount();
                    var postItems = new Array(postCount);
                    for(var i = 0; i < postCount; ++i) {
                        var record = posts.getAt(i);

                        var owner = record.get("owner");
                        var ownerName = record.get("ownerName");
                        var dte = record.get("dte");
                        var postId = record.get("id");
                        var content = record.get("content");

                        postItems[i] = {
                            xtype: 'form',
                            url: (i == 0) ? '/service/discussion/delete-topic' : '/service/discussion/delete-post',
                            instance: this,
                            title: 'Posted by ' + ownerName + ' at ' + dte,
                            padding: 10,
                            margin: '0 10px 0 0',
                            items: [{
                                border: false,
                                padding: 10,
                                html: content.replace(/\n/g, "<br />"),
                                id: 'post' + postId
                            },{
                                xtype: 'hidden',
                                name: 'postId',
                                value: postId
                            }],
                        };

                        var bbar = [];

                        if(owner == SessionGlobals.id) {
                            bbar[bbar.length] = {
                                xtype: 'button',
                                text: 'Modify',
                                id: 'modify' + postId,
                                postId: postId,
                                content: content,
                                handler: this.modifyPost
                            };
                        }

                        if(owner == SessionGlobals.id || SessionGlobals.role == 2) {
                            bbar[bbar.length] = {
                                xtype: 'button',
                                text: 'Delete',
                                id: 'delete' + postId,
                                handler: (i == 0) ? this.deleteTopic : this.deletePost
                            };
                        }

                        if(bbar.length > 0) {
                            Ext.apply(postItems[i], { bbar: bbar });
                        }
                    }

                    this.add(postItems);

                    if(this.boardStatus != 3 /* Closed */) {
                        this.add({
                            xtype: 'form',
                            url: '/service/discussion/post-reply',
                            title: 'Post Reply',
                            padding: 10,
                            margin: '0 10px 0 0',
                            layout: 'fit',
                            instance: this,
                            items: [{
                                xtype: 'textarea',
                                name: 'message',
                                padding: 5,
                                margin: '0 5px 0 0',
                                grow: true,
                                maxLength: 1024,
                                enforceMaxLength: true,
                                allowBlank: false
                            },{
                                xtype: 'hidden',
                                name: 'topicId',
                                value: this.topicId
                            }],
                            bbar: [{
                                xtype: 'button',
                                text: 'Post',
                                handler: this.postReply
                            }]
                        });
                    }

                    Ext.getBody().down("div#" + this.id+"-body").down("div.x-box-inner").setStyle("overflow-y","auto");
                    if(this.newPost)
                        Ext.getBody().down("div#" + this.id+"-body").down("div.x-box-inner").scroll("b", 5000);
                }
            }
        });

        this.callParent(arguments);
    },

    postReply: function() {
        var form = this.up('form');
        if(form.getForm().isValid()) {
            form.submit({
                success: function() {
                    var newPanel = new CM.Discussion.Topic({ class: form.instance.class, courseId: form.instance.courseId, boardId: form.instance.boardId,
                        boardName: form.instance.boardName, boardStatus: form.instance.boardStatus, topicId: form.instance.topicId,
                        topicName: form.instance.topicName, newPost: true });
                    form.instance.destroy();

                    PageGlobals.contentPanel.add(newPanel);
                    PageGlobals.contentPanel.getLayout().setActiveItem(newPanel.class + "-topic" + newPanel.topicId);
                },
                failure: function() { Ext.Msg.alert("Error","Unable to post reply. Try refreshing the page."); }
            });
        }
    },

    deleteTopic: function() {
        Ext.Msg.confirm("Delete Topic", "Are you sure you want to delete this topic?", function(btn) {
            if(btn == "yes") {
                var form = this.up('form');
                if(form.getForm().isValid()) {
                    form.submit({
                        success: function() {
                            var threadListPanel = PageGlobals.contentPanel.getChildByElement(this.class + "-thread-list");
                            if (threadListPanel) {
                                threadListPanel.destroy();
                            }
                            
                            PageGlobals.contentPanel.add(new CM.Discussion.ThreadList({ class: form.instance.class, courseId: form.instance.courseId,
                                boardId: form.instance.boardId, boardName: form.instance.boardName }));

                            PageGlobals.contentPanel.getLayout().setActiveItem(form.instance.class + '-thread-list');

                            form.instance.destroy();
                            Ext.Msg.alert("Topic Deleted", "The topic has been successfully deleted.");
                        },
                        failure: function() { Ext.Msg.alert("Error","Unable to delete topic. Try refreshing the page."); }
                    });
                }
            }
        }, this);
    },

    deletePost: function() {
        Ext.Msg.confirm("Delete Post", "Are you sure you want to delete this post?", function(btn) {
            if(btn == "yes") {
                var form = this.up('form');
                if(form.getForm().isValid()) {
                    form.submit({
                        success: function() {
                            form.destroy();
                        },
                        failure: function() { Ext.Msg.alert("Error","Unable to delete post. Try refreshing the page."); }
                    });
                }
            }
        }, this);
    },

    modifyPost: function() {
        var form = this.up('form');

        form.add({
            xtype: 'form',
            url: '/service/discussion/modify-post',
            border: false,
            layout: 'fit',
            instance: form.instance,
            items: [{
                xtype: 'textarea',
                name: 'message',
                padding: 5,
                margin: '0 5px 0 0',
                grow: true,
                maxLength: 1024,
                enforceMaxLength: true,
                allowBlank: false,
                value: this.content
            },{
                xtype: 'hidden',
                name: 'postId',
                value: this.postId
            }],
            bbar: [{
                xtype: 'button',
                text: 'Save',
                handler: form.instance.saveModification
            }]
        });

        Ext.get('post' + this.postId).destroy();
        var dockedItems = form.getDockedItems("");
        for(var i = 0; i < dockedItems.length; ++i) {
            if(dockedItems[i].dock == "bottom") {
                form.removeDocked(dockedItems[i], true);
            }
        }
    },

    saveModification: function() {
        var form = this.up('form');
        if(form.getForm().isValid()) {
            form.submit({
                success: function() {
                    var newPanel = new CM.Discussion.Topic({ class: form.instance.class, courseId: form.instance.courseId, boardId: form.instance.boardId,
                        boardName: form.instance.boardName, boardStatus: form.instance.boardStatus, topicId: form.instance.topicId,
                        topicName: form.instance.topicName, newPost: false });
                    form.instance.destroy();
                    PageGlobals.contentPanel.add(newPanel);
                    PageGlobals.contentPanel.getLayout().setActiveItem(newPanel.class + "-topic" + newPanel.topicId);
                    Ext.Msg.alert("Success", "Your post has been modified.");
                },
                failure: function() { Ext.Msg.alert("Error", "Unable to modify post. Try refreshing the page."); }
            });
        }
<<<<<<< HEAD
    }
=======
     }
>>>>>>> upstream/master
});