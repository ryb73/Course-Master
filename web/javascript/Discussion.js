Ext.define('DiscussionBoard', {
    extend: 'Ext.data.Model',
    fields: [ 'name', 'postCount' ]
});

Ext.define('DiscussionTopic', {
    extend: 'Ext.data.Model',
    fields: [ 'name', 'postedBy', 'postedOn', 'replies' ]
});

var discussionBoards = Ext.create('Ext.data.Store', {
    model: 'DiscussionBoard',
    data: [
        { name: '<a href="#board=1">Board 1</a>', postCount: 12 },
        { name: '<a href="#board=2">Board 2</a>', postCount: 3 }
    ]
});

var topics = Ext.create('Ext.data.Store', {
    model: 'DiscussionTopic',
    data: [
        { name: '<a href="#topic=1">Topic 1</a>', postedBy: 'Ryan', postedOn: "2 days ago", replies: 0 },
        { name: '<a href="#topic=2">Topic 2</a>', postedBy: 'Rohit', postedOn: "5 days ago", replies: 2 }
    ]
});

var hashChanged = function(token) {
    if(token.indexOf("board") >= 0) {
        contentPanel.removeAll();
        contentPanel.add(
            Ext.create('Ext.grid.Panel', {
                border: false,
                store: topics,
                title: 'Discussion Board > Board 1',
                columns: [
                    {
                        text: 'Name',
                        dataIndex: 'name'
                    },
                    {
                        text: 'Posted By',
                        dataIndex: 'postedBy'
                    },
                    {
                        text: 'On',
                        dataIndex: 'postedOn'
                    },
                    {
                        text: 'Replies',
                        dataIndex: 'replies'
                    }
                ]
            })
        );
    } else if(token.indexOf("topic") >= 0) {
        contentPanel.removeAll();
        contentPanel.add(
            {
                html: '<div class="discussion-post">\
                            <div class="poster-info">\
                                <h1>Ryan Biwer</h1>\
                                <h2>Posted On: 3:14pm, 3/22/12</h2>\
                            </div>\
                            <div class="post-content">\
                                Testing 1 2 3\
                            </div>\
                        </div>\
                        <div class="discussion-post">\
                            <div class="poster-info">\
                                <h1>Rohit Dhiman</h1>\
                                <h2>Posted On: 5:29pm, 3/22/12</h2>\
                            </div>\
                            <div class="post-content">\
                                cool cool cool\
                            </div>\
                        </div><button>Reply</button>'
            }
        );
    } else {
        contentPanel.removeAll();
        contentPanel.add(
            Ext.create('Ext.grid.Panel', {
                border: false,
                store: discussionBoards,
                title: 'Discussion Board',
                columns: [
                    {
                        text: 'Name',
                        width: 700,
                        dataIndex: 'name'
                    },
                    {
                        text: 'Posts',
                        flex: 1,
                        dataIndex: 'postCount'
                    }
                ]
            })
        );
    }
};

var contentPanel = Ext.create('Ext.panel.Panel', {
    items: [ ]
});

Ext.onReady(function() {
    viewport = Ext.create('Ext.container.Viewport', {
        layout: 'border',
        items: [{
            region: 'north',
            height: 50,
            border: false,
            layout: 'hbox',
            items: [{
                bodyCls: 'page-header title',
                flex: 1,
                html: 'Course Master'
            }, {
                bodyCls: 'page-header',
                flex: 1,
                style: 'text-align:right;',
                html: '<div>Logged in as: ' + user.id + '</div>'
            }]
        },{
            region: 'west',
            border: false,
            layout: 'vbox',
            margins: '5',
            width: 200,
            bodyCls: 'classes-sidebar',
            items: [{
                bodyCls: 'classes-menu-item',
                html: '<a href="../action/dashboard">Dashboard</a>'
            }, {
                bodyCls: 'classes-menu-item',
                html: '<a href="#">CS 111</a>'
            }, {
                bodyCls: 'classes-menu-item',
                html: '<a href="#">CS 201</a><ul>\
                        <li><a href="#">Calendar</a></li>\
                        <li><a href="#">Dropbox</a></li>\
                        <li><a href="#">Discussion Board</a></li></ul>'
            }, {
                bodyCls: 'classes-menu-item',
                html: '<a href="#">Math 231</a>'
            }, {
                bodyCls: 'classes-menu-item',
                html: '<a href="#">Ling 100</a>'
            }]
        },
        {
            region: 'center',
            border: false,
            items: [ contentPanel ]
        }]
    });

    Ext.History.init(function() {
        var token = document.location.hash.replace("#", "");
        hashChanged(token);
    });
    Ext.History.on('change', hashChanged);
});
