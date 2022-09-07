import { ActiveLogin } from "src/model/postgres/ActiveLogin";
import { LoginUser } from "src/model/postgres/LoginUser";

export enum AuthFunction {
    LOGIN = "LOGIN",
    REGISTER = "REGISTER",
    LINK_ACCOUNT = "LINK_ACCOUNT",
}

export interface AuthStateData {
    function?: AuthFunction;
}

export interface AuthResult {
    login: ActiveLogin;
    user?: LoginUser;
}
