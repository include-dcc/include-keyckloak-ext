package bio.ferlab.keycloak.authenticators;

import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.requiredactions.util.UpdateProfileContext;
import org.keycloak.authentication.requiredactions.util.UserUpdateProfileContext;
import org.keycloak.events.EventBuilder;
import org.keycloak.events.EventType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.FormMessage;
import org.keycloak.services.validation.Validation;
import org.keycloak.userprofile.UserProfile;
import org.keycloak.userprofile.UserProfileContext;
import org.keycloak.userprofile.UserProfileProvider;
import org.keycloak.userprofile.ValidationException;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

import static org.keycloak.authentication.authenticators.broker.AbstractIdpAuthenticator.ENFORCE_UPDATE_PROFILE;
import static org.keycloak.forms.login.LoginFormsProvider.UPDATE_PROFILE_CONTEXT_ATTR;
import static org.keycloak.services.validation.Validation.isBlank;

public class UserProfileExistAuthenticator implements Authenticator {
    private static final Logger logger = Logger.getLogger(UserProfileExistAuthenticator.class);

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        UserModel user = context.getUser();

        if (userExists(context)) {
            context.success();
        } else {
            UpdateProfileContext userBasedContext = new UserUpdateProfileContext(context.getRealm(), user);
            Response challenge = context.form()
                    .setAttribute(UPDATE_PROFILE_CONTEXT_ATTR, userBasedContext)
                    .setFormData(null)
                    .createUpdateProfilePage();
            context.challenge(challenge);
        }

    }

    private boolean userExists(AuthenticationFlowContext context)  {
        String enforceUpdateProfile = context.getAuthenticationSession().getAuthNote(ENFORCE_UPDATE_PROFILE);
        if (Boolean.parseBoolean(enforceUpdateProfile)) {
            return true;
        }
        return !isBlank(context.getUser().getFirstAttribute("isComplete"));
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        EventBuilder event = context.getEvent();
        event.event(EventType.UPDATE_PROFILE);
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        UserModel user = context.getUser();

        // These 3 attributes should not be replaced
        formData.replace(UserModel.FIRST_NAME, Collections.singletonList(user.getFirstName()));
        formData.replace(UserModel.LAST_NAME, Collections.singletonList(user.getLastName()));
        formData.replace(UserModel.EMAIL, Collections.singletonList(user.getEmail()));
        formData.addFirst("user.attributes.isComplete", "true");

        UserProfileProvider provider = context.getSession().getProvider(UserProfileProvider.class);
        UserProfile profile = provider.create(UserProfileContext.UPDATE_PROFILE, formData, user);
        try {
            // backward compatibility with old account console where attributes are not removed if missing
            profile.update(false);

            context.success();
        } catch (ValidationException pve) {
            List<FormMessage> errors = Validation.getFormErrorsFromValidation(pve.getErrors());
            Response challenge = context.form()
                    .setErrors(errors)
                    .setFormData(formData)
                    .createResponse(UserModel.RequiredAction.UPDATE_PROFILE);
            context.challenge(challenge);
        }

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
