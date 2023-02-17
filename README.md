# devs-smtp
**devs-smtp** is a simple, lightweight SMTP server for developers.
It is used to mock a SMTP server to check email-sending functions reducing the risk of using personal email or the cost of accessing the organization's SMTP server. Devs-SMTP temporarily stores emails in the database which enable developer access and views email content like a mailbox directly in browser
This approach does not require the involvement of an IMAP/POP3 server or any real email account to view email content.

### Features
* Viewing email content, attachments
* View raw mimemessage
* Export email to Outbox, Gmail files
* ...

### Configuration
* ` smtp.server.port` SMTP running port, default  `5025`<br>
* ` server.port` Web UI server, default 8089 

## Build and Start
Start from source `./mvnw spring-boot:run`

Jar file support coming soon

### Build and start from docker image
Build docker image: ```docker build -t devs-smtp:latest .```

Start docker image: ```docker run -p 5025:5025 -p 8089:8089 -d  devs-smtp:latest```


### Under the hook of devs-smtp
Devs-smtp relies on Apache protocols-smtp library which we use to receive mail messages. A spring application event will be fired when SMTP server receives an email, A listener parses, and stores the email in the database

### credit
