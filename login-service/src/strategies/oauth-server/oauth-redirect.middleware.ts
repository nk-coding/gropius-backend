import {
    HttpException,
    HttpStatus,
    Injectable,
    NestMiddleware,
} from "@nestjs/common";
import { Request, Response } from "express";
import { OauthServerStateData } from "./oauth-autorize.middleware";

@Injectable()
export class OauthRedirectMiddleware implements NestMiddleware {
    async use(req: Request, res: Response, next: () => void) {
        console.log("oauth-callback middleware");
        const state: OauthServerStateData = res.locals.state || {};
        if (!state.redirect) {
            throw new HttpException(
                "No redirect address in state for request",
                HttpStatus.BAD_REQUEST,
            );
        }
        const url = new URL(state.redirect);
        url.searchParams.append("code", "THIS_WILL_BE_THE_CODE");
        if (state.state) {
            url.searchParams.append("state", state.state);
        }
        res.status(302)
            .setHeader("Location", url.toString())
            .setHeader("Content-Length", 0)
            .end();
    }
}
