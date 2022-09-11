import { Injectable, NestMiddleware } from "@nestjs/common";
import { Request, Response } from "express";

@Injectable()
export class TokenAuthorizationCodeMiddleware implements NestMiddleware {
    async use(req: Request, res: Response, next: () => void) {}
}
