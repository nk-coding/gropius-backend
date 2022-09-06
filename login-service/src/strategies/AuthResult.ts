import { ActiveLogin } from "src/model/postgres/ActiveLogin";
import { LoginUser } from "src/model/postgres/LoginUser";

export enum AuthFunction {
    LOGIN = "LOGIN",
    REGISTER = "REGISTER",
    LINK_ACCOUNT = "LINK_ACCOUNT",
}

export interface AuthStateData {
    function: AuthFunction;
    auth_user: string;
    state: string;
}

export interface AuthResult {
    authStateData: AuthStateData | object;
    login: ActiveLogin;
    user?: LoginUser;
}
