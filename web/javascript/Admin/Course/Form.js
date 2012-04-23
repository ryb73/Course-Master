Ext.define("CM.Admin.Course.Form", {
    extend: "Ext.form.Panel",

    initComponent: function() {
        var profs = new Ext.data.Store({
            fields: ['value', 'name'],
            data : [
                { value: 1, name: 'Jayson Rock'       },
                { value: 2, name: 'John Boyland'     }
            ]
        });

        Ext.apply(this, {
            id: 'course-form-panel',
            title: 'New Course Form',
            border: false,
            layout: 'anchor',
            defaults: {
                padding: '10 10 0',
                anchor: '25%'
            },
            defaultType: 'textfield',
            items: [{
                fieldLabel: 'Course Name',
                name: 'name',
                allowBlank: false,
            }, {
                fieldLabel: 'Department',
                name: 'dept',
                allowBlank: false
            }, {
                fieldLabel: 'Course Number',
                name: 'num',
                allowBlank: false
            }, {
                fieldLabel: 'Section Number',
                name: 'sect',
                allowBlank: false
            }, {
                fieldLabel: 'Credits',
                name: 'cred',
                allowBlank: false
            }, {
                fieldLabel: 'Semester',
                name: 'sem',
                allowBlank: false
            }, {
                fieldLabel: 'Professor',
                name: 'prof',
                xtype: 'combobox',
                store: profs,
                queryMode: 'local',
                displayField: 'name',
                valueField: 'value',
                allowBlank: false
            }],
            buttons: [{
                text: 'Submit',
                formBind: true,
                disabled: true,
                handler: function() {
                    var form = this.up('form').getForm();
                    if (form.isValid()) {
                        form.submit({
                            success: function(form, action) {
                               Ext.Msg.alert('Success', action.result.msg);
                            },
                            failure: function(form, action) {
                                Ext.Msg.alert('Failed', action.result.msg);
                            }
                        });
                    }
                }
            }]
        });

        this.callParent(arguments);
    }
});