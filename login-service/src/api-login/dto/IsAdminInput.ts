import { HttpException, HttpStatus } from "@nestjs/common";

export interface IsAdminInput {
    isAdmin?: boolean;
}

export function isAdminInputCheck(input: IsAdminInput): IsAdminInput {
    if (input != undefined && typeof input != "boolean") {
        throw new HttpException(
            "If isAdmin is given it must be a valid boolean",
            HttpStatus.BAD_REQUEST,
        );
    }
    return input;
}
