## Initial steps:
* Install JDK 1.8

## Changing of the configuration
There are three profiles: **prod**, **test** and **dev**.
**Dev** profile is default.

Main properties which are can be changed (see corresponding property file in the *src/main/resources* folder):
* *server.port=8080* - The port of the application
* *stripe.webhook.endpoint.secret=...* - Add endpoint on the page: [webhooks](https://dashboard.stripe.com/account/webhooks).
And get the Signing secret from the Details page of this webhook.
* *stripe.api.key=...* - Get the *Secret key* from the page: [apikeys](https://dashboard.stripe.com/account/apikeys)

For local testing of webhooks you can use [ngrok](https://ngrok.com/). Possible steps:
* Download the ngrok
* Create a tunnel: ngrok http 8080
* Add endpoint (something like http://5e6c9f8b.ngrok.io) on the [webhooks](https://dashboard.stripe.com/account/webhooks) page.

## Building the application
* gradlew.bat clean build
* Find the *payment.jar* file in the *build/libs* folder

## Running the application
* For the *prod* profile: 
    - java -jar -Dspring.profiles.active=prod payment.jar
* For the *test* profile: 
    - java -jar -Dspring.profiles.active=test payment.jar
* For the *dev* profile: 
    - java -jar -Dspring.profiles.active=dev payment.jar
    - java -jar payment.jar