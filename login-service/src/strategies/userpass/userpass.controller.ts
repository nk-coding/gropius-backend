import {
    Body,
    Controller,
    Get,
    HttpException,
    HttpStatus,
    Param,
    Post,
} from "@nestjs/common";
import { StrategyInstance } from "src/model/postgres/StrategyInstance";
import { StrategyInstanceService } from "src/model/services/strategy-instance.service";
import { GenericStrategyController } from "../generic-strategy.controller";
import { StrategiesService } from "../strategies.service";
import { CreateStrategyInstanceInput } from "../_inputs/CreateStrategyInstanceInput";
import { UserpassLoginInput } from "./UserpassLoginInput";

@Controller("userpass")
export class StrategyUserpassController extends GenericStrategyController {
    constructor(strategiesService: StrategiesService) {
        super("userpass", strategiesService);
    }

    @Post(":id/login")
    async userpassLogin(
        @Param("id") id: string,
        @Body() body: UserpassLoginInput,
    ): Promise<string> {
        const instance = await this.idToStrategyInstance(id);
        console.log("Login witn userpass " + id);
        return "Login";
    }
}
