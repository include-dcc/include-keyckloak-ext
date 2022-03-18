package bio.ferlab.keycloak.authenticators;

import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import java.util.Arrays;
import java.util.List;

public class EmailWhitelistAuthenticator implements Authenticator {
    private static final Logger logger = Logger.getLogger(EmailWhitelistAuthenticator.class);

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        AuthenticatorConfigModel config = context.getAuthenticatorConfig();
        List<String> emails = Arrays.asList(config.getConfig().get("emailWhitelist").split("\n"));
        String email = context.getUser().getEmail();
        if (email != null && emails.contains(email)) {
            context.success();
        } else {
            logger.warn("User email not in whitelist");
            context.failure(AuthenticationFlowError.ACCESS_DENIED);
        }
    }

    @Override
    public void action(AuthenticationFlowContext context) {

    }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {

    }

    @Override
    public void close() {

    }
}
