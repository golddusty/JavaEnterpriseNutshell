package oreilly.jent.mail;

// Example 11-3: SpamCleaner.java

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.search.*;
import java.util.Properties;

public class SpamCleaner {

	static String mailhost = "imap.mycompany.com";
	static String smtphost = "mail.mycompany.com";
	static String redirectEmail = "spamlog@mycompany.com";
	static String[] badSubjects = {"XXX", "MAKEMONEYFAST", "!!!!"};
		
	public static void main(String[] args) {
		
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", smtphost);
		Session session = Session.getDefaultInstance(props, null);
		
		try {
			Store imapStore = session.getStore("imap");
			imapStore.connect(mailhost, "scott", "yu7xx");
			Folder defaultFolder = imapStore.getDefaultFolder();
			Folder inboxFolder = defaultFolder.getFolder("INBOX");
			
			inboxFolder.open(Folder.READ_WRITE);
			// Assemble some search Criteria, using nested OrTerm objects
			
			SearchTerm spamCriteria = null;
			for(int i=0; i<badSubjects.length; i++) {
				SubjectTerm st = new SubjectTerm(badSubjects[i]);
				
				if(spamCriteria == null)
					spamCriteria = st;
				else
					spamCriteria = new OrTerm(spamCriteria, st);
			}
			
			Message[] msgs = inboxFolder.search(spamCriteria);
			
			if(msgs != null)
				for (int i = 0; i < msgs.length; i++) {
				// Redirect the message
				Message m = new MimeMessage((MimeMessage)msgs[i]);
				m.setRecipient(Message.RecipientType.TO,
					new InternetAddress(redirectEmail));
				
				// Clear out the cc: field so we don't loop or anything
				m.setHeader("CC", "");
				Transport.send(m);
				// Now delete the original
				System.out.println("Deleting Message "+msgs[i].getMessageNumber()+":"
					+ msgs[i].getSubject());
				msgs[i].setFlag(Flags.Flag.DELETED, true);
			}
			
			// Close inbox folder, with "Expunge" flag set to true
			inboxFolder.close(true);
			imapStore.close();
		} catch (MessagingException me) {
			me.printStackTrace(System.out);
		}
	} // end main()
}
