import {
    HttpException,
    HttpStatus,
    Injectable,
    NestMiddleware,
} from "@nestjs/common";
import { Request, Response } from "express";
import { ActiveLoginService } from "src/model/services/active-login.service";
import { StrategyInstanceService } from "src/model/services/strategy-instance.service";
import { AuthStateData } from "./AuthResult";
import { StrategiesService } from "./strategies.service";

@Injectable()
export class ErrorHandlerMiddleware implements NestMiddleware {
    constructor(
        private readonly strategiesService: StrategiesService,
        private readonly strategyInstanceService: StrategyInstanceService,
        private readonly activeLoginService: ActiveLoginService,
    ) {}

    async use(req: Request, res: Response, next: () => void) {
        const errorMessage = (res.locals?.state as AuthStateData)
            ?.authErrorMessage;
        if (errorMessage) {
            throw new HttpException(errorMessage, HttpStatus.UNAUTHORIZED);
        } else if (
            res.locals?.state == undefined ||
            res.locals?.state == null
        ) {
            throw new HttpException(
                "State of request was lost. Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR,
            );
        } else {
            next();
        }
    }
}
