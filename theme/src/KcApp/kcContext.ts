import {getKcContext} from "keycloakify";

export const {kcContext} = getKcContext<
    {
        pageId: "login-update-profile.ftl";

    } |
    {
        pageId: "login.ftl";
    }>({
    /* Uncomment to test outside of keycloak, ⚠️ don't forget to run 'yarn keycloak' at least once */
    // "mockPageId": "terms.ftl",
    // "mockPageId": "login-update-profile.ftl",
    /**
     * Customize the simulated kcContext that will let us
     * dev the page outside keycloak (with auto-reload)
     */
    "mockData": [
        {
            "pageId": "login.ftl",
            "social": {
                "providers": [

                    {
                        "providerId": "google",
                        "alias": "google",
                        "displayName": "Google"
                    }
                ]
            },
            "registrationDisabled": true

        }
    ]
});

export type KcContext = NonNullable<typeof kcContext>;