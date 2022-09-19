import { HttpException, HttpStatus } from "@nestjs/common";

export class CreateStrategyInstanceInput {
    name?: string;
    instanceConfig: object;
    type: string;
    isLoginActive?: boolean;
    isSelfRegisterActive?: boolean;
    isSyncActive?: boolean;
    doesImplicitRegister?: boolean;
}

export function createStrategyInstanceInputCheck(
    input: CreateStrategyInstanceInput,
): CreateStrategyInstanceInput {
    if (
        input.name != undefined &&
        (typeof input.name != "string" || input.name.trim().length <= 0)
    ) {
        throw new HttpException(
            "If name is given it must be a non empty string",
            HttpStatus.BAD_REQUEST,
        );
    }
    if (input.name != undefined && !input.name.match(/[^a-zA-Z0-9_\- ]/g)) {
        throw new HttpException(
            "Name of strategy instance may only contain alphanumeric characters, -, _ and space",
            HttpStatus.BAD_REQUEST,
        );
    }
    if (typeof input.instanceConfig != "object") {
        throw new HttpException(
            "Instance config must be given as object",
            HttpStatus.BAD_REQUEST,
        );
    }
    if (typeof input.type != "string" || input.type.trim().length <= 0) {
        throw new HttpException(
            "Type of stragety must be specified on creation",
            HttpStatus.BAD_REQUEST,
        );
    }
    if (
        input.isLoginActive != undefined &&
        typeof input.isLoginActive != "boolean"
    ) {
        throw new HttpException(
            "If isLoginActive is given it must be a valid boolean",
            HttpStatus.BAD_REQUEST,
        );
    }
    if (
        input.isSelfRegisterActive != undefined &&
        typeof input.isSelfRegisterActive != "boolean"
    ) {
        throw new HttpException(
            "If isSelfRegisterActive is given it must be a valid boolean",
            HttpStatus.BAD_REQUEST,
        );
    }
    if (
        input.isSyncActive != undefined &&
        typeof input.isSyncActive != "boolean"
    ) {
        throw new HttpException(
            "If isSyncActive is given it must be a valid boolean",
            HttpStatus.BAD_REQUEST,
        );
    }
    if (
        input.doesImplicitRegister != undefined &&
        typeof input.doesImplicitRegister != "boolean"
    ) {
        throw new HttpException(
            "If doesImplicitRegister is given it must be a valid boolean",
            HttpStatus.BAD_REQUEST,
        );
    }
    return input;
}
