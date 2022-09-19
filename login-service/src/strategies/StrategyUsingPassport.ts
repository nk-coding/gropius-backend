import * as passport from "passport";
import { Strategy } from "./Strategy";
import { StrategyInstance } from "src/model/postgres/StrategyInstance";
import { AuthStateData, AuthResult } from "./AuthResult";
import { JwtService } from "@nestjs/jwt";
import { StrategyInstanceService } from "src/model/services/strategy-instance.service";

export abstract class StrategyUsingPassport extends Strategy {
    constructor(
        typeName: string,
        strategyInstanceService: StrategyInstanceService,
        protected readonly passportJwtService: JwtService,
        canLoginRegister = true,
        canSync = false,
        needsRedirectFlow = false,
        allowsImplicitSignup = false,
    ) {
        super(
            typeName,
            strategyInstanceService,
            canLoginRegister,
            canSync,
            needsRedirectFlow,
            allowsImplicitSignup,
        );
    }

    private readonly passportInstances: Map<string, passport.Strategy> =
        new Map();

    abstract createPassportStrategyInstance(
        strategyInstance: StrategyInstance,
    ): passport.Strategy;

    protected getAdditionalPassportOptions(
        strategyInstance: StrategyInstance,
        authStateData: AuthStateData | object,
    ): passport.AuthenticateOptions {
        return {};
    }

    getPassportStrategyInstanceFor(
        strategyInstance: StrategyInstance,
    ): passport.Strategy {
        if (this.passportInstances.has(strategyInstance.id)) {
            return this.passportInstances.get(strategyInstance.id);
        } else {
            const newInstance =
                this.createPassportStrategyInstance(strategyInstance);
            console.log(
                `Created new passport strategy for strategy ${this.typeName}, instance: ${strategyInstance.id}`,
            );
            this.passportInstances.set(strategyInstance.id, newInstance);
            return newInstance;
        }
    }

    public override async performAuth(
        strategyInstance: StrategyInstance,
        authStateData: AuthStateData | object,
        req: any,
        res: any,
    ): Promise<{
        result: AuthResult | null;
        returnedState: AuthStateData;
        info: any;
    }> {
        return new Promise((resolve, reject) => {
            const passportStrategy =
                this.getPassportStrategyInstanceFor(strategyInstance);
            const jwtService = this.passportJwtService;
            passport.authenticate(
                passportStrategy,
                {
                    session: false,
                    state: jwtService.sign(authStateData),
                    ...this.getAdditionalPassportOptions(
                        strategyInstance,
                        authStateData,
                    ),
                },
                (err, user: AuthResult | false, info) => {
                    console.log("passport callback", err, user, info);
                    if (err) {
                        reject(err);
                    } else {
                        let returnedState = {};
                        if (info.state && typeof info.state == "string") {
                            returnedState = jwtService.verify(info.state);
                        } else if (info.state) {
                            reject("State not returned as JWT");
                        } else if (authStateData) {
                            returnedState = authStateData;
                        }
                        resolve({ result: user || null, returnedState, info });
                    }
                },
            )(req, res, (a) => {
                console.log("next called by passport", a);
                return resolve({
                    result: null,
                    returnedState: null,
                    info: null,
                });
            });
        });
    }
}
