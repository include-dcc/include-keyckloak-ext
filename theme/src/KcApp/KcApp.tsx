import {memo} from "react";

import {Login} from "./Login";
import {LoginUpdateProfile} from "./LoginUpdateProfile";
// import { Terms } from "./Terms";
import {KcApp as KcAppBase} from "keycloakify/lib/components/KcApp";
import "./kcMessagesExtension";
import {KcProps} from "keycloakify/lib/components/KcProps";
import {KcContextBase} from "keycloakify/lib/getKcContext/KcContextBase";
import {Terms} from "./Terms";

export const KcApp = memo(({ kcContext, ...props }: {
    kcContext: KcContextBase;
} & KcProps) => {
    switch (kcContext.pageId) {
        case "login.ftl":
            return <Login {...{kcContext, ...props}} />;
        case "login-update-profile.ftl":
            return <LoginUpdateProfile {...{kcContext, ...props}} />;
        case "terms.ftl": return <Terms {...{ kcContext, ...props }} />;
        // case "register.ftl":
        //     return <Register {...{kcContext, ...props}} />;
        // case "info.ftl":
        //     return <Info {...{kcContext, ...props}} />;
        // case "error.ftl":
        //     return <Error {...{kcContext, ...props}} />;
        default:
            return <KcAppBase {...{kcContext, ...props}} />;
    }
});