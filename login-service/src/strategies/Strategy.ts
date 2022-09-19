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
     * For strategies that can sync, this checks the existance and format of `imsTemplatedFieldsFilter` in the instance config.
     * This is expected to contain the fields with the values that are expected for an IMS to be considered an ims fot this strategy instance.
     *
     * @param instanceConfig The config object to check for validity
     * @return `true` iff the config is valid, `false` or an error mesage if the config has errors
     */
    protected checkInstanceConfig(instanceConfig: object): boolean | string {
        if (this.canSync) {
            const imsTemplatedFieldsFilter =
                instanceConfig["imsTemplatedFieldsFilter"];
            if (!imsTemplatedFieldsFilter) {
                return "Instances of strategies that can sync must configure the `imsTemplatedFieldsFilter` that sets the expected templated values on the ims";
            }
            if (typeof imsTemplatedFieldsFilter !== "object") {
                return "`imsTemplatedFieldsFilter`must be a object/json on the fields and values that are expected on the ims";
            }
        }
        return true;
    }

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

    getSyncTokenForLoginData(
        loginData: UserLoginData,
    ): string | null | Promise<string | null> {
        return null;
    }

    /**
     * Returns the object containing the templated fields and their values that an *IMS* needs to match,
     * in order to be considered tha IMS that is represented by the strategy instance given.
     * Values will be compared field by field with the templated values of the IMS.
     *
     * The fields `id`, `name` and `description` of the returned object will not be compared to the templated values,
     * but instead to the actual fields of the IMS with those respective names.
     *
     * For example: The API-Url must match in order for a github IMS belonging to a github strategy instance
     *
     * Can/Should be overridden by strategies capable of sync.
     * Default implementation returns `imsTemplatedFieldsFilter` of instance config
     *
     * @param instance The strategy instance for which the templated values should be retuned
     * @returns An object which, if it matches the templated fields of an IMS, the given instance is the matching strategy instance for that IMS
     * Null if the strategy does not sync
     */
    getImsTemplatedValuesForStrategyInstance(
        instance: StrategyInstance,
    ): object | null | Promise<object | null> {
        if (this.canSync) {
            return instance.instanceConfig["imsTemplatedFieldsFilter"];
        }
        return null;
    }

    /**
     * Returns the object containing the templated fields and their values that an *IMSUser* needs to match,
     * in order to be considered an IMSUser that belongs to the given `loginData`
     * (i.e. the IMS is a login of the user of this login data on the ims of this login data)
     *
     * The fields `id`, `username`, `displayName` and `email` of the returned object will not be compared to the templated values,
     * but instead to the actual fields of the IMSUser with those respective names.
     *
     * For example: The username on Github must match the username in the login data.
     *
     * Can/Should/Must be overridden by strategies capable of sync.
     * Default implementation returns the `loginData.data` field unchanged
     *
     * @param loginData The login data for which the templated field values should be returned, representing a login of the user using a strategy instance
     * @returns An object which, if it matches the templated fields of an IMSUser, the given loginData is the matching login for that IMSUser
     * Null if the strategy does not sync
     */
    getImsUserTemplatedValuesForLoginData(
        loginData: UserLoginData,
    ): object | null | Promise<object | null> {
        if (this.canSync) {
            return loginData.data;
        }
        return null;
    }

    /**
     * Does the opposite of `getImsUserTemplatedValuesForLoginData`.
     *
     * Returns an object that needs to match the data field of a `LoginData` in order for the IMSUser to be considerd matching the login data.
     *
     * The `imsUserTemplatedFields` should also contain the fields `id`, `username`, `displayName` and `email` directly of the IMSUser
     * in addition to the templated fields and values.
     *
     * For example: Given templated fields of an IMSUser containing its username this should return the login data object that
     *   also matches the user with that username
     *
     * Can/Should/Must be overridden by strategies capable of sync.
     * Default implementation returns the `imsUserTemplatedFields` unchanged
     *
     *
     * @param imsUserTemplatedFields Templated fields and values as well as the fields `id`, `username`, `displayName` and `email` of the IMSUser.
     * @returns An object, that the `.data` field of a login data needs to match.
     */
    getLoginDataDataForImsUserTemplatedFields(
        imsUserTemplatedFields: object,
    ): object | null | Promise<object | null> {
        if (this.canSync) {
            return imsUserTemplatedFields;
        }
        return null;
    }

    abstract performAuth(
        strategyInstance: StrategyInstance,
        authStateData: AuthStateData | object,
        req: any,
        res: any,
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
