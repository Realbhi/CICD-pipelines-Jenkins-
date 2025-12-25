
## **Big picture (one-line summary)**

* **E-mail Notification** → *old, basic, job-level emails*
* **Extended E-mail Notification (Email-ext)** → *modern, flexible, pipeline-friendly emails*

They can **co-exist**, but you should normally **use only one** (almost always Email-ext).

---

## **1) “E-mail Notification” (Basic Mailer)**

This comes from the **Mailer plugin**.

### What it is

* Legacy / original Jenkins email feature
* Very limited
* Mostly designed for **Freestyle jobs**

### How it works

* Jenkins sends mail on **simple events**

  * Build success
  * Build failure
  * Build unstable
* Uses:

  * SMTP server
  * Default recipients
* Content is **static and minimal**

### Typical usage

* Old Jenkins setups
* Freestyle jobs with:

  ```
  Post-build action → E-mail Notification
  ```

### Limitations (important)

* ❌ Poor support for Pipelines
* ❌ No conditional logic
* ❌ Hard to customize email body
* ❌ No attachments
* ❌ No HTML control per build

Think of this as:

> “Send a simple mail when build fails/succeeds”

---

## **2) “Extended E-mail Notification” (Email-ext plugin)**

This comes from the **Email Extension Plugin** (`email-ext`).

### What it is

* Modern, powerful email system
* Designed for **Pipeline jobs**
* This is what **most real projects use**

### How it works

You explicitly send emails using:

```groovy
emailext(...)
```

or job-level triggers.

### What it supports

* ✅ Pipelines (Declarative & Scripted)
* ✅ HTML emails
* ✅ Attachments (logs, reports)
* ✅ Conditional triggers
* ✅ Dynamic recipients
* ✅ Custom subjects & bodies
* ✅ Fine-grained control (success, failure, unstable, aborted, etc.)

Example in a pipeline:

```groovy
post {
    failure {
        emailext(
            subject: "Build Failed: ${env.JOB_NAME}",
            body: "Check console: ${env.BUILD_URL}",
            to: "peacee.abhi@gmail.com"
        )
    }
}
```

---

## **3)Why Jenkins shows both (and why that’s confusing)**

Jenkins allows **both plugins to be installed at the same time**, so:

* **E-mail Notification** = Mailer plugin
* **Extended E-mail Notification** = Email-ext plugin

They are **independent**.
They **do not share configuration**, except SMTP details.

If both are configured, Jenkins does **not merge them** — they work separately.

---

### Mental model (easy to remember)

Think of it like this:

| Feature          | E-mail Notification | Extended E-mail |
| ---------------- | ------------------- | --------------- |
| Age              | Old                 | Modern          |
| Plugin           | Mailer              | Email-ext       |
| Pipeline support | Poor                | Excellent       |
| HTML mail        | ❌                   | ✅               |
| Attachments      | ❌                   | ✅               |
| Conditions       | ❌                   | ✅               |
| Control          | Very low            | Very high       |

---

## **Additional info : **

### 1) How jenkins and email works :

```
Jenkins
  ↓
SMTP client (inside Jenkins JVM)
  ↓
smtp.gmail.com : 587
  ↓
Gmail SMTP server
  ↓
Recipient mailbox
```

### 2) Mandatory feilds :

| Setting                    | Mandatory | Why                            |
| -------------------------- | --------- | ------------------------------ |
| SMTP Server                | ✅         | Needed to connect              |
| SMTP Port                  | ✅         | Defines protocol               |
| Username                   | ✅         | Auth identity                  |
| App Password               | ✅         | Gmail auth                     |
| TLS enabled                | ✅         | Required by Gmail              |
| Default Recipients         | ❌         | Pipeline can override          |
| Reply-To List              | ❌         | Cosmetic (needed when sending reply)|
| Default user e-mail suffix | ❌         | Only for auto-generated emails |
| Default Content Type       | ❌         | Can override per mail          |

