import { HttpException, HttpStatus } from "@nestjs/common";

export interface RegisterUserInput {
    username: string;
    displayName: string;
    email?: string;
}

export function registerUserInputCheck(
    input: RegisterUserInput,
): RegisterUserInput {
    if (!input.username || input.username.trim().length <= 0) {
        throw new HttpException(
            "Username must be given and can't be empty",
            HttpStatus.BAD_REQUEST,
        );
    }
    if (!input.displayName || input.displayName.trim().length <= 0) {
        throw new HttpException(
            "Display name must be given and can't be empty",
            HttpStatus.BAD_REQUEST,
        );
    }
    if (input.email != undefined && input.email.trim().length <= 0) {
        throw new HttpException(
            "If email is given it can't be empty",
            HttpStatus.BAD_REQUEST,
        );
    }
    return input;
}
