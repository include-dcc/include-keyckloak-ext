Include Keycloak Extension
==========================

This project contain code for keycloak extension used in include.

- EmailWhiteListAuthenticator : Allow only users with configured email
- UserProfileExistAuthenticator : Aloow only users that have already a profile in the portal

Run this command to package and deploy in KC :

```
mvn clean package && cp target/include-keycloak-ext-1.0-SNAPSHOT.jar ~/workspace/keycloak-14.0.0/standalone/deployments      
```