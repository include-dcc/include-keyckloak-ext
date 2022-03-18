package bio.ferlab.keycloak.authenticators;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.connections.httpclient.HttpClientProvider;
import org.keycloak.models.*;
import org.keycloak.models.utils.FormMessage;
import org.keycloak.util.JsonSerialization;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UserProfileExistAuthenticator implements Authenticator {
    private static final Logger logger = Logger.getLogger(UserProfileExistAuthenticator.class);

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        AuthenticatorConfigModel config = context.getAuthenticatorConfig();
        String userApi = config.getConfig().get("userProfileApiUri");
        List<String> excludedClients = Arrays.asList(config.getConfig().get("excludedClients").split(","));

        String clientId = context.getAuthenticationSession().getClient().getClientId();

        try {
            if (excludedClients.contains(clientId) || userExists(context, userApi)) {
                context.success();
            } else {
                String errorMessage = config.getConfig().get("errorMessage");
                context.forkWithErrorMessage(new FormMessage(errorMessage));
            }

        } catch (IOException e) {
            context.failure(AuthenticationFlowError.INTERNAL_ERROR);
        }
    }

    private boolean userExists(AuthenticationFlowContext context, String userApi) throws IOException {
        CloseableHttpClient httpClient = context.getSession().getProvider(HttpClientProvider.class).getHttpClient();
        HttpGet get = new HttpGet(userApi + "/user/exists?user_id=" + context.getUser().getId());
        boolean exists = false;
        CloseableHttpResponse response = httpClient.execute(get);
        InputStream content = response.getEntity().getContent();
        try {
            if (response.getStatusLine().getStatusCode() == 200) {
                Map json = JsonSerialization.readValue(content, Map.class);
                Object val = json.get("exists");
                exists = Boolean.TRUE.equals(val);
            }

        } finally {
            EntityUtils.consumeQuietly(response.getEntity());
        }

        return exists;
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
