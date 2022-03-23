package bio.ferlab.keycloak.authenticators;

import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.authenticators.broker.IdpReviewProfileAuthenticator;
import org.keycloak.authentication.authenticators.broker.IdpReviewProfileAuthenticatorFactory;
import org.keycloak.authentication.authenticators.broker.util.SerializedBrokeredIdentityContext;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.representations.idm.IdentityProviderRepresentation;

import static org.keycloak.services.validation.Validation.isBlank;

public class CustomReviewProfileAuthenticator extends IdpReviewProfileAuthenticator {
    private static final Logger logger = Logger.getLogger(CustomReviewProfileAuthenticator.class);


    protected boolean requiresUpdateProfilePage(AuthenticationFlowContext context, SerializedBrokeredIdentityContext userCtx, BrokeredIdentityContext brokerContext) {
        String enforceUpdateProfile = context.getAuthenticationSession().getAuthNote(ENFORCE_UPDATE_PROFILE);
        if (Boolean.parseBoolean(enforceUpdateProfile)) {
            return true;
        }

        String updateProfileFirstLogin;
        AuthenticatorConfigModel authenticatorConfig = context.getAuthenticatorConfig();
        if (authenticatorConfig == null || !authenticatorConfig.getConfig().containsKey(IdpReviewProfileAuthenticatorFactory.UPDATE_PROFILE_ON_FIRST_LOGIN)) {
            updateProfileFirstLogin = IdentityProviderRepresentation.UPFLM_MISSING;
        } else {
            updateProfileFirstLogin = authenticatorConfig.getConfig().get(IdpReviewProfileAuthenticatorFactory.UPDATE_PROFILE_ON_FIRST_LOGIN);
        }

        boolean userProfileComplete = !isBlank(userCtx.getFirstAttribute("user.profile.complete"));
        return IdentityProviderRepresentation.UPFLM_ON.equals(updateProfileFirstLogin)
                || (IdentityProviderRepresentation.UPFLM_MISSING.equals(updateProfileFirstLogin) && !userProfileComplete);
    }
//
//    @Override
//    protected void actionImpl(AuthenticationFlowContext context, SerializedBrokeredIdentityContext userCtx, BrokeredIdentityContext brokerContext) {
//
//        super.actionImpl(context, userCtx, brokerContext);
//
//        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
//        List<FormMessage> errors = FormUtils.validate(formData);
//
//        if (errors != null && !errors.isEmpty()) {
//            // Merge with errors that were potentially added by super.actionImpl()
//            errors.forEach(error -> context.form().addError(error));
//
//            Response challenge = context.form()
//                    .setAttribute(LoginFormsProvider.UPDATE_PROFILE_CONTEXT_ATTR, userCtx)
//                    .setFormData(formData)
//                    .createUpdateProfilePage();
//
//            context.challenge(challenge);
//            return;
//        }
//
//        // Update user extended attributes.
//        try{
//            String email = context.getHttpRequest().getDecodedFormParameters().getFirst("email");
//            UserModel userModel = context.getSession().users().getUserByEmail(email, context.getRealm());
//            AttributeUserProfile updatedProfile = AttributeFormDataProcessor.toUserProfile(formData);
//            FormUtils.updateUserAttributes(userModel, updatedProfile);
//        }catch(Exception e){
//            logger.error("Failed to update user attributes.", e);
//        }
//    }
}
