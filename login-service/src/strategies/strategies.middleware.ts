import {
    HttpException,
    HttpStatus,
    Injectable,
    NestMiddleware,
} from "@nestjs/common";
import { Request, Response } from "express";
import passport from "passport";
import { ActiveLogin, LoginState } from "src/model/postgres/ActiveLogin";
import { LoginUser } from "src/model/postgres/LoginUser";
import { StrategyInstance } from "src/model/postgres/StrategyInstance";
import { ActiveLoginService } from "src/model/services/active-login.service";
import { AuthClientService } from "src/model/services/auth-client.service";
import { LoginUserService } from "src/model/services/login-user.service";
import { StrategyInstanceService } from "src/model/services/strategy-instance.service";
import { AuthFunction, AuthResult, AuthStateData } from "./AuthResult";
import { PerformAuthFunctionService } from "./perform-auth-function.service";
import { StrategiesService } from "./strategies.service";
import { ensureState } from "./utils";

@Injectable()
export class StrategiesMiddleware implements NestMiddleware {
    constructor(
        private readonly strategiesService: StrategiesService,
        private readonly strategyInstanceService: StrategyInstanceService,
        private readonly performAuthFunctionService: PerformAuthFunctionService,
    ) {}

    private async idToStrategyInstance(id: string): Promise<StrategyInstance> {
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
        ensureState(res);
        const id = req.params.id;
        console.log("id", id);
        const instance = await this.idToStrategyInstance(id);
        const strategy = await this.strategiesService.getStrategyByName(
            instance.type,
        );
        console.log("strategies middleware; state", res.locals.state);

        const functionError =
            this.performAuthFunctionService.checkFunctionIsAllowed(
                res.locals.state,
                instance,
            );
        if (functionError != null) {
            (res.locals.state as AuthStateData).authErrorMessage =
                functionError;
            return next();
        }

        const result = await strategy.performAuth(
            instance,
            res.locals.state || {},
            req,
            res,
            next,
        );
        console.log("Strategy result", result);

        const authResult = result.result;
        if (authResult) {
            const executionResult =
                await this.performAuthFunctionService.performRequestedAction(
                    authResult,
                    res.locals.state,
                    instance,
                );
            res.locals.state = { ...result.returnedState, ...executionResult };
        } else {
            (res.locals.state as AuthStateData).authErrorMessage =
                result.info?.message?.toString() ||
                JSON.stringify(result.info) ||
                "Login unsuccessfull";
        }
        next();
    }
}
