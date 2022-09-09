import { ActiveLogin } from "src/model/postgres/ActiveLogin";
import { LoginUser } from "src/model/postgres/LoginUser";
import { UserLoginData } from "src/model/postgres/UserLoginData";

export enum AuthFunction {
    LOGIN = "LOGIN",
    REGISTER = "REG",
    REGISTER_WITH_SYNC = "REG_SYNC",
    LINK_ACCOUNT = "LINK",
    LINK_ACCOUNT_WITH_SYNC = "LINK_SYNC",
}

export interface AuthStateData {
    function?: AuthFunction;
    loggedInUser?: LoginUser | string;
    activeLogin?: ActiveLogin | string;
    authErrorMessage?: string;
    authErrorType?: string;
}

export interface AuthResult {
    dataActiveLogin: object;
    dataUserLoginData: object;
    loginData?: UserLoginData;
}