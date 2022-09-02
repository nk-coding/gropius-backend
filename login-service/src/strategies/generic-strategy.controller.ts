import {
    Body,
    Get,
    HttpException,
    HttpStatus,
    Param,
    Post,
} from "@nestjs/common";
import { StrategyInstance } from "src/model/postgres/StrategyInstance";
import { StrategiesService } from "./strategies.service";
import { CreateStrategyInstanceInput } from "./_inputs/CreateStrategyInstanceInput";

export abstract class GenericStrategyController {
    constructor(
        protected strategyName: string,
        protected readonly strategiesService: StrategiesService,
    ) {}

    @Get()
    async getGenericStrategyInfo(): Promise<StrategyInstance[]> {
        return this.strategiesService
            .getStrategyByName(this.strategyName)
            .getAllInstances();
    }

    @Get(":id")
    async getSpecificStrategyInstance(
        @Param("id") id: string,
    ): Promise<StrategyInstance> {
        const strategy = this.strategiesService.getStrategyByName(
            this.strategyName,
        );
        if (!id) {
            throw new HttpException(
                "No Id of strategy instance given",
                HttpStatus.BAD_REQUEST,
            );
        }
        const instance = await strategy.getInstanceById(id);
        if (!instance) {
            throw new HttpException(
                `No Strategy instance of type ${strategy.typeName} with id ${id}`,
                HttpStatus.NOT_FOUND,
            );
        }
        return instance;
    }

    async idToStrategyInstance(id: string): Promise<StrategyInstance> {
        const strategy = this.strategiesService.getStrategyByName(
            this.strategyName,
        );
        if (!id) {
            throw new HttpException(
                "No Id of strategy instance given",
                HttpStatus.BAD_REQUEST,
            );
        }
        const instance = await strategy.getInstanceById(id);
        if (!instance) {
            throw new HttpException(
                `No Strategy instance of type ${strategy.typeName} with id ${id}`,
                HttpStatus.NOT_FOUND,
            );
        }
        if (instance.type !== strategy.typeName) {
            throw new HttpException(
                "No userpass strategy instance with the given id",
                HttpStatus.BAD_REQUEST,
            );
        }
        return instance;
    }

    @Post()
    async createNewStrategyInstance(
        @Body() body: CreateStrategyInstanceInput,
    ): Promise<StrategyInstance> {
        const strategy = this.strategiesService.getStrategyByName(
            this.strategyName,
        );
        if (
            body.instanceConfig == null ||
            body.isLoginActive == null ||
            body.isRegisterActive == null ||
            body.isSyncActive == null
        ) {
            throw new HttpException(
                "instanceConfig, isLoginActive, isRegisterActive and isSyncActive must be given",
                HttpStatus.BAD_REQUEST,
            );
        }
        if (typeof body.name != "string" && body.name != null) {
            throw new HttpException(
                "Clear text name of strategy instance must be a string",
                HttpStatus.BAD_REQUEST,
            );
        }
        if (
            body.name != null &&
            body.name.replace(/[^a-zA-Z0-9-_]/g, "") != body.name
        ) {
            throw new HttpException(
                "The clear text name can only contain a-z, A-Z, 0-9, -, _",
                HttpStatus.BAD_REQUEST,
            );
        }
        if (await strategy.existsInstanceClearName(body.name)) {
            throw new HttpException(
                "Clear text name already exists. Must be unique",
                HttpStatus.BAD_REQUEST,
            );
        }
        try {
            return await strategy.createNewInstance(
                body.name,
                body.instanceConfig,
                !!body.isLoginActive,
                !!body.isRegisterActive,
                !!body.isSyncActive,
            );
        } catch (err) {
            throw new HttpException(
                (err as Error).message,
                HttpStatus.BAD_REQUEST,
            );
        }
    }
}
