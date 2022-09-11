import {
    All,
    Body,
    Controller,
    Get,
    HttpException,
    HttpStatus,
    Param,
    Post,
} from "@nestjs/common";
import { AuthClientService } from "src/model/services/auth-client.service";

@Controller("oauth")
export class OauthServerController {
    constructor(private readonly authClientService: AuthClientService) {}

    @All(":id/authorize")
    authorize(@Param("id") id: string) {
        return `Authorize Endpoint for ${id}`;
    }

    @All(":id/callback")
    callback(@Param("id") id: string) {
        return `Callback Endpoint for ${id}`;
    }

    @All(":id?/token")
    token(@Param("id") id: string, @Body() body) {
        switch (body.grant_type) {
            case "authorization_code":
                const client = this.authClientService.findOneBy({
                    id: body.client_id,
                });
                if (!client) {
                    throw new HttpException(
                        {
                            error: "invalid_client",
                            error_description: "Invalid client id",
                        },
                        HttpStatus.BAD_REQUEST,
                    );
                }
                break;
        }

        return `Token Endpoint for ${id}`;
    }
}
