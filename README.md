# Designing microservices course
This repository is intended for demonstration purposes for students during the "Basics of microservices architecture 
course" which is going to be taught in 2019 autumn term for 4 years students if ITMO university, department of 
informational systems.

## Disclaimer
Project is created to demonstrate main approaches and some of patterns which are used for creating microservices-based
system. Some of architectural decisions were made especially for complicating system in order for us to have a room
for applying some of patters, some were made to simplify system to order for us not having to implement real-world 
business logic. Some of the services or business logic are just a mocks. We also didn't aim to create perfect code
but tried to make it clear, without duplications, tested and logged. But it still can't be considered as some best
practices.

## Overview

### Business process description
Project consists of several services communicating to ensure proper execution of some business processes. TODO sukhoa : 
here should be description of the system.

### Architecture overview
Typically each individual service is just a small monolith or hexagonal application responsible for data and operations 
from some certain business area. For example order management, user management etc. Service may have a number of inbound
channels and outbound channels. Inbound channels enables service to get some data from the outside world. It may be 
different types of data - client request, some event, resource state or whatever. Inbound channels in our system are 
represented by REST API of the service and the queues or topics on which the service is subscribed. The REST API calls 
may sometimes overlay or simple duplicate the messaging API logic. In our particular case it have been made for testing 
purposes and making developer’s life easier. We use Swagger to consume our REST API during developing. So REST API and 
messaging API should be designed to be consistent - REST calls and equivalent messaging call should always lead to same 
results in system and produce the same response. We achieve this by always delegating all the business operations to 
the services layer. Data from inbound channels regardless of Its nature are processed by the services (we mean internal 
services, DDD term) of the microservice. Only services are responsible for all the business logic and there is no way 
to work around them. The only thing the REST API and messaging API layer is responsible is to transform data into the 
form which may be fed to services. Note that equivalent API not necessarily have the same incoming data - just because 
the messaging API receives the messages and the REST API may receive some dto which may have slightly different structure
and some path/request parameters.

Outbound channels - queues and topics to where we publish our outcome events and messages and our local datastore.

here is the picture

Services don’t have any shared entities, data structures and each service has its own schema in our Postgres instance 
which enables for us:

1. Deploy services and migrate service’s schema and data independently
1. Switch to another DBMS (Polyglot persistence approach) at any time we decide without any impact to other services.

Services typically consist of two modules:
1. app module contains the implementation of service - properties,  services, controllers, dao objects, domain and dto 
classes, utility classes. It builds into some jar or war file which might be executed or deployed to some web server.
1. api module contains all the stuff server would like to show to the outside world (more precisely to other services, 
because the only thing is showed to outside is API Gateway). It may be some data structures (messages, requests, responses), 
connection info (queue and topic information), and REST Client implementation which allows for others to consume server’s API
via java client wrapper.

