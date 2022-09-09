import * as passport from "passport";
import { ActiveLogin } from "src/model/postgres/ActiveLogin";
import { LoginUser } from "src/model/postgres/LoginUser";
import { StrategyInstance } from "src/model/postgres/StrategyInstance";
import { StrategyInstanceService } from "src/model/services/strategy-instance.service";
import { AuthResult, AuthStateData } from "./AuthResult";

export interface StrategyVariable {
    name: string;
    displayName?: string;
    type: "boolean" | "number" | "object" | "string";
    nullable?: boolean;
}

export abstract class Strategy {
    constructor(
        public readonly typeName: string,
        private readonly strategyInstanceService: StrategyInstanceService,
        public readonly canLoginRegister: boolean = true,
        public readonly canSync: boolean = false,
        public readonly needsRedirectFlow = false,
        public readonly allowsImplicitSignup = false,
    ) {}

    /**
     * Checks the given config for a instance of this strategy for validity
     *
     * @param instanceConfig The config object to check for validity
     * @return `true` iff the config is valid
     */
    protected abstract checkInstanceConfig(instanceConfig: object): boolean;

    async createNewInstance(
        name: string | null,
        instanceConfig: object,
        isLoginActive: boolean,
        isRegisterActive: boolean,
        isSyncActive: boolean,
    ): Promise<StrategyInstance> {
        if (!this.checkInstanceConfig(instanceConfig)) {
            throw new Error("Instance config format invalid");
        }
        const instance = new StrategyInstance(this.typeName);
        instance.name = name.replace(/[^a-zA-Z0-9-_]/g, "");
        instance.instanceConfig = instanceConfig;
        instance.isLoginActive = !!isLoginActive;
        instance.isRegisterActive = !!isRegisterActive;
        instance.isSyncActive = !!isSyncActive;

        return await this.strategyInstanceService.save(instance);
    }

    async existsInstanceClearName(name: string): Promise<boolean> {
        return (await this.strategyInstanceService.countBy({ name })) > 0;
    }

    async getAllInstances(): Promise<StrategyInstance[]> {
        return this.strategyInstanceService.findBy({ type: this.typeName });
    }

    async getInstanceById(id: string): Promise<StrategyInstance> {
        return this.strategyInstanceService.findOneBy({
            id,
            type: this.typeName,
        });
    }

    get acceptsVariables(): {
        [variableName: string]: StrategyVariable;
    } {
        return {};
    }

    abstract performAuth(
        strategyInstance: StrategyInstance,
        authStateData: AuthStateData | object,
        req: any,
        res: any,
        next: () => void,
    ): Promise<{
        result: AuthResult | null;
        returnedState: AuthStateData;
        info: any;
    }>;

    toJSON() {
        return {
            typeName: this.typeName,
            canLoginRegister: this.canLoginRegister,
            canSync: this.canSync,
        };
    }
}

export abstract class StrategyUsingPassport extends Strategy {
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
        next: () => void,
    ): Promise<{
        result: AuthResult | null;
        returnedState: AuthStateData;
        info: any;
    }> {
        return new Promise((resolve, reject) => {
            const passportStrategy =
                this.getPassportStrategyInstanceFor(strategyInstance);
            passport.authenticate(
                passportStrategy,
                {
                    session: false,
                    state: Buffer.from(JSON.stringify(authStateData)).toString(
                        "base64url",
                    ),
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
                            returnedState = JSON.parse(
                                Buffer.from(info.state, "base64url").toString(
                                    "utf-8",
                                ),
                            );
                        } else if (info.state) {
                            returnedState = info.state;
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
