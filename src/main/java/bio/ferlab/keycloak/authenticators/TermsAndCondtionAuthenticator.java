package bio.ferlab.keycloak.authenticators;

import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.common.util.Time;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.FormMessage;

import javax.ws.rs.core.Response;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.keycloak.services.validation.Validation.isBlank;

public class TermsAndCondtionAuthenticator implements Authenticator {
    private static final Logger logger = Logger.getLogger(TermsAndCondtionAuthenticator.class);
    public static final String USER_ATTRIBUTE = "terms_and_conditions";

    @Override
    public void authenticate(AuthenticationFlowContext context) {

        if (termsAndConditionAccepted(context)) {
            context.success();
        } else {
            Response challenge = context.form().createForm("terms.ftl");
            context.challenge(challenge);
        }

    }

    private boolean termsAndConditionAccepted(AuthenticationFlowContext context) {

        return !isBlank(context.getUser().getFirstAttribute(USER_ATTRIBUTE));
    }

    @Override
    public void action(AuthenticationFlowContext context) {

        if (context.getHttpRequest().getDecodedFormParameters().containsKey("cancel")) {
            context.getUser().removeAttribute(USER_ATTRIBUTE);
            List<FormMessage> errors = Collections.singletonList(new FormMessage("You must accept terms and conditions"));
            Response challenge = context.form()
                    .setErrors(errors)
                    .createForm("terms.ftl");
            context.challenge(challenge);
            return;
        }

        context.getUser().setAttribute(USER_ATTRIBUTE, Arrays.asList(Integer.toString(Time.currentTime())));

        context.success();
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
