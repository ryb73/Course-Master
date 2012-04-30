package com.coursemaster.servlet.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

public class EmailUtil {

    /**
     * Method to instantiate email properties
     * @param _host Mailer host
     * @param _user Mailer user
     * @param _pass Mailer password
     */
    public static void init(String _host, String _user, String _pass) {
        host = _host;
        user = _user;
        pass = _pass;
    }

    /**
     * Method to send an email through the system.
     * Spins off a thread.
     * @param to Recipient
     * @param subject Subject
     * @param message Message Body
     */
    public static void sendEmail(String to, String subject, String message) {
        logger.trace("Sending an email");

        new Thread(new Mailer(to, subject, message)).start();
   }

    /**
     * Private class intended to send mail
     * in an asynchronous fashion
     */
    private static class Mailer implements Runnable {
        /**
         * Constructor requiring email fields
         * @param _to Message recipient
         * @param _subject Message subject
         * @param _message Message body
         */
        public Mailer(String _to, String _subject, String _message) {
            to = _to;
            subject = _subject;
            message = _message;
        }

        /**
         * Entry point for thread
         * From Runnable interface
         */
        public void run() {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
 
            Session s = Session.getDefaultInstance(props);
            Message m = new MimeMessage(s);

            try {
                m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                m.setSubject(subject);
                m.setText(message);

                Transport t = s.getTransport();
                t.connect(host, 587, user, pass);
                t.sendMessage(m,  m.getAllRecipients());
                t.close();
            }
            catch (Exception e) {
               e.printStackTrace();
            }
        }
        private String to, subject, message;
    }

    private static String host, user, pass;
    private static Logger logger = Logger.getLogger(EmailUtil.class);
}
