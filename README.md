# devs-smtp
**devs-smtp** is a simple, lightweight SMTP server for developers. It is used to mock an SMTP server to check email-sending functions reducing the risk of using personal email or the cost of accessing the organization's SMTP server. Devs-SMTP will temporarily store the email in the database therefore developer can access and views the actual email content directly in SMTP rather than viewing it from a real email inbox.
This approach does not require the involvement of an IMAP/POP3 server or any real email account to view email content.

### Features
* Viewing email content, attachments
* View raw mimemessage
* Export email to Outbox, Gmail files
* ...

### Configuration


### Under the hook of devs-smtp
Devs-smtp relies on Apache protocols-smtp library which we use to receive mail messages. A spring application event will be fired when SMTP server receives an email, A listener parses, and stores the email in the database

### credit
