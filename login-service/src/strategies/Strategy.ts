import * as passport from "passport";
import { ActiveLogin } from "src/model/postgres/ActiveLogin";
import { LoginUser } from "src/model/postgres/LoginUser";
import { StrategyInstance } from "src/model/postgres/StrategyInstance";
import { UserLoginData } from "src/model/postgres/UserLoginData";
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
        isSelfRegisterActive: boolean,
        isSyncActive: boolean,
    ): Promise<StrategyInstance> {
        if (!this.checkInstanceConfig(instanceConfig)) {
            throw new Error("Instance config format invalid");
        }
        const instance = new StrategyInstance(this.typeName);
        instance.name = name.replace(/[^a-zA-Z0-9-_]/g, "");
        instance.instanceConfig = instanceConfig;
        instance.isLoginActive = !!isLoginActive;
        instance.isSelfRegisterActive = !!isSelfRegisterActive;
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

    getSyncTokenFor(
        loginData: UserLoginData,
    ): string | null | Promise<string | null> {
        return null;
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
