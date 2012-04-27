Ext.define("CM.Admin.User.Form", {
    extend: "Ext.form.Panel",

    initComponent: function() {
        var roleStore = new Ext.data.Store({
            fields: ['value', 'name'],
            data : [
                { value: 1, name: 'Student'       },
                { value: 2, name: 'Professor'     },
                { value: 3, name: 'Administrator' }
            ]
        });

        Ext.apply(this, {
            id: 'user-form-panel',
            title: 'New User Form',
            border: false,
            layout: 'anchor',
            defaults: {
                padding: '10 10 0',
                anchor: '25%'
            },
            defaultType: 'textfield',
            items: [{
                fieldLabel: 'Full Name',
                name: 'fullname',
                allowBlank: false,
            }, {
                fieldLabel: 'Email Address',
                name: 'email',
                allowBlank: false,
                vtype: 'email'
            }, {
                fieldLabel: 'Password',
                name: 'password',
                allowBlank: false
            }, {
                fieldLabel: 'Role',
                name: 'role',
                xtype: 'combobox',
                store: roleStore,
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
                               Ext.Msg.alert('Success', 'Your entry was successfully stored.');
                            },
                            failure: function(form, action) {
                                Ext.Msg.alert('Failed', action.result.errors.message);
                            }
                        });
                    }
                }
            }]
        });

        this.callParent(arguments);
    }
});