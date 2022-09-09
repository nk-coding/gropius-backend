import { Injectable } from "@nestjs/common";
import { StrategyInstanceService } from "src/model/services/strategy-instance.service";
import { StrategiesService } from "../strategies.service";
import { Strategy, StrategyUsingPassport, StrategyVariable } from "../Strategy";
import * as passportLocal from "passport-local";
import { StrategyInstance } from "src/model/postgres/StrategyInstance";
import * as passport from "passport";
import { LoginUserService } from "src/model/services/login-user.service";
import { UserLoginDataService } from "src/model/services/user-login-data.service";
import { ActiveLogin } from "src/model/postgres/ActiveLogin";
import { LoginUser } from "src/model/postgres/LoginUser";
import { AuthResult } from "../AuthResult";

@Injectable()
export class UserpassStrategyService extends StrategyUsingPassport {
    constructor(
        strategiesService: StrategiesService,
        strategyInstanceService: StrategyInstanceService,
        private readonly loginDataService: UserLoginDataService,
        private readonly loginUserService: LoginUserService,
    ) {
        super("userpass", strategyInstanceService, true, false, false, false);
        strategiesService.addStrategy("userpass", this);
    }

    override get acceptsVariables(): {
        [variableName: string]: StrategyVariable;
    } {
        return {
            username: {
                name: "string",
                displayName: "Username",
                type: "string",
            },
            password: {
                name: "string",
                displayName: "Password",
                type: "string",
            },
        };
    }

    protected override checkInstanceConfig(instanceConfig: object): boolean {
        return Object.keys(instanceConfig).length === 0;
    }

    public override createPassportStrategyInstance(
        strategyInstance: StrategyInstance,
    ): passport.Strategy {
        const loginDataService = this.loginDataService;
        const loginUserService = this.loginUserService;
        return new passportLocal.Strategy(
            {},
            async (
                username,
                password,
                done: (err: any, user: AuthResult | false, info: any) => any,
            ) => {
                const dataActiveLogin = {};
                const dataUserLoginData = {
                    password,
                };
                const loginDataCandidates =
                    await loginDataService.findForStrategyWithDataContaining(
                        strategyInstance,
                        { password },
                    );
                const loginDataForCorrectUser = await loginDataService
                    .createQueryBuilder("loginData")
                    .leftJoinAndSelect(`loginData.user`, "user")
                    .where(`user.username = :username`, { username })
                    .andWhereInIds(
                        loginDataCandidates.map((candidate) => candidate.id),
                    )
                    .getMany();
                if (loginDataForCorrectUser.length != 1) {
                    done(
                        null,
                        { dataActiveLogin, dataUserLoginData },
                        { message: "Username or password incorrect" },
                    );
                }
                console.log(`Auth for ${username} with ${password}`);
                done(
                    null,
                    {
                        loginData: loginDataForCorrectUser[0],
                        dataActiveLogin,
                        dataUserLoginData,
                    },
                    {},
                );
            },
        );
    }
}
