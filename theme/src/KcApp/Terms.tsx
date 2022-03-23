import {memo} from "react";
import {Template} from "keycloakify/lib/components/Template";
import type {KcProps} from "keycloakify";
import {useKcMessage} from "keycloakify/lib/i18n/useKcMessage";
import {useCssAndCx} from "tss-react";
import {KcContext} from "./kcContext";

type KcContext_Terms = Extract<KcContext, { pageId: "terms.ftl"; }>;

export const Terms = memo(({ kcContext, ...props }: { kcContext: KcContext_Terms } & KcProps) => {
    const { msg, msgStr } = useKcMessage();

    const { cx } = useCssAndCx();

    const { url } = kcContext;

    return (
        <Template
            {...{ kcContext, ...props }}
            doFetchDefaultThemeResources={true}
            displayMessage={false}
            headerNode={msg("termsTitle")}
            formNode={
                <>
                    <h1>Heloooooo</h1>
                    <div id="kc-terms-text">{msg("termsText")}</div>
                    <form className="form-actions" action={url.loginAction} method="POST">
                        <input
                            className={cx(
                                props.kcButtonClass,
                                props.kcButtonClass,
                                props.kcButtonClass,
                                props.kcButtonPrimaryClass,
                                props.kcButtonLargeClass,
                            )}
                            name="accept"
                            id="kc-accept"
                            type="submit"
                            value={msgStr("doAccept")}
                        />
                        <input
                            className={cx(props.kcButtonClass, props.kcButtonDefaultClass, props.kcButtonLargeClass)}
                            name="cancel"
                            id="kc-decline"
                            type="submit"
                            value={msgStr("doDecline")}
                        />
                    </form>
                    <div className="clearfix" />
                </>
            }
        />
    );
});