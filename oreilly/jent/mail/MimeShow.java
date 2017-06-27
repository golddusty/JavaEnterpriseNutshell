package oreilly.jent.mail;

// Example 11-4: Multipart Display

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.io.*;

public class MimeShow {
	static String mailhost = "pop.company.com";

	public static void main(String[] args) {
	Session session = Session.getDefaultInstance(System.getProperties(), null);

	try {
		Store popStore = session.getStore("pop3");
		popStore.connect(mailhost, "username", "password");

		// Get a default folder, and use it to open a real folder
		Folder defaultPopFolder = popStore.getDefaultFolder();
		defaultPopFolder = defaultPopFolder.getFolder("INBOX");
		defaultPopFolder.open(Folder.READ_ONLY);
		Message[] msgs = defaultPopFolder.getMessages();
		
		if(msgs != null)
			for (int i = 0; i < msgs.length; i++) {
				
				// Display the message envelope
				System.out.println("\n\r----------------------------------");
				System.out.println("Subject: " + msgs[i].getSubject());
				
				Address[] from = msgs[i].getFrom();
				
				if(from != null)
					for(int a = 0; a < from.length; a++)
						System.out.println("From: " + from[a]);
						
				// Display the content
				if (msgs[i].isMimeType("text/plain")) {
					System.out.println((String)msgs[i].getContent());
				} else if (msgs[i].isMimeType("multipart/*")) {
					Multipart mp = (Multipart)msgs[i].getContent();
					int count = mp.getCount();
					
					for(int m = 0; m < count; m++) {
						showBodyPart((MimeBodyPart)mp.getBodyPart(m));
					}
				} else if (msgs[i].isMimeType("message/rfc822")) {
					System.out.println("Nested Message");
				} else {
					System.out.println(msgs[i].getContent().toString());
				}
			}// end for
			
		} catch (MessagingException me) {
			me.printStackTrace(System.out);
		} catch (IOException ie) {
			ie.printStackTrace(System.out);
		}
	} // end main()
		
	// Show or save a MIME Body Part; Very Simple
	public static void showBodyPart(MimeBodyPart p) {
		
		try {
			String contentType = p.getContentType();
			System.out.println("-- MIME Part: " + contentType);
		
			if(contentType.startsWith("text/"))
				System.out.println((String)p.getContent());
			else if(p.getFileName() != null) {
				File f = new File(p.getFileName());
			
				FileOutputStream fos = new FileOutputStream(f);
				byte[] b = new byte[1024];
				InputStream is = p.getInputStream();
				
				while(is.read(b) > 0)
					fos.write(b);
				
				is.close();
				fos.close();
				System.out.println("Attachment saved as " + f.getAbsolutePath());
			} else {
				System.out.println(
				"Can not display this content type and no filename supplied.");
			}
		} catch(Exception e) {
			System.out.println("Exception Caught: " + e.getMessage());
		}
	}
}