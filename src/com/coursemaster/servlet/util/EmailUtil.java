package com.coursemaster.servlet.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

public class EmailUtil {

    public static void init(String _host, String _user, String _pass) {
        host = _host;
        user = _user;
        pass = _pass;
    }

    public static void sendEmail() {
        logger.trace("Starting message sending attempt");

       String to = "muell372@uwm.edu";

       Properties props = new Properties();
       props.put("mail.transport.protocol", "smtp");
       props.put("mail.smtp.auth", "true");
       props.put("mail.smtp.starttls.enable", "true");

       Session s = Session.getDefaultInstance(props);
       Message m = new MimeMessage(s);

       try {
           m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
           m.setSubject("Test Message");
           m.setText("This message is being sent as a test.");

           Transport t = s.getTransport();
           t.connect(host, 587, user, pass);
           t.sendMessage(m,  m.getAllRecipients());
           t.close();
       }
       catch (Exception e) {
           e.printStackTrace();
       }
   }

    private static String host, user, pass;
    private static Logger logger = Logger.getLogger(EmailUtil.class);
}
