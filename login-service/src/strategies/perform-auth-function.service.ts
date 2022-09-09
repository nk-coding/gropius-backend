import { Injectable } from "@nestjs/common";
import { ActiveLogin, LoginState } from "src/model/postgres/ActiveLogin";
import { LoginUser } from "src/model/postgres/LoginUser";
import { StrategyInstance } from "src/model/postgres/StrategyInstance";
import { ActiveLoginService } from "src/model/services/active-login.service";
import { LoginUserService } from "src/model/services/login-user.service";
import { AuthStateData, AuthFunction, AuthResult } from "./AuthResult";

@Injectable()
export class PerformAuthFunctionService {
    private readonly REGISTRATION_EXPIRATION_TIME_SEC =
        process.env.GROPIUS_REGISTRATION_EXPIRATION_TIME_SEC || 600;

    constructor(
        private readonly loginUserService: LoginUserService,
        private readonly activeLoginService: ActiveLoginService,
    ) {}

    public checkFunctionIsAllowed(
        state: AuthStateData,
        instance: StrategyInstance,
    ): string | null {
        switch (state?.function) {
            case AuthFunction.REGISTER_WITH_SYNC:
            //Fallthrough as for instances without sync capability, registration will happen without sync
            case AuthFunction.REGISTER:
                if (!instance.isRegisterActive) {
                    return "Registration not enabled for this strategy instance";
                }
                break;
            case AuthFunction.LINK_ACCOUNT:
                if (!instance.isRegisterActive) {
                    return "Registration and simple linking using this strategy instance is disabled. Try linking with sync enabled.";
                }
                break;
            case AuthFunction.LINK_ACCOUNT_WITH_SYNC:
                if (!instance.isRegisterActive && !instance.isSyncActive) {
                    return "Registration and sync using this strategy are disabled. This disables any linking.";
                }
                break;
            case AuthFunction.LOGIN:
                if (!instance.isLoginActive) {
                    return "Login using this strategy instance not enabled";
                }
                break;
        }
        return null;
    }

    public async performRequestedAction(
        authResult: AuthResult,
        state: AuthStateData,
        instance: StrategyInstance,
    ): Promise<AuthStateData> {
        if (authResult.loginData) {
            // sucessfully authenticated as one user
            if (
                state.function == AuthFunction.REGISTER ||
                state.function == AuthFunction.REGISTER_WITH_SYNC
            ) {
                return {
                    authErrorMessage: "This account is already registered",
                };
            } else if (
                state.function == AuthFunction.LINK_ACCOUNT ||
                state.function == AuthFunction.LINK_ACCOUNT_WITH_SYNC
            ) {
                return {
                    authErrorMessage:
                        "This account is already registered and linked to a Gropius account",
                };
            } else if (state.function == AuthFunction.LOGIN) {
                const loggedInUser = await authResult.loginData.user;
                //todo: login user
                const activeLogin = new ActiveLogin(instance);
                activeLogin.state = LoginState.VALID_LOGIN;
                activeLogin.data = authResult.dataActiveLogin;
                activeLogin.loginInstanceFor = Promise.resolve(
                    authResult.loginData,
                );
                return {
                    activeLogin: await this.activeLoginService.save(
                        activeLogin,
                    ),
                    loggedInUser: loggedInUser,
                };
            }
        } else {
            if (
                state.function == AuthFunction.REGISTER ||
                state.function == AuthFunction.REGISTER_WITH_SYNC ||
                (instance.doesImplicitRegister &&
                    state.function == AuthFunction.LOGIN)
            ) {
                //todo register user
            } else if (
                state.function == AuthFunction.LINK_ACCOUNT ||
                state.function == AuthFunction.LINK_ACCOUNT_WITH_SYNC
            ) {
                let userToLinkWith: LoginUser;
                if (!state.loggedInUser) {
                    if (instance.doesImplicitRegister) {
                        // todo register user
                    } else {
                        return {
                            authErrorMessage:
                                "You must be logged in in order to link a new strategy instance account to your gropius account",
                        };
                    }
                } else if (typeof state.loggedInUser == "string") {
                    userToLinkWith = await this.loginUserService.findOneBy({
                        id: state.loggedInUser,
                    });
                } else if (
                    typeof state.loggedInUser == "object" &&
                    typeof state.loggedInUser.id == "string"
                ) {
                    userToLinkWith = await this.loginUserService.findOneBy({
                        id: state.loggedInUser.id,
                    });
                }
                //todo link account with logged in user
            }
        }
        return { authErrorMessage: "Unknown error during authentication" };
    }
}
