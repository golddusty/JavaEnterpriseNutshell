package oreilly.jent.mail;

// Example 11-1: Sending an SMTP Mail Message

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class MailSend {

	public static void main(String[] args) {
	
		Properties props = System.getProperties();
		props.put("mail.smtp.host", "mail.college.edu");
		Session session = Session.getDefaultInstance(props, null);
		
		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("you@yourhost.com"));
			msg.setRecipients(Message.RecipientType.TO,
			InternetAddress.parse("me@myhost.com", false));
			msg.setSubject("Test Message");
			msg.setText("This is the sample Message Text");
			msg.setHeader("X-Mailer", "O'Reilly SimpleSender");
			
			Transport.send(msg);
		} catch (AddressException ae) {
			ae.printStackTrace(System.out);
		} catch (MessagingException me) {
			me.printStackTrace(System.out);
		}
	}
}