import {
    All,
    Body,
    Controller,
    Get,
    HttpException,
    HttpStatus,
    Post,
    Query,
    Res,
    SetMetadata,
    UseGuards,
} from "@nestjs/common";
import { UserLoginDataImsUser } from "src/model/postgres/UserLoginDataImsUser";
import { UserLoginDataImsUserService } from "src/model/services/user-login-data-ims-user";
import { StrategiesService } from "src/strategies/strategies.service";
import { CheckSyncSecretGuard } from "./check-sync-secret.guard";
import { GetImsTokenResult } from "./dto/GetImsTokenResult";

@Controller()
@UseGuards(CheckSyncSecretGuard)
export class SyncImsUserController {
    constructor(
        private readonly imsUserService: UserLoginDataImsUserService,
        private readonly strategyService: StrategiesService,
    ) {}

    @Get("getIMSToken")
    async getIMSToken(
        @Query("imsUser") imsUserId: string,
    ): Promise<GetImsTokenResult> {
        if (!imsUserId || imsUserId.trim().length <= 0) {
            throw new HttpException(
                "Missing query parameter imsUser",
                HttpStatus.BAD_REQUEST,
            );
        }
        let imsUser: UserLoginDataImsUser;
        try {
            imsUser = await this.imsUserService.findOneBy({
                neo4jId: imsUserId,
            });
        } catch (err) {
            console.error("Error loading imsuser by neo4jid", err);
            throw new HttpException(
                "Could not load ims user by id. Check the id syntax",
                HttpStatus.BAD_REQUEST,
            );
        }
        if (!imsUser) {
            return {
                token: null,
                isImsUserKnown: false,
                message: "No ims user known for this id",
            };
        }
        const loginData = await imsUser.loginData;
        const strategyInstance = await loginData.strategyInstance;
        const strategy = this.strategyService.getStrategyByName(
            strategyInstance.type,
        );
        return {
            token: await strategy.getSyncTokenFor(loginData),
            isImsUserKnown: true,
            message: null,
        };
    }
}
