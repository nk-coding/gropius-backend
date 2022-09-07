import {
    HttpException,
    HttpStatus,
    Injectable,
    NestMiddleware,
} from "@nestjs/common";
import { Request, Response } from "express";
import passport from "passport";
import { StrategyInstance } from "src/model/postgres/StrategyInstance";
import { AuthClientService } from "src/model/services/auth-client.service";
import { StrategyInstanceService } from "src/model/services/strategy-instance.service";
import { AuthFunction } from "./AuthResult";
import { StrategiesService } from "./strategies.service";

@Injectable()
export class StrategiesMiddleware implements NestMiddleware {
    constructor(
        private readonly strategiesService: StrategiesService,
        private readonly strategyInstanceService: StrategyInstanceService,
    ) {}

    async idToStrategyInstance(id: string): Promise<StrategyInstance> {
        if (!id) {
            throw new HttpException(
                "No Id of strategy instance given",
                HttpStatus.BAD_REQUEST,
            );
        }
        const instance = await this.strategyInstanceService.findOneBy({ id });
        if (!instance) {
            throw new HttpException(
                `No Strategy instance with id ${id}`,
                HttpStatus.NOT_FOUND,
            );
        }
        return instance;
    }

    async use(req: Request, res: Response, next: () => void) {
        const id = req.params.id;
        console.log("id", id);
        const instance = await this.idToStrategyInstance(id);
        const strategy = await this.strategiesService.getStrategyByName(
            instance.type,
        );
        console.log("strategies middleware; state", res.locals.state);
        const result = await strategy.performAuth(
            instance,
            res.locals.state || {},
            req,
            res,
            next,
        );
        console.log("Strategy result", result);
        if (result.result) {
            if (result.returnedState) {
                res.locals.state = result.returnedState;
            }
            next();
        } else {
            throw new HttpException(
                result.info?.message ?? result.info ?? "Login not successfull",
                HttpStatus.UNAUTHORIZED,
            );
        }

        /*if (result.result == null) {
            console.log("Result null, returning info");
            throw new HttpException(
                result.info.message,
                HttpStatus.BAD_REQUEST,
            );
        } else {
            console.log("Auth successfull");
            return "Logged in as " + result.result.user.displayName;
        }
        throw new HttpException("Test", HttpStatus.BAD_REQUEST);*/
    }
}
