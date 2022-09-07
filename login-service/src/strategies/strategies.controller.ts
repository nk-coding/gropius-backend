import {
    All,
    Controller,
    Get,
    HttpException,
    HttpStatus,
    Param,
} from "@nestjs/common";
import { StrategiesService } from "./strategies.service";

@Controller()
export class StrategiesController {
    constructor(private readonly strategiesService: StrategiesService) {}

    @Get()
    async getAllStrategyTypes() {
        return this.strategiesService.getAllStrategies();
    }

    @All("oauth/:id/login")
    test(@Param("id") id: string) {
        return "Test successfull for " + id;
    }
}
