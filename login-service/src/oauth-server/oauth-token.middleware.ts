import {
    HttpException,
    HttpStatus,
    Injectable,
    NestMiddleware,
} from "@nestjs/common";
import { Request, Response } from "express";
import { TokenService } from "src/backend-services/token.service";
import { TokenAuthorizationCodeMiddleware } from "./token-authorization-code.middleware";

@Injectable()
export class OauthTokenMiddleware implements NestMiddleware {
    constructor(
        private readonly tokenService: TokenService,
        private readonly tokenResponseCodeMiddleware: TokenAuthorizationCodeMiddleware,
    ) {}

    async use(req: Request, res: Response, next: () => void) {
        console.log("Oauth token middleware");
        console.log(req.params);
        const grant_type = req.body.grant_type;
        switch (grant_type) {
            case "authorization_code": //Request for token based on obtained code
                this.tokenResponseCodeMiddleware.use(req, res, () => {
                    console.log("authorization_code called next");
                    next();
                });
                break;
            case "password": //Request for token immediately containing username + password
            //Fallthrough to custom grant where all credentials are acceptd
            case "post_credentials": //Extension/Non standard: Request for token immediately containing credentials
                //todo: call passport strategy with given parameters
                break;
            case "refresh_token": //Request for new token using refresh token
                break;
            case "client_credentials": //Request for token for stuff on client => not supported
            default:
                throw new HttpException(
                    { error: "unsupported_grant_type" },
                    HttpStatus.BAD_REQUEST,
                );
        }
    }
}
