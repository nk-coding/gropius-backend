import { StrategyInstance } from "src/model/postgres/StrategyInstance";
import { StrategyInstanceService } from "src/model/services/strategy-instance.service";

export abstract class Strategy {
    constructor(
        public readonly typeName: string,
        private readonly strategyInstanceService: StrategyInstanceService,
        public readonly canLoginRegister: boolean = true,
        public readonly canSync: boolean = false,
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
        const instance = new StrategyInstance();
        instance.name = name.replace(/[^a-zA-Z0-9-_]/g, "");
        instance.instanceConfig = instanceConfig;
        instance.isLoginActive = !!isLoginActive;
        instance.isRegisterActive = !!isRegisterActive;
        instance.isSyncActive = !!isSyncActive;
        instance.type = this.typeName;

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

    toJSON() {
        return {
            typeName: this.typeName,
            canLoginRegister: this.canLoginRegister,
            canSync: this.canSync,
        };
    }
}
