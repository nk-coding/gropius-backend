import { Injectable } from "@nestjs/common";
import { StrategyInstanceService } from "src/model/services/strategy-instance.service";
import { StrategiesService } from "../strategies.service";
import { Strategy, StrategyUsingPassport } from "../Strategy";
import * as passportLocal from "passport-local";
import { StrategyInstance } from "src/model/postgres/StrategyInstance";
import * as passport from "passport";
import { LoginUserService } from "src/model/services/login-user.service";
import { UserLoginDataService } from "src/model/services/user-login-data.service";
import { ActiveLogin } from "src/model/postgres/ActiveLogin";
import { LoginUser } from "src/model/postgres/LoginUser";

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

    override get acceptsVariables(): { [variableName: string]: string } {
        return {
            username: "string",
            password: "string",
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
        return new passportLocal.Strategy({}, (username, password, done) => {
            const user = loginUserService.findOneBy({ username });
            if (!user) {
                done(null, null, { message: "Username unknown" });
            }
            console.log(`Auth for ${username} with ${password}`);
            done(null, user);
        });
    }
}
