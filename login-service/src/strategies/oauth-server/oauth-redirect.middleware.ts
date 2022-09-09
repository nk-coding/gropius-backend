import {
    HttpException,
    HttpStatus,
    Injectable,
    NestMiddleware,
} from "@nestjs/common";
import { Request, Response } from "express";
import { AuthStateData } from "../AuthResult";
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
        const errorMessage = (res.locals?.state as AuthStateData)
            ?.authErrorMessage;
        if (errorMessage) {
            url.searchParams.append(
                "error",
                encodeURIComponent(
                    (res.locals?.state as AuthStateData).authErrorType ||
                        "invalid_request",
                ),
            );
            url.searchParams.append(
                "error_description",
                encodeURIComponent(
                    (
                        res.locals?.state as AuthStateData
                    ).authErrorMessage?.replace(
                        /[^\x20-\x21\x23-\x5B\x5D-\x7E]/g,
                        "",
                    ),
                ),
            );
        } else if (
            res.locals?.state == undefined ||
            res.locals?.state == null
        ) {
            url.searchParams.append("error", "server_error");
            url.searchParams.append(
                "error_description",
                encodeURIComponent(
                    "State of request was lost. Internal server error",
                ),
            );
        } else {
            url.searchParams.append("code", "THIS_WILL_BE_THE_CODE");
            if (state.state) {
                url.searchParams.append("state", state.state);
            }
        }
        res.status(302)
            .setHeader("Location", url.toString())
            .setHeader("Content-Length", 0)
            .end();
    }
}
