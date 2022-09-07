import { All, Controller, Get, Param, Post } from "@nestjs/common";

@Controller("oauth")
export class OauthServerController {
    @All(":id/authorize")
    authorize(@Param("id") id: string) {
        return `Authorize Endpoint for ${id}`;
    }

    @All(":id/callback")
    callback(@Param("id") id: string) {
        return `Callback Endpoint for ${id}`;
    }

    @Post(":id/token")
    token(@Param("id") id: string) {
        return `Token Endpoint for ${id}`;
    }
}
