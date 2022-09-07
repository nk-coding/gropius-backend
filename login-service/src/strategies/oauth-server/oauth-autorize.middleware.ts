import {
    HttpException,
    HttpStatus,
    Injectable,
    NestMiddleware,
} from "@nestjs/common";
import { query, Request, Response } from "express";
import { StrategyInstance } from "src/model/postgres/StrategyInstance";
import { AuthClientService } from "src/model/services/auth-client.service";
import { StrategyInstanceService } from "src/model/services/strategy-instance.service";
import { AuthFunction } from "../AuthResult";
import { StrategiesService } from "../strategies.service";

@Injectable()
export class OauthAutorizeMiddleware implements NestMiddleware {
    constructor(private readonly authClientService: AuthClientService) {}

    async use(req: Request, res: Response, next: () => void) {
        console.log("oauth-authorize middleware");
        const params = { ...req.query } as { [name: string]: string };
        const clientId = params.client_id;
        const client = await this.authClientService.findOneBy({ id: clientId });
        if (!clientId || !client) {
            throw new HttpException(
                "client_id not given or not a valid client id",
                HttpStatus.BAD_REQUEST,
            );
        }
        if (
            params.response_type != undefined &&
            params.response_type !== "code"
        ) {
            throw new HttpException(
                "response_type must be set to 'code'. Other flow types not supported",
                HttpStatus.NOT_IMPLEMENTED,
            );
        }
        if (
            !!params.redirect_uri &&
            !client.redirectUrls.includes(params.redirect_uri)
        ) {
            throw new HttpException(
                "Given redirect URI not valid for this client.",
                HttpStatus.BAD_REQUEST,
            );
        }
        const redirect = params.redirect_uri || client.redirectUrls[0];
        const state = params.state;

        res.locals.state = {
            function: AuthFunction.LOGIN,
            state,
            redirect,
            clientId,
        };
        next();
    }
}
