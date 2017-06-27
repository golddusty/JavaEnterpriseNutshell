package oreilly.jent.mail;

// Example 11-5: Sending a Multipart Message

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.io.File;
import java.util.Properties;

public class MimeAttach {

	public static void main(String[] args) {
	
		try {
			
			Properties props = System.getProperties();
			props.put("mail.smtp.host", "mail.company.com");
			
			Session session = Session.getDefaultInstance(props, null);
			
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("logs@company.com"));
			msg.setRecipient(Message.RecipientType.TO,
			new InternetAddress("root@company.com"));
			msg.setSubject("Today's Logs");
			
			Multipart mp = new MimeMultipart();
			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setContent("Log file for today is attached.", "text/plain");
			mp.addBodyPart(mbp1);
			
			File f = new File("/var/logs/today.log");
			
			MimeBodyPart mbp = new MimeBodyPart();
			mbp.setFileName(f.getName());
			mbp.setDataHandler(new DataHandler(new FileDataSource(f)));
			mp.addBodyPart(mbp);
			msg.setContent(mp);
			Transport.send(msg);
			
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}
}