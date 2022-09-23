import { Inject, Injectable } from "@nestjs/common";
import { StrategyInstanceService } from "src/model/services/strategy-instance.service";
import { StrategiesService } from "../../model/services/strategies.service";
import * as passportOauth from "passport-oauth2";
import { StrategyInstance } from "src/model/postgres/StrategyInstance.entity";
import * as passport from "passport";
import { LoginUserService } from "src/model/services/login-user.service";
import { UserLoginDataService } from "src/model/services/user-login-data.service";
import { AuthFunction, AuthResult, AuthStateData } from "../AuthResult";
import { StrategyUsingPassport } from "../StrategyUsingPassport";
import { JwtService } from "@nestjs/jwt";
import { UserLoginData } from "src/model/postgres/UserLoginData.entity";
import { ActiveLoginService } from "src/model/services/active-login.service";
import { checkType } from "../utils";

@Injectable()
export class OauthStrategyService extends StrategyUsingPassport {
    constructor(
        strategiesService: StrategiesService,
        strategyInstanceService: StrategyInstanceService,
        private readonly loginDataService: UserLoginDataService,
        private readonly loginUserService: LoginUserService,
        @Inject("PassportStateJwt")
        passportJwtService: JwtService,
        private readonly activeLoginService: ActiveLoginService,
    ) {
        super("oauth", strategyInstanceService, strategiesService, passportJwtService, true, true, true, true);
    }

    protected override checkInstanceConfig(instanceConfig: object): boolean | string {
        const checkResults = [
            super.checkInstanceConfig(instanceConfig),
            checkType(instanceConfig, "authorizationUrl", "string"),
            checkType(instanceConfig, "tokenUrl", "string"),
            checkType(instanceConfig, "clientId", "string"),
            checkType(instanceConfig, "clientSecret", "string"),
        ];
        const error = checkResults.find((v) => v !== true);
        if (error != undefined) {
            return error;
        }
        try {
            new URL(instanceConfig["authorizationUrl"]);
            new URL(instanceConfig["tokenUrl"]);
        } catch (err) {
            console.error(err);
            return err.message ?? err;
        }
    }

    override async getSyncTokenForLoginData(loginData: UserLoginData): Promise<string | null> {
        const syncLogins = (
            await this.activeLoginService.findValidForLoginDataSortedByExpiration(loginData, true)
        ).filter((login) => !!login.data["accessToken"]);
        return syncLogins[0]?.data["accessToken"] ?? null;
    }

    override getImsUserTemplatedValuesForLoginData(loginData: UserLoginData): object {
        return {
            username: loginData.data["username"],
        };
    }

    override getLoginDataDataForImsUserTemplatedFields(imsUser: object): object | Promise<object> {
        return {
            username: imsUser["username"],
        };
    }

    protected override getAdditionalPassportOptions(
        strategyInstance: StrategyInstance,
        authStateData: object | AuthStateData,
    ): passport.AuthenticateOptions {
        const mode = (authStateData as AuthStateData).function;
        if (mode == AuthFunction.REGISTER_WITH_SYNC) {
            return {
                scope: "repo",
            };
        } else {
            return {};
        }
    }

    public override createPassportStrategyInstance(strategyInstance: StrategyInstance): passport.Strategy {
        const loginDataService = this.loginDataService;
        return new passportOauth.Strategy(
            {
                authorizationURL:
                    strategyInstance.instanceConfig["authorizationUrl"] ?? "https://github.com/login/oauth/authorize",
                clientID: strategyInstance.instanceConfig["clientId"] ?? process.env.GROPIUS_OAUTH_CLIENT_ID,
                clientSecret:
                    strategyInstance.instanceConfig["clientSecret"] ?? process.env.GROPIUS_OAUTH_CLIENT_SECRET,
                tokenURL: strategyInstance.instanceConfig["tokenUrl"] ?? "https://github.com/login/oauth/access_token",
                store: {
                    store: (req, state, meta, callback) => callback(null, state),
                    verify: (req, providedState, callback) => callback(null, true, providedState),
                } as any,
            },
            (
                accessToken: string,
                refreshToken: string,
                profile: any,
                done: (err, user: AuthResult | false, info) => void,
            ) => {
                fetch("https://api.github.com/user", {
                    headers: {
                        Authorization: `Bearer ${accessToken}`,
                    },
                })
                    .then((res) => res.json())
                    .then(async (user) => {
                        const username = user.login;
                        const dataActiveLogin = {
                            accessToken,
                            refreshToken,
                        };
                        const dataUserLoginData = {
                            username,
                        };
                        const loginDataCandidates = await loginDataService.findForStrategyWithDataContaining(
                            strategyInstance,
                            {
                                username,
                            },
                        );
                        if (loginDataCandidates.length != 1) {
                            console.error("Oauth login didn's find unique login data", loginDataCandidates);
                            done(
                                null,
                                {
                                    dataActiveLogin,
                                    dataUserLoginData,
                                    mayRegister: true,
                                },
                                {
                                    message: "No unique user found",
                                },
                            );
                        } else {
                            done(
                                null,
                                {
                                    loginData: loginDataCandidates[0],
                                    dataActiveLogin,
                                    dataUserLoginData,
                                    mayRegister: true,
                                },
                                {},
                            );
                        }
                    })
                    .catch((err) => done(err, false, {}));
            },
        );
    }
}
