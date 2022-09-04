import { Injectable, NestMiddleware } from "@nestjs/common";
import passport from "passport";

@Injectable()
export class StrategiesMiddleware implements NestMiddleware {
    use(req: any, res: any, next: () => void) {
        passport.authenticate("")(req, res, next);
    }
}
