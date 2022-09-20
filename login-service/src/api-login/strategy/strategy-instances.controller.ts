import {
    All,
    Body,
    Controller,
    Delete,
    Get,
    HttpException,
    HttpStatus,
    Param,
    Post,
    Put,
    UseGuards,
} from "@nestjs/common";
import {
    CheckAccessTokenGuard,
    NeedsAdmin,
} from "src/api-login/check-access-token.guard";
import { DefaultReturn } from "src/defaultReturn";
import { StrategyInstance } from "src/model/postgres/StrategyInstance.entity";
import { StrategyInstanceService } from "src/model/services/strategy-instance.service";
import { StrategiesService } from "src/model/services/strategies.service";
import { Strategy } from "../../strategies/Strategy";
import {
    CreateStrategyInstanceInput,
    createStrategyInstanceInputCheck,
} from "./dto/CreateStrategyInstanceInput";
import {
    UpdateStrategyInstanceInput,
    updateStrategyInstanceInputCheck,
} from "./dto/UpdateStrategyInstance";

@Controller()
export class StrategyInstancesController {
    constructor(
        private readonly strategiesService: StrategiesService,
        private readonly strategyInstanceService: StrategyInstanceService,
    ) {}

    @Get(["strategyInstance", "strategy/:type/instance"])
    async getAllStrategyInstances(
        @Param("type") type: string,
    ): Promise<StrategyInstance[]> {
        if (type) {
            return this.strategyInstanceService.findBy({ type });
        } else {
            return this.strategyInstanceService.find();
        }
    }

    @Get(["strategyInstance/:id", "strategy/:type/instance/:id"])
    async getStrategyInstance(
        @Param("id") id: string,
        @Param("type") type?: string,
    ) {
        const instance = await this.strategyInstanceService.findOneBy({
            id,
            type,
        });
        if (!instance) {
            throw new HttpException(
                "Id is not a valid strategy instance id",
                HttpStatus.NOT_FOUND,
            );
        }
        const strategy = this.strategiesService.getStrategyByName(
            instance.type,
        );
        const nonRedirectUrls = strategy.needsRedirectFlow
            ? {}
            : {
                  postLogin: `/strategy/oauth/${instance.id}/token/login`,
                  postRegister: `/strategy/oauth/${instance.id}/token/register`,
                  postRegisterSync: `/strategy/oauth/${instance.id}/token/register-sync`,
              };
        return {
            id: instance.id,
            name: instance.name,
            type: instance.type,
            isLoginActive: instance.isLoginActive,
            isSelfRegisterActive: instance.isSelfRegisterActive,
            isSyncActive: instance.isSyncActive,
            doesImplicitRegister: instance.doesImplicitRegister,
            urls: {
                ...nonRedirectUrls,
                redirectLogin: `/strategy/oauth/${instance.id}/authorize/login`,
                redirectRegister: `/strategy/oauth/${instance.id}/authorize/register`,
                redirectRegisterSync: `/strategy/oauth/${instance.id}/authorize/register-sync`,
            },
        };
    }

    @Post(["strategyInstance", "strategy/:type/instance"])
    @UseGuards(CheckAccessTokenGuard)
    @NeedsAdmin()
    async createNewInstance(
        @Body() input: CreateStrategyInstanceInput,
        @Param("type") type?: string,
    ): Promise<StrategyInstance> {
        input.type = type || input.type;
        createStrategyInstanceInputCheck(input);
        const strategy = this.strategiesService.getStrategyByName(input.type);
        if (!strategy) {
            throw new HttpException(
                "Strategy with given type name does not exist",
                HttpStatus.BAD_REQUEST,
            );
        }
        try {
            return strategy.createOrUpdateNewInstance(input);
        } catch (err) {
            console.error(err);
            throw new HttpException(err.message ?? err, HttpStatus.BAD_REQUEST);
        }
    }

    @Put(["strategyInstance/:id", "strategy/:type/instance/:id"])
    @UseGuards(CheckAccessTokenGuard)
    @NeedsAdmin()
    async updateStrategyInstance(
        @Body() input: UpdateStrategyInstanceInput,
        @Param("id") id: string,
        @Param("type") type?: string,
    ): Promise<StrategyInstance> {
        updateStrategyInstanceInputCheck(input);

        const instance = await this.strategyInstanceService.findOneBy({
            id,
            type,
        });
        if (!instance) {
            throw new HttpException(
                "Id is not a valid strategy instance id",
                HttpStatus.NOT_FOUND,
            );
        }
        const stragety = this.strategiesService.getStrategyByName(
            instance.type,
        );
        if (!stragety) {
            throw new Error(
                `Strategy ${instance.type} for instance ${instance.id} not found`,
            );
        }
        return stragety.createOrUpdateNewInstance(input, instance);
    }

    @Delete(["strategyInstance/:id", "strategy/:type/instance/:id"])
    @UseGuards(CheckAccessTokenGuard)
    @NeedsAdmin()
    async deleteStrategyInstance(
        @Body() input: UpdateStrategyInstanceInput,
        @Param("id") id: string,
        @Param("type") type?: string,
    ): Promise<DefaultReturn> {
        const instance = await this.strategyInstanceService.findOneBy({
            id,
            type,
        });
        if (!instance) {
            throw new HttpException(
                "Id is not a valid strategy instance id",
                HttpStatus.NOT_FOUND,
            );
        }
        await this.strategyInstanceService.remove(instance);
        return new DefaultReturn("delete-strategyInstance");
    }
}
