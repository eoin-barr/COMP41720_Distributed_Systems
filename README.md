# COMP41720_Distributed_Systems

![Architecture](https://res.cloudinary.com/dk0r9bcxy/image/upload/v1673516153/portfolio-website/distributed-arch_p50lwg.png)

This repository contains the COMP41720 Distributed Systems labs, each lab corresponds to a different approach to distributing a set of services (insurance quotation portal). These services included 3 quotation services (auldfellas, girlpower, dodgydrivers), an insurance "broker" and a client. The broker interacts with the quotation services, which in turn is interacted with by the client.

### Approaches to distribution include:

- <b>Lab 2: Remote Method Invocation</b>

  - Remote Method Invocation (RMI) is a Java technology that allows an object to invoke methods on an object that exists in another JVM. RMI uses object serialization to marshal and unmarshal method arguments and return values between the client and server. RMI also allows remote objects to be passed as method arguments or return values. RMI provides transparent remote invocation, meaning that a programmer can write code as if the remote object is a local object.

- <b>Lab 3: Web Services</b>

  - Web Services is a way to expose the functionality of an application over the Internet using a standard protocol, such as HTTP. They are typically based on open standards such as XML, SOAP, and WSDL, and are designed to be platform- and language-independent. Web Services can be discovered and invoked using the UDDI and SOAP protocols, respectively. They can be used to create distributed applications that can be accessed from any platform or programming language. They are often used for integrating different systems and applications, and for creating mashups that combine data and functionality from multiple sources.

- <b>Lab 4: Message Oriented Middleware</b>

  - Message Oriented Middleware (MOM) is a type of middleware that facilitates the communication between applications using message-based communication. It is designed to send and receive messages asynchronously and provides abstractions for message queues, topics and publish-subscribe models. MOM allows applications to communicate with each other independently of the underlying network and transport protocol, and provides features such as reliability, security, and scalability.

- <b>Lab 5: Representational State Transfer</b>

  - Representational State Transfer (REST) is an architectual style for building web services. It is based on the HTTP protocol, and uses a set of constraints that ensure that the service is stateless and cacheable. The main concepts in REST are resources, which are identified by URIs, and representations of those resources, which are returned to the client in response to requests. RESTful services use a simple set of HTTP methods (such as GET, POST, PUT, and DELETE) to interact with resources.

- <b>Lab 6: Actor Programming</b>
  - The Actor model is a mathematical model of concurrent computation that treats "actors" as the universal primitives of concurrent computation. Each actor has its own mailbox and behavior, and can create new actors or send messages to other actors. Actors are isolated and can only communicate with other actors through message passing. The Actor model is highly concurrent and fault-tolerant, making it well-suited to building distributed systems. For this module the Akka Actor Model implementation was used.
