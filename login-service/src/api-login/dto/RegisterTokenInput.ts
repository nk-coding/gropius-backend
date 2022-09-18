import { HttpException, HttpStatus } from "@nestjs/common";

export interface RegisterTokenInput {
    register_token: string;
}

export interface AdminLinkUserIdInput {
    user_to_link_to_id: string;
}

export function adminLinkUserIdInputCheck(
    input: AdminLinkUserIdInput,
): AdminLinkUserIdInput {
    if (
        !input.user_to_link_to_id ||
        input.user_to_link_to_id.trim().length <= 0
    ) {
        throw new HttpException(
            "User id for user to link the new login to must be given and can't be empty.",
            HttpStatus.BAD_REQUEST,
        );
    }
    return input;
}
