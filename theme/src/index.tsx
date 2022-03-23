import ReactDOM from "react-dom";
import {defaultKcProps, getKcContext} from "keycloakify";
// import { KcApp } from "keycloakify/lib/components/KcApp";
import {KcApp} from "./KcApp"
import { kcContext } from "./KcApp/kcContext";
import "./index.scss";

ReactDOM.render(
        <KcApp kcContext={kcContext!} {...{
            ...defaultKcProps,
            "kcHtmlClass": ["login-mdc"]
            // "kcFormCardClass": [...defaultKcProps.kcFormCardClass, classNames.kcFormCardClass],
            // "kcButtonPrimaryClass": [...defaultKcProps.kcButtonPrimaryClass, classNames.kcButtonPrimaryClass],
            // "kcInputClass": [...defaultKcProps.kcInputClass, classNames.kcInputClass]
        }}/>,
    document.getElementById("root")
);