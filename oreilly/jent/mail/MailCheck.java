package oreilly.jent.mail;

// Example 11-2: IMAP and POP Message Retrieval

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class MailCheck {
	static String mailhost = "mail.college.edu";

	public static void main(String[] args) {
		Session session = Session.getDefaultInstance(System.getProperties(), new myAuthenticator());
		
		try {
			Store imapStore = session.getStore(new URLName("imap://" + mailhost));
			imapStore.connect();
			System.out.println(imapStore.getURLName());
			
			// Get a default folder, and use it to open a real folder
			Folder defaultFolder = imapStore.getDefaultFolder();
			defaultFolder = defaultFolder.getFolder("INBOX");
			defaultFolder.open(Folder.READ_WRITE);
			
			Message[] msgs = defaultFolder.getMessages();
			if(msgs != null)
				for (int i = 0; i < msgs.length; i++) {
					System.out.println(msgs[i].getSubject());
				}
				
			// Now, get it with POP3, using authenticator
			Store popStore = session.getStore("pop3");
			popStore.connect(mailhost, null, null);
			
			// Get a default folder, and use it to open a real folder
			Folder defaultPopFolder = popStore.getDefaultFolder();
			defaultPopFolder = defaultPopFolder.getFolder("INBOX");
			defaultPopFolder.open(Folder.READ_ONLY);
			
			msgs = defaultPopFolder.getMessages();
			if(msgs != null)
				for (int i = 0; i < msgs.length; i++) {
					System.out.println(msgs[i].getSubject());
				}
				
		} catch (MessagingException me) {
			me.printStackTrace(System.out);
		}
	} // end main()
}

class myAuthenticator extends Authenticator {
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication("mailname", "test");
	}
}