import { Injectable, NestMiddleware } from "@nestjs/common";
import { Request, Response } from "express";
import { AuthStateData } from "src/strategies/AuthResult";
import { StrategiesMiddleware } from "src/strategies/strategies.middleware";
import { ensureState } from "src/strategies/utils";
import { OauthHttpException } from "./OauthHttpException";

@Injectable()
export class PostCredentialsMiddleware implements NestMiddleware {
    constructor(private readonly strategyMiddleware: StrategiesMiddleware) {}

    async use(req: Request, res: Response, next: (error?: any) => void) {
        ensureState(res);
        let state: AuthStateData = res.locals.state;
        const mockRes = {};
        for (const key in res) {
            if (Object.prototype.hasOwnProperty.call(res, key)) {
                const value = res[key];
                if (typeof value == "function") {
                    mockRes[key] = () => mockRes;
                } else {
                    mockRes[key] = mockRes;
                }
            }
        }
        (mockRes as Response).locals.state = { ...state };
        await new Promise(async (resolve, reject) => {
            try {
                await this.strategyMiddleware.use(req, mockRes as Response, () => resolve(undefined));
            } catch (err) {
                reject(err);
            }
        });

        state = { ...state, ...(mockRes as Response).locals.state };
        res.locals.state = state;

        if (!state.activeLogin) {
            if (state.authErrorMessage) {
                throw new OauthHttpException(state.authErrorType || "invalid_request", state.authErrorMessage);
            }
            throw new OauthHttpException("invalid_request", "Unauthorized");
        }

        next();
    }
}
