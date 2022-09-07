import { Injectable } from "@nestjs/common";
import { StrategyInstanceService } from "src/model/services/strategy-instance.service";
import { StrategiesService } from "../strategies.service";
import { Strategy, StrategyUsingPassport } from "../Strategy";
import * as passportOauth from "passport-oauth2";
import { StrategyInstance } from "src/model/postgres/StrategyInstance";
import * as passport from "passport";
import { LoginUserService } from "src/model/services/login-user.service";
import { UserLoginDataService } from "src/model/services/user-login-data.service";
import { ActiveLogin } from "src/model/postgres/ActiveLogin";
import { LoginUser } from "src/model/postgres/LoginUser";
import { AuthResult } from "../AuthResult";

@Injectable()
export class OauthStrategyService extends StrategyUsingPassport {
    constructor(
        strategiesService: StrategiesService,
        strategyInstanceService: StrategyInstanceService,
        private readonly loginDataService: UserLoginDataService,
        private readonly loginUserService: LoginUserService,
    ) {
        super("oauth", strategyInstanceService, true, false, true, true);
        strategiesService.addStrategy("oauth", this);
    }

    protected override checkInstanceConfig(instanceConfig: object): boolean {
        return Object.keys(instanceConfig).length === 0; //todo
    }

    public override createPassportStrategyInstance(
        strategyInstance: StrategyInstance,
    ): passport.Strategy {
        const loginDataService = this.loginDataService;
        const loginUserService = this.loginUserService;
        return new passportOauth.Strategy(
            {
                authorizationURL: "https://github.com/login/oauth/authorize",
                clientID: process.env.GROPIUS_OAUTH_CLIENT_ID,
                clientSecret: process.env.GROPIUS_OAUTH_CLIENT_SECRET,
                tokenURL: "https://github.com/login/oauth/access_token",
                store: {
                    store: (req, state, meta, callback) =>
                        callback(null, state),
                    verify: (req, providedState, callback) =>
                        callback(null, true, providedState),
                } as any,
            },
            (
                accessToken: string,
                refreshToken: string,
                profile: any,
                done: (err, user: AuthResult | false, info) => void,
            ) => {
                /*const user = loginUserService.findOneBy({ username });
                if (!user) {
                    done(null, false, { message: "Username unknown" });
                }
                console.log(`Auth for ${username} with ${password}`);
                done(null, user);*/
                console.log("Got access token", accessToken, refreshToken);
                fetch("https://api.github.com/user", {
                    headers: { Authorization: `Bearer ${accessToken}` },
                })
                    .then((res) => res.json())
                    .then(async (user) => {
                        const username = user.login;
                        const loginUser = await loginUserService.findOneBy({
                            username,
                        });
                        if (!loginUser) {
                            done(null, false, { message: "User not found" });
                        }
                        done(
                            null,
                            { user: loginUser, login: undefined as any },
                            {},
                        );
                    })
                    .catch((err) => done(err, false, {}));
            },
        );
    }
}
