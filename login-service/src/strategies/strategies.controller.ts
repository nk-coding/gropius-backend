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

    @All("post/:id")
    test(@Param("id") id: string) {
        return "Post strategy data for " + id;
    }
}
