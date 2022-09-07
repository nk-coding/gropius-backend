import {
    HttpException,
    HttpStatus,
    Injectable,
    NestMiddleware,
} from "@nestjs/common";
import { Request, Response } from "express";
import { StrategyInstance } from "src/model/postgres/StrategyInstance";
import { AuthClientService } from "src/model/services/auth-client.service";
import { StrategyInstanceService } from "src/model/services/strategy-instance.service";
import { AuthFunction } from "../AuthResult";
import { StrategiesService } from "../strategies.service";

@Injectable()
export class OauthRedirectMiddleware implements NestMiddleware {
    async use(req: Request, res: Response, next: () => void) {
        console.log("oauth-callback middleware");
        res.status(202).send(
            "At this point I would redirect you to your edirect adress, but i don't know it",
        );
        next();
    }
}
