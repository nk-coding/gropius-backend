import { Injectable } from "@nestjs/common";
import { ActiveLogin } from "src/model/postgres/ActiveLogin";
import { LoginUser } from "src/model/postgres/LoginUser";
import { StrategyInstance } from "src/model/postgres/StrategyInstance";
import { LoginState, UserLoginData } from "src/model/postgres/UserLoginData";
import { ActiveLoginService } from "src/model/services/active-login.service";
import { LoginUserService } from "src/model/services/login-user.service";
import { UserLoginDataService } from "src/model/services/user-login-data.service";
import { AuthStateData, AuthFunction, AuthResult } from "./AuthResult";
import { StrategiesService } from "./strategies.service";

/**
 * Contains the logic how the system is supposed to create and link login data ans active logins when users authenticate.
 * Defines how the sign up and sign in prcess work from the point when passport has processed the request and returned information about the credentials
 */
@Injectable()
export class PerformAuthFunctionService {
    constructor(
        private readonly loginUserService: LoginUserService,
        private readonly activeLoginService: ActiveLoginService,
        private readonly userLoginDataService: UserLoginDataService,
        private readonly strategiesService: StrategiesService,
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

    private async createActiveLogin(
        instance: StrategyInstance,
        data: object,
        loginData: UserLoginData,
        supportsSync: boolean,
    ): Promise<ActiveLogin> {
        const activeLogin = new ActiveLogin(instance);
        activeLogin.data = data;
        activeLogin.loginInstanceFor = Promise.resolve(loginData);
        activeLogin.supportsSync = supportsSync;
        activeLogin.expires = new Date(
            Date.now() +
                parseInt(
                    process.env.GROPIUS_REGISTRATION_EXPIRATION_TIME_MS,
                    10,
                ),
        );
        return this.activeLoginService.save(activeLogin);
    }

    private async loginExistingUser(
        authResult: AuthResult,
        instance: StrategyInstance,
    ): Promise<AuthStateData> {
        console.log("Logging in");
        const loggedInUser = await authResult.loginData.user;
        return {
            activeLogin: await this.createActiveLogin(
                instance,
                authResult.dataActiveLogin,
                authResult.loginData,
                false,
            ),
            loggedInUser: loggedInUser,
        };
    }

    private async continueExistingRegistration(
        authResult: AuthResult,
        instance: StrategyInstance,
        supportsSync: boolean,
    ): Promise<AuthStateData> {
        const loggedInUser = await authResult.loginData.user;
        let loginData = authResult.loginData;
        loginData.data = authResult.dataUserLoginData;
        loginData.expires = new Date(
            Date.now() +
                parseInt(
                    process.env.GROPIUS_REGISTRATION_EXPIRATION_TIME_MS,
                    10,
                ),
        );
        loginData = await this.userLoginDataService.save(loginData);
        return {
            activeLogin: await this.createActiveLogin(
                instance,
                authResult.dataActiveLogin,
                authResult.loginData,
                supportsSync,
            ),
            loggedInUser: loggedInUser,
        };
    }

    private async registerNewUser(
        authResult: AuthResult,
        instance: StrategyInstance,
        supportsSync: boolean,
    ): Promise<AuthStateData> {
        console.log(
            "Registering new user with login data",
            authResult.dataUserLoginData,
        );
        let loginData = new UserLoginData();
        loginData.data = authResult.dataUserLoginData;
        loginData.expires = new Date(
            Date.now() +
                parseInt(
                    process.env.GROPIUS_REGISTRATION_EXPIRATION_TIME_MS,
                    10,
                ),
        );
        loginData.state = LoginState.WAITING_FOR_REGISTER;
        loginData.strategyInstance = Promise.resolve(instance);
        loginData = await this.userLoginDataService.save(loginData);
        return {
            activeLogin: await this.createActiveLogin(
                instance,
                authResult.dataActiveLogin,
                loginData,
                supportsSync,
            ),
            loggedInUser: null,
        };
        // Todo: Answer must contain temporary api key
    }

    private async getUserToLinkWith(
        state: AuthStateData,
    ): Promise<LoginUser | null> {
        if (typeof state.loggedInUser == "string") {
            return await this.loginUserService.findOneBy({
                id: state.loggedInUser,
            });
        } else if (
            typeof state.loggedInUser == "object" &&
            typeof state.loggedInUser.id == "string"
        ) {
            return await this.loginUserService.findOneBy({
                id: state.loggedInUser.id,
            });
        } else {
            return null;
        }
    }

    private async linkAccountToUser(
        authResult: AuthResult,
        state: AuthStateData,
        instance: StrategyInstance,
        supportsSync: boolean,
    ): Promise<AuthStateData> {
        const loggedInUser = await this.getUserToLinkWith(state);
        if (loggedInUser == null) {
            return {
                authErrorMessage:
                    "You need to be logged in to link credentials to an account",
                authErrorType: "access_denied",
            };
        }
        let loginData = new UserLoginData();
        loginData.data = authResult.dataUserLoginData;
        loginData.state = LoginState.VALID;
        loginData.strategyInstance = Promise.resolve(instance);
        loginData.user = Promise.resolve(loggedInUser);
        loginData = await this.userLoginDataService.save(loginData);
        return {
            activeLogin: await this.createActiveLogin(
                instance,
                authResult.dataActiveLogin,
                loginData,
                supportsSync,
            ),
            loggedInUser: await loginData.user,
        };
    }

    private async linkExistingAccountIfSameUser(
        authResult: AuthResult,
        state: AuthStateData,
        instance: StrategyInstance,
        supportsSync: boolean,
    ): Promise<AuthStateData> {
        const loggedInUser = await this.getUserToLinkWith(state);
        const loginDataUser = await authResult.loginData.user;
        if (loggedInUser != null && loggedInUser.id !== loginDataUser.id) {
            return {
                authErrorMessage:
                    "This account is already registered and linked to a Gropius account",
            };
        }
        let loginData = authResult.loginData;
        loginData.data = authResult.dataUserLoginData;
        loginData = await this.userLoginDataService.save(loginData);
        return {
            activeLogin: await this.createActiveLogin(
                instance,
                authResult.dataActiveLogin,
                loginData,
                supportsSync,
            ),
            loggedInUser: await loginData.user,
        };
    }

    public async performRequestedAction(
        authResult: AuthResult,
        state: AuthStateData,
        instance: StrategyInstance,
    ): Promise<AuthStateData> {
        if (authResult.loginData) {
            // sucessfully found login data matching the authentication
            if (
                authResult.loginData.expires != null &&
                authResult.loginData.expires <= new Date()
            ) {
                // Found login data is expired => shouldn't happen as expired login data are filtered when searhcing for them
                return {
                    authErrorMessage:
                        "The login using this strategy instance has expired. If you were just registering, try starting the registration again. If this error happens again, something internally went wrong.",
                };
            }
            if (
                state.function == AuthFunction.REGISTER ||
                state.function == AuthFunction.REGISTER_WITH_SYNC
            ) {
                if (
                    authResult.loginData.state ==
                    LoginState.WAITING_FOR_REGISTER
                ) {
                    return this.continueExistingRegistration(
                        authResult,
                        instance,
                        state.function == AuthFunction.REGISTER_WITH_SYNC,
                    );
                } else {
                    return {
                        authErrorMessage: "This account is already registered",
                    };
                }
            } else if (
                state.function == AuthFunction.LINK_ACCOUNT ||
                state.function == AuthFunction.LINK_ACCOUNT_WITH_SYNC
            ) {
                if (
                    authResult.loginData.state !=
                    LoginState.WAITING_FOR_REGISTER
                ) {
                    this.linkExistingAccountIfSameUser(
                        authResult,
                        state,
                        instance,
                        state.function == AuthFunction.LINK_ACCOUNT_WITH_SYNC,
                    );
                } else {
                    return {
                        authErrorMessage:
                            "The credentials you are trying to link to are currently in a registration process. Please finish it or wait until it expires",
                    };
                }
            } else if (state.function == AuthFunction.LOGIN) {
                switch (authResult.loginData.state) {
                    case LoginState.WAITING_FOR_REGISTER:
                        return {
                            authErrorMessage:
                                "For these credentials a registration process is still running. Complete (or restart) the registration before logging in",
                        };
                    case LoginState.BLOCKED:
                        return {
                            authErrorMessage:
                                "The login to this account using this specific strategy instance was blocked by the administrator.",
                        };
                    case LoginState.VALID:
                        return this.loginExistingUser(authResult, instance);
                }
            }
        } else {
            if (
                state.function == AuthFunction.REGISTER ||
                state.function == AuthFunction.REGISTER_WITH_SYNC ||
                (instance.doesImplicitRegister &&
                    state.function == AuthFunction.LOGIN)
            ) {
                return this.registerNewUser(
                    authResult,
                    instance,
                    state.function == AuthFunction.REGISTER_WITH_SYNC,
                );
            } else if (
                state.function == AuthFunction.LINK_ACCOUNT ||
                state.function == AuthFunction.LINK_ACCOUNT_WITH_SYNC
            ) {
                if (!state.loggedInUser) {
                    if (instance.doesImplicitRegister) {
                        return this.registerNewUser(
                            authResult,
                            instance,
                            state.function ==
                                AuthFunction.LINK_ACCOUNT_WITH_SYNC,
                        );
                    } else {
                        return {
                            authErrorMessage:
                                "You must be logged in in order to link a new strategy instance account to your gropius account",
                        };
                    }
                } else {
                    return this.linkAccountToUser(
                        authResult,
                        state,
                        instance,
                        state.function == AuthFunction.LINK_ACCOUNT_WITH_SYNC,
                    );
                }
            } else if (
                state.function == AuthFunction.LOGIN &&
                !instance.doesImplicitRegister
            ) {
                return { authErrorMessage: "Invalid user credentials." };
            }
        }
        return { authErrorMessage: "Unknown error during authentication" };
    }
}
