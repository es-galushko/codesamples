# User Management Java Example
Project's part that was responsible to provide an ability to manage users' access to the servers.

Consists of two modules:
## service
Business-logic and dao-layer are implemented here.

This module was built to a .jar and included in the web project as a library.   
## web
Contains controllers and utils to interface between services and UI that was on JSP.

Web project was built to a .war and deployed to GlassFish Server.
