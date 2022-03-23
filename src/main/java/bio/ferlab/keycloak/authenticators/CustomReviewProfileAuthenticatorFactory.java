package bio.ferlab.keycloak.authenticators;

import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.authenticators.broker.IdpReviewProfileAuthenticatorFactory;
import org.keycloak.models.KeycloakSession;

public class CustomReviewProfileAuthenticatorFactory extends IdpReviewProfileAuthenticatorFactory {
    static CustomReviewProfileAuthenticator SINGLETON = new CustomReviewProfileAuthenticator();

    @Override
    public Authenticator create(KeycloakSession session) {
        return SINGLETON;
    }

    @Override
    public String getId() {
        return "custom-review-profile";
    }

    @Override
    public String getDisplayType() {
        return "Custom Review Profile";
    }
}