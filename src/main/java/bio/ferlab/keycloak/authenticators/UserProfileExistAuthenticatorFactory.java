package bio.ferlab.keycloak.authenticators;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.authentication.ConfigurableAuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;

public class UserProfileExistAuthenticatorFactory implements AuthenticatorFactory, ConfigurableAuthenticatorFactory {

    private static final UserProfileExistAuthenticator SINGLETON = new UserProfileExistAuthenticator();

    @Override
    public String getDisplayType() {
        return "Verify User Profile exist authenticator";
    }

    @Override
    public String getReferenceCategory() {
        return "Verify  User Profile exist authenticator";
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return "Connect to user profile api to verify if user profile exist";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }

    private static final List<ProviderConfigProperty> configProperties = new ArrayList<ProviderConfigProperty>();

    static {
        ProviderConfigProperty userProfileUri = new ProviderConfigProperty();
        userProfileUri.setName("userProfileApiUri");
        userProfileUri.setLabel("URI of user profile api");
        userProfileUri.setType(ProviderConfigProperty.STRING_TYPE);
        configProperties.add(userProfileUri);

        ProviderConfigProperty errorMessage = new ProviderConfigProperty();
        errorMessage.setName("errorMessage");
        errorMessage.setLabel("Message to display to users if profile does not exist");
        errorMessage.setType(ProviderConfigProperty.STRING_TYPE);
        configProperties.add(errorMessage);

        ProviderConfigProperty excludedClient = new ProviderConfigProperty();
        excludedClient.setName("excludedClients");
        excludedClient.setLabel("Clients to exclude for profile verificaction, separate by comma.");
        excludedClient.setType(ProviderConfigProperty.STRING_TYPE);
        configProperties.add(excludedClient);

    }

    @Override
    public Authenticator create(KeycloakSession keycloakSession) {
        return SINGLETON;
    }

    @Override
    public void init(Config.Scope scope) {

    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return "user-profile-exist-authenticator";
    }

    private static AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED,
            AuthenticationExecutionModel.Requirement.ALTERNATIVE,
            AuthenticationExecutionModel.Requirement.DISABLED
    };
    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }
}
