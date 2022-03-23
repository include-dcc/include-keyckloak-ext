import {useState, memo} from "react";
import {Template} from "./Template";
import type {KcProps} from "keycloakify";
import "./kcMessagesExtension"
import {useKcMessage} from "keycloakify/lib/i18n/useKcMessage";
import {useCssAndCx} from "tss-react";
import {useConstCallback} from "powerhooks/useConstCallback";
import {KcContext} from "./kcContext";


const include_logo = require('./assets/include-logo-beta.svg')

type KcContext_Login = Extract<KcContext, { pageId: "login.ftl"; }>;
export const Login = memo(({kcContext, ...props}: { kcContext: KcContext_Login } & KcProps) => {
    const {social, realm, url, usernameEditDisabled, login, auth, registrationDisabled} = kcContext;

    const {msg, msgStr} = useKcMessage();

    const {cx} = useCssAndCx();

    const [isLoginButtonDisabled, setIsLoginButtonDisabled] = useState(false);

    const onSubmit = useConstCallback(() => (setIsLoginButtonDisabled(true), true));

    return (
        <Template
            {...{kcContext, ...props}}
            doFetchDefaultThemeResources={true}
            displayInfo={social.displayInfo}
            displayWide={realm.password && social.providers !== undefined}
            headerNode={false}
            formNode={
                <div className="column">
                    <div className="logo-container">
                        <a href="https://includedcc.org/portal/">
                            <img src={include_logo} className="image"/>
                        </a>
                    </div>
                    <div className="title">
                        <a href="https://includedcc.org/portal/">
                            <h3>
                                {realm.displayNameHtml}
                            </h3>
                        </a>
                    </div>
                    <div className="title">
                        <h2>
                            {/*{msg("loginWith")}*/}
                            Log in with
                        </h2>
                    </div>
                    <div id="kc-form"
                         className={cx(realm.password && social.providers !== undefined && props.kcContentWrapperClass)}>

                        {realm.password && social.providers !== undefined && (
                            <div id="kc-social-providers"
                                 className={cx(props.kcFormSocialAccountContentClass, props.kcFormSocialAccountClass)}>
                                <div className="ui list">
                                    {social.providers.map(p => (
                                        <div className="item">
                                            <a href={p.loginUrl} id={`zocial-${p.alias}`}
                                               className={cx("ui large fluid button zocial", p.providerId)}>
                                                {/*<img src="${url.resourcesPath}/img/${p.alias}-icon.svg"*/}
                                                {/*     className="social-icon" id="${p.alias}-icon"/>*/}
                                                <span className="sr-only">Log in with </span>
                                                <span className={cx(props.kcFormSocialAccountClass, "kc-social-icon-text")}>{p.displayName}</span>
                                            </a>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        )}
                    </div>
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