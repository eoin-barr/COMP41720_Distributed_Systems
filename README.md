# COMP41720_Distributed_Systems

![Architecture](https://res.cloudinary.com/dk0r9bcxy/image/upload/v1673516153/portfolio-website/distributed-arch_p50lwg.png)

Repo for college module practicals, tasked with distributing an insurance quotation portal. Initial code included 3 quotation services, an insurance "broker" and a client. The broker interacts with the quotation services, which in turn is interacted with by the client.

Approaches to distribution include:

- <b>Remote Method Invocation</b>
- <b>Web Services</b>
- <b>Message Oriented Middleware</b>
- <b>REST</b>

- <b>Actor Programming</b>
  - The Actor model is a mathematical model of concurrent computation that treats "actors" as the universal primitives of concurrent computation. Each actor has its own mailbox and behavior, and can create new actors or send messages to other actors. Actors are isolated and can only communicate with other actors through message passing. The Actor model is highly concurrent and fault-tolerant, making it well-suited to building distributed systems. For this module the Akka Actor Model implementation was used.
