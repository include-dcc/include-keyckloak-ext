import { useState, memo } from "react";
import { Template } from "keycloakify/lib/components/Template";
import type { KcProps } from "keycloakify";
import { useKcMessage } from "keycloakify/lib/i18n/useKcMessage";
import { useCssAndCx } from "tss-react";
import { useConstCallback } from "powerhooks/useConstCallback";
import {KcContext} from "./kcContext";

type KcContext_Login = Extract<KcContext, { pageId: "login.ftl"; }>;
export const Login = memo(({ kcContext, ...props }: { kcContext: KcContext_Login } & KcProps) => {
    const { social, realm, url, usernameEditDisabled, login, auth, registrationDisabled } = kcContext;

    const { msg, msgStr } = useKcMessage();

    const { cx } = useCssAndCx();

    const [isLoginButtonDisabled, setIsLoginButtonDisabled] = useState(false);

    const onSubmit = useConstCallback(() => (setIsLoginButtonDisabled(true), true));

    return (
        <Template
            {...{ kcContext, ...props }}
            doFetchDefaultThemeResources={true}
            displayInfo={social.displayInfo}
            displayWide={realm.password && social.providers !== undefined}
            headerNode={msg("doLogIn")}
            formNode={
                <div id="kc-form" className={cx(realm.password && social.providers !== undefined && props.kcContentWrapperClass)}>
                    {realm.password && social.providers !== undefined && (
                        <div id="kc-social-providers" className={cx(props.kcFormSocialAccountContentClass, props.kcFormSocialAccountClass)}>
                            <ul
                                className={cx(
                                    props.kcFormSocialAccountListClass,
                                    social.providers.length > 4 && props.kcFormSocialAccountDoubleListClass,
                                )}
                            >
                                {social.providers.map(p => (
                                    <li key={p.providerId} className={cx(props.kcFormSocialAccountListLinkClass)}>
                                        <a href={p.loginUrl} id={`zocial-${p.alias}`} className={cx("zocial", p.providerId)}>
                                            <span>{p.displayName}</span>
                                        </a>
                                    </li>
                                ))}
                            </ul>
                        </div>
                    )}
                </div>
            }
            infoNode={
                realm.password &&
                realm.registrationAllowed &&
                !registrationDisabled && (
                    <div id="kc-registration">
                        <span>
                            {msg("noAccount")}
                            <a tabIndex={6} href={url.registrationUrl}>
                                {msg("doRegister")}
                            </a>
                        </span>
                    </div>
                )
            }
        />
    );
});